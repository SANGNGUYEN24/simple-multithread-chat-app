package hihi.sang;

// A Java program for a Server

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

// Server class
public class Server
{
    public static void main(String[] args) throws IOException
    {
        // Server is listening on port 5056
        ServerSocket serverSocket = new ServerSocket(5056);
        // Running infinite loop for getting client request
        while (true)
        {
            Socket socket = null;
            try
            {
                // Socket object to receive incoming client requests
                socket = serverSocket.accept();

                System.out.println("A new client is connected : " + socket);

                // Obtaining input and out streams
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // Create a new thread object
                Thread thread = new ClientHandler(socket, dis, dos);

                // Invoking the start() method
                thread.start();

            }
            catch (Exception e){
                socket.close();
                e.printStackTrace();
            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread {
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket socket;


    // Constructor
    public ClientHandler(Socket socket, DataInputStream dis, DataOutputStream dos) {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        String received;
        String toreturn;
        while (true) {
            try {
                // Ask user what he wants
                dos.writeUTF("What do you want?[Date | Time]..\n" +
                        "Type Exit to terminate connection.");

                // Receive the answer from client
                received = dis.readUTF();

                if (received.equals("Exit")) {
                    System.out.println("Client " + this.socket + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.socket.close();
                    System.out.println("Connection closed");
                    break;
                }

                // Creating Date object
                Date date = new Date();

                // Write on output stream based on the answer from the client
                switch (received) {
                    case "Date" -> {
                        toreturn = fordate.format(date);
                        dos.writeUTF(toreturn);
                    }
                    case "Time" -> {
                        toreturn = fortime.format(date);
                        dos.writeUTF(toreturn);
                    }
                    default -> dos.writeUTF("Invalid input");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}