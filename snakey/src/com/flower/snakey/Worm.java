package com.flower.snakey;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * 贪吃蛇
 *
 * @author mosen
 */
public class Worm extends JPanel {
    /**
     * 定义格子大小
     */
    private static final int CELL_SIZE = 26;
    /**
     * 墙
     */
    private Cell[][] wall;
    /**
     * 蛇
     */
    private Cell[] snaker;
    /**
     * 食物
     */
    private Cell food;
    /**
     * 定义蛇运动的方向,3,6,9,12=右，下，左，上
     */
    private int direction = 3;
    /**
     * 背景图
     */
    private static BufferedImage background;
    /**
     * 格子图，蛇和食物颜色图标
     */
    private static BufferedImage cellIcon;
    /**
     * 游戏结束图
     */
    private static BufferedImage gameOver;
    /**
     * 行数
     */
    private static final int ROW = 21;
    /**
     * 列数
     */
    private static final int COL = 20;
    /**
     * 定时器
     */
    private Timer timer;
    /**
     * 定义间隔时间/毫秒
     */
    private long time = 300;

    /**
     * 是否结束
     */
    private boolean gameIsOver;

    // 静态代码块，加载图片
    static {
        try {
            background = ImageIO.read(Worm.class.getResource("background.png"));
            cellIcon = ImageIO.read(Worm.class.getResource("T.png"));
            gameOver = ImageIO.read(Worm.class.getResource("game-over.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重写paint()方法
     */
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, null);
        paintWall(g);
        paintSnaker(g);
        paintFood(g);
        if (gameIsOver) {
            g.drawImage(gameOver, 0, 0, null);
        }
    }

    /**
     * 画食物
     *
     * @param g
     */
    private void paintFood(Graphics g) {
        int x = food.getCol() * CELL_SIZE;
        int y = food.getRow() * CELL_SIZE;
        g.drawImage(food.getImage(), x - 1, y - 1, null);
    }

    /**
     * 画蛇
     *
     * @param g
     */
    private void paintSnaker(Graphics g) {
        for (Cell cell : snaker) {
            int x = cell.getCol() * CELL_SIZE;
            int y = cell.getRow() * CELL_SIZE;
            g.drawImage(cell.getImage(), x - 1, y - 1, null);
        }
    }

    /**
     * 画墙
     *
     * @param g
     */
    private void paintWall(Graphics g) {
        for (int row = 0; row < wall.length; row++) {
            Cell[] line = wall[row];
            for (int col = 0; col < line.length; col++) {
                Cell cell = line[col];
                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;
                if (cell == null) {
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    /**
     * 进行属性初始化 实现KeyAdapter进行按键监听
     */
    public void Action() {
        wall = new Cell[ROW][COL];
        snaker = new Cell[4];
        snaker[0] = new Cell(0, 3, cellIcon);
        snaker[1] = new Cell(0, 2, cellIcon);
        snaker[2] = new Cell(0, 1, cellIcon);
        snaker[3] = new Cell(0, 0, cellIcon);
        food = foodMaker();

        startAction();

        KeyAdapter keyAdapter = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (gameIsOver) {
                    if (key == KeyEvent.VK_S) {
                        restart();
                    }
                }
                switch (key) {
                    case KeyEvent.VK_LEFT:
                        changeLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        changeRight();
                        break;
                    case KeyEvent.VK_DOWN:
                        changeDown();
                        break;
                    case KeyEvent.VK_UP:
                        changeUp();
                }
                repaint();
            }

        };
        this.requestFocus();
        this.addKeyListener(keyAdapter);
    }

    /**
     * 重新开始游戏
     * 重新初始化，创建Timer
     */
    protected void restart() {
        time = 300;
        direction = 3;
        gameIsOver = false;
        snaker = new Cell[4];
        snaker[0] = new Cell(0, 3, cellIcon);
        snaker[1] = new Cell(0, 2, cellIcon);
        snaker[2] = new Cell(0, 1, cellIcon);
        snaker[3] = new Cell(0, 0, cellIcon);
        food = foodMaker();
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                snakerMove();
                repaint();
            }

        }, time, time);

    }

    /**
     * 转向上
     */
    private void changeUp() {
        if (direction != 6) {
            direction = 12;
            snakerMove();
        }
    }

    /**
     * 转向下
     */
    private void changeDown() {
        if (direction != 12) {
            direction = 6;
            snakerMove();
        }
    }

    /**
     * 转向右
     */
    private void changeRight() {
        if (direction != 9) {
            direction = 3;
            snakerMove();
        }
    }

    /**
     * 转向左
     */
    private void changeLeft() {
        if (direction != 3) {
            direction = 9;
            snakerMove();
        }
    }

    /**
     * 启动Timer 开始移动
     */
    private void startAction() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                snakerMove();
                repaint();
            }
        }, time, time);
    }

    /**
     * 实现蛇的移动
     */
    private void snakerMove() {
        if (snaker.length - 1 >= 0){
            System.arraycopy(snaker, 0, snaker, 1, snaker.length - 1);
        }
        snaker[0] = getNewHead();

        if (outofBound() || eatSelf()) {
            gameOver();
        }
        if (getFood()) {
            eatFood();
            food = foodMaker();
        }
    }

    /**
     * 游戏结束
     */
    private void gameOver() {
        timer.cancel();
        gameIsOver = true;
    }

    /**
     * 判断蛇头是否撞倒自己
     *
     * @return
     */
    private boolean eatSelf() {
        int row0 = snaker[0].getRow();
        int col0 = snaker[0].getCol();
        for (int i = 1; i < snaker.length; i++) {
            if (row0 == snaker[i].getRow() && col0 == snaker[i].getCol()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断蛇是否撞倒边界
     *
     * @return
     */
    private boolean outofBound() {

        int row = snaker[0].getRow();
        int col = snaker[0].getCol();
        if (row < 0 || row > ROW - 1 || col < 0 || col > COL - 1) {
            return true;
        }
        return false;
    }

    /**
     * 判断蛇是否碰到食物
     *
     * @return
     */
    private boolean getFood() {

        Cell c = snaker[0];
        if (c.getRow() == food.getRow() && c.getCol() == food.getCol()) {
            return true;
        }
        return false;
    }

    /**
     * 实现蛇吃食物长大
     */
    private void eatFood() {
        int end = snaker.length;
        snaker = Arrays.copyOf(snaker, end + 1);//用数组扩容实现蛇变长
        for (int i = end; i > 0; i--) {
            //去掉数组最后一个元素，并将数组每个元素向后移动一位
            snaker[i] = snaker[i - 1];
        }
        snaker[0] = food; //新蛇头

        // 让蛇吃食物后移动速度加快
        if (time != 20) {
            time -= 10;
            timer.cancel();
            startAction();
        }
    }

    /**
     * 在蛇移动方向的前面产生新的蛇头
     *
     * @return
     */
    private Cell getNewHead() {
        Cell c = snaker[0];
        int row = c.getRow();
        int col = c.getCol();

        switch (direction) {
            case 3:
                return new Cell(row, col + 1, cellIcon);//向右移动时
            case 6:
                return new Cell(row + 1, col, cellIcon);//向下移动时
            case 9:
                return new Cell(row, col - 1, cellIcon);//向左移动时
            case 12:
                return new Cell(row - 1, col, cellIcon);//向上移动时
            default:
                return null;
        }
    }

    /**
     * 随机制造食物
     *
     * @return
     */
    private Cell foodMaker() {
        Random r = new Random();
        int row;
        int col;
        do {
            row = r.nextInt(ROW);
            col = r.nextInt(COL);
        } while (isOnSnaker(row, col));

        return new Cell(row, col, cellIcon);
    }

    /**
     * 判断食物是否出现在蛇的身上
     *
     * @param row
     * @param col
     * @return
     */
    private boolean isOnSnaker(int row, int col) {
        Cell cell = new Cell(row, col, cellIcon);
        for (Cell value : snaker) {
            if (cell.getRow() == value.getRow() && cell.getCol() == value.getCol()) {
                return true;
            }
        }
        return false;
    }

}
