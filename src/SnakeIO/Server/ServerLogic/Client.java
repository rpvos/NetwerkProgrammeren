package SnakeIO.Server.ServerLogic;

import SnakeIO.Data;
import SnakeIO.DataSnake;
import SnakeIO.Directions;
import SnakeIO.Server.GameLogic.GameField;
import SnakeIO.Server.GameLogic.Snake;

import java.awt.geom.Point2D;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private boolean running;
    private Socket socket;

    private Thread inputThread;
    private Thread outputThread;

    private DataInputStream dIn;
    private DataOutputStream dOut;
    private ObjectInputStream oIn;
    private ObjectOutputStream oOut;

    private String username;

    private GameField gamefield;
    private Snake snake;
    private Directions direction;

    private Server server;

    public Client(Socket socket, GameField gameField, Server server) {
        this.running = true;
        this.socket = socket;

        this.server = server;

        this.gamefield = gameField;
        this.snake = new Snake(gameField.validSpot());
        //communicate this with client where the snake spawns
        this.gamefield.addSnake(snake);
        this.direction = null;

        this.inputThread = new Thread(() -> {
            try {
                this.dIn = new DataInputStream(socket.getInputStream());
                this.oIn = new ObjectInputStream(socket.getInputStream());
                this.username = this.dIn.readUTF();
                System.out.printf("User %s connected\n", this.username);

                ArrayList<Point2D> positions = new ArrayList<>();

                while (running) {
                    String input = dIn.readUTF();

                    //check if the message is "closing connection"
                    if (input.equals(Data.CLOSINGCONNECTION)) {
                        //if it is this close all the streams
                        disconnect();
                    } else if (input.equals(Data.DATASNAKE)) {
                        try {
                            //read in the data from snake
                            DataSnake dataSnake = (DataSnake) oIn.readObject();

                            //set the direction of the snake and the location
                            snake.setDirection(dataSnake.getDirection());
                            snake.setPositions(dataSnake.getSnakePositions());

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.outputThread = new Thread(() -> {
            try {
                this.dOut = new DataOutputStream(socket.getOutputStream());
                this.oOut = new ObjectOutputStream(socket.getOutputStream());

                dOut.writeUTF("Connected successfully to " + Data.serverName);

                Point2D pos = gameField.validSpot();
                dOut.writeInt((int) pos.getX());
                dOut.writeInt((int) pos.getY());

                while (running) {
                    DataSnake snakeData = new DataSnake(snake.getPositions(), snake.getDirection(), snake.isAte(), snake.isDead());
                    oOut.writeObject(snakeData);

                    oOut.writeObject(this.gamefield.getFruits());
                }

//                while (running) {
//                    //step 1 send if there was a collision between this snake head and another snake body
//                    dOut.writeBoolean(this.snake.isDead());
//                    // step 2 send if snake has eaten a fruit
//                    dOut.writeBoolean(this.snake.isAte());
//                    // step 3 send fruit positions and snake positions
//
//                    dOut.writeInt(this.server.getClients().size());
//
//                    for (Client client : this.server.getClients()) {
//                        Snake snake = client.getSnake();
//
//                        //send direction
//                        dOut.writeUTF(snake.getDirection().toString());
//
//                        ArrayList<Point2D> positions = snake.getPositions();
//                        //send amount of segments
//                        dOut.writeInt(positions.size());
//                        //send individual segment
//                        for (Point2D posi : positions) {
//                            dOut.writeInt((int) posi.getX());
//                            dOut.writeInt((int) posi.getY());
//                        }
//                    }
//
//                    dOut.writeInt(this.gamefield.getFruits().size());
//
//                    for (Point2D fruit : this.gamefield.getFruits()) {
//                        dOut.writeInt((int) fruit.getY());
//                        dOut.writeInt((int) fruit.getX());
//                    }
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.inputThread.start();
        this.outputThread.start();


    }

    private Snake getSnake() {
        return this.snake;
    }

    private void disconnect() {
        running = false;

        try {
            //close all the streams
            dOut.close();
            dIn.close();
            oOut.close();
            oIn.close();

            System.out.println(username+" disconnected from ip "+socket.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}