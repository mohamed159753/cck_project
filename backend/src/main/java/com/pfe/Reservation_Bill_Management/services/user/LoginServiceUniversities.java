package com.pfe.Reservation_Bill_Management.services.user;


import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@org.springframework.stereotype.Service
public class LoginServiceUniversities {
	
	private final RestTemplate restTemplate;
	
	@Autowired
    public LoginServiceUniversities(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String fetchProjects() {
        String url = "https://iam-apigateway-proxy.mesrscloud.rnu.tn/v3/projects";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", "MIIFKQYJKoZIhvcNAQcCoIIFGjCCBRYCAQExDTALBglghkgBZQMEAgEwggL3BgkqhkiG9w0BBwGgggLoBIIC5HsidG9rZW4iOnsiZXhwaXJlc19hdCI6IjIwMjUtMDQtMTFUMTM6MzA6NDguOTIzMDAwWiIsIm1ldGhvZHMiOlsicGFzc3dvcmQiXSwiY2F0YWxvZyI6W10sInJvbGVzIjpbeyJuYW1lIjoidmRjX293bmVyIiwiaWQiOiJmYTE2M2U4YmM5ZTdjNjJhMGFmYjEyODc5ZDkyOTkyNiJ9LHsibmFtZSI6InZkY19hZG0iLCJpZCI6ImNiZWQ5M2E5NjE2MDRkY2JhZGE0NzU0ODY5YzFlNGIyIn0seyJuYW1lIjoiYXBwcm92X2FkbSIsImlkIjoiYWQwNTE1MDUyMWY3NGZjMjhiZjdlMzJlN2JlY2FkN2MifSx7Im5hbWUiOiJ0ZV9hZG1pbiIsImlkIjoiZmExNjNlOGJjOWU3YzYyYTBhZmIxMjg3ODQzMjk5MWIifSx7Im5hbWUiOiJ0YWdfYWRtIiwiaWQiOiI1NDNmMjhjYWFmMTU0MGZiOTcwY2I5NjFmOWE0NmI2OCJ9XSwicHJvamVjdCI6eyJkb21haW4iOnsibmFtZSI6IkNDSyIsImlkIjoiOTZlODc5ZTg2YTI2NGQ4Mzk1MWQxMTg3M2Q0NzA3NjQifSwibmFtZSI6InRuLWdsb2JhbC0xX2Nja19yZXNlYXV4IiwiaWQiOiJjZDk4YTE2OWJhNzk0OWNiYjU2ZWE2ZDA1YTA2YjBmNCJ9LCJpc3N1ZWRfYXQiOiIyMDI1LTA0LTEwVDEzOjMwOjQ4LjkyMzAwMFoiLCJ1c2VyIjp7ImRvbWFpbiI6eyJuYW1lIjoiQ0NLIiwiaWQiOiI5NmU4NzllODZhMjY0ZDgzOTUxZDExODczZDQ3MDc2NCJ9LCJuYW1lIjoibW9oYW1lZC5tZW5zaTI0QGdtYWlsLmNvbSIsImlkIjoiZWU4YTAyZmQzNDYyNDljMmIxMWZmMjkxNzA5ZDM2MzAifX19MYICBTCCAgECAQEwXDBWMQswCQYDVQQGEwJDTjELMAkGA1UECAwCc2MxCzAJBgNVBAcMAmNkMQswCQYDVQQKDAJIVzEQMA4GA1UECwwHQ2xvdWRCVTEOMAwGA1UEAwwFdG9rZW4CAhAAMAsGCWCGSAFlAwQCATANBgkqhkiG9w0BAQEFAASCAYCtGZkxD2+UZFJtPa7xktdn83BWUPo0I6JSY93J-lJZG3u3AJLhRoaTfWxaxOWlw1xS0YS3EjIZKoRrUCRm9-D166CFik6OadY0M-eRy6QEs1oXnd8Dl0tQbm7x9xf-MyFat5NRBX-Uq4+FOdruftj1b7A+z3wcBf-wweIQ6KXdqzMeEZVA78VnqoyYegLYzGcaYdlLlJouva08uXCd-0T8TbTCtc3ume1itMMokOvo-7FMFNRAiN1LuQRZ7+2y5r8bxlbEyTvhHibAvtrlTgNUN1+-bJHKUrhTQaero1t7Xz6hjT8aWiYK7nz0Hwmi2ngwvQ0eZ9ztFwzb3RhxQYRZLAwj+xFOxAhQ6f45gPNOYa6TYanRRm2lQJinc-Auj3TqPT6chz8AIqH-JRTaRNbMcGjok6aA+V49blhq1epQA8TJ+E3lOd65Sf3g6DY48aLG2hNc-iDgKyONWCDFUq77kUdOmc3rV1xXluMAbFXt55wBfe-S4Z7OuLOLAm+5QM8="); // truncated token
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

}
