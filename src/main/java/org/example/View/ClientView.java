package org.example.View;

import org.example.Control.ClientControl;
import org.example.Model.Phong;
import org.example.Model.Request;
import org.example.Model.Response;
import org.example.Model.TaiSan;
import java.math.BigDecimal;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientView extends JFrame implements ActionListener {
    private JTextField txtMaPhong;
    private JTextField txtTenPhong;
    private JTextField txtMoTa;
    private JButton btnSuaPhong;
    private JLabel lblKetQua;

    private JTextField txtMaTaiSan;
    private JTextField txtTenTaiSan;
    private JTextField txtLoai;
    private JTextField txtPhongTaiSan;
    private JTextField txtGiaTri;
    private JButton btnSuaTaiSan;

    public ClientView() {
        super("Quản lý tài sản - Cập nhật thông tin");

        txtMaPhong = new JTextField(20);
        txtTenPhong = new JTextField(20);
        txtMoTa = new JTextField(20);

        btnSuaPhong = new JButton("Sửa phòng");
        btnSuaPhong.addActionListener(this);

        lblKetQua = new JLabel(
                "Nhập mã phòng hoặc mã tài sản đã tồn tại để sửa."
        );

        txtMaTaiSan = new JTextField(20);
        txtTenTaiSan = new JTextField(20);
        txtLoai = new JTextField(20);
        txtPhongTaiSan = new JTextField(20);
        txtGiaTri = new JTextField(20);

        btnSuaTaiSan = new JButton("Sửa tài sản");
        btnSuaTaiSan.addActionListener(this);

        JPanel formPhong = new JPanel(new GridLayout(3, 2, 10, 10));
        formPhong.add(new JLabel("Mã phòng:"));
        formPhong.add(txtMaPhong);
        formPhong.add(new JLabel("Tên phòng mới:"));
        formPhong.add(txtTenPhong);
        formPhong.add(new JLabel("Mô tả mới:"));
        formPhong.add(txtMoTa);

        JPanel formTaiSan = new JPanel(new GridLayout(5, 2, 10, 10));
        formTaiSan.add(new JLabel("Mã tài sản:"));
        formTaiSan.add(txtMaTaiSan);
        formTaiSan.add(new JLabel("Tên tài sản mới:"));
        formTaiSan.add(txtTenTaiSan);
        formTaiSan.add(new JLabel("Loại:"));
        formTaiSan.add(txtLoai);
        formTaiSan.add(new JLabel("Mã phòng:"));
        formTaiSan.add(txtPhongTaiSan);
        formTaiSan.add(new JLabel("Giá trị:"));
        formTaiSan.add(txtGiaTri);

        JPanel cotSuaPhong = createFormPanel(
                "Sửa phòng", formPhong, btnSuaPhong
        );
        JPanel cotSuaTaiSan = createFormPanel(
                "Sửa tài sản", formTaiSan, btnSuaTaiSan
        );

        JPanel haiCot = new JPanel(new GridLayout(1, 2, 20, 0));
        haiCot.add(cotSuaPhong);
        haiCot.add(cotSuaTaiSan);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(new JLabel("Kết quả:"));
        bottom.add(lblKetQua);

        JPanel content = new JPanel(new BorderLayout(10, 15));
        content.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        content.add(haiCot, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);

        setContentPane(content);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel createFormPanel(String title, JPanel form, JButton button) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.add(button);

        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(form, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSuaPhong) {
            suaPhong();
        } else if (e.getSource() == btnSuaTaiSan) {
            suaTaiSan();
        }
    }

    private void suaPhong() {
        String ma = txtMaPhong.getText().trim();
        String ten = txtTenPhong.getText().trim();
        String moTa = txtMoTa.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            showMessage("Mã và tên phòng không được trống.");
            return;
        }

        Phong phong = new Phong(ma, ten, moTa);

        Request request = new Request("SUA_PHONG");
        request.phong = phong;

        ClientControl client = new ClientControl();

        try {
            client.openConnection();
            client.sendData(request);

            Response response = client.receiveData();

            lblKetQua.setText(response.thongBao);
            showMessage(response.thongBao);

        } catch (Exception ex) {
            ex.printStackTrace();
            lblKetQua.setText("Chưa nhận được kết quả sửa.");
            showMessage("Lỗi liên lạc. Kiểm tra server và dữ liệu trước khi thử lại.");
        } finally {
            client.closeConnection();
        }
    }

    private void suaTaiSan() {
        String ma = txtMaTaiSan.getText().trim();
        String ten = txtTenTaiSan.getText().trim();
        String loai = txtLoai.getText().trim();
        String maPhong = txtPhongTaiSan.getText().trim();
        String gia = txtGiaTri.getText().trim();

        if (ma.isEmpty() || ten.isEmpty() || loai.isEmpty()
                || maPhong.isEmpty() || gia.isEmpty()) {
            showMessage("Vui lòng nhập đầy đủ thông tin tài sản.");
            return;
        }

        BigDecimal giaTri;

        try {
            giaTri = new BigDecimal(gia);

            if (giaTri.signum() < 0) {
                showMessage("Giá trị không được âm.");
                return;
            }
        } catch (NumberFormatException e) {
            showMessage("Giá trị phải là số, ví dụ: 15000000.");
            return;
        }

        TaiSan taiSan = new TaiSan(
                ma, ten, loai, maPhong, giaTri
        );

        Request request = new Request("SUA_TAI_SAN");
        request.taiSan = taiSan;

        ClientControl client = new ClientControl();

        try {
            client.openConnection();
            client.sendData(request);

            Response response = client.receiveData();

            lblKetQua.setText(response.thongBao);
            showMessage(response.thongBao);

        } catch (Exception e) {
            e.printStackTrace();
            lblKetQua.setText("Chưa nhận được kết quả sửa.");
            showMessage("Lỗi liên lạc. Kiểm tra server và dữ liệu trước khi thử lại.");
        } finally {
            client.closeConnection();
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}