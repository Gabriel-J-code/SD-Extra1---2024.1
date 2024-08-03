package back;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

class ClientHandler extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    private ServerTCP server;

    public ClientHandler(Socket aClientSocket, ServerTCP server) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.server = server;
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public Socket getClientSocket(){
        return clientSocket;
    }

    public void sendMessage(String message){
        try {
            out.writeUTF(message);
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
            try {
                clientSocket.close();
            } catch (IOException f) {
                /* close failed */}
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
            try {
                clientSocket.close();
            } catch (IOException f) {
                /* close failed */}
        }
    }

    public void run() {
        try { // an echo server
            while (clientSocket.isBound()) {
                String data = in.readUTF(); 
                server.reciveMessage(this, data);               
            }            
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                /* close failed */}
        }
    }

    public void close(){
        try {
            this.in.close();
            this.out.close();
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}