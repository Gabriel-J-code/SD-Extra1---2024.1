package back;

import java.io.IOException;
import java.net.*;

public class ServerTCPClientCatcher extends Thread{
    private ServerTCP server;
    private ServerSocket serverSocket;

    public ServerTCPClientCatcher(int serverPort, ServerTCP serverTCP) {
        this.server = serverTCP;
        try {
            serverSocket =  new ServerSocket(serverPort);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            while (serverSocket.isBound()) {
                Socket clientSocket = serverSocket.accept();                
                ClientHandler handler = new ClientHandler(clientSocket, server);
                server.addClient(handler);
                handler.start();                
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
