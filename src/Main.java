
import javax.swing.*;

import front.NetworkSetup;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NetworkSetup();
            }
        });
    }
}
