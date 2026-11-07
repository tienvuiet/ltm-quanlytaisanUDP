package org.example.Model;

import java.io.Serializable;

public class Request implements Serializable {
    public String action;

    public Phong phong;
    public TaiSan taiSan;

    public Request(String action) {
        this.action = action;
    }
}