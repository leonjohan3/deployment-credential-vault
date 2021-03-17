package org.dcv.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

@AllArgsConstructor
@RequiredArgsConstructor
//@Value
public class Task {
    @NonNull
    @Getter
    String message;

    @NonNull
    @Getter
    Map<String, String> mdcContext;

    BlockingQueue<String> blockingQueue;

    public Optional<BlockingQueue<String>> getBlockingQueue() {
        return Optional.ofNullable(blockingQueue);
    }
}
