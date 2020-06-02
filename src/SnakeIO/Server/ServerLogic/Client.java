package SnakeIO.Server.ServerLogic;

import SnakeIO.Data;

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

    public Client(Socket socket) {
        this.socket = socket;


        this.inputThread = new Thread(() -> {
            try {
                this.din = new DataInputStream(socket.getInputStream());
                this.username = this.din.readUTF();

                while (true) {
                    System.out.println(username + ": " + din.readUTF());

                    //todo receive data here
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

                //todo send data here
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.inputThread.start();
        this.outputThread.start();


    }
}