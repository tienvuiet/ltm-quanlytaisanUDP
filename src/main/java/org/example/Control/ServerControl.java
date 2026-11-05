package org.example.Control;

import org.example.Model.Phong;
import org.example.Model.Request;
import org.example.Model.Response;
import org.example.Model.TaiSan;
import org.example.View.ServerView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ServerControl {
    private Connection con;
    private DatagramSocket myServer;
    private ServerView view;
    private int serverPort = 5555;
    // 	Gói vừa nhận, đồng thời chứa địa chỉ client để trả lời
    private DatagramPacket receivePacket;

    public ServerControl(ServerView view) throws Exception {
        this.view = view;
        con = DBConnection.getConnection();
        openServer(serverPort);
        view.showMessage("UDP server đang chạy ở cổng " + serverPort);
        while (true) {
            listenning();
        }
    }
    // 	Mở cổng UDP 5555
    private void openServer(int portNumber) throws Exception {
        myServer = new DatagramSocket(portNumber);
    }
    //	Nhận yêu cầu, chọn chức năng và trả kết quả
    private void listenning() throws Exception {
        Response response;

        try {
            Request request = receiveData();

            if (request == null || request.action == null) {
                response = new Response(false, "Yêu cầu không hợp lệ.");
            } else {
                view.showMessage("Nhận yêu cầu: " + request.action);

                switch (request.action) {
                    case "SUA_PHONG":
                        response = suaPhong(request.phong);
                        break;
                    case "SUA_TAI_SAN":
                        response = suaTaiSan(request.taiSan);
                        break;
                    default:
                        response = new Response(
                                false, "Chức năng chưa được hỗ trợ."
                        );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response(false, "Lỗi xử lý yêu cầu.");
        }

        if (receivePacket != null && receivePacket.getAddress() != null) {
            sendData(response);
        }
    }
//	Khôi phục Request từ dữ liệu nhận được
    private Request receiveData() throws Exception {
        byte[] receiveData = new byte[60000];

        receivePacket = new DatagramPacket(
                receiveData, receiveData.length
        );

        myServer.receive(receivePacket);

        ByteArrayInputStream bais = new ByteArrayInputStream(
                receivePacket.getData(),
                0,
                receivePacket.getLength()
        );

        ObjectInputStream ois = new ObjectInputStream(bais);

        return (Request) ois.readObject();
    }
//	Gửi Response về client
    private void sendData(Response response) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(response);
        oos.flush();

        byte[] sendData = baos.toByteArray();

        InetAddress address = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();

        DatagramPacket sendPacket = new DatagramPacket(
                sendData, sendData.length, address, clientPort
        );

        myServer.send(sendPacket);
    }

    private Response suaPhong(Phong phong) throws Exception {
        if (phong == null
                || phong.maPhong == null
                || phong.maPhong.trim().isEmpty()
                || phong.tenPhong == null
                || phong.tenPhong.trim().isEmpty()) {
            return new Response(false, "Mã và tên phòng không được trống.");
        }

        String sql = "UPDATE phong SET ten_phong=?, mo_ta=? "
                + "WHERE ma_phong=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phong.tenPhong);
            ps.setString(2, phong.moTa);
            ps.setString(3, phong.maPhong);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                return new Response(true, "Sửa phòng thành công!");
            }

            return new Response(false, "Không tìm thấy phòng.");
        }
    }

    private Response suaTaiSan(TaiSan taiSan) throws Exception {
        if (taiSan == null
                || taiSan.maTaiSan == null
                || taiSan.maTaiSan.trim().isEmpty()
                || taiSan.tenTaiSan == null
                || taiSan.tenTaiSan.trim().isEmpty()
                || taiSan.loai == null
                || taiSan.loai.trim().isEmpty()
                || taiSan.maPhong == null
                || taiSan.maPhong.trim().isEmpty()
                || taiSan.giaTri == null
                || taiSan.giaTri.signum() < 0) {

            return new Response(false, "Thông tin tài sản không hợp lệ.");
        }

        String sql = "UPDATE tai_san "
                + "SET ten_tai_san=?, loai=?, ma_phong=?, gia_tri=? "
                + "WHERE ma_tai_san=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, taiSan.tenTaiSan);
            ps.setString(2, taiSan.loai);
            ps.setString(3, taiSan.maPhong);
            ps.setBigDecimal(4, taiSan.giaTri);
            ps.setString(5, taiSan.maTaiSan);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                return new Response(true, "Sửa tài sản thành công!");
            }

            return new Response(false, "Không tìm thấy tài sản.");
        }
    }
}