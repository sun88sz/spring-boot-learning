package com.sun;

//public class ThreadSnowFlake extends Thread {
//
//    MagicSnowFlake msf;
//
//    int cnt = 0;
//
//    public ThreadSnowFlake(MagicSnowFlake msf) {
//        this.msf = msf;
//    }
//
//    @Override
//    public void run() {
//        System.out.println();
//        if (msf != null) {
//            while (cnt < 10) {
//                System.out.print(Thread.currentThread().getId() + " : " + msf.nextId());
//                cnt++;
//            }
//        }
//    }
//}
//
//
//class AlgorithmMain {
//
//    public static void main(String[] args) {
//
//        MagicSnowFlake msf = new MagicSnowFlake(196, 2);
//
//        ThreadSnowFlake t1 = new ThreadSnowFlake(msf);
//        ThreadSnowFlake t2 = new ThreadSnowFlake(msf);
//
//        t1.start();
//        t2.start();
//    }
//}