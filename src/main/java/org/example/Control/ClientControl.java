package org.example.Control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.example.Model.Request;
import org.example.Model.Response;

public class ClientControl {
    private int serverPort = 5555;
    private int clientPort = 6666;
    private String serverHost = "localhost";
    private DatagramSocket myClient;

    public ClientControl() {
    }
    // mở socket UDP ở cổng client 6666
    public void openConnection() throws Exception {
        myClient = new DatagramSocket(clientPort);
        myClient.setSoTimeout(5000);
    }
    // đóng socket sau khi dùng xong.
    public void closeConnection() {
        if (myClient != null) {
            myClient.close();
        }
    }
    // chuyển Request thành byte rồi gửi đến server 5555.
    public void sendData(Request request) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);   // cái này chuyển này

        oos.writeObject(request);
        oos.flush();

        InetAddress address = InetAddress.getByName(serverHost);
        byte[] sendData = baos.toByteArray();

        DatagramPacket sendPacket = new DatagramPacket(
                sendData, sendData.length, address, serverPort
        );

        myClient.send(sendPacket);
    }
    // nhận byte và khôi phục thành Response.
    public Response receiveData() throws Exception {
        byte[] receiveData = new byte[60000];

        DatagramPacket receivePacket = new DatagramPacket(
                receiveData, receiveData.length
        );

        myClient.receive(receivePacket);

        ByteArrayInputStream bais = new ByteArrayInputStream(
                receivePacket.getData(),
                0,
                receivePacket.getLength()
        );

        ObjectInputStream ois = new ObjectInputStream(bais);

        return (Response) ois.readObject();
    }
}