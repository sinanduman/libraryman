package com.sinanduman.library;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sinanduman.library.config.ConfigProperties;
import com.sinanduman.library.utility.DateUtility;
import com.sinanduman.library.utility.Validation;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LibrarymanApplicationTests {

    @Autowired
    private ConfigProperties configProperties;

    @Test
    @Order(0)
    void contextLoads() {
    }

    @Test
    @Order(1)
    public void string_is_empty() {
        Assertions.assertTrue(Validation.isEmpty(""));
    }

    @Test
    @Order(2)
    public void string_is_not_empty() {
        Assertions.assertFalse(Validation.isEmpty("a"));
    }

    @Test
    @Order(11)
    public void phone_number_is_valid_one() {
        Assertions.assertTrue(Validation.isValidPhoneNumber("05552800229"));
    }

    @Test
    @Order(12)
    public void phone_number_is_valid_two() {
        Assertions.assertTrue(Validation.isValidPhoneNumber("5552800229"));
    }

    @Test
    @Order(13)
    public void phone_number_is_valid_three() {
        Assertions.assertTrue(Validation.isValidPhoneNumber("555 2800229"));
    }

    @Test
    @Order(14)
    public void phone_number_is_valid_four() {
        Assertions.assertTrue(Validation.isValidPhoneNumber("555 280 02 29"));
    }

    @Test
    @Order(15)
    public void phone_number_is_not_valid() {
        Assertions.assertFalse(Validation.isValidPhoneNumber("0555 280-02-29"));
    }

    @Test
    @Order(20)
    public void email_is_valid() {
        Assertions.assertTrue(Validation.isValidEmail("sinanduman@gmail.com"));
    }

    @Test
    @Order(21)
    public void email_is_not_valid() {
        Assertions.assertFalse(Validation.isValidEmail("sinanduman@gmail"));
    }

    @Test
    @Order(31)
    public void date_is_valid() {
        Assertions.assertTrue(Validation.isValidDate("2022-08-06T19:23:00"));
    }

    @Test
    @Order(32)
    public void date_is_valid_two() {
        Assertions.assertTrue(Validation.isValidDate("2022-08-06 19:23:00"));
    }

    @Test
    @Order(33)
    public void date_is_valid_three() {
        Assertions.assertTrue(Validation.isValidDate("2022-08-06T19:23:00.000Z"));
    }

    @Test
    @Order(34)
    public void date_is_not_valid() {
        Assertions.assertFalse(Validation.isValidDate("2022-08-0619:23:00"));
    }

    @Test
    @Order(35)
    public void date_length_is_valid() {
        String sampleDate = "2022-02-20T10:00:00XYZT";
        int sampleLength = "2022-02-20T10:00:00".length();
        String newSample = sampleDate.trim().substring(0, sampleLength);
        Assertions.assertFalse(Validation.isValidDate("2022-08-0619:23:00"));
    }

    @Test
    @Order(41)
    public void shouldPassHMAC256Verification() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJpYW0ubGlicmFyeS1zeXN0ZW0uY29tIiwiaWF0IjoxNjU2MjM5NDUyLCJleHAiOjE2ODc4NjE4NTIsImF1ZCI6ImlhbS5saWJyYXJ5LXN5c3RlbS5jb20iLCJzdWIiOiJzaW5hbiIsInNjb3BlcyI6WyJsaWIuYm0uciIsImxpYi5iby5jIiwibGliLmJvLnIiXX0.TUehO1PrYHH6W3q-IVDlId2SeuXhYlPRgyH6Qh-_SyI";
        Algorithm algorithmString = Algorithm.HMAC256(configProperties.getSecretKey());
        Algorithm algorithmBytes = Algorithm.HMAC256(configProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        DecodedJWT decoded = JWT.decode(token);
        algorithmString.verify(decoded);
        algorithmBytes.verify(decoded);
    }

    @Test
    @Order(42)
    public void shouldPassBasicAuthentication() throws Exception {
        String authorization = "Basic c2luYW5kdW1hbkBnbWFpbC5jb206MTIzNDQzMjE=";
        String username = "sinanduman@gmail.com";
        String password = "12344321";

        String base64Credentials = authorization.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        String [] payload = credentials.split(":", 2);
        Assertions.assertTrue(payload[0].equals(username) && payload[1].equals(password));
    }

    @Test
    @Order(44)
    public void threeRoleClaimsExists() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJpYW0ubGlicmFyeS1zeXN0ZW0uY29tIiwiaWF0IjoxNjU2MjM5NDUyLCJleHAiOjE2ODc4NjE4NTIsImF1ZCI6ImlhbS5saWJyYXJ5LXN5c3RlbS5jb20iLCJzdWIiOiJzaW5hbiIsInNjb3BlcyI6WyJsaWIuYm0uciIsImxpYi5iby5jIiwibGliLmJvLnIiXX0.TUehO1PrYHH6W3q-IVDlId2SeuXhYlPRgyH6Qh-_SyI";
        Algorithm algorithmString = Algorithm.HMAC256(configProperties.getSecretKey());
        DecodedJWT decoded = JWT.decode(token);
        algorithmString.verify(decoded);
        List<String> claimList = decoded.getClaims().get("scopes").asList(String.class);
        Assertions.assertTrue(claimList.size() == 3);
    }

    @Test
    @Order(45)
    public void createToken() {

        List<String> claims = List.of("lib.m.d", "lib.bo.r", "lib.bo.c", "lib.bm.c", "lib.bm.r", "lib.m.r.all");
        String userId = "10";

        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        Date from = DateUtility.fromLocalDateTime(now);
        Date to = DateUtility.fromLocalDateTime(now.plusDays(1));

        Algorithm algorithm = Algorithm.HMAC256(configProperties.getSecretKey());
        String token = JWT.create()
                .withIssuer(configProperties.getIssuer())
                .withAudience(configProperties.getAudiance())
                .withSubject(userId)
                .withClaim("scopes", claims)
                .withIssuedAt(from)
                .withExpiresAt(to)
                .sign(algorithm);

        System.out.println(token);

        DecodedJWT decoded = JWT.decode(token);
        List<String> claimList = decoded.getClaim("scopes").asList(String.class);
        Assertions.assertEquals(5, claimList.size());
    }
}
