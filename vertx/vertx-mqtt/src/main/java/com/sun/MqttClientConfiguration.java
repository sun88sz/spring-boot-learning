package com.sun;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttTopicSubscription;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Sun
 * @date : 2018/9/20 10:26
 */
@Configuration
public class MqttClientConfiguration {


    @Bean
    public Vertx vertx() {

        VertxOptions options = new VertxOptions();
        options.setWorkerPoolSize(20);

        Vertx vertx = Vertx.vertx(options);
        return vertx;
    }



    @Bean
    public MqttClient mqttClient(Vertx vertx) {
        MqttClient client = MqttClient.create(vertx);

        client.connect(1883, "localhost", s -> {
            System.out.println(client.clientId());
        });

        client.publishHandler(s -> {
            System.out.println("There are new message in topic: " + s.topicName());
            System.out.println("Content(as string) of the message: " + s.payload().toString());
            System.out.println("QoS: " + s.qosLevel());
        });

        client
                .subscribeCompletionHandler(mqttSubAckMessage -> {
                    System.out.println("Id of just received SUBACK packet is " + mqttSubAckMessage.messageId());
                    for (int s : mqttSubAckMessage.grantedQoSLevels()) {
                        if (s == 0x80) {
                            System.out.println("Failure");
                        } else {
                            System.out.println("Success. Maximum QoS is " + s);
                        }
                    }
                });

        client
                .unsubscribeCompletionHandler(id -> {
                    System.out.println("Id of just received UNSUBACK packet is " + id);
                });
        
        client.pingResponseHandler(s -> {
            //The handler will be called time to time by default
            System.out.println("We have just received PINGRESP packet");
        });
        
        return client;
    }

//    public void xx() {
//        for (int i = 0; i < 10; i++) {
//
//            MqttServer mqttServer = MqttServer.create(vertx);
//            mqttServer.endpointHandler(endpoint -> {
//                // handling endpoint
//            })
//                    .listen(ar -> {
//
//                        // handling start listening
//                    });
//        }
//    }

//    DeploymentOptions options = new DeploymentOptions().setInstances(10);
//vertx.deployVerticle("com.mycompany.MyVerticle", options);


}
