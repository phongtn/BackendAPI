package com.wind.domain.example;

import com.wind.dto.SampleDto;
import com.wind.external.sample.ExternalSampleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/example")
public class ExampleController {

    private final ExternalSampleService sampleService;


    public ExampleController(ExternalSampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GetMapping
    public List<SampleDto> findAll() {
        return sampleService.allSampleDto();
    }
}
