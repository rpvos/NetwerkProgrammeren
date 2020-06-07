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
    private Thread updateThread;

    public static void main(String[] args) {
        Server server = new Server(Data.port);
        server.start();
    }

    private Server(int port) {
        this.running = true;
        this.port = port;
        this.clients = new ArrayList<>();
        this.gameField = new GameField();
        //the getto update timer
        this.updateThread = new Thread(() -> {
            long lastTick = -1;
            while (running) {
                if (lastTick == -1)
                    lastTick = System.currentTimeMillis();

                long now = System.currentTimeMillis();
                double var = (now - lastTick) / 1000.0;

                lastTick = now;
                update(var);
            }
        });
    }

    private void update(double deltaTime) {
        this.gameField.update(deltaTime);
    }

    private void start() {

        try (
                //make socket as resource so it closes automatically when an error occurs
                ServerSocket socket = new ServerSocket(this.port)
        ) {
            System.out.printf("Starting server\n ip: %s\n port: %d\n", socket.getInetAddress(), port);
            this.updateThread.start();

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


}