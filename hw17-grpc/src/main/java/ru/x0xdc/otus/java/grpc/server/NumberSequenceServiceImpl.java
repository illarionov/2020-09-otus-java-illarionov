package ru.x0xdc.otus.java.grpc.server;

import io.grpc.stub.StreamObserver;
import ru.x0xdc.otus.java.grpc.protobuf.generated.NumberSequenceReply;
import ru.x0xdc.otus.java.grpc.protobuf.generated.NumberSequenceRequest;
import ru.x0xdc.otus.java.grpc.protobuf.generated.NumberSequenceServiceGrpc;

import java.util.concurrent.ScheduledExecutorService;

public class NumberSequenceServiceImpl extends NumberSequenceServiceGrpc.NumberSequenceServiceImplBase {

    private final ScheduledExecutorService executorService;

    public NumberSequenceServiceImpl(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void getNumbers(NumberSequenceRequest request, StreamObserver<NumberSequenceReply> responseObserver) {
        NumberGenerator generator = new NumberGenerator(executorService, responseObserver,
                request.getFirstValue(), request.getLastValue());
        generator.start();
    }

}
