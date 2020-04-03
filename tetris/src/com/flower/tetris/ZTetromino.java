package com.flower.tetris;

/**
 * 初始化 Z 型方块的初始进场形态
 * 
 * @author mosen
 * @date 2020-04-03 15:24
 **/
public class ZTetromino extends Tetromino {

    public ZTetromino() {
        cells[0] = new Cell(1, 4, Z_COLOR);
        cells[1] = new Cell(0, 3, Z_COLOR);
        cells[2] = new Cell(0, 4, Z_COLOR);
        cells[3] = new Cell(1, 5, Z_COLOR);
        states = new Offset[]{
                new Offset(0, 0, -1, 1, 0, 1, 1, 0),
                new Offset(0, 0, -1, -1, -1, 0, 0, 1)

        };
    }
}
