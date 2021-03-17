package org.dcv.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;

@Slf4j
//public class ApplicationContextCloseEventListener implements ApplicationListener<ContextRefreshedEvent> {
public class ApplicationContextCloseEventListener {
    //    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.warn("contextRefreshedEvent: {}", contextRefreshedEvent);
    }
}
