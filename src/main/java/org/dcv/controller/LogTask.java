package org.dcv.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

@Slf4j
//@Value
@RequiredArgsConstructor
public class LogTask implements Runnable {
    private final BlockingQueue<Task> blockingQueue;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void run() {
        boolean shouldContinue = true;
        Task task;
        try {
            log.info("starting");

            while (shouldContinue) {
                try {
                    task = blockingQueue.take();

                    final Map<String, String> originalMdcContext = MDC.getCopyOfContextMap();

                    if (!task.getMdcContext().isEmpty()) {
                        MDC.setContextMap(task.getMdcContext());
                    }
                    // do not log above the above block

                    if (task.getMessage().equalsIgnoreCase("stop")) {
                        shouldContinue = false;
                    } else if (task.getMessage().equalsIgnoreCase("throw")) {
                        throw new IllegalStateException("bla");
                    } else {
                        log.info("message: {}", task.getMessage());

                        if (task.getBlockingQueue().isPresent()) {
                            task.getBlockingQueue().get().put(task.getMessage().toUpperCase());
                        }
                    }

                    // do not log below this block
                    if (nonNull(originalMdcContext)) {
                        MDC.setContextMap(originalMdcContext);
                    } else {
                        MDC.clear();
                    }
                } catch (InterruptedException e) {
                    log.error("", e);
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {

            if (shouldContinue) {

                try {
                    applicationEventPublisher.publishEvent(new ConsumerStoppedEvent(this));
                } catch (Throwable t) {
                    System.err.println(format("LogTask, terminate vm, error: %s", t.getMessage()));
                    System.exit(1);
                }
            }
        }
        log.info("stopping");
    }

//    @Override
//    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
//        this.applicationEventPublisher = applicationEventPublisher;
//    }
}
