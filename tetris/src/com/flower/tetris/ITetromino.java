package com.flower.tetris;

/**
 * 初始化 I 型方块的初始进场形态
 */
public class ITetromino extends Tetromino {

    public ITetromino() {
        cells[0] = new Cell(0, 4, I_COLOR);
        cells[1] = new Cell(0, 3, I_COLOR);
        cells[2] = new Cell(0, 5, I_COLOR);
        cells[3] = new Cell(0, 6, I_COLOR);
        states = new Offset[]{
                new Offset(0, 0, -1, 0, 1, 0, 2, 0),
                new Offset(0, 0, 0, -1, 0, 1, 0, 2)

        };
    }

}

	



