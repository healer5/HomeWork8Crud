package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws SQLException {
        new DatabaseInitService().initDatabase();
        Connection connection = Database.getInstance().getConnection();
        ClientService clientService = new ClientService(connection);

        logger.info("Client {id = " + clientService.create("Helen") + ", name = \'Helen\'}");
        logger.info(clientService.getById(2));
        clientService.setName(4, "Olena");
        clientService.deleteById(3);


        List<Client> clients = clientService.listAll();
        for (Client client : clients) {
            logger.info(String.valueOf(client));
        }
    }
}