package com.flower.tetris;

import javax.swing.*;

/**
 * @author mosen
 * @date 2020-04-03 22:53
 **/
public class Main {
    public static void main(String[] args) {
        //Java Swing API
        JFrame frame = new JFrame("俄罗斯方块-简单版");
        Tetris pane = new Tetris();
        frame.add(pane);
        // 去除窗口的装饰
        // frame.setUndecorated(true);
        frame.setSize(530, 580);
        // 居中
        frame.setLocationRelativeTo(null);
        // 关闭窗口时候, 立即关闭软件
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        pane.action();
    }
}
