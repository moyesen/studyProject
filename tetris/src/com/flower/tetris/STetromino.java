package com.flower.tetris;

/**
 * 初始化 S 型方块的初始进场形态
 * 
 * @author mosen
 * @date 2020-04-03 15:24
 **/
public class STetromino extends Tetromino {

    public STetromino() {
        cells[0] = new Cell(0, 4, S_COLOR);
        cells[1] = new Cell(0, 5, S_COLOR);
        cells[2] = new Cell(1, 3, S_COLOR);
        cells[3] = new Cell(1, 4, S_COLOR);
        states = new Offset[]{
                new Offset(0, 0, -1, 0, 1, 1, 0, 1),
                new Offset(0, 0, 0, 1, 1, -1, 1, 0)};
    }
}
