package com.flower.tetris;

/**
 * 初始化 O 型方块的初始进场形态
 *
 * @author mosen
 * @date 2020-04-03 15:24
 **/
public class OTetromino extends Tetromino {

    public OTetromino() {
        cells[0] = new Cell(0, 4, O_COLOR);
        cells[1] = new Cell(0, 5, O_COLOR);
        cells[2] = new Cell(1, 4, O_COLOR);
        cells[3] = new Cell(1, 5, O_COLOR);
        states = new Offset[]{
                new Offset(0, 0, 0, 1, 1, 0, 1, 1),
                new Offset(0, 0, 0, 1, 1, 0, 1, 1)
        };
    }

}
