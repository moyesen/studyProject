package com.flower.tetris;

import java.util.Arrays;
import java.util.Random;

/**
 * 四格方块抽象类
 *
 * @author mosen
 * @date 2020-04-03 15:14
 **/
public abstract class Tetromino {
    protected Cell[] cells = new Cell[4];

    public static final int I_COLOR = 0x00F0F0;
    public static final int T_COLOR = 0xA000F1;
    public static final int S_COLOR = 0x00F000;
    public static final int Z_COLOR = 0xF00000;
    public static final int J_COLOR = 0x0000F0;
    public static final int L_COLOR = 0xF0A000;
    public static final int O_COLOR = 0xF0F000;
    public static final int BORDER_COLOR = 0x555255;

    /**
     * 简单工厂方法，用于生产随机的4格方块对象
     */
    public static Tetromino randomInstance() {
        Random random = new Random();
        int n = random.nextInt(7);
        switch (n) {
            case 0:
                return new ITetromino();
            case 1:
                return new JTetromino();
            case 2:
                return new TTetromino();
            case 3:
                return new LTetromino();
            case 4:
                return new STetromino();
            case 5:
                return new ZTetromino();
            case 6:
                return new OTetromino();
        }
        return null;
    }

    public String toString() {
        return Arrays.toString(cells);
    }

    public void moveLeft() {
        for (int i = 0; i < cells.length; i++) {
            cells[i].moveLeft();
        }
    }

    public void moveRight() {
        for (int i = 0; i < cells.length; i++) {
            cells[i].moveRight();
        }
    }

    public void softDrop() {
        for (Cell cell : cells) {
            cell.drop();
        }
    }

    /**
     * 向右旋转
     */
    public void rotateRight() {
        index++;
        Tetromino.Offset offset = states[index % states.length];
        Cell o = cells[0];
        int row = o.getRow();
        int col = o.getCol();
        cells[1].setRow(row + offset.row1);
        cells[1].setCol(col + offset.col1);
        cells[2].setRow(row + offset.row2);
        cells[2].setCol(col + offset.col2);
        cells[3].setRow(row + offset.row3);
        cells[3].setCol(col + offset.col3);
    }

    /**
     * 向左旋转
     */
    public void rotateLeft() {
        index--;
        Tetromino.Offset offset = states[index % states.length];
        Cell o = cells[0];
        int row = o.getRow();
        int col = o.getCol();
        cells[1].setRow(row + offset.row1);
        cells[1].setCol(col + offset.col1);
        cells[2].setRow(row + offset.row2);
        cells[2].setCol(col + offset.col2);
        cells[3].setRow(row + offset.row3);
        cells[3].setCol(col + offset.col3);

    }


    /**
     * 旋转的状态就是在基本位置上加上相对位置的数值
     * 下面设置每一个方块的偏移量
     *
     * @return
     */
    protected class Offset {
        int row0, col0, row1, col1, row2, col2, row3, col3;

        public Offset(int row0, int col0, int row1, int col1, int row2,
                      int col2, int row3, int col3) {
            super();
            this.row0 = row0;
            this.col0 = col0;
            this.row1 = row1;
            this.col1 = col1;
            this.row2 = row2;
            this.col2 = col2;
            this.row3 = row3;
            this.col3 = col3;
        }
    }

    protected Tetromino.Offset[] states;
    protected int index = 10000 - 1;


    public Cell[] getCells() {
        return cells;
    }

    public boolean contains(int row, int col) {
        for (Cell cell : cells) {
            if (cell.getRow() == row && cell.getCol() == col) {
                return true;
            }
        }
        return false;
    }

}
