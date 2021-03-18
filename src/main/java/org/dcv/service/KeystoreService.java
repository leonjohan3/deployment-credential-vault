package org.dcv.service;

import lombok.extern.slf4j.Slf4j;
import org.dcv.config.ApplicationConfiguration;
import org.dcv.dto.SecretKeyEntry;
import org.dcv.task.ReadSingleSecretKeyEntryResponse;
import org.dcv.task.ReadSingleSecretKeyEntryTask;
import org.dcv.task.WriteSecretKeyEntryTask;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the KeystoreReadWriteWorker
 */
@Service
@Slf4j
public class KeystoreService {

    private static ForkJoinPool forkJoinPool = new ForkJoinPool(1, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
    private ApplicationConfiguration applicationConfiguration;

    @Value("${password.keystore}")
    private String keystorePassword;

    public KeystoreService(final ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public static class MyRecursiveTask extends RecursiveTask<String> {

        private static final AtomicInteger counter = new AtomicInteger(0);
        private Map<String, String> mdcContext;

        public MyRecursiveTask(final Map<String, String> mdcContext) {
//            log.info("run:{}", mdcContext);
            this.mdcContext = mdcContext;
        }

        @Override
        protected String compute() {
            try {
                MDC.setContextMap(mdcContext);
                log.info("run");

                if (counter.incrementAndGet() == 2) {
                    log.info("sleeping 1s");
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                MDC.clear();
            }
            return "bla";
        }
    }

//    private ForkJoinPool forkJoinPool = new ForkJoinPool(1);

    public ReadSingleSecretKeyEntryResponse getSecretKeyEntry(final SecretKeyEntry secretKeyEntry)  {
//        ForkJoinT forkJoinPool;
//        final String[] result = {""};
//        forkJoinPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                log.info("run");
//                result[0] = "bla";
//            }
//        });
//        TimeUnit.SECONDS.sleep(1);
//        return result[0];
//        return forkJoinPool.invoke(new ReadSingleSecretKeyEntryTask(MDC.getCopyOfContextMap(), "oFFline33",
//        return forkJoinPool.invoke(new ReadSingleSecretKeyEntryTask(MDC.getCopyOfContextMap(), applicationConfiguration.getKeystorePassword(),
        return forkJoinPool.invoke(new ReadSingleSecretKeyEntryTask(MDC.getCopyOfContextMap(), keystorePassword,
                applicationConfiguration.getKeystore(), secretKeyEntry.getAlias()));
//        return forkJoinPool.invoke(new MyRecursiveTask(MDC.getCopyOfContextMap()));
    }

    public Exception setSecretKeyEntry(final SecretKeyEntry secretKeyEntry)  {
        return forkJoinPool.invoke(new WriteSecretKeyEntryTask(MDC.getCopyOfContextMap(), keystorePassword,
                applicationConfiguration.getKeystore(), secretKeyEntry.getAlias(), secretKeyEntry.getSecretKeyValue()));
//        return forkJoinPool.invoke(new MyRecursiveTask(MDC.getCopyOfContextMap()));
    }
}
