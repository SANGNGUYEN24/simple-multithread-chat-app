package hihi.sang;
// A Java program for a Client

import java.net.*;
import java.io.*;

import static java.lang.System.in;

public class Client {
    // Initialize socket and input output streams
    private Socket socket = null;
    // private DataInputStream input = null;
    private BufferedReader input = null;
    private DataOutputStream out = null;

    // Constructor to put ip address and port
    public Client(String address, int port) {
        // Establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // Takes input from terminal
            input = new BufferedReader(new InputStreamReader(in));

            // Sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException u) {
            System.out.println(u);
        }

        // String to read message from input
        String line = "";

        // Keep reading until "Over" is input
        while (!line.equals("Over")) {
            try {
                line = input.readLine();
                out.writeUTF(line);
            } catch (IOException i) {
                System.out.println(i);
            }
        }

        // Close the connection
        try {
            input.close();
            out.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 5000);
    }
}