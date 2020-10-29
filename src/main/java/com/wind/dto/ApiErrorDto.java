package com.wind.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@ToString
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorDto {

    private HttpStatus statusCode;

    private String messages;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;

    private String traceId;

    public ApiErrorDto(HttpStatus statusCode, String messages) {
        super();
        this.statusCode = statusCode;
        this.messages = messages;
        this.timestamp = LocalDateTime.now();
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }


}
