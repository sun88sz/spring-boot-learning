package com.sun.util;

import com.sun.bean.User;

/**
 * @author Sun
 */
public class DefaultUserFactory implements UserFactory {

    @Override
    public User createUser() {
        return User.builder().age(1).name("DefaultUserFactoryUser").build();
    }
}
