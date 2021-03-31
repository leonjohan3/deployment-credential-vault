package org.dcv.service;

import org.dcv.config.ApplicationConfiguration;
import org.dcv.dto.SecretKeyEntry;
import org.dcv.dto.SecretKeyEntryBase;
import org.dcv.dto.SecretKeyEntryKeyName;
import org.dcv.task.ReadSecretKeyEntriesResponse;
import org.dcv.task.ReadSecretKeyEntriesTask;
import org.dcv.task.ReadSingleSecretKeyEntryResponse;
import org.dcv.task.ReadSingleSecretKeyEntryTask;
import org.dcv.task.WriteSecretKeyEntryTask;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ForkJoinPool;

/**
 * Manages the KeystoreReadWriteWorker
 */
@Service
//@Slf4j
public class KeystoreService {

    private static ForkJoinPool forkJoinPool = new ForkJoinPool(1, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
    private ApplicationConfiguration applicationConfiguration;

    @Value("${password.keystore}")
    private String keystorePassword;

    public KeystoreService(final ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    /*

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
     */

    public ReadSingleSecretKeyEntryResponse getSecretKeyEntry(final SecretKeyEntryKeyName secretKeyEntry) {
        return forkJoinPool.invoke(new ReadSingleSecretKeyEntryTask(MDC.getCopyOfContextMap(), keystorePassword,
                applicationConfiguration.getKeystore(), secretKeyEntry.getAlias()));
    }

    public ReadSecretKeyEntriesResponse getSecretKeyEntries(final SecretKeyEntryBase secretKeyEntry) {
        return forkJoinPool.invoke(new ReadSecretKeyEntriesTask(MDC.getCopyOfContextMap(), keystorePassword,
                applicationConfiguration.getKeystore(), secretKeyEntry.getAliasPrefix()));
    }

    public Exception setSecretKeyEntry(final SecretKeyEntry secretKeyEntry) {
        return forkJoinPool.invoke(new WriteSecretKeyEntryTask(MDC.getCopyOfContextMap(), keystorePassword,
                applicationConfiguration.getKeystore(), secretKeyEntry.getAlias(), secretKeyEntry.getSecretKeyValue()));
    }
}
