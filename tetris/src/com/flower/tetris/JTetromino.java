package com.flower.tetris;

/**
 * 初始化 I 型方块的初始进场形态
 *
 * @author mosen
 * @date 2020-04-03 15:20
 **/
public class JTetromino extends Tetromino {

    public JTetromino() {
        cells[0] = new Cell(0, 4, J_COLOR);
        cells[1] = new Cell(0, 3, J_COLOR);
        cells[2] = new Cell(0, 5, J_COLOR);
        cells[3] = new Cell(1, 5, J_COLOR);
        states = new Offset[]{
                new Offset(0, 0, -1, 0, 1, 0, 1, -1),
                new Offset(0, 0, 0, 1, 0, -1, -1, -1),
                new Offset(0, 0, 1, 0, -1, 0, -1, 1),
                new Offset(0, 0, 0, -1, 0, 1, 1, 1)
        };
    }

}
