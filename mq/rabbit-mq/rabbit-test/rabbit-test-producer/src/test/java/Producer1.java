import com.sun.RabbitProducerApplication;
import com.sun.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RabbitProducerApplication.class)
public class Producer1 {

    @Autowired
    private RabbitTemplate amqpTemplate;

    @Test
    public void test() {
        User user = User.createUser();
        amqpTemplate.convertAndSend("simpleExchange", "com.sun.topicA", user);
    }


}
