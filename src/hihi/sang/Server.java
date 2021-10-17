package hihi.sang;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    // Constructor
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServerSocket() throws IOException {
        // Check not null before access
        if (serverSocket != null) serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2001);
        Server server = new Server(serverSocket);
        server.startServer();
    }


}