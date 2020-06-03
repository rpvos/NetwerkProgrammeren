package SnakeIO.Client.ServerLogic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
                        //todo send snake postitions
//                        dout
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
            dout.writeUTF("close connection");

            din.close();
            dout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
