package com.sun.util;

import com.sun.bean.User;

/**
 * @author Sun
 */
public interface UserFactory {

    /**
     * 工厂方法
     * @return
     */
    default User createUser(){
        return new User();
    }
}
