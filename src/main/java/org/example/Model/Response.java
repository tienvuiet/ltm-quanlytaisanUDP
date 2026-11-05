package org.example.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    public boolean thanhCong;
    public String thongBao;

    public ArrayList<Phong> dsPhong = new ArrayList<>();
    public ArrayList<TaiSan> dsTaiSan = new ArrayList<>();

    public Response(boolean thanhCong, String thongBao) {
        this.thanhCong = thanhCong;
        this.thongBao = thongBao;
    }
}