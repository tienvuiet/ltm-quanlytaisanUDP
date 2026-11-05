package org.example.Model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Request implements Serializable {
    public String action;

    public Phong phong;
    public TaiSan taiSan;

    public String tuKhoa = "";
    public String maPhong = "";
    public BigDecimal giaTri;

    public Request(String action) {
        this.action = action;
    }
}