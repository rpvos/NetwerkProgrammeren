package SnakeIO.Client.ServerLogic;

import SnakeIO.Client.GameLogic.Fruit;
import SnakeIO.Client.GameLogic.Snake;
import SnakeIO.Client.LogicHub;
import SnakeIO.Data;
import sun.rmi.runtime.Log;

import java.awt.geom.Point2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private final String host;
    private final int port;

    private Thread inputThread;
    private Thread outputThread;

    private DataInputStream din;
    private DataOutputStream dout;
    private boolean running;


    public Server(String host, int port) {
        this.host = host;
        this.port = port;

        this.inputThread = null;
        this.outputThread = null;

        this.din = null;
        this.dout = null;

        this.running = true;
    }

    public void connect() {
        try {
            Socket socket = new Socket(host, port);

            this.din = new DataInputStream(socket.getInputStream());

            inputThread = new Thread(() -> {
                try {
                    //display if connected succesfully
                    System.out.println(din.readUTF());

                    //get the starting location of the snake
                    int x = din.readInt();
                    int y = din.readInt();
                    LogicHub.getLogicHub().setStart(x,y);
                    Snake snake = LogicHub.getLogicHub().getSnake();


//                    while (running) {
//                        System.out.println(din.readUTF());
//                        //step 1 receive if there was a collision between this snake head and another snake body
//                        if (din.readBoolean()){
//                            snake.isDead();
//                        }
//
//                        //step 2 receive if snake has eaten a fruit
//                        if (din.readBoolean()){
//                            snake.hasEaten();
//                        }
//                        // step 3 receive fruit positions and snake positions
//                        ArrayList<Fruit> fruits = new ArrayList<>();
//                        int amount = din.readInt();
//                        for (int i = 0; i < amount; i++) {
//                            //read x and y of the segment
//                            fruits.add(new Fruit(new Point2D.Double(din.readInt(), din.readInt())));
//                        }
//                        // step 4 set the fruit positions of the logic hub
//
//                        LogicHub.getLogicHub().setFruits(fruits);
//
//
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


            this.dout = new DataOutputStream(socket.getOutputStream());

            outputThread = new Thread(() -> {
//                try {
//                    Snake snake = LogicHub.getLogicHub().getSnake();
//
//                    while (running) {
//                        //send direction
//                        dout.writeUTF(snake.getDirection().toString());
//
//                        ArrayList<Point2D> positions = snake.getPositions();
//                        //send amount of segments
//                        dout.writeInt(positions.size());
//                        //send individual segment
//                        for (Point2D pos : positions) {
//                            dout.writeInt((int) pos.getX());
//                            dout.writeInt((int) pos.getY());
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
            dout.writeUTF(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startInput() {
        inputThread.start();
    }

    public void disconnect() {
        running = false;

        try {
            dout.writeUTF(Data.CLOSINGCONNECTION);

            din.close();
            dout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startOutput() {
        outputThread.start();
    }
}
