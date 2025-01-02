package com.example.demo.model;

import java.util.UUID;

import lombok.Data;

@Data
public class License {
    private String serial;
    private String magickey;
    private String usernumma;
    private String owner;
    private String status = "0";
    private String cpuid = "";
    private String diskid = "";
    private String diskpart = "";
    private String boardid = "";
    private String kifesto = UUID.randomUUID().toString();
    private String mazolo = "_____FoxDecryptionPassword;)___________";
}
