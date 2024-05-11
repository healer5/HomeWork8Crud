package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final Connection connection;

    public ClientService(Connection connection) {
        this.connection = Database.getInstance().getConnection();
    }

    public long create(String name) throws SQLException {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Invalid name provided: " + name);
        }
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO CLIENT (name) VALUES(?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            return generatedKeys.next() ? generatedKeys.getLong(1) : -1;
        } catch (SQLException e) {
            throw new SQLException("Error during executing query: " + e.getMessage(), e);
        }
    }

    public String getById(long id) throws SQLException {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("Invalid ID provided: " + id);
        }
        try (PreparedStatement statement = connection.prepareStatement("SELECT name FROM CLIENT WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getString("name") : null;
            }
        } catch (SQLException e) {
            throw new SQLException("Error: " + e.getMessage(), e);
        }
    }

    public void setName(long id, String name) throws SQLException {
        if (!isValidName(name) || !isValidId(id)) {
            throw new IllegalArgumentException("Invalid ID or name provided");
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE CLIENT SET name = ? WHERE id = ?")) {
            statement.setString(1, name);
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Next errors " + e.getMessage(), e);
        }
    }

    public void deleteById(long id) throws SQLException {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("Invalid ID provided: " + id);
        }
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM CLIENT WHERE id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error during executing query: " + e.getMessage(), e);
        }
    }

    public List<Client> listAll() {
        String query = "SELECT * FROM CLIENT";
        List<Client> clients = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Client client = new Client();
                client.setId((int) resultSet.getLong("id"));
                client.setName(resultSet.getString("name"));
                clients.add(client);
            }
            return clients;
        } catch (SQLException e) {
            logger.error("Error fetching list of clients: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    private boolean isValidId(long id) {
        if (id < 1) {
            logger.error("Invalid client ID: {}", id);
            return false;
        }
        return true;
    }

    private boolean isValidName(String name) {
        Objects.requireNonNull(name, "Client name must be defined");
        if (name.length() <= 2 || name.length() >= 50) {
            logger.error("Invalid client name length: {}", name.length());
            return false;
        }
        return true;
    }
}

