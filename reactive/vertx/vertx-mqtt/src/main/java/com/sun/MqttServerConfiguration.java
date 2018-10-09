package com.sun;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttTopicSubscription;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Sun
 * @date : 2018/9/20 10:26
 */
@Configuration
public class MqttServerConfiguration {


    @Bean
    public Vertx vertx() {

        VertxOptions options = new VertxOptions();
        options.setWorkerPoolSize(20);

        Vertx vertx = Vertx.vertx(options);
        return vertx;
    }

    @Bean
    public MqttServer mqttServer(Vertx vertx) {

        MqttServer mqttServer = MqttServer.create(vertx);
        mqttServer.endpointHandler(endpoint -> {

            // shows main connect info
            System.out.println("MQTT client [" + endpoint.clientIdentifier() + "] request to connect, clean session = " + endpoint.isCleanSession());

            if (endpoint.auth() != null) {
                System.out.println("[username = " + endpoint.auth().userName() + ", password = " + endpoint.auth().password() + "]");
            }
            if (endpoint.will() != null) {
                System.out.println("[will topic = " + endpoint.will().willTopic() + " msg = " + endpoint.will().willMessage() +
                        " QoS = " + endpoint.will().willQos() + " isRetain = " + endpoint.will().isWillRetain() + "]");
            }

            System.out.println("[keep alive timeout = " + endpoint.keepAliveTimeSeconds() + "]");

            // accept connection from the remote client
            endpoint.accept(false);


            // disconnect
            endpoint.disconnectHandler(v -> {
                System.out.println("Received disconnect from client");
            });

            // subscribe
            endpoint.subscribeHandler(subscribe -> {
                List<MqttQoS> grantedQosLevels = new ArrayList<>();
                for (MqttTopicSubscription s : subscribe.topicSubscriptions()) {
                    System.out.println("Subscription for " + s.topicName() + " with QoS " + s.qualityOfService());
                    grantedQosLevels.add(s.qualityOfService());
                }
                // ack the subscriptions request
                endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);

            });


            // unsubcribe
            endpoint.unsubscribeHandler(unsubscribe -> {
                for (String t : unsubscribe.topics()) {
                    System.out.println("Unsubscription for " + t);
                }
                // ack the subscriptions request
                endpoint.unsubscribeAcknowledge(unsubscribe.messageId());
            });

            // publishHandler
            endpoint.publishHandler(message -> {
                System.out.println("Just received message [" + message.payload().toString(Charset.defaultCharset()) + "] with QoS [" + message.qosLevel() + "]");
                if (message.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
                    endpoint.publishAcknowledge(message.messageId());
                } else if (message.qosLevel() == MqttQoS.EXACTLY_ONCE) {
                    endpoint.publishReceived(message.messageId());
                }
            }).publishReleaseHandler(messageId -> {
                endpoint.publishComplete(messageId);
            });

            // publish
            endpoint.publish("my_topic",
                    Buffer.buffer("Hello from the Vert.x MQTT server"),
                    MqttQoS.EXACTLY_ONCE,
                    false,
                    false);
            // specifing handlers for handling QoS 1 and 2
            endpoint.publishAcknowledgeHandler(messageId -> {
                System.out.println("Received ack for message = " + messageId);
            }).publishReceivedHandler(messageId -> {
                endpoint.publishRelease(messageId);
            }).publishCompletionHandler(messageId -> {
                System.out.println("Received ack for message = " + messageId);
            });

            endpoint.pingHandler(v -> {
                System.out.println("Ping received from client");
            });

            mqttServer.close(v -> {
                System.out.println("MQTT server closed");
            });

        }).listen(ar -> {
            if (ar.succeeded()) {
                System.out.println("MQTT server is listening on port " + ar.result().actualPort());
            } else {
                System.out.println("Error on starting the server");
                ar.cause().printStackTrace();
            }
        });

        return mqttServer;
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
