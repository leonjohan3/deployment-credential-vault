package org.dcv.controller;

import org.springframework.context.ApplicationEvent;

public class ConsumerStoppedEvent extends ApplicationEvent {
    public ConsumerStoppedEvent(Object source) {
        super(source);
    }
}
