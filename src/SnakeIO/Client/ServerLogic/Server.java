package SnakeIO.Client.ServerLogic;

import SnakeIO.Client.GameLogic.Snake;
import SnakeIO.Client.LogicHub;
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

        this.running = false;
    }

    public void connect() {
        try {
            Socket socket = new Socket(host, port);
            this.running = true;

            inputThread = new Thread(() -> {
                try {
                    this.din = new DataInputStream(socket.getInputStream());
                    System.out.println(din.readUTF());

                    while (running) {
                        System.out.println(din.readUTF());
                        //todo step 1 receive if there was a collision between this snake head and another snake body
                        //todo step 2 receive if snake has eaten a fruit
                        //todo step 3 receive fruit positions and snake positions
                        //todo step 4 set the fruit positions of the logic hub
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            outputThread = new Thread(() -> {
                try {
                    this.dout = new DataOutputStream(socket.getOutputStream());

                    while (running) {
                        Snake snake = LogicHub.getLogicHub().getSnake();
                        //send direction
                        dout.writeUTF(snake.getDirection().toString());

                        ArrayList<Point2D> positions = snake.getPositions();
                        //send amount of segments
                        dout.writeInt(positions.size());
                        //send individual segment
                        for (Point2D pos : positions) {
                            dout.writeInt((int) pos.getX());
                            dout.writeInt((int) pos.getY());
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            inputThread.start();
            outputThread.start();

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

    public void disconnect() {
        running = false;

        try {
            dout.writeUTF("closing connection");

            din.close();
            dout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
