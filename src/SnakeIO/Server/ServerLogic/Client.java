package SnakeIO.Server.ServerLogic;

import SnakeIO.Client.LogicHub;
import SnakeIO.Data;
import SnakeIO.Server.GameLogic.Directions;
import SnakeIO.Server.GameLogic.GameField;
import SnakeIO.Server.GameLogic.Snake;

import java.awt.geom.Point2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private boolean running;
    private Socket socket;

    private Thread inputThread;
    private Thread outputThread;

    private DataInputStream din;
    private DataOutputStream dout;

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
                this.din = new DataInputStream(socket.getInputStream());
                this.username = this.din.readUTF();

                ArrayList<Point2D> positions = new ArrayList<>();

                while (running) {
                    String input = din.readUTF();
                    //check if the message is "closing connection"
                    if (input.equals("closing connection")) {
                        //if it is this close all the streams
                        disconnect();
                    } else {
                        //else it is snake direction or the snake positions
                        this.direction = Directions.valueOf(input);
                        //receive how many segments the snake has
                        int amount = din.readInt();
                        for (int i = 0; i < amount; i++) {
                            //read x and y of the segment
                            positions.add(new Point2D.Double(din.readInt(), din.readInt()));
                        }

                        //set the snake direction and the positions
                        this.snake.setDirection(direction);
                        this.snake.setPositions(positions);

                        positions.clear();
                        System.out.println("snake has been updated");
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.outputThread = new Thread(() -> {
            try {
                this.dout = new DataOutputStream(socket.getOutputStream());

                dout.writeUTF("Connected successfully to " + Data.serverName);

                Point2D pos = gameField.validSpot();
                dout.writeInt((int) pos.getX());
                dout.writeInt((int) pos.getY());

                while (running) {
                    //step 1 send if there was a collision between this snake head and another snake body
                    dout.writeBoolean(this.snake.isDead());
                    // step 2 send if snake has eaten a fruit
                    dout.writeBoolean(this.snake.isAte());
                    // step 3 send fruit positions and snake positions

                    dout.writeInt(this.server.getClients().size());

                    for (Client client : this.server.getClients()) {
                        Snake snake = client.getSnake();

                        //send direction
                        dout.writeUTF(snake.getDirection().toString());

                        ArrayList<Point2D> positions = snake.getPositions();
                        //send amount of segments
                        dout.writeInt(positions.size());
                        //send individual segment
                        for (Point2D posi : positions) {
                            dout.writeInt((int) posi.getX());
                            dout.writeInt((int) posi.getY());
                        }
                    }

                    dout.writeInt(this.gamefield.getFruits().size());

                    for (Point2D fruit: this.gamefield.getFruits()) {
                        dout.writeInt((int)fruit.getY());
                        dout.writeInt((int)fruit.getX());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.inputThread.start();
        this.outputThread.start();


    }

    private Snake getSnake(){
        return this.snake;
    }

    private void disconnect() {
        running = false;

        try {
            dout.close();
            din.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}