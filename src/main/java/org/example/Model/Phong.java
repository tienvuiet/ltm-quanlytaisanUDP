package org.example.Model;

import java.io.Serializable;

public class Phong implements Serializable {

    public String maPhong;
    public String tenPhong;
    public String moTa;

    public Phong(String maPhong, String tenPhong, String moTa) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return maPhong + " - " + tenPhong;
    }
}