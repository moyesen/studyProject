package com.flower.tetris;
/**
 * 格子
 */
public class Cell {
	private int row;
	private int col;
	private int color;
	
	public Cell(int row, int col, int color) {
	  super();
	  this.row = row;
	  this.col = col;
	  this.color = color;
  }
	
	public void moveLeft(){
		col--;
	}
	
	public void moveRight(){
		col++;
	}
	
	public void drop(){
		row++;
	}
	
	
	public int getRow() {
  	return row;
  }

	public void setRow(int row) {
  	this.row = row;
  }

	public int getCol() {
  	return col;
  }

	public void setCol(int col) {
  	this.col = col;
  }

	public int getColor() {
  	return color;
  }

	public void setColor(int color) {
  	this.color = color;
  }

  public String toString() {
	  return  row + "," + col ;
  }
	
}
