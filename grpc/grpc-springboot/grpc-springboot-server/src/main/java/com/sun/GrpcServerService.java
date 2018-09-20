package com.sun;

import net.devh.springboot.autoconfigure.grpc.server.GrpcService;

/**
 * @author Sun
 * @date 2018-09-19
 */
//@GrpcService(SimpleGrpc.class)
//public class GrpcServerService extends SimpleGrpc.SimpleImplBase {
//
//    @Override
//    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
//        HelloReply reply = HelloReply.newBuilder().setMessage("Hello =============> " + req.getName()).build();
//        responseObserver.onNext(reply);
//        responseObserver.onCompleted();
//    }
//}