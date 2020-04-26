package com.alibaba.csp.sentinel.dashboard.util;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * AsyncUtils
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Slf4j
public final class AsyncUtils {
    public static <R> CompletableFuture<R> newFailedFuture(Throwable ex) {
        CompletableFuture<R> future = new CompletableFuture<>();
        future.completeExceptionally(ex);
        return future;
    }

    public static <R> CompletableFuture<List<R>> sequenceFuture(List<CompletableFuture<R>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(AsyncUtils::getValue)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
                );
    }

    public static <R> CompletableFuture<List<R>> sequenceSuccessFuture(List<CompletableFuture<R>> futures) {
        return CompletableFuture.supplyAsync(() -> futures.parallelStream()
                .map(AsyncUtils::getValue)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
    }

    public static <T> T getValue(CompletableFuture<T> future) {
        try {
            return future.get(10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.error("getValue for async result failed", ex);
        }
        return null;
    }

    public static boolean isSuccessFuture(CompletableFuture future) {
        return future.isDone() && !future.isCompletedExceptionally() && !future.isCancelled();
    }

    private AsyncUtils() {}
}
