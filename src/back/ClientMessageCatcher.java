package back;
import java.io.DataInputStream;
import java.io.IOException;

public class ClientMessageCatcher extends Thread{
    DataInputStream in;
    ClientTCP client;

    public ClientMessageCatcher(ClientTCP client) {
        try {
            this.client = client;
            in = new DataInputStream(client.getSocket().getInputStream());            
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        try { // an echo server
            while (client.getSocket().isBound()) {
                String data = in.readUTF();
                client.getChat().reciveMessage(data);
            }
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
            try {
                client.getSocket().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } 
    }

    public void close(){
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
