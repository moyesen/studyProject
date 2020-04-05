package com.flower.snakey;

import java.awt.Image;

/**
 * 格子
 *
 * @author mosen
 */
public class Cell {

    private int row;
    private int col;
    private Image image;

    public Cell(int row, int col, Image image) {
        super();
        this.row = row;
        this.col = col;
        this.image = image;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void Down() {
        row++;
    }

    public void Left() {
        col--;
    }

    public void Right() {
        col++;
    }

}
