package back;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import front.ChatInterface;

public class ClientTCP implements SocketComunication{
    private Socket socket;
    private DataOutputStream output;  
    private ChatInterface chat; 
    private ClientMessageCatcher msgCatcher; 

    public ClientTCP(String serverIp, int serverPort, ChatInterface chat) throws IOException{
        socket = new Socket(serverIp, serverPort);
        output = new DataOutputStream(socket.getOutputStream());
        msgCatcher = new ClientMessageCatcher(this);        
        this.chat = chat;
        chat.setSocket(this);
        chat.reciveMessage("Conexão estabelecida");
        msgCatcher.start();
    }   

    public Socket getSocket(){
        return this.socket;
    }

    public ChatInterface getChat(){
        return this.chat;
    }

    @Override
    public void sendMessage(String message) {
        try {
            output.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            msgCatcher.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    
} 