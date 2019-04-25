package com.sun.test.service;

import com.sun.test.dao.ADao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BServiceImpl implements BService {

    @Autowired
    private ADao aDao;

    @Override
//    @Transactional
    public void b() {
        System.out.println("b");

        aDao.insert(String.valueOf(Math.random()));

        aDao.insert("B");

//        System.out.println(1/0);
    }

}
