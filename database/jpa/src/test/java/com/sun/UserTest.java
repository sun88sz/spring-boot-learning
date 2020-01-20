package com.sun;

import com.sun.dao.UserRepository;
import com.sun.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = JpaApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class UserTest {

    @Resource
    private UserRepository userRepository;

    @Test
    public void testH2() {

        User user = User.builder().name("xxxx").url("http://111.com").build();
        User save = userRepository.save(user);
        log.info("user.id {}", save.getId());

        Optional<User> byId = userRepository.findById(save.getId());
        log.info("xx {}", byId.orElse(null));

        User del = User.builder().id(save.getId()).build();
        userRepository.delete(del);


        Optional<User> byId2 = userRepository.findById(save.getId());
        log.info("xx {}", byId2.orElse(null));
    }

    @Test
    public void xxxx() {

        User user = User.builder().name("xxxx").url("http://111.com").build();
        userRepository.save(user);


        User query = User.builder().id(1L).name("xxx").url("http").build();

        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withIncludeNullValues()
                .withMatcher("url", ExampleMatcher.GenericPropertyMatchers.startsWith()); //改变“Null值处理方式”：包括
//                .withIgnorePaths("id","name","sex","age","focus","addTime","remark","customerType");  //忽略其他属性

        //创建实例
        Example<User> ex = Example.of(query, matcher);

        List<User> all = userRepository.findAll(ex);

    }
}
