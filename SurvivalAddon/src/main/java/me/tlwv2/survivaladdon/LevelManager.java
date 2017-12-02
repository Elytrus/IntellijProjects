package me.tlwv2.survivaladdon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
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

    public LevelManager(String url, String username, String password) {
        this.logger = Bukkit.getLogger();

        this.points = new HashMap<>();
        this.levels = new HashMap<>();
        this.multipliers = new HashMap<>();

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
        ResultSet points = unsafeQuery(this.connection, "SELECT * FROM points_db.points");
    }

    public String dumpPlayer(Player player){
        String uuid = player.getUniqueId().toString();
        ResultSet end = unsafeQuery(this.connection, "SELECT * FROM points_db.points " +
                "FULL JOIN points_db.levels ON points_db.points.uuid = points_db.levels.uuid " +
                "FULL JOIN points_db.multipliers ON points_db.points.uuid = points_db.multipliers.uuid " +
                "WHERE points_db.points.uuid = " + uuid + ";");
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
}
