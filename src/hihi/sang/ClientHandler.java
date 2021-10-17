package hihi.sang;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

public class ClientHandler implements Runnable {
    public static Vector<ClientHandler> clientList = new Vector<>();
    private Socket socket;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String clientName;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientName = bufferedReader.readLine();
            clientList.add(this);
            broadcastMessage("SERVER: " + clientName + " has entered the group chat!");

        } catch (Exception e) {
            e.printStackTrace();
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    @Override
    public void run() {
        String messFromClient;
        while (socket.isConnected()) {
            try {
                messFromClient = bufferedReader.readLine();
                broadcastMessage(messFromClient);
            } catch (IOException e) {
                e.printStackTrace();
                closeEverything(socket, bufferedWriter, bufferedReader);
                break;
            }
        }

    }


    public void broadcastMessage(String messToBeSent) {
        for (ClientHandler client : clientList) {
            try{
            if (!client.clientName.equals(this.clientName)) {
                client.bufferedWriter.write(messToBeSent);
                // \n
                client.bufferedWriter.newLine();
                // Flush the buffer before it's full
                client.bufferedWriter.flush();
            }

            } catch (IOException e) {
                e.printStackTrace();
                closeEverything(socket, bufferedWriter, bufferedReader);
            }
        }
    }

    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        removeClient();
        try {
            if (socket != null)
                socket.close();
            if (bufferedWriter != null)
                bufferedWriter.close();
            if (bufferedReader != null)
                bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void removeClient(){
        clientList.remove(this);
        broadcastMessage("SERVER: " + this.clientName + " has left the group chat!");

    }
}
