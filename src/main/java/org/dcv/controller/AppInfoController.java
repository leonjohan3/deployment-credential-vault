package org.dcv.controller;

import lombok.extern.slf4j.Slf4j;
import org.dcv.config.BuildInfoConfig;
import org.dcv.dto.ProcessingError;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.dcv.config.BuildInfoConfig.BuildInfo;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@Slf4j
public class AppInfoController {

    private BuildInfoConfig buildInfoConfig;

    public AppInfoController(final BuildInfoConfig buildInfoConfig) {
        this.buildInfoConfig = buildInfoConfig;
    }

    @GetMapping(value = "/build-info", produces = APPLICATION_JSON_VALUE)
    public @Valid BuildInfo getBuildInfo() {
        return buildInfoConfig.getBuildInfo();
    }

    @GetMapping(value = "/bla", produces = APPLICATION_JSON_VALUE)
//    @ResponseStatus(CREATED)
//    public String test(@Valid @RequestBody final ProcessingError processingError) {
    public ProcessingError test() {
//        log.info("aaa:{}", processingError.getErrorMessage());
        log.info("aaa:");
        return new ProcessingError("bla");
    }
}
