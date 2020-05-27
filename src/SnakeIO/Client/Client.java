package SnakeIO.Client;

import SnakeIO.Data;

public class Client {


    private final int port;
    private final Server server;

    public static void main(String[] args) {
        Client client = new Client(Data.host,Data.port);
        client.connect();
    }

    public Client(String host, int port) {
        this.port = port;
        this.server = new Server(host, port);
    }

    private void connect() {
        server.connect();
    }
}
