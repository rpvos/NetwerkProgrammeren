package SnakeIO.Server.ServerLogic;

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

    public Client(Socket socket, GameField gameField) {
        this.running = true;
        this.socket = socket;

        this.gamefield = gameField;
        this.snake = new Snake(gameField.validSpot());
        //todo communicate this with client where the snake spawns
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
                    //todo step 1 send if there was a collision between this snake head and another snake body
                    //todo step 2 send if snake has eaten a fruit
                    //todo step 3 send fruit positions and snake positions
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.inputThread.start();
        this.outputThread.start();


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