import com.sun.VertxMqttApplication;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author : Sun
 * @date : 2018/9/20 11:41
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VertxMqttApplication.class)
public class VertxMqttTest {

    @Autowired
    private MqttClient mqttClient;

    @Test
    public void sendTest() {
        
        
        mqttClient.subscribe("temp",2);
        
        mqttClient.publish("temp",
                Buffer.buffer("hello"),
                MqttQoS.AT_LEAST_ONCE,
                false,
                false);

    }
}
