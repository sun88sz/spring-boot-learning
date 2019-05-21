package com.sun;

import java.io.*;

public class Reader {

    public static void main(String args[]) {
        System.out.println("file Go...");
        // 这里改成你要遍历的目录路径
        recursiveFiles("D:\\Programming\\workspace\\gcb\\gcb-storage\\src\\main\\java");
        System.out.println("file End.");
    }

    /**
     * Xiwi
     * 遍历文件/文件夹 - 函数
     * [String]path        文件路径
     */
    private static void recursiveFiles(String path) {

        // 创建 File对象
        File file = new File(path);

        // 取 文件/文件夹
        File files[] = file.listFiles();

        // 对象为空 直接返回
        if (files == null) {
            return;
        }

        // 目录下文件
        if (files.length == 0) {
            System.out.println(path + "该文件夹下没有文件");
        }

        // 存在文件 遍历 判断
        for (File f : files) {
            // 判断是否为 文件夹
            if (f.isDirectory()) {
                // 为 文件夹继续遍历
                recursiveFiles(f.getAbsolutePath());
                // 判断是否为 文件
            } else if (f.isFile()) {
                readController(f);
            } else {
                System.out.print("未知错误文件");
            }
        }
    }

    private static void readController(File file) {
        String fileName = file.getName();
        if (fileName.toLowerCase().endsWith("controller.java")) {


            System.out.println();
            System.out.println(fileName + "----------------------------------------------------");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String str = null;
                while ((str = br.readLine()) != null) {
                    if (str.trim().startsWith("@RequestMapping")) {
                        System.out.println(str.trim());
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
