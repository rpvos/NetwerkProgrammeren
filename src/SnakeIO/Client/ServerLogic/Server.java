package SnakeIO.Client.ServerLogic;

import SnakeIO.Client.GameLogic.Snake;
import SnakeIO.Client.LogicHub;
import SnakeIO.Data;
import SnakeIO.DataSnake;
import SnakeIO.Timer;

import java.awt.geom.Point2D;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private final String host;
    private final int port;
    private String serverName;

    private Thread inputThread;
    private Thread outputThread;

    private DataInputStream dIn;
    private DataOutputStream dOut;

    private ObjectInputStream oIn;
    private ObjectOutputStream oOut;

    private boolean running;
    private boolean userNameIsSet;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;

        this.inputThread = null;
        this.outputThread = null;

        this.dIn = null;
        this.dOut = null;

        this.oIn = null;
        this.oOut = null;

        this.running = true;
        this.userNameIsSet = false;
    }

    public void connect() {
        try {
            Socket socket = new Socket(host, port);

            this.dIn = new DataInputStream(socket.getInputStream());
            this.oIn = new ObjectInputStream(socket.getInputStream());

            inputThread = new Thread(() -> {
                try {
                    //display if connected succesfully
                    this.serverName = dIn.readUTF();
                    System.out.println("Connected successfully to " + serverName);

                    //get the starting location of the snake
                    int x = dIn.readInt();
                    int y = dIn.readInt();
                    LogicHub.getLogicHub().setStart(x, y);

                    while (running) {
                        //data gotten from the server
                        DataSnake dataSnake = (DataSnake) oIn.readObject();
                        //the snake from the client
                        Snake snake = LogicHub.getLogicHub().getSnake();

                        //if the server detected a collision the snake died
                        if (dataSnake.isDead())
                            snake.died();

                        //if the server detected that the snake ate something, the snake is getting another part
                        if (dataSnake.isAte()) {
                            snake.hasEaten();
                        }

                        ArrayList<Point2D> fruits = new ArrayList<>();
                        int amount = dIn.readInt();
                        for (int i = 0; i < amount; i++) {
                            fruits.add((Point2D) oIn.readObject());
                        }
                        System.out.println(fruits);//todo remove
                        LogicHub.getLogicHub().setFruits(fruits);
                    }
                } catch (ClassNotFoundException | IOException ignored) {
                }
            });


            this.dOut = new DataOutputStream(socket.getOutputStream());
            this.oOut = new ObjectOutputStream(socket.getOutputStream());

            outputThread = new Thread(() -> {
                try {
                    Timer timer = new Timer(1000);

                    while (running) {
                        if (userNameIsSet) {
                            if (timer.timeout()) {
                                dOut.writeUTF(Data.DATASNAKE);

                                Snake snake = LogicHub.getLogicHub().getSnake();
                                DataSnake dataSnake = new DataSnake(snake.getPositions(), snake.getDirection(), false, snake.isDead());
                                oOut.writeObject(dataSnake);
                                timer.mark();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            System.out.println("No server detected");
        }
    }

    public void setUsername(String username) {
        userNameIsSet = true;
        try {
            dOut.writeUTF(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startInput() {
//        if (inputThread != null)//todo ugly
        inputThread.start();
    }

    public void disconnect() {
        running = false;

        try {
            dOut.writeUTF(Data.CLOSINGCONNECTION);

            dIn.close();
            dOut.close();

            oIn.close();
            oOut.close();

            System.out.println("Disconnected from " + serverName);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startOutput() {
        outputThread.start();
    }
}
