package ru.x0xdc.otus.java.grpc.server;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.jmock.lib.concurrent.DeterministicScheduler;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.x0xdc.otus.java.grpc.protobuf.generated.NumberSequenceReply;
import ru.x0xdc.otus.java.grpc.protobuf.generated.NumberSequenceRequest;
import ru.x0xdc.otus.java.grpc.protobuf.generated.NumberSequenceServiceGrpc;
import ru.x0xdc.otus.java.grpc.utils.SeqenceEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.x0xdc.otus.java.grpc.utils.SeqenceEvent.onEnd;
import static ru.x0xdc.otus.java.grpc.utils.SeqenceEvent.onNext;

@RunWith(JUnit4.class)
public class NumberSequenceServiceImplTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    DeterministicScheduler executorService;

    NumberSequenceServiceGrpc.NumberSequenceServiceStub numberServiceStub;

    @Before
    public void setup() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        executorService = new DeterministicScheduler();

        Server server = InProcessServerBuilder
                .forName(serverName)
                .directExecutor()
                .addService(new NumberSequenceServiceImpl(executorService))
                .build()
                .start();

        grpcCleanup.register(server);

        ManagedChannel channel = InProcessChannelBuilder.forName(serverName).directExecutor().build();
        grpcCleanup.register(channel);

        numberServiceStub = NumberSequenceServiceGrpc.newStub(channel);
    }

    @Test
    public void greeterImpl_replyMessage() throws Exception {
        List<SeqenceEvent<Integer>> events = Collections.synchronizedList(new ArrayList<>());
        numberServiceStub.getNumbers(NumberSequenceRequest.newBuilder().setFirstValue(1).setLastValue(5).build(), new StreamObserver<NumberSequenceReply>() {
            @Override
            public void onNext(NumberSequenceReply value) {
                events.add(SeqenceEvent.onNext(value.getNum()));
            }

            @Override
            public void onError(Throwable t) {
                throw new RuntimeException(t);
            }

            @Override
            public void onCompleted() {
                events.add(SeqenceEvent.onEnd());
            }
        });
        executorService.tick(20, TimeUnit.SECONDS);

        assertThat(events)
                .containsExactly(onNext(1), onNext(2), onNext(3), onNext(4), onEnd());
    }
}