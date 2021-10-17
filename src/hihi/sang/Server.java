package hihi.sang;

// A Java program for a Server

import java.io.*;
import java.util.*;
import java.net.*;

// Server class
public class Server {

    // Vector to store active clients
    static Vector<ClientHandler> activeClients = new Vector<>();

    // Counter for clients
    static int i = 0;

    public static void main(String[] args) throws IOException {
        // Server is listening on port 1234
        ServerSocket serverSocket = new ServerSocket(1234);

        Socket socket;

        // Running infinite loop for getting client request
        while (true) {
            // Accept the incoming request
            socket = serverSocket.accept();

            System.out.println("New client request received : " + socket);

            // Obtain input and output streams
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            System.out.println("Creating a new handler for this client...");

            // Create a new handler object for handling this request.
            ClientHandler client = new ClientHandler(socket, "client " + i, dis, dos);

            // Create a new Thread with this object.
            Thread thread = new Thread(client);

            System.out.println("Adding this client to active client list");

            System.out.println(dis);
            System.out.println(dos);
            // Add this client to active clients list
            activeClients.add(client);

            // Start the thread.
            thread.start();

            // Increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;
        }
    }
}

// ClientHandler class
class ClientHandler implements Runnable {
    Scanner scn = new Scanner(System.in);
    private final String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket socket;
    boolean isloggedin;

    // constructor
    public ClientHandler(Socket socket, String name,
                         DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.socket = socket;
        this.isloggedin = true;
        System.out.println("ClientHandler is called");
    }

    @Override
    public void run() {

        String received;
        while (true) {
            try {
                // Receive the string
                received = dis.readUTF();

                System.out.println(received);

                if (received.equals("logout")) {
                    this.isloggedin = false;
                    this.socket.close();
                    break;
                }

                // Break the string into message and recipient part
                StringTokenizer st = new StringTokenizer(received, "#");
                String MsgToSend = st.nextToken();
                String recipient = st.nextToken();

                // Search for the recipient in the connected devices list.
                // [activeClients] is the vector storing client of active users
                for (ClientHandler client : Server.activeClients) {
                    // If the recipient is found, write on its
                    // output stream
                    System.out.println("Print message");
                    if (client.name.equals(recipient) && client.isloggedin) {
                        client.dos.writeUTF(this.name + " : " + MsgToSend);
                        break;
                    }
                }
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        try {
            // Closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}