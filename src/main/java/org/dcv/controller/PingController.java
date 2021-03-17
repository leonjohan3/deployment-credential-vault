package org.dcv.controller;

import lombok.extern.slf4j.Slf4j;
import org.dcv.util.BuildInfoConfig;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.SynchronousQueue;

import static java.util.Collections.EMPTY_MAP;
import static java.util.Objects.isNull;
import static org.dcv.util.BuildInfoConfig.BuildInfo;

@Slf4j
@RestController
@Validated
public class PingController implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;
    private BlockingQueue<Task> blockingQueue = new SynchronousQueue<>(true);
    private SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor("bla-");
    private BuildInfoConfig buildInfoConfig;

    public PingController(final BuildInfoConfig buildInfoConfig) {
        this.buildInfoConfig = buildInfoConfig;
    }

    //    @Autowired
//    private TaskExecutor taskExecutor;
//    @EventListener
//    public void myEventListener(final ContextRefreshedEvent contextRefreshedEvent) {
//        log.info("contextRefreshedEvent: {}", contextRefreshedEvent);
//    }

    @EventListener
    public void myEventListener(final ConsumerStoppedEvent consumerStoppedEvent) throws InterruptedException {
        log.info("consumerStoppedEvent: {}", consumerStoppedEvent);
        simpleAsyncTaskExecutor.execute(new LogTask(blockingQueue, applicationEventPublisher));
        Thread.currentThread().interrupt();
        blockingQueue.put(new Task("2nd message", EMPTY_MAP));
    }

//    private ThreadFactory threadFactory;

    @PostConstruct
//    @Async
    public void postConstruct() throws InterruptedException {
        log.info("postConstruct");
        simpleAsyncTaskExecutor.execute(new LogTask(blockingQueue, applicationEventPublisher));
//        Thread.currentThread().interrupt();
        blockingQueue.put(new Task("1st message", EMPTY_MAP));
    }

    @PreDestroy
    public void preDestroy() throws InterruptedException {
        blockingQueue.put(new Task("stop", EMPTY_MAP));
        log.info("preDestroy");
    }

    @GetMapping(value = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    public Callable<String> ping() {
        final Map<String, String> mdcContext = MDC.getCopyOfContextMap();

//        return new Callable<String>() {
//
//            public Callable() {
//
//            }
//            @Override
//            public String call() throws Exception {
//                return null;
//            }
//        }

        return () -> {
            final BlockingQueue<String> localBlockingQueue = new SynchronousQueue<>(true);
            blockingQueue.put(new Task("message", isNull(mdcContext) ? EMPTY_MAP : mdcContext, localBlockingQueue));
            return localBlockingQueue.take();
        };
    }

    @GetMapping(value = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @Valid Callable<BuildInfo> getBuildInfo() {
//        return () -> buildInfoConfig.buildInfo();
//    }
    public @Valid BuildInfo getBuildInfo() {
        return buildInfoConfig.buildInfo();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
