package org.kairofcraft.enxadab.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mesaMysql {

    private static JavaPlugin plugin;

    public mesaMysql(JavaPlugin plugin) {mesaMysql.plugin = plugin;}
    private static Connection connection;
    public static void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {return;}

        String url = plugin.getConfig().getString("url");
        String user = plugin.getConfig().getString("user");
        String password = plugin.getConfig().getString("password");

        try {connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {throw new SQLException("Não foi possível conectar ao banco de dados: " + e.getMessage(), e);}
    }
    public Connection getConnection() {
        return connection;
    }
    public static void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    public static void createHomesTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS homes ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "player_uuid VARCHAR(36) NOT NULL, "
                + "home_name VARCHAR(50) NOT NULL, "
                + "world VARCHAR(50) NOT NULL, "
                + "x DOUBLE NOT NULL, "
                + "y DOUBLE NOT NULL, "
                + "z DOUBLE NOT NULL, "
                + "yaw FLOAT NOT NULL, "
                + "pitch FLOAT NOT NULL"
                + ");";

        Statement statement = connection.createStatement();
        statement.executeUpdate(createTableSQL);
        statement.close();
    }
    public static boolean insertHome(Player player, String homeName) throws SQLException {
        String playerUuid = player.getDisplayName();

        // Verificar se já existe uma home com o mesmo nome para o jogador
        String checkSQL = "SELECT COUNT(*) FROM homes WHERE player_uuid = ? AND home_name = ?";
        PreparedStatement checkStatement = connection.prepareStatement(checkSQL);
        checkStatement.setString(1, playerUuid);
        checkStatement.setString(2, homeName);

        ResultSet resultado = checkStatement.executeQuery();
        resultado.next();
        int counta = resultado.getInt(1);
        resultado.close();
        checkStatement.close();
        if (counta > 0) {
            return false;
        }
        String world = player.getWorld().getName();
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();
        String insertSQL = "INSERT INTO homes (player_uuid, home_name, world, x, y, z, yaw, pitch) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        preparedStatement.setString(1, playerUuid);
        preparedStatement.setString(2, homeName);
        preparedStatement.setString(3, world);
        preparedStatement.setDouble(4, x);
        preparedStatement.setDouble(5, y);
        preparedStatement.setDouble(6, z);
        preparedStatement.setFloat(7, yaw);
        preparedStatement.setFloat(8, pitch);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
    }
    public static Map<String, Object> pesquisHomeEspecifica (String playerUuid, String homeName) throws SQLException {
        String selectSQL = "SELECT * FROM homes WHERE player_uuid = ? AND home_name = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
        preparedStatement.setString(1, playerUuid);
        preparedStatement.setString(2, homeName);

        ResultSet resultSet = preparedStatement.executeQuery();

        Map<String, Object> homeData = new HashMap<>();
        if (resultSet.next()) {
            homeData.put("id", resultSet.getInt("id"));
            homeData.put("player_uuid", resultSet.getString("player_uuid"));
            homeData.put("home_name", resultSet.getString("home_name"));
            homeData.put("world", resultSet.getString("world"));
            homeData.put("x", resultSet.getDouble("x"));
            homeData.put("y", resultSet.getDouble("y"));
            homeData.put("z", resultSet.getDouble("z"));
            homeData.put("yaw", resultSet.getFloat("yaw"));
            homeData.put("pitch", resultSet.getFloat("pitch"));
        }

        if(homeData.get("player_uuid")!=playerUuid){
            resultSet.close();
            preparedStatement.close();
            return homeData;
        }
        if(homeData.get("home_name")!=homeName){
            resultSet.close();
            preparedStatement.close();
            return homeData;
        }

        resultSet.close();
        preparedStatement.close();

        return homeData;
    }
    public static boolean deletarHomeEspecifica(String playerUuid, String homeName) throws SQLException {
        String selectSQL = "SELECT * FROM homes WHERE player_uuid = ? AND home_name = ?";
        String deleteSQL = "DELETE FROM homes WHERE player_uuid = ? AND home_name = ?";
        PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
        selectStatement.setString(1, playerUuid);
        selectStatement.setString(2, homeName);
        ResultSet resultado = selectStatement.executeQuery();
        if(!(resultado.next())){
            resultado.close();
            selectStatement.close();
            return false;}
        PreparedStatement deleteStatement = connection.prepareStatement(deleteSQL);
        deleteStatement.setString(1, playerUuid);
        deleteStatement.setString(2, homeName);
        deleteStatement.executeUpdate();
        resultado.close();
        selectStatement.close();
        return true;


    }
    public static int contarHomesPorPlayer(String playerUuid) throws SQLException {
        String countSQL = "SELECT COUNT(*) FROM homes WHERE player_uuid = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(countSQL);
        preparedStatement.setString(1, playerUuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        int count = 0;
        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        resultSet.close();
        preparedStatement.close();
        return count;
    }
    public static List<String> obterNomesDasHomes(String playerUuid) throws SQLException {
        String selectSQL = "SELECT home_name FROM homes WHERE player_uuid = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
        preparedStatement.setString(1, playerUuid);

        ResultSet resultado = preparedStatement.executeQuery();

        List<String> homeNames = new ArrayList<>();
        while (resultado.next()) {
            homeNames.add(resultado.getString("home_name"));
        }

        resultado.close();
        preparedStatement.close();

        return homeNames;
    }

}
