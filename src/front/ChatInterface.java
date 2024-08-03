package front;

import javax.swing.*;

import back.SocketComunication;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatInterface extends JFrame {
    private String name;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    private SocketComunication socket;

    public ChatInterface(String name) {   
        this.name = name;     
        // Configuração da janela principal
        setTitle("Chat Socket - "+name);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Área de texto para exibir as mensagens
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // Painel inferior com o campo de texto e botão de enviar
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        inputField = new JTextField();
        inputPanel.add(inputField, BorderLayout.CENTER);

        sendButton = new JButton("Enviar");
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        // Adiciona ação ao botão de enviar
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Adiciona ação ao campo de texto para enviar a mensagem ao pressionar Enter
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        setVisible(true);
    }

    public void setSocket(SocketComunication socket){
        this.socket = socket;
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.trim().isEmpty()) {
            socket.sendMessage(name + ": " + message);

            chatArea.append("Você: " + message + "\n");            
            inputField.setText("");
        }
    }

    public void reciveMessage(String message){
        if (!message.trim().isEmpty()) {
            chatArea.append(message + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ChatInterface chatInterface = new ChatInterface("default");
                chatInterface.setVisible(true);
            }
        });
    }
}
