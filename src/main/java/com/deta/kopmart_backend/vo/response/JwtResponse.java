package com.deta.kopmart_backend.vo.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String account;
    private String name;
    private String role;

    public JwtResponse(String token, String account, String name, String role) {
        this.token = token;
        this.account = account;
        this.name = name;
        this.role = role;
    }
}
