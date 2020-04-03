package com.flower.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * 俄罗斯方块
 * 继承了空白区域，扩展了方块墙和正在下落的方块
 */
public class Tetris extends JPanel {
    /**
     * 行
     */
    private static final int ROWS = 20;

    /**
     * 列
     */
    private static final int COLS = 10;

    /**
     * 方块墙
     */
    private Cell[][] wall = new Cell[ROWS][COLS];

    /**
     * 正在下落的方块
     */
    private Tetromino tetromino;

    /**
     * 下一个下落的方块
     */
    private Tetromino nextOne;

    /**
     * 分数
     */
    private int score;

    /**
     * 消除的行数
     */
    private int lines;

    /**
     * 得分数组
     */
    private static final int[] SCORE_LEVEL = {0, 1, 4, 10, 100};

    /**
     * 格子尺寸
     */
    private static final int CELL_SIZE = 26;

    /**
     * 格子边框颜色
     */
    private static final int BORDER = 0xB5D0FF;

    /**
     * 字体颜色
     */
    private static final int FONT_COLOR = 0X667799;

    /**
     * 字体大小
     */
    private static final int FONT_SIZE = 28;

    /**
     * 游戏结束标志
     */
    private boolean gameOver;

    /**
     * 暂停标志
     */
    private boolean pause;

    /**
     * 定时器
     */
    private Timer timer;

    /**
     * 随机生成下一个方块
     */
    public void nextTetromino() {
        if (wall[0][4] != null) {
            gameOver = true;
            gameOverAction();
            repaint();
        }
        if (nextOne == null) {
            nextOne = Tetromino.randomInstance();
        }
        tetromino = nextOne;
        nextOne = Tetromino.randomInstance();
    }

    /**
     * 是否能继续下落
     *
     * @return
     */
    public boolean canDrop() {
        Cell[] cells = tetromino.getCells();
        // 判断是否到边界
        for (Cell cell : cells) {
            if (cell.getRow() == ROWS - 1) {//遇到底边界, 不能下降了!
                return false;
            }
        }
        // 判断下面是否有不为空的格子
        for (Cell cell : cells) {
            int row = cell.getRow() + 1;
            int col = cell.getCol();
            if (wall[row][col] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 方块下落结束
     */
    public void landToWall() {
        Cell[] cells = tetromino.getCells();
        for (Cell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            wall[row][col] = cell;
        }
    }

    /**
     * 下落流程控制
     * 如果当前方块能够下落就当前方块下落一步,
     * 如果不能下落就着陆,并生成下个方块
     */
    public void softDropAction() {
        if (canDrop()) {
            tetromino.softDrop();
        } else {
            landToWall();    //清除满行, 并计分
            destroy();
            // 检查游戏是否结束
            isGameOver();
            nextTetromino();
        }
    }

    /**
     * 显示墙和方块
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (tetromino.contains(row, col)) {
                    str.append(row).append(",").append(col).append(" ");
                } else {
                    str.append(wall[row][col]).append(" ");
                }
            }
            str.append("\n");
        }

        return str.toString();
    }

    /**
     * 清空墙
     */
    public void emptyWall() {
        for (int row = 0; row < ROWS; row++) {
            Arrays.fill(wall[row], null);
        }
    }

    /**
     * 销毁（destroy）满行 并且计分
     */
    public void destroy() {
        int lines = 0;// 统计本次销毁的行数
        for (int row = 0; row < ROWS; row++) {
            if (fullCells(row)) {
                clearLine(row);
                lines++;// 每清除一行就累计加1
            }
        }
        //
        score += SCORE_LEVEL[lines];
        this.lines += lines;
    }

    /**
     * 消除行的同时该消除行的其他行到下一行
     *
     * @param row
     */
    public void clearLine(int row) {
        for (int i = row; i >= 1; i--) {
            System.arraycopy(wall[i - 1], 0, wall[i], 0, COLS);
        }
    }

    /**
     * 判断是否满行
     *
     * @param row
     * @return
     */
    public boolean fullCells(int row) {
        Cell[] line = wall[row];
        for (Cell cell : line) {
            if (cell == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 启动软件
     */
    public void action() {
        startAction();
        nextTetromino();
        //this代表当前的面板
        this.requestFocus();//请求输入焦点
        /**
         * 请求到输入焦点，当前面板就可以接受键盘事件了。
         * 增加当前面板的按键（key）事件监听（Listener)
         * KeyListenr是一个接口口，由Swing定义的
         * 只要实现接口，就可以获得哪个按键按下
         */
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_Q) {
                    System.exit(0);
                }
                if (gameOver) {
                    if (key == KeyEvent.VK_S) {
                        startAction();
                    }
                    return;
                }
                if (pause) {
                    if (key == KeyEvent.VK_C) {
                        continueAction();
                    }
                    return;//提前结束方法
                }

                switch (key) {
                    case KeyEvent.VK_DOWN:
                        softDropAction();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRightAction();
                        break;
                    case KeyEvent.VK_LEFT:
                        moveLeftAction();
                        break;
                    case KeyEvent.VK_UP:
                        rotateRightAction();
                        break;
                    case KeyEvent.VK_SPACE:
                        hardDropAction();
                        break;
                    case KeyEvent.VK_P:
                        pauseAction();
                        break;
                    case KeyEvent.VK_Z:
                        rotateLeftAction();
                        break;
                }
                repaint();
            }
        });
    }

    /**
     * 键盘向左旋转控制，边界和重合的处理
     */
    public void rotateLeftActioin() {
        tetromino.rotateLeft();
        if (outOfRange() || coincide()) {
            tetromino.rotateRight();
        }
    }

    /**
     * 键盘向右旋转控制，边界和重合的处理
     */
    public void rotateRightAction() {
        tetromino.rotateRight();
        if (outOfRange() || coincide()) {
            tetromino.rotateLeft();
        }
    }

    /**
     * 键盘向左旋转控制，边界和重合的处理
     */
    public void rotateLeftAction() {
        tetromino.rotateLeft();
        if (outOfRange() || coincide()) {
            tetromino.rotateRight();
        }
    }


    /**
     * 键盘向右控制，边界和重合的处理
     */
    public void moveRightAction() {
        tetromino.moveRight();
        //outOfRange 出界 coincide：重合
        if (outOfRange() || coincide()) {
            tetromino.moveLeft();
        }
    }

    /**
     * 键盘向左控制，边界和重合的处理
     */
    public void moveLeftAction() {
        tetromino.moveLeft();
        if (outOfRange() || coincide()) {
            tetromino.moveRight();
        }
    }

    /**
     * 键盘控制时，边界列（就是左右）的处理
     *
     * @return
     */
    private boolean outOfRange() {
        Cell[] cells = tetromino.getCells();
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            int col = cell.getCol();
            if (col < 0 || col >= COLS) {
                return true;
            }
        }
        return false;
    }

    /**
     * 重合的处理，返回false停止
     */
    private boolean coincide() {
        Cell[] cells = tetromino.getCells();
        for (Cell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            if (row >= 0 && row < ROWS && wall[row][col] != null) {
                return true;//重合
            }
        }
        return false;
    }

    /**
     * 下落流程控制
     * 如果但前方块能够下落就当前方块下落一步
     * 如果不能下录就着陆，并生成下个方块
     */
    public void softDropActiong() {
        if (canDrop()) {
            tetromino.softDrop();
        } else {
            landToWall();
            destroy();//清除满行，并计算分数
            //检查游戏结束了吗？
            isGameOver();
            nextTetromino();
        }
    }

    /**
     * 检查游戏是否结束
     */
    public void isGameOver() {
        if (wall[0][4] != null) {
            gameOver = true;
            gameOverAction();
            repaint();
        }
    }

    private void gameOverAction() {
        timer.cancel();
    }


    /**
     * 硬下落流程：能够下落就继续下落
     */
    public void hardDropAction() {
        while (canDrop()) {
            tetromino.softDrop();
        }
        landToWall();
        destroy();//清除满行，并计分
        isGameOver();
        nextTetromino();
    }


    /**
     * 控制游戏开始，start，调用计时器
     */
    public void startAction() {
        gameOver = false;
        pause = false;
        clearWall();
        nextTetromino();
        repaint();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                softDropAction();
                repaint();
            }
        }, 800, 800);

    }

    /**
     * 清墙
     */
    public void clearWall() {
        for (Cell[] line : wall) {
            Arrays.fill(line, null);
        }
    }

    /**
     * 控制暂停
     */
    public void pauseAction() {
        pause = true;
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * 控制继续
     */
    public void continueAction() {
        pause = false;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                softDropAction();
                repaint();
            }
        }, 0, 800);
    }


    private static Image background;

    static {
        try {
            //Tetris.class 和 tetris.png 在同一个package中
            background = ImageIO.read(Tetris.class.getResource("image/tetris.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重写JPanel类中的paint方法, 修改绘制行为
     * paint: 绘制, 涂抹
     * Graphics g: 理解为 绑定到当前面板的画笔
     */
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, this);
        g.translate(15, 15);
        //g.setColor(new Color(0x00ff00));
        //g.drawString("HI", 50, 50);
        //绘制墙
        paintWall(g);
        //绘制方块
        paintTetromino(g);
        //绘制下一个方块
        paintNextOne(g);
        //绘制分数
        paintScore(g);
    }

    /**
     * 画分数，行数
     *
     * @param g
     */
    private void paintScore(Graphics g) {
        Font f = getFont();
        Font font = new Font(f.getName(), Font.BOLD, FONT_SIZE);
        int x = 12 * CELL_SIZE - 10;
        int y = 6 * CELL_SIZE + 5;
        g.setColor(new Color(FONT_COLOR));//设置颜色
        g.setFont(font);
        String str = "SCORE" + this.score;
        g.drawString(str, x, y);//绘制字符串

        y += 55;
        str = "LINES:" + this.lines;
        g.drawString(str, x, y);

        y += 55;
        str = "[p]Pause";
        if (pause) {
            str = "[S]Start!";
        }
        g.drawString(str, x, y);

    }


    /**
     * 画方块
     *
     * @param g
     */
    private void paintTetromino(Graphics g) {
        if (tetromino == null) {
            return;
        }
        Cell[] cells = tetromino.getCells();
        for (Cell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            g.setColor(new Color(cell.getColor()));
            g.fillRect(CELL_SIZE * col, CELL_SIZE * row, CELL_SIZE, CELL_SIZE);
            g.setColor(new Color(Tetromino.BORDER_COLOR));
            g.drawRect(CELL_SIZE * col, CELL_SIZE * row, CELL_SIZE, CELL_SIZE);
        }
    }

    /**
     * 画墙
     *
     * @param g
     */
    public void paintWall(Graphics g) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Cell cell = wall[row][col];
                if (cell == null) {
                    g.setColor(new Color(BORDER));
                    g.drawRect(CELL_SIZE * col, CELL_SIZE * row, CELL_SIZE, CELL_SIZE);
                } else {
                    g.setColor(new Color(cell.getColor()));
                    g.fillRect(CELL_SIZE * col, CELL_SIZE * row, CELL_SIZE, CELL_SIZE);
                    g.setColor(new Color(BORDER));
                    g.drawRect(CELL_SIZE * col, CELL_SIZE * row, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    /**
     * 画下一个方块
     *
     * @param g
     */
    public void paintNextOne(Graphics g) {
        if (nextOne == null) {
            return;
        }
        Cell[] cells = nextOne.getCells();
        for (Cell cell : cells) {
            int row = cell.getRow() + 1;
            int col = cell.getCol() + 10;
            g.setColor(new Color(cell.getColor()));
            g.fillRect(CELL_SIZE * col, CELL_SIZE * row,
                    CELL_SIZE, CELL_SIZE);
        }
    }

}





