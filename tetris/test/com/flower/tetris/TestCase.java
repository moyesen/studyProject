package com.flower.tetris;

import org.junit.Test;

/**
 * test: 测试
 */
public class TestCase {
    @Test
    public void testTetromino() {
        System.out.println("测试随机生成4格方块:");
        Tetromino t = Tetromino.randomInstance();
        System.out.println(t.getClass());
        System.out.println(t);
        t.softDrop();
        System.out.println(t);
    }

    @Test
    public void testSoftDropAction() {
        System.out.println("测试下落流程");
        Tetris tetris = new Tetris();
        tetris.nextTetromino();
        System.out.println(tetris);

        for (int i = 0; i < 25; i++) {
            tetris.softDropAction();
            System.out.println(tetris);
        }

    }
}



