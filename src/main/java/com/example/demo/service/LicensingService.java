package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.demo.model.License;

@Service
public class LicensingService {

    private List<License> licenses = new ArrayList<>();

    public void clearLicenses() {
        licenses.clear();
    }

    public License addLicense(String serial, String magicKey, String userID, String fullName) {
        if (getLicenseBySerial(serial) != null) {
            throw new IllegalArgumentException("Serial already taken, choose another one");
        }
        if (getLicenseByMagicKey(magicKey) != null) {
            throw new IllegalArgumentException("Magic key already taken, choose another one");
        }
        var lic = new License();
        lic.setSerial(serial);
        lic.setMagickey(magicKey);
        lic.setUsernumma(userID);
        lic.setOwner(fullName);
        licenses.add(lic);
        return lic;
    }

    public License activateLicense(String serial, String cpuID, String diskID, String partitionID, String boardID) {
        var lic = getLicenseBySerial(serial);
        if (lic == null) {
            return null;
        }
        lic.setCpuid(cpuID);
        lic.setDiskid(diskID);
        lic.setDiskpart(partitionID);
        lic.setBoardid(boardID);
        lic.setStatus("1");
        return lic;
    }

    public License getLicenseBySerial(@NonNull String serial) {
        return licenses.stream().filter(lic -> serial.equals(lic.getSerial())).findFirst().orElse(null);
    }

    public License getLicenseByBearerToken(@NonNull String bearerToken) {
        return licenses.stream().filter(lic -> bearerToken.equals(lic.getKifesto())).findFirst().orElse(null);
    }

    public License getLicenseByMagicKey(@NonNull String magicKey) {
        return licenses.stream().filter(lic -> magicKey.equals(lic.getMagickey())).findFirst().orElse(null);
    }
}
