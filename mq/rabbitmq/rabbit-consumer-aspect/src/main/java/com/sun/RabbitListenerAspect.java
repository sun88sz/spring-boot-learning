package com.sun;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rabbitmq.client.Channel;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Description: <br/>
 * Date: 2018-08-26
 *
 * @author Sun
 */
@Aspect
@Component
public class RabbitListenerAspect {
    final static Logger log = LoggerFactory.getLogger(RabbitListener.class);


    @Pointcut("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    public void rabbitListenerAspect() {
    }

    @AfterThrowing(pointcut = "rabbitListenerAspect()", throwing = "e")
    public void handleThrowing(JoinPoint point, Exception e) {
        Object[] args = point.getArgs();
        if (args != null && args.length > 0) {
            List<Object> argsList = Arrays.asList(args);
            Optional<Object> channelOpt = argsList.stream().filter(f -> f instanceof Channel).findFirst();
            Optional<Object> messageOpt = argsList.stream().filter(f -> f instanceof Message).findFirst();
            if (channelOpt.isPresent() && messageOpt.isPresent()) {
                Channel c = (Channel) channelOpt.get();
                Message m = (Message) messageOpt.get();
                try {
                    c.basicNack(m.getMessageProperties().getDeliveryTag(), false, false);
                } catch (IOException ioe) {
                    log.error("mq重置错误 {}", ioe);
                }
            }

            List<Object> collect = argsList.stream().filter(f -> !(f instanceof Channel)).filter(f -> !(f instanceof Message)).collect(Collectors.toList());
            // json
            log.error("mq消费错误 {} \r\n mq消费内容 {}", e, collect);
        }
    }
}
