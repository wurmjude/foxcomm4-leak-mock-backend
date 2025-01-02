package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.License;
import com.example.demo.service.LicensingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

    @Autowired
    private LicensingService licensingService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        licensingService.clearLicenses();
    }

    @Test
    public void TestDoNotAllowDuplicateSerial() throws Exception {
        licensingService.addLicense("testSerial", "magicKey", "", "");
        assertThrows(IllegalArgumentException.class, () ->
            licensingService.addLicense("testSerial", "magicKey2", "", ""));
    }

    @Test
    public void TestDoNotAllowDuplicateMagicKey() throws Exception {
        licensingService.addLicense("testSerial", "magicKey", "", "");
        assertThrows(IllegalArgumentException.class, () ->
            licensingService.addLicense("testSerial2", "magicKey", "", ""));
    }

    @Test
    public void TestGetLicenseByBearerToken200() throws Exception {
        var request = get("/Engine/Preloader").header("authorization", "Bearer token");
        var result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        var lic = objectMapper.readValue(result.getResponse().getContentAsString(), License.class);
        assertEquals("0", lic.getStatus());
    }

    @Test
    public void TestGetLicenseByBearerToken401() throws Exception {
        mockMvc.perform(get("/Engine/Preloader").header("authorization", "asd")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/Engine/Preloader")).andExpect(status().isUnauthorized());
    }

    @Test
    public void TestGetLicenseByMagicKey200() throws Exception {
        var orig = licensingService.addLicense("testSerial", "magicKey", "userID", "fullName");
        var request = get("/Checklua/Gipszelo").header("authorization", "Bearer magicKey");
        var result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        var lic = objectMapper.readValue(result.getResponse().getContentAsString(), License.class);
        assertEquals(orig, lic);
    }

    @Test
    public void TestGetLicenseByMagicKey401() throws Exception {
        mockMvc.perform(get("/Checklua/Gipszelo")).andExpect(status().isUnauthorized());
    }

    @Test
    public void TestValidateLicense200() throws Exception {
        var orig = licensingService.addLicense("testSerial", "magicKey", "userID", "fullName");
        var request = get("/Checklua/Serlod")
            .header("authorization", "Bearer magicKey")
            .param("Serl", "testSerial");
        var result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        var lic = objectMapper.readValue(result.getResponse().getContentAsString(), License.class);
        assertEquals(orig, lic);
    }

    @Test
    public void TestValidateLicense400() throws Exception {
        licensingService.addLicense("testSerial", "magicKey", "userID", "fullName");
        var request = get("/Checklua/Serlod")
            .header("authorization", "Bearer magicKey")
            .param("Serl", "testSerial2");
        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    public void TestValidateLicense401() throws Exception {
        mockMvc.perform(get("/Checklua/Serlod")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/Checklua/Serlod").header("authorization", "Bearer magicKey")).andExpect(status().isUnauthorized());
    }

    @Test
    public void TestValidateLicense403() throws Exception {
        licensingService.addLicense("testSerial", "magicKey", "userID", "fullName");
        licensingService.addLicense("testSerial2", "magicKey2", "userID2", "fullName2");
        var request = get("/Checklua/Serlod")
            .header("authorization", "Bearer magicKey")
            .param("Serl", "testSerial2");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @Test
    public void TestActivateLicense200() throws Exception {
        licensingService.addLicense("testSerial", "magicKey", "userID", "fullName");
        var request = get("/Checklua/Serial")
            .header("authorization", "Bearer magicKey")
            .param("AddSer", "testSerial")
            .param("Addcp", "cpu")
            .param("Addhd", "disk")
            .param("Partid", "part")
            .param("Bdsn", "mb");
        var result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        var lic = objectMapper.readValue(result.getResponse().getContentAsString(), License.class);
        assertEquals("testSerial", lic.getSerial());
        assertEquals("1", lic.getStatus());
        assertEquals("cpu", lic.getCpuid());
        assertEquals("disk", lic.getDiskid());
        assertEquals("part", lic.getDiskpart());
        assertEquals("mb", lic.getBoardid());
    }

    @Test
    public void TestActivateLicense400() throws Exception {
        var request = get("/Checklua/Serial")
            .header("authorization", "Bearer magicKey")
            .param("Serl", "testSerial");
        mockMvc.perform(request).andExpect(status().isBadRequest());
    }
}
