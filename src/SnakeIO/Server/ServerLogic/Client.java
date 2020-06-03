package SnakeIO.Server.ServerLogic;

import SnakeIO.Data;
import SnakeIO.Server.GameLogic.GameField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket socket;

    private Thread inputThread;
    private Thread outputThread;

    private DataInputStream din;
    private DataOutputStream dout;

    private String username;

    private GameField gamefield;

    public Client(Socket socket, GameField gameField) {
        this.socket = socket;
        this.gamefield = gameField;


        this.inputThread = new Thread(() -> {
            try {
                this.din = new DataInputStream(socket.getInputStream());
                this.username = this.din.readUTF();

                while (true) {
                    String input = din.readUTF();
                    System.out.println(username + ": " + input);
                    //todo check if the message is "closing connection"
                    //todo if it is this close all the streams

                    //todo else it is normal data for the snake positions
                }

            } catch (IOException e) {
                //todo implement good disconnect handling
                e.printStackTrace();
            }
        });

        this.outputThread = new Thread(() -> {
            try {
                this.dout = new DataOutputStream(socket.getOutputStream());

                dout.writeUTF("Connected successfully to " + Data.serverName);

                while(true){
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
}