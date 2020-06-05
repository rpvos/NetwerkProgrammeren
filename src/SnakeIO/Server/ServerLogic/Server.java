package SnakeIO.Server.ServerLogic;

import SnakeIO.Data;
import SnakeIO.Server.GameLogic.GameField;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private boolean running;
    private final int port;
    private ArrayList<Client> clients;
    private GameField gameField;

    public static void main(String[] args) {
        Server server = new Server(Data.port);
        server.start();
    }

    private Server(int port) {
        this.running = true;
        this.port = port;
        this.clients = new ArrayList<>();
        this.gameField = new GameField();
    }

    private void start() {

        try (
                //make socket as resource so it closes automatically when an error occurs
                ServerSocket socket = new ServerSocket(this.port);
        ) {
            System.out.printf("Starting server\n ip: %s\n port: %d\n", socket.getInetAddress(), port);

            while (running) {
                System.out.println("Waiting for clients");
                Socket client = socket.accept();

                clients.add(new Client(client, gameField, this));
                System.out.println("Client connection from " + client.getInetAddress().getHostName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Client> getClients() {
        return clients;
    }


}