package ru.x0xdc.otus.java.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ru.x0xdc.otus.java.grpc.client.ClientNumberObserver;
import ru.x0xdc.otus.java.grpc.protobuf.generated.NumberSequenceServiceGrpc;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(Constants.SERVER_HOST, Constants.SERVER_PORT)
                .usePlaintext()
                .build();

        try {
            ClientNumberObserver clientNumberObserver = new ClientNumberObserver(executorService, NumberSequenceServiceGrpc.newStub(channel));
            clientNumberObserver.start();
            clientNumberObserver.awaitCompletion();
        } finally {
            executorService.shutdown();
        }
    }
}
