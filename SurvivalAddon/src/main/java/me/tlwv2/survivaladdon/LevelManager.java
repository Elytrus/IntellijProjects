package me.tlwv2.survivaladdon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by Moses on 2017-12-01.
 */
public class LevelManager {
    private Connection connection;
    private Logger logger;
    private HashMap<String, Integer> points, levels;
    private HashMap<String, Float> multipliers;
    private ArrayList<Change> changes;

    public LevelManager(String url, String username, String password) {
        this.logger = Bukkit.getLogger();

        this.points = new HashMap<>();
        this.levels = new HashMap<>();
        this.multipliers = new HashMap<>();
        this.changes = new ArrayList<>();

        this.connection = null;

        try(Connection conn = DriverManager.getConnection(url, username, password)){
            this.connection = conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //MISC. METHODS ----------------------------------------------------------------------------------------------------

    public void init(){
        try{
            query(this.connection, "CREATE DATABASE points_db");
            query(this.connection, "CREATE TABLE points_db.points (uuid CHAR(36), points INT);");
            query(this.connection, "CREATE TABLE points_db.levels (uuid CHAR(36), level INT);");
            query(this.connection, "CREATE TABLE points_db.multipliers (uuid CHAR(36), multiplier FLOAT(24));");
        }
        catch(SQLException e){
            if(e.getErrorCode() == 1050){
                sqlWarn(e.getMessage());
            }
        }
    }

    public void fetchAll(){
        //POINTS
        ResultSet points = unsafeQuery(this.connection, "SELECT * FROM points_db.points");

        try {
            while(points.next()){
                this.points.put(points.getString("uuid"), points.getInt("points"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //LEVELS

        ResultSet levels = unsafeQuery(this.connection, "SELECT * FROM points_db.points");

        try {
            while(levels.next()){
                this.levels.put(levels.getString("uuid"), levels.getInt("level"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //MULTIPLIERS

        ResultSet multipliers = unsafeQuery(this.connection, "SELECT * FROM multipliers_db.multipliers");

        try {
            while(multipliers.next()){
                this.multipliers.put(multipliers.getString("uuid"), multipliers.getFloat("multiplier"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clear(){
        this.points.clear();
        this.levels.clear();
        this.multipliers.clear();
    }

    public void reload(){
        clear();
        fetchAll();
    }

    public String dumpPlayer(Player player){
        String uuid = player.getUniqueId().toString();
        ResultSet end = unsafeQuery(this.connection, "SELECT * FROM points_db.points " +
                "FULL JOIN points_db.levels ON points_db.points.uuid = points_db.levels.uuid " +
                "FULL JOIN points_db.multipliers ON points_db.points.uuid = points_db.multipliers.uuid " +
                "WHERE points_db.points.uuid = '" + uuid + "';");
        try {
            return player.getName() + " : {Points: " + end.getInt("points") + ", Level: " + end.getInt("level") +
                    ", Multiplier: " + end.getFloat("multiplier") + "}";
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //GETTERS AND SETTERS ----------------------------------------------------------------------------------------------

    public void setPoints(String uuid, int value){
        this.points.put(uuid, value);
        this.changes.add(new PointsChange(uuid, value));
    }

    public int getPoints(String uuid){
        return this.points.get(uuid);
    }

    public void setLevel(String uuid, int value){
        this.levels.put(uuid, value);
        this.changes.add(new LevelsChange(uuid, value));
    }

    public int getLevel(String uuid){
        return this.levels.get(uuid);
    }

    public void setMultiplier(String uuid, float value){
        this.multipliers.put(uuid, value);
    }

    //MISC. CHECKS -----------------------------------------------------------------------------------------------------

    public boolean ready(){
        return this.connection != null;
    }

    //PRIVATE METHODS --------------------------------------------------------------------------------------------------

    private ResultSet query(Connection connection, String query) throws SQLException{
        try (Statement statement = connection.createStatement()) {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            throw e;
        }
    }

    private ResultSet unsafeQuery(Connection connection, String query){
        try{
            return query(connection, query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sqlWarn(String warning){
        this.logger.warning("[WARNING] An error occurred during database operations: " + warning);
    }

    private abstract class Change<V>{
        protected String uuid;
        protected V value;

        public Change(String uuid, V value) {
            this.uuid = uuid;
            this.value = value;
        }

        public abstract void execute(Connection conn);
    }

    private class PointsChange extends Change<Integer>{
        public PointsChange(String uuid, Integer value) {
            super(uuid, value);
        }

        @Override
        public void execute(Connection connection){
            unsafeQuery(connection, "IF EXISTS (SELECT * FROM points_db.points WHERE uuid = '" + this.uuid + "') BEGIN" +
                    "UPDATE points_db.points SET points = " + this.value + " WHERE uuid = '" + this.uuid + "'" +
                    "END ELSE BEGIN" +
                    "INSERT INTO points_db.points VALUES ('" + this.uuid + "', " + this.value + ") END;");
        }
    }

    private class LevelsChange extends Change<Integer>{
        public LevelsChange(String uuid, Integer value) {
            super(uuid, value);
        }

        @Override
        public void execute(Connection connection){
            unsafeQuery(connection, "IF EXISTS (SELECT * FROM points_db.levels WHERE uuid = '" + this.uuid + "') BEGIN" +
                    "UPDATE points_db.levels SET level = " + this.value + " WHERE uuid = '" + this.uuid + "'" +
                    "END ELSE BEGIN" +
                    "INSERT INTO points_db.levels VALUES ('" + this.uuid + "', " + this.value + ") END;");
        }
    }

    private class MultiplierChange extends Change<Float>{
        public MultiplierChange(String uuid, Float value) {
            super(uuid, value);
        }

        @Override
        public void execute(Connection connection){
            unsafeQuery(connection, "IF EXISTS (SELECT * FROM points_db.multipliers WHERE uuid = '" + this.uuid + "') BEGIN" +
                    "UPDATE points_db.multipliers SET multipliers = " + this.value + " WHERE uuid = '" + this.uuid + "'" +
                    "END ELSE BEGIN" +
                    "INSERT INTO points_db.multipliers VALUES ('" + this.uuid + "', " + this.value + ") END;");
        }
    }
}
