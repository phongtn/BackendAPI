package com.wind.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResponseStatus implements Serializable {

    @Autowired
    private MessageLocaleService messageLocaleService;

    public ResponseStatus(String code, boolean setMessageImplicitly) {
        setCode(code, setMessageImplicitly);
    }

    private String code;

    /**
     * Set the code. this will implicitly set the message based on the locale
     *
     */
    public void setCode(String code) {
        setCode(code, true);
    }

    /**
     * Set the code
     *
     */
    public void setCode(String code, boolean setMessageImplicitly) {
        this.code = code;
        if (setMessageImplicitly) {
            this.message = messageLocaleService.getMessage(code);
        }
        this.displayMessage = this.message;
    }

    public String getCode() {
        return code;
    }

    @JsonProperty("message")
    private String message;

    @JsonProperty("displayMessage")
    private String displayMessage;
}
