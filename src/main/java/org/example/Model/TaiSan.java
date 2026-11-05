package org.example.Model;

import java.io.Serializable;
import java.math.BigDecimal;

public class TaiSan implements Serializable {

    public String maTaiSan;
    public String tenTaiSan;
    public String loai;
    public String maPhong;
    public BigDecimal giaTri;

    public TaiSan(String maTaiSan, String tenTaiSan, String loai,
                  String maPhong, BigDecimal giaTri) {
        this.maTaiSan = maTaiSan;
        this.tenTaiSan = tenTaiSan;
        this.loai = loai;
        this.maPhong = maPhong;
        this.giaTri = giaTri;
    }
}