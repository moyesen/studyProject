package com.flower.tetris;

/**
 * 初始化 T 型方块的初始进场形态
 *
 * @author mosen
 * @date 2020-04-03 15:26
 **/
public class TTetromino extends Tetromino {

    public TTetromino() {
        cells[0] = new Cell(0, 4, T_COLOR);
        cells[1] = new Cell(0, 3, T_COLOR);
        cells[2] = new Cell(0, 5, T_COLOR);
        cells[3] = new Cell(1, 4, T_COLOR);
        states = new Offset[]{
                new Offset(0, 0, -1, 0, 1, 0, 0, -1),
                new Offset(0, 0, 0, 1, 0, -1, -1, 0),
                new Offset(0, 0, 1, 0, -1, 0, 0, 1),
                new Offset(0, 0, 0, -1, 0, 1, 1, 0)
        };
    }

}
