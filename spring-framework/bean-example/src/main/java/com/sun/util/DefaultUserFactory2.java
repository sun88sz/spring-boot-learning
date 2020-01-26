package com.sun.util;

import com.sun.bean.User;

/**
 * @author Sun
 */
public class DefaultUserFactory2 implements UserFactory {

    @Override
    public User createUser() {
        return User.builder().age(1).name("DefaultUserFactoryUser2").build();
    }
}
