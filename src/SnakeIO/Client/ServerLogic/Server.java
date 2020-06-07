package SnakeIO.Client.ServerLogic;

import SnakeIO.Client.GameLogic.Snake;
import SnakeIO.Client.LogicHub;
import SnakeIO.Data;
import SnakeIO.DataSnake;

import java.awt.geom.Point2D;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private final String host;
    private final int port;

    private Thread inputThread;
    private Thread outputThread;

    private DataInputStream dIn;
    private DataOutputStream dOut;

    private ObjectInputStream oIn;
    private ObjectOutputStream oOut;

    private boolean running;

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
    }

    public void connect() {
        try {
            Socket socket = new Socket(host, port);

            this.dIn = new DataInputStream(socket.getInputStream());
            this.oIn = new ObjectInputStream(socket.getInputStream());

            inputThread = new Thread(() -> {
                try {
                    //display if connected succesfully
                    System.out.println(dIn.readUTF());

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
                            snake.isDead();

                        //if the server detected that e ate something, the snake is getting another part
                        if (dataSnake.isAte())
                            snake.hasEaten();


                        ArrayList<Point2D> fruits = (ArrayList<Point2D>) oIn.readObject();
                        System.out.println(fruits.size());
                        LogicHub.getLogicHub().setFruits(fruits);
                    }
                } catch (IOException e) {
                    System.out.println("No server detected");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });


            this.dOut = new DataOutputStream(socket.getOutputStream());
            this.oOut = new ObjectOutputStream(socket.getOutputStream());

            outputThread = new Thread(() -> {
//                try {
//                    Snake snake = LogicHub.getLogicHub().getSnake();
//
//                    while (running) {
//                        //send direction
//                        dOut.writeUTF(snake.getDirection().toString());
//
//                        ArrayList<Point2D> positions = snake.getPositions();
//                        //send amount of segments
//                        dOut.writeInt(positions.size());
//                        //send individual segment
//                        for (Point2D pos : positions) {
//                            dOut.writeInt((int) pos.getX());
//                            dOut.writeInt((int) pos.getY());
//                        }
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
        try {
            dOut.writeUTF(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startInput() {
        if (inputThread != null)//todo ugly
            inputThread.start();
    }

    public void disconnect() {
        running = false;

        try {
            dOut.writeUTF(Data.CLOSINGCONNECTION);

            dIn.close();
            dOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startOutput() {
        outputThread.start();
    }
}
