package com.sun.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.MessageInterpolator;
import javax.validation.Validator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
@Slf4j
public class ValidationMessageResolveUtil {

    private static Validator validator = null;
    private static MessageInterpolator msgInterpolator = null;

    static {
        if (validator == null) {
            LocalValidatorFactoryBean factory = ApplicationContextUtil.getBean(LocalValidatorFactoryBean.class);
            validator = factory.getValidator();
            msgInterpolator = factory.getMessageInterpolator();
        }
    }

    private static final Pattern elpattern = Pattern.compile("\\{[^{}]+\\}");

    public static String resolveMessage(String message) {
        Matcher matcher = elpattern.matcher(message);
        try {
            while (matcher.find()) {
                String el = matcher.group();
                // 用资源文件信息（资源文件必须是 ValidationMessages.properties）
                // 替换message = {key}{my.empty.message}
                // 注解这里的key会替换成注解NotEmpty定义的key，即
                // message = {user.name.empty}{my.empty.message}
                String val = msgInterpolator.interpolate(el, null);
                if (StringUtils.isBlank(val))
                    continue;
                message = message.replace(el, val);
            }
        } catch (Exception e) {
            log.error("验证引擎进行数据校验时出现异常, message:{}", message, e);
        }
        return message;
    }

}
