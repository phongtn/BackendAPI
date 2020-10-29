package com.wind.external.sample;

import com.wind.dto.SampleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExternalSampleServiceImpl implements ExternalSampleService {

    @Value("${external.service.sample.endpoint}")
    private String endpoint;

    private final RestTemplate restTemplate;

    public ExternalSampleServiceImpl(@Qualifier("sampleServiceRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<SampleDto> allSampleDto() {
        String url = this.endpoint + "/posts";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.ACCEPT_LANGUAGE, "en");
        HttpEntity<?> entityReq = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<SampleDto[]> response = restTemplate.exchange(url, HttpMethod.GET, entityReq, SampleDto[].class);
            HttpStatus status = response.getStatusCode();
            SampleDto[] body = response.getBody();
            if (HttpStatus.OK.equals(status) && body != null) {
                return List.of(body);
            }
        } catch (HttpClientErrorException e) {
            log.error("Get external data error", e);
        }
        return new ArrayList<>();
    }
}
