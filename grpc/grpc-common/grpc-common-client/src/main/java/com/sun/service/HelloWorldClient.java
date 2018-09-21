package com.sun.service;

import java.util.concurrent.TimeUnit;

import com.sun.GreeterGrpc;
import com.sun.GreeterOuterClass;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : Sun
 * @date : 2018/9/21 10:32
 */
public class HelloWorldClient {
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldClient.class);
    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    /**
     * Construct client connecting to HelloWorld server at {@code host:port}.
     */
    public HelloWorldClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example
                // we disable TLS to avoid
                // needing certificates.
                .usePlaintext(true));
    }

    /**
     * Construct client for accessing RouteGuide server using the existing
     * channel.
     */
    HelloWorldClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * Say hello to server.
     */
    public String greet(String name) {
        logger.info("Will try to greet " + name + " ...");
        GreeterOuterClass.HelloRequest.Builder builder = GreeterOuterClass.HelloRequest.newBuilder();
        GreeterOuterClass.HelloRequest request = builder.setName(name).build();
        GreeterOuterClass.HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            logger.error("RPC failed: {0}", e.getStatus());
            return null;
        }
        return response.getMessage();
    }

    public static void main(String[] args) throws Exception {
        HelloWorldClient client = new HelloWorldClient("localhost", 50051);
        try {
            String user = "world";
            if (args.length > 0) {
                user = args[0];
            }
            String greet = client.greet(user);

            logger.info(greet);
        } finally {
            client.shutdown();
        }
    }
}
