package com.flower.snakey;

import javax.swing.*;

/**
 * @author mosen
 * @date 2020-04-05 10:22
 **/
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Worm worm = new Worm();
        frame.add(worm);
        // 窗口大小
        frame.setSize(530, 580);
        frame.setTitle("贪吃蛇");
        // 窗口居中
        frame.setLocationRelativeTo(null);
        // 关闭窗口的时候关闭程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 会自动调用paint()方法
        frame.setVisible(true);
        worm.Action();

    }
}
