#Socket programming

_Prerequisite: Assuming that Java JDK is installed in your OS._

**How to run:**

1. Cd to `hihi.sang` directory and run the command to activate the server class.

    - java Server.java

2. Run this command to create a client:
    - java Client.java

**When you run Server:**
- `server = new ServerSocket(port);` is called to create a ServerSocket with port 5000.
- ` socket = server.accept();` will wait for a client.

**When you run Client**
- A client is created with an **ip address** and port **5000**, it opens an Input Stream and an Output Stream.
- Client came, Server will get the socket from the client, the Input Stream and listen messages.
- When the client write a message into terminal, `out.writeUTF(line);` will write that message into the client's Output Stream, which sends the message to Server's Input Stream.
- Server receives messages from Input Stream of the server's socket and display them.