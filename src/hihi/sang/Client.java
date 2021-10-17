package hihi.sang;
// A Java program for a Client

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.in;

public class Client {
    private String clientName;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Socket socket;

    public Client(Socket socket, String clientName) {
        this.clientName = clientName;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.socket = socket;
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(clientName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(in);
            while (socket.isConnected()) {
                String messToBeSent = scanner.nextLine();
                bufferedWriter.write(clientName + ": " + messToBeSent);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void readMessage() {

        new Thread(() -> {
            String messFromGroupChat;
            try{
                while(socket.isConnected()){
                    messFromGroupChat = bufferedReader.readLine();
                    System.out.println(messFromGroupChat);
                }
            } catch (IOException e) {
                e.printStackTrace();
                closeEverything(socket, bufferedWriter, bufferedReader);
            }
        }).start();

    }

    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
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

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(in);
        System.out.println("Enter your nickname: ");
        String clientName = scanner.nextLine();

        Socket socket =  new Socket("localhost", 2001);
        Client client = new Client(socket, clientName);
        client.readMessage();
        client.sendMessage();
    }
}


