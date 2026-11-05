package org.example.udp;

import org.example.View.ClientView;
import javax.swing.SwingUtilities;

public class UDP {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientView view = new ClientView();
            view.setVisible(true);
        });
    }
}