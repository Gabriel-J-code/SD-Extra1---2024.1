package front;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.NumberFormatter;

import back.ClientTCP;
import back.ServerTCP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;

public class NetworkSetup extends JFrame {
    private JComboBox<String> roleSelector;
    private JTextField nameField;
    private JTextField ipField;
    private JFormattedTextField portField;
    private JButton okButton;
    private JButton cancelButton;

    public NetworkSetup() {
        // Configuração da janela principal
        setTitle("Configuração de Rede");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        // Campo de seleção para Server/Client
        roleSelector = new JComboBox<>(new String[] {"Client", "Server"});
        roleSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Server".equals(roleSelector.getSelectedItem().toString())) {
                    ipField.setEnabled(false);
                    ipField.setText("");
                } else {
                    ipField.setEnabled(true);
                }
            }
        });

        // Campo de texto para IP com filtro de entrada
        nameField = new JTextField();

        // Campo de texto para IP com filtro de entrada
        ipField = new JTextField();
        ((AbstractDocument) ipField.getDocument()).setDocumentFilter(new IPAddressFilter());
        ipField.setEnabled(true); 

        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        NumberFormatter numberFormatter = new NumberFormatter(format);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(0);
        numberFormatter.setMaximum(65535);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setCommitsOnValidEdit(true);
        portField = new JFormattedTextField(numberFormatter);
        portField.setText("6666");

        // Botão OK
        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                dispose(); // Fecha a janela
                ChatInterface chat = startNetwork();
                chat.setVisible(true);
            }
        });

        // Botão Cancelar
        cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Encerra a aplicação
            }
        });

        // Adiciona os componentes à janela
        add(new JLabel("Selecione o papel:"));
        add(roleSelector);
        add(new JLabel("Seu nome:"));
        add(nameField);
        add(new JLabel("IP:"));
        add(ipField);
        add(new JLabel("Porta:"));
        add(portField);
        add(okButton);
        add(cancelButton);

        // Configura a visibilidade da janela
        setVisible(true);
    }

    private ChatInterface startNetwork() {
        String role = roleSelector.getSelectedItem().toString();
        System.out.println(role);
        String name = nameField.getText();
        String ip = ipField.getText();
        int port = Integer.parseInt(portField.getText());       
        ChatInterface chat = new ChatInterface(name);;

        if (role == "Server") {
            new ServerTCP(port,chat);
            chat.setVisible(true);                   
        }else{
            try {
                new ClientTCP(ip, port, chat);
            } catch (IOException e) {
                e.printStackTrace();
            }
            chat.setVisible(true);            
        }

        return chat;

        // Aqui você pode adicionar o código para manipular as informações
        /*System.out.println("Papel: " + role);
        System.out.println("IP: " + ip);
        System.out.println("Porta: " + port);*/
    }
}

class IPAddressFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.insert(offset, string);

        if (isValidIPAddress(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.replace(offset, offset + length, text);

        if (isValidIPAddress(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.delete(offset, offset + length);

        if (isValidIPAddress(sb.toString())) {
            super.remove(fb, offset, length);
        }
    }

    private boolean isValidIPAddress(String text) {
        if (text.isEmpty()) {
            return true;
        }

        String[] parts = text.split("\\.");
        if (parts.length > 4) {
            return false;
        }

        for (String part : parts) {
            if (part.length() > 3 || !part.matches("\\d*") || (part.length() > 0 && Integer.parseInt(part) > 255)) {
                return false;
            }
        }

        return true;
    }
}
