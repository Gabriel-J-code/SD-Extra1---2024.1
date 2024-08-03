package back;

import front.ChatInterface;
import java.util.ArrayList;
import java.util.List;
import java.net.*;

public class ServerTCP implements SocketComunication {

    private ChatInterface chat; 
    private ServerTCPClientCatcher clientCatcher; 
    private List<ClientHandler> clients = new ArrayList<ClientHandler>();
    
    public ServerTCP(int serverPort, ChatInterface chat) {
        clientCatcher = new ServerTCPClientCatcher(serverPort,this);
        this.chat = chat;
        chat.setSocket(this);
        try {
            chat.reciveMessage("Aguardando conexao no endereco: " + InetAddress.getLocalHost() + ":" + serverPort);
        } catch (UnknownHostException e) {
            chat.reciveMessage(e.getMessage());
        }
        clientCatcher.start();
    }

    public void addClient(ClientHandler client){
        clients.add(client);
    }

    @Override
    public void sendMessage(String message){
        for (ClientHandler clientHandler : clients) {
           clientHandler.sendMessage(message); 
        }
    }

    public void sendClientMessageToOthers(ClientHandler client, String message){
        for (ClientHandler clientHandler : clients) {
            if (!clientHandler.equals(client)) {
                clientHandler.sendMessage(message);             
            }            
        }
    }

    @Override
    public void close() {
        for (ClientHandler clientHandler : clients) {
            clientHandler.close();
        }
        clientCatcher.close();
    }
    
    public void reciveMessage(ClientHandler client,String message) {
        this.sendClientMessageToOthers(client, message);
        chat.reciveMessage(message);
    }
}
