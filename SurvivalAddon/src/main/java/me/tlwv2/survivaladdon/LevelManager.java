package me.tlwv2.survivaladdon;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by Moses on 2017-12-01.
 */
public class LevelManager {
    private Connection connection;
    private Logger logger;

    public LevelManager(String url, String username, String password) {
        this.logger = Bukkit.getLogger();

        try{
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        this.connection = null;

        try{
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.init();
    }

    //MISC. METHODS ----------------------------------------------------------------------------------------------------

    public void init(){
        try{
            update(this.connection, "CREATE DATABASE points_db");
            update(this.connection, "CREATE TABLE points_db.points (uuid VARCHAR(255), points INT);");
            update(this.connection, "CREATE TABLE points_db.levels (uuid VARCHAR(255), level INT);");
            update(this.connection, "CREATE TABLE points_db.multipliers (uuid VARCHAR(255), multiplier FLOAT(24));");
        }
        catch(SQLException e){
            if(e.getErrorCode() == 1007){
                System.out.println(e.getMessage());
            }
            else{
                printException(e);
            }
        }
    }

    public String dumpPlayer(OfflinePlayer player){
        return dumpPlayer(player.getUniqueId().toString(), player.getName());
    }

    public String dumpPlayer(String uuid, String name){
        ResultSet end = unsafeQuery(this.connection, "SELECT * FROM points_db.points " +
                "FULL JOIN points_db.levels ON points_db.points.uuid = points_db.levels.uuid " +
                "FULL JOIN points_db.multipliers ON points_db.points.uuid = points_db.multipliers.uuid " +
                "WHERE points_db.points.uuid = '" + uuid + "';");
        try {
            return name + " : {Points: " + end.getInt("points") + ", Level: " + end.getInt("level") +
                    ", Multiplier: " + end.getFloat("multiplier") + "}";
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void incrementPoints(String uuid, int value){
        int points = getPoints(uuid);
        int level = getLevel(uuid), newLevel;
        float multiplier = getMultiplier(uuid);

        if(points == -1){
            this.logger.severe("Unable to find points for UUID " + uuid + "!");
            return;
        }

        if(level == -1){
            this.logger.severe("Unable to find level for UUID " + uuid + "!");
            return;
        }

        if(multiplier == -1.0){
            this.logger.severe("Unable to find multiplier for UUID " + uuid + "!");
            return;
        }

        points = points + (int)(multiplier * value);
        setPoints(uuid, points);

        if(Addons.getInstance().canLevelUp(level, points)){
            newLevel = Addons.getInstance().getLevel(points);
            setLevel(uuid, newLevel);
            Bukkit.getPluginManager().callEvent(new LevelUpEvent(Bukkit.getPlayer(UUID.fromString(uuid)), level, newLevel));
        }
    }

    public void updateLevel(String uuid){
        int points = getPoints(uuid);

        if(points == -1){
            this.logger.severe("Could not update level because points do not exist for UUID " + uuid);
            return;
        }

        int level = Addons.getInstance().getLevel(points);
        setLevel(uuid, level);
    }

    public void die(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //GETTERS AND SETTERS ----------------------------------------------------------------------------------------------

    public void setPoints(String uuid, int value){
        updatePropertyInTable(this.connection, "points_db.points", "points", uuid, value);
    }

    public int getPoints(String uuid){
        ResultSet results = getPropertyFromTable(this.connection, "points_db.points", uuid);
        try {
            int pts = results.getInt("points");
            closeSet(results);
            return pts;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void setLevel(String uuid, int value){
        updatePropertyInTable(this.connection, "points_db.levels", "level", uuid, value);
    }

    public int getLevel(String uuid){
        ResultSet results = getPropertyFromTable(this.connection, "points_db.levels", uuid);
        try {
            int lvl = results.getInt("level");
            closeSet(results);
            return lvl;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void setMultiplier(String uuid, float value){
        updatePropertyInTable(this.connection, "points_db.multipliers", "multiplier", uuid, value);
    }

    public float getMultiplier(String uuid){
        ResultSet results = getPropertyFromTable(this.connection, "points_db.multipliers", uuid);
        try {
            float mlptr = results.getFloat("multiplier");
            closeSet(results);
            return mlptr;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1.0f;
    }

    public boolean points(String uuid){
        return existsIn("points_db.points", uuid);
    }

    public boolean level (String uuid){
        return existsIn("points_db.levels", uuid);
    }

    public boolean multiplier(String uuid){
        return existsIn("points_db.multipliers", uuid);
    }

    //MISC. CHECKS -----------------------------------------------------------------------------------------------------

    public boolean ready(){
        return this.connection != null;
    }

    //PRIVATE METHODS --------------------------------------------------------------------------------------------------

    private ResultSet query(Connection connection, String query) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            throw e;
        }
    }

    private int update(Connection connection, String query) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    private ResultSet unsafeQuery(Connection connection, String query){
        try{
            return query(connection, query);
        }
        catch(SQLException e){
            printException(e);
        }

        return null;
    }

    private int unsafeUpdate(Connection connection, String query){
        try{
            return update(connection, query);
        }
        catch(SQLException e){
            printException(e);
        }

        return -1;
    }

    private ResultSet getPropertyFromTable(Connection connection, String table, String uuid){
        return unsafeQuery(connection, "SELECT * FROM " + table + " WHERE uuid = '" + uuid + "' LIMIT 1;");
    }

    private int updatePropertyInTable(Connection connection, String table, String property, String uuid, Object value){
        try {
            return update(connection,"UPDATE " + table + "SET " + property + " = " + value + " WHERE uuid = '" + uuid + "';");
        } catch (SQLException e) {
            this.logger.warning("SQL Error: " + e.getMessage());
            this.logger.warning("Attempting alternate method of updating value " + property + " for uuid " + uuid);

            return unsafeUpdate(connection, "INSERT INTO " + table + "VALUES ('" + uuid + "', " + value + ");");
        }
    }

    private boolean existsIn(String table, String uuid){
        ResultSet results = unsafeQuery(this.connection, "SELECT EXISTS(SELECT 1 FROM " + table + " WHERE uuid = '" + uuid + "')" +
                " AS \"exists\";");
        try {
            boolean exists = results.getInt("exists") == 1;
            closeSet(results);
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void closeSet(ResultSet set){
        try {
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            set.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sqlWarn(String warning) {
        this.logger.warning("[WARNING] An error occurred during database operations: " + warning);
    }

    public void printException(SQLException e){
        e.printStackTrace();
        System.out.println();
        System.out.println("Message: " + e.getMessage());
        System.out.println("State: " + e.getSQLState());
        System.out.println("Error Code: " + e.getErrorCode());
    }
}
