package hihi.sang;
// A Java program for a Client
import java.io.*;
import java.net.*;
import java.util.Scanner;

// Client class
public class Client
{
    public static void main(String[] args) throws IOException
    {
        try
        {
            Scanner scanner = new Scanner(System.in);

            // Getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // Establish the connection with server port 5056
            Socket socket = new Socket(ip, 5056);

            // Obtaining input and out streams
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // The following loop performs the exchange of information between client and client handler
            while (true)
            {
                System.out.println(dis.readUTF());
                String tosend = scanner.nextLine();
                dos.writeUTF(tosend);

                // If client sends exit,close this connection
                // and then break from the while loop
                if(tosend.equals("Exit"))
                {
                    System.out.println("Closing this connection : " + socket);
                    socket.close();
                    System.out.println("Connection closed");
                    break;
                }

                // printing date or time as requested by client
                String received = dis.readUTF();
                System.out.println(received);
            }

            // closing resources
            scanner.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}