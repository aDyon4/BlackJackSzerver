package com.example.bjszerver;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.LightBase;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;


public class HelloController {


    @FXML private TextField tfText;
    @FXML private ListView<String> lvList;
    @FXML private Label f;

    DatagramSocket socket = null;
    Thread thread = null;

    public void initialize(){
        try {
            socket = new DatagramSocket(678);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                fogad();
            }
        });
        thread.setDaemon(true);
        thread.start();

    }

    private void fogad(){
        byte[] adat = new byte[256];
        DatagramPacket packet = new DatagramPacket(adat, adat.length);
        while (true) {
            try {
                socket.receive(packet);
                String uzenet = new String(adat, 0, packet.getLength(), "utf-8");
                String ip = packet.getAddress().getHostAddress();
                int port = packet.getPort();
                Platform.runLater(() -> onFogad(uzenet, ip, port));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onFogad(String uzenet, String ip, int port) {
        String[] s = uzenet.split(":");

        if (s[0].equals("join")) {
            String osszeg = s[1];
            f.setText("Összeg: " + s[1]);
            kuld("joined:"+s[1], ip, port);

        }
        else if(s[0].equals("bet")){
            System.out.printf("bet:%s", s[1]);
            String tet = s[1];
        }
        else if(s[0].equals("exit")){
            System.out.println("exit");
            f.setText("KILÉPETT");
            kuld("paid:", ip, port);
        }
    }

    private void kuld(String uzenet, String ip, int port){
        try {
            byte[] adat = uzenet.getBytes("utf-8");
            InetAddress ipv4 = Inet4Address.getByName(ip);
            DatagramPacket packet = new DatagramPacket(adat, adat.length, ipv4, port);
            socket.send(packet);
            System.out.printf("%s:%d -> %s\n", ip, port, uzenet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}