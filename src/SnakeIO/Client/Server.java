package SnakeIO.Client;

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

    public Server(String host, int port) {
        this.host = host;
        this.port = port;

        this.inputThread = null;
        this.outputThread = null;

        this.din = null;
        this.dout = null;
    }

    public void connect() {
        try {
            Socket socket = new Socket(host, port);

            inputThread = new Thread(() -> {
                try {
                    this.din = new DataInputStream(socket.getInputStream());

                    while (true) {
                        System.out.println(din.readUTF());
                        //todo receive data
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            outputThread = new Thread(() -> {
                try {
                    this.dout = new DataOutputStream(socket.getOutputStream());

                    Scanner scanner = new Scanner(System.in);
                    while (true) {
                        dout.writeUTF(scanner.nextLine());
                        //todo send data
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
}
