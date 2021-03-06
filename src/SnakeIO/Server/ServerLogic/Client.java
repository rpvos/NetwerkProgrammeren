package SnakeIO.Server.ServerLogic;

import SnakeIO.Data;
import SnakeIO.DataSnake;
import SnakeIO.Directions;
import SnakeIO.Server.GameLogic.GameField;
import SnakeIO.Server.GameLogic.Snake;
import SnakeIO.Timer;
import com.sun.jndi.ldap.pool.PoolCleaner;

import java.awt.geom.Point2D;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private boolean running;
    private Socket socket;

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

        //check if the message is "closing connection"
        //if it is this close all the streams
        //read in the data from snake
        //set the direction of the snake and the location
        Thread inputThread = new Thread(() -> {
            try {
                this.dIn = new DataInputStream(socket.getInputStream());
                this.oIn = new ObjectInputStream(socket.getInputStream());
                this.username = this.dIn.readUTF();
                System.out.printf("User %s connected\n", this.username);

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

        Thread outputThread = new Thread(() -> {
            try {
                this.dOut = new DataOutputStream(socket.getOutputStream());
                this.oOut = new ObjectOutputStream(socket.getOutputStream());

                dOut.writeUTF(Data.serverName);

                Point2D pos = gameField.validSpot();
                dOut.writeInt((int) pos.getX());
                dOut.writeInt((int) pos.getY());

                Timer timer = new Timer();

                while (running) {
                    if (timer.timeout()) {
                        dOut.writeUTF(Data.DATASNAKE);
                        DataSnake snakeData = new DataSnake(snake.getPositions(), snake.getDirection(), snake.isAte(), snake.isDead());
                        oOut.writeObject(snakeData);

                        dOut.writeUTF(Data.DATAFRUIT);
                        ArrayList<Point2D> fruits = gameField.getFruits();
                        int amount = fruits.size();
                        dOut.writeInt(amount);
                        for (Point2D fruit : fruits) {
                            oOut.writeObject(fruit);
                        }

                        dOut.writeUTF(Data.DATAOTHERSNAKES);
                        ArrayList<Client> clients = new ArrayList<>(Server.getClients());
                        clients.remove(this);
                        dOut.writeInt(clients.size());
                        for (Client client : clients) {
                            Snake snake = client.snake;
                            DataSnake dataSnake = new DataSnake(snake.getPositions(), snake.getDirection(), false, snake.isDead());
                            oOut.writeObject(dataSnake);

                            ArrayList<Point2D> positions = snake.getPositions();
                            dOut.writeInt(positions.size());
                            for (int i = 0; i < positions.size(); i++) {
                                oOut.writeObject(positions.get(i));
                            }
                        }



                        timer.mark();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        inputThread.start();
        outputThread.start();


    }

    private void disconnect() {
        running = false;

        try {
            //give the streams time to finnish
            Thread.sleep(500);
            //close all the streams
            dOut.close();
            dIn.close();
            oOut.close();
            oIn.close();

            gamefield.removeSnake(snake);

            System.out.println(username + " disconnected from ip " + socket.getInetAddress());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}