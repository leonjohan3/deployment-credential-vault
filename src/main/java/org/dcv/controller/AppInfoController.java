package org.dcv.controller;

import lombok.extern.slf4j.Slf4j;
import org.dcv.config.BuildInfoConfiguration;
import org.dcv.dto.BuildInfo;
import org.dcv.dto.ProcessingError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@Slf4j
public class AppInfoController {

    private BuildInfoConfiguration buildInfoConfiguration;

    public AppInfoController(final BuildInfoConfiguration buildInfoConfiguration) {
        this.buildInfoConfiguration = buildInfoConfiguration;
    }

    @GetMapping(value = "/build-info", produces = APPLICATION_JSON_VALUE)
    public @Valid BuildInfo getBuildInfo() {
        return buildInfoConfiguration.getBuildInfo();
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
