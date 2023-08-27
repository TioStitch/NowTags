package de.tiostitch.tags.data;

import org.bukkit.command.CommandException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerData {

    private Connection connection;
    private final String host, database, username, password;
    private final int port;

    public PlayerData(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    // Método para estabelecer uma conexão com o banco de dados
    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            connection = DriverManager.getConnection(url, username, password);
        }
    }

    // Método para desconectar do banco de dados
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // Método para criar a tabela de jogadores, se ela não existir
    public void createTable() throws SQLException {
        connect();
        String sql = "CREATE TABLE IF NOT EXISTS rank_players (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "uuid VARCHAR(36) NOT NULL," +
                "nickname VARCHAR(16) NOT NULL," +
                "tag_atual VARCHAR(16) NOT NULL," +
                "tags TEXT NOT NULL" +
                ")";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.executeUpdate();
        statement.close();
    }

    public void setPlayerTag(String playerName, String tag) {
        try {
            String query = "UPDATE rank_players SET tag_atual = ? WHERE nickname = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, tag);
            statement.setString(2, playerName);
            statement.executeUpdate();
        } catch (SQLException | CommandException | IndexOutOfBoundsException ignored) {
        }
    }

    public String getPlayerTag(String playerName) throws SQLException {
        String query = "SELECT tag_atual FROM rank_players WHERE nickname = ?";
        String tag_atual = "";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerName);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                tag_atual = result.getString("tag_atual");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tag_atual;
    }

    public void addPlayerTag(String playerName, String amigoName) {
        try {
            String query = "UPDATE rank_players SET tags = CONCAT(tags, ?) WHERE nickname = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "," + amigoName);
            statement.setString(2, playerName);
            statement.executeUpdate();
        } catch (SQLException | CommandException | IndexOutOfBoundsException ignored) {
        }
    }

    public void removePlayerTag(String playerName, String amigoName) {
        try {
            String query = "UPDATE rank_players SET tags = REPLACE(tags, ?, '') WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "," + amigoName);
            statement.setString(2, playerName);
            statement.executeUpdate();
        } catch (SQLException | CommandException | IndexOutOfBoundsException ignored) {
        }
    }

    public List<String> playerTags(String playerName) {
        List<String> amigos = new ArrayList<>();
        try {
            String query = "SELECT tags FROM rank_players WHERE nickname = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerName);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String friends = result.getString("tags");
                if (friends != null) {
                    amigos = new ArrayList<>(Arrays.asList(friends.split(",")));
                    amigos.remove(0); // Remove o primeiro elemento vazio
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amigos;
    }

    // Método para criar um jogador, se ele não existir
    public void createPlayer(String uuid, String nickname) throws SQLException {
        connect();
        String checkIfExists = "SELECT id FROM rank_players WHERE uuid = ?";
        PreparedStatement checkStatement = connection.prepareStatement(checkIfExists);
        checkStatement.setString(1, uuid);
        if (!checkStatement.executeQuery().next()) { // O jogador não existe ainda
            String sql = "INSERT INTO rank_players (uuid, nickname, tag_atual) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);
            statement.setString(2, nickname);
            statement.setString(3, "");
            statement.executeUpdate();
            statement.close();
        }
        checkStatement.close();
    }
}