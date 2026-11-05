package org.example.udp;

import org.example.Control.ServerControl;
import org.example.View.ServerView;

public class ServerRun {
    public static void main(String[] args) {
        try {
            ServerView view = new ServerView();
            new ServerControl(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}