package org.mrpaulwoods.common;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ResponseObserver<T> implements StreamObserver<T> {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(ResponseObserver.class);

    private final List<T> list = Collections.synchronizedList(new ArrayList<>());
    private final CountDownLatch latch;
    private Throwable throwable;

    private ResponseObserver(int countDown) {
        this.latch = new CountDownLatch(countDown);
    }

    public static <T> ResponseObserver<T> create() {
        return create(1);
    }

    public static <T> ResponseObserver<T> create(int countDown) {
        return new ResponseObserver<>(countDown);
    }

    @Override
    public void onNext(T t) {
        log.info("received item: {}", t);
        this.list.add(t);
    }

    @Override
    public void onError(Throwable throwable) {
        log.info("received error: {}", throwable.getMessage());
        this.throwable = throwable;
        this.latch.countDown();
    }

    @Override
    public void onCompleted() {
        log.info("received completed");
        this.latch.countDown();
    }

    public void await() {
        try {
            //noinspection ResultOfMethodCallIgnored
            this.latch.await(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> getItems() {
        return this.list;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

}
