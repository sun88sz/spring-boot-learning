package com.sun.test.service;

import com.sun.test.dao.ADao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AServiceImpl implements AService {

    @Autowired
    private ADao aDao;

    @Autowired
    private BService bService;


    @Override
    @Transactional
    public void a() {
        System.out.println("a");
        aDao.insert("A");

        try {
            bService.b();
        } catch (Exception e) {
            System.out.println("------------------------------------------------------------------!!!!!!");
            e.printStackTrace();
        }

//        System.out.println(1 / 0);
    }

}
