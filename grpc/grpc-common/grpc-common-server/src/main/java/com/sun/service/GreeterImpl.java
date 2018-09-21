package com.sun.service;

import com.sun.GreeterGrpc;
import com.sun.GreeterOuterClass;

import io.grpc.stub.StreamObserver;

/**
 * @author : Sun
 * @date : 2018/9/21 09:48
 */
public class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void sayHello(GreeterOuterClass.HelloRequest request, StreamObserver<GreeterOuterClass.HelloReply> responseObserver) {
        //如在我们的服务定义中指定的那样，我们组织并填充一个 Feature 应答对象返回给客户端。在这个 例子中，我们通过一个单独的私有方法checkFeature()来实现。
        GreeterOuterClass.HelloReply reply = GreeterOuterClass.HelloReply.newBuilder().setMessage("hello , " + request.getName()).build();
        //我们使用应答观察者的 onNext() 方法返回 Feature。
        responseObserver.onNext(reply);
        //我们使用应答观察者的 onCompleted() 方法来指出我们已经完成了和 RPC的交互。
        responseObserver.onCompleted();
    }
    
}
