package com.flower.mangame;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * 躲弹珠小游戏
 *
 * @author mosen
 * @date 2020-04-04 16:17
 **/
public class Game extends JFrame {
    /**
     * 游戏开始时间
     */
    private long firsTime;
    /**
     * 游戏结束时间
     */
    private long lastTime;
    /**
     * 游戏面板
     */
    private JPanel jPanel = null;

    /**
     * 游戏躲珠人角色
     */
    private JButton heroBtn = null;

    /**
     * 按键起来
     */
    private boolean keyUp;

    /**
     * 按键下去
     */
    private boolean keyDown;

    /**
     * 左方向键
     */
    private boolean keyLeft;

    /**
     * 右方向键
     */
    private boolean keyRight;

    /**
     * 定义玩家的行走步伐，数值越大，移动速度越快
     */
    private int step = 3;

    /**
     * 躲珠人位置（x,y）
     */
    private Point point;

    /**
     * 躲珠人位置x
     */
    private double x = 0.0;

    /**
     * 躲珠人位置y
     */
    private double y = 0.0;

    /**
     * 定义了子弹的个数
     */
    private int bulletNum = 70;

    /**
     * 游戏是否活着
     */
    private boolean gameAlive = true;

    /**
     * 游戏结束提示标签，显示时间信息
     */
    private JLabel jLabel = null;

    /**
     * 重新开始按钮
     */
    private JButton restartBtn = null;

    /**
     * 弹珠列表
     */
    private ArrayList<JButton> btnList = new ArrayList<>();

    /**
     * 弹珠线程列表
     */
    private ArrayList<Thread> threadList = new ArrayList<>();

    /**
     * 活着红色图标
     */
    private URL aliveGifUrl = this.getClass().getResource("game_play_alive_red.gif");

    /**
     * 挑战失败时蓝色图标
     */
    private URL deadGifUrl = this.getClass().getResource("game_play_dead_blue.gif");

    public Game() {
        super();
        initialize();
    }


    /**
     * 重新开始按钮
     *
     * @return
     */
    private JButton getRestartBtn() {
        if (restartBtn == null) {
            restartBtn = new JButton();
            restartBtn.setBounds(new Rectangle(478, 361, 164, 51));
            restartBtn.setText("重新开始");
            restartBtn.setVisible(false);
            restartBtn.addActionListener(new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    restartBtn.setVisible(false);
                    jLabel.setVisible(false);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    reset();

                }
            });
        }
        return restartBtn;
    }

    /**
     * 游戏重置
     */
    public void reset() {
        keyUp = false;
        keyDown = false;
        keyLeft = false;
        keyRight = false;
        int init = 0;
        while (init < bulletNum) {
            btnList.get(init).setBounds(new Rectangle(-50, -50, 10, 10));
            init++;
        }
        gameAlive = true;
        heroBtn.setIcon(new ImageIcon(aliveGifUrl));
        heroBtn.setLocation(320, 320);
        point = heroBtn.getLocation();
        x = point.getX();
        y = point.getY();
        firsTime = new Date().getTime();
    }

    /**
     * 开始游戏,准备70个弹珠，躲弹珠人角色
     */
    public void start() {
        int init = 0;
        while (init < bulletNum) {
            JButton jb = new JButton();
            jb.setBounds(new Rectangle(-50, -50, 10, 10));
            jb.setEnabled(false);
            BulletThread bulletThread = new BulletThread(jb);
            Thread thread = new Thread(bulletThread);
            btnList.add(jb);
            threadList.add(thread);
            init++;
        }
        HeroThread heroThread = new HeroThread();
        Thread tm = new Thread(heroThread);
        tm.start();
    }

    /**
     * 开始弹珠线程
     */
    public void startBulletThread() {
        int init = 0;
        while (init < bulletNum) {
            threadList.get(init).start();
            init++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化窗口界面
     */
    private void initialize() {
        this.setSize(700, 700);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(1);
            }
        });
        this.setResizable(false);
        this.setContentPane(getJPanel());
        this.setTitle("躲弹珠小游戏！");
        this.setVisible(true);

        Image image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("fireCloud.jpg"));
        ImageIcon img = new ImageIcon(image);
        //要设置的背景图片
        JLabel imgLabel = new JLabel();
        imgLabel.setIcon(img);
        //将背景图放在标签里。
        this.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
        imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        // 设置背景标签的位置
        Container contain = this.getContentPane();
        ((JPanel) contain).setOpaque(false);
        this.setBackground(new Color(1, 1, 1));
    }

    /**
     * 初始化游戏面板
     *
     * @return
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(42, -33, 595, 308));
            jLabel.setFont(new Font("Dialog", Font.BOLD, 24));
            jLabel.setForeground(new Color(250, 2, 2));
            jLabel.setEnabled(true);
            jLabel.setVisible(false);
            jPanel = new JPanel();
            jPanel.setLayout(null);
            jPanel.add(getHeroBtn(), null);
            jPanel.setForeground(new Color(1, 1, 1));
            jPanel.setBackground(new Color(1, 1, 1));
            jPanel.setVisible(true);
            jPanel.add(jLabel, null);
            jPanel.add(getRestartBtn(), null);
        }
        return jPanel;
    }

    /**
     * 躲弹珠人按钮初始化
     *
     * @return
     */
    private JButton getHeroBtn() {
        if (heroBtn == null) {
            heroBtn = new JButton();
            heroBtn.setBounds(new Rectangle(320, 320, 30, 30));
            heroBtn.setBackground(new Color(1, 1, 1));
            point = heroBtn.getLocation();
            x = point.getX();
            y = point.getY();
            heroBtn.setIcon(new ImageIcon(aliveGifUrl));
            heroBtn.addKeyListener(new java.awt.event.KeyAdapter() {
                /**
                 * Invoked when a key has been released.
                 * @param e
                 */
                public void keyReleased(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == 10) {
                        if (!gameAlive) {
                            restartBtn.setVisible(false);
                            jLabel.setVisible(false);
                            reset();
                        }
                    }
                    if (e.getKeyCode() == 37) {
                        keyLeft = false;
                    }
                    if (e.getKeyCode() == 38) {
                        keyUp = false;
                    }
                    if (e.getKeyCode() == 39) {
                        keyRight = false;
                    }
                    if (e.getKeyCode() == 40) {
                        keyDown = false;
                    }
                }

                /**
                 * Invoked when a key has been pressed.
                 *
                 * @param e
                 */
                public void keyPressed(java.awt.event.KeyEvent e) {
                    // 触发按左键
                    if (e.getKeyCode() == 37) {
                        keyLeft = true;
                    }
                    // 触发按上键
                    if (e.getKeyCode() == 38) {
                        keyUp = true;
                    }
                    // 触发按右键
                    if (e.getKeyCode() == 39) {
                        keyRight = true;
                    }
                    // 触发按下键
                    if (e.getKeyCode() == 40) {
                        keyDown = true;
                    }
                }
            });
        }
        return heroBtn;
    }

    /**
     * 躲弹珠人线程
     */
    class HeroThread implements Runnable {
        public void run() {
            while (true) {
                while (gameAlive) {
                    point = heroBtn.getLocation();
                    // 向上
                    if (keyUp) {
                        if (keyLeft) {
                            // 同时按左键
                            x = point.getX();
                            y = point.getY();
                            if (x > 0 && y > 0) {
                                heroBtn.setLocation((int) x - step, (int) y - step);
                            }
                        } else if (keyRight) {
                            // 同时按右键
                            x = point.getX();
                            y = point.getY();
                            if (x + 40 < 700 && y > 0) {
                                heroBtn.setLocation((int) x + step, (int) y - step);
                            }
                        } else {
                            // 只按上键
                            x = point.getX();
                            y = point.getY();
                            if (y > 0) {
                                heroBtn.setLocation((int) x, (int) y - step);
                            }
                        }
                    }
                    if (keyDown) {
                        if (keyLeft) {
                            x = point.getX();
                            y = point.getY();
                            if (y + 60 < 700 && x > 0) {
                                heroBtn.setLocation((int) x - step, (int) y
                                        + step);
                            }
                        } else if (keyRight) {
                            x = point.getX();
                            y = point.getY();
                            if (x + 40 < 700 && y + 60 < 700) {
                                heroBtn.setLocation((int) x + step, (int) y
                                        + step);
                            }
                        } else {
                            x = point.getX();
                            y = point.getY();
                            if (y + 60 < 700) {
                                heroBtn.setLocation((int) x, (int) y + step);
                            }
                        }
                    }
                    if (keyLeft) {
                        if (keyUp) {
                            x = point.getX();
                            y = point.getY();
                            if (x > 0 && y > 0) {
                                heroBtn.setLocation((int) x - step, (int) y - step);
                            }
                        } else if (keyDown) {
                            x = point.getX();
                            y = point.getY();
                            if (y + 60 < 700 && x > 0) {
                                heroBtn.setLocation((int) x - step, (int) y + step);
                            }
                        } else {
                            x = point.getX();
                            y = point.getY();
                            if (x > 0) {
                                heroBtn.setLocation((int) x - step, (int) y);
                            }
                        }
                    }
                    if (keyRight) {
                        if (keyUp) {
                            x = point.getX();
                            y = point.getY();
                            if (x + 40 < 700 && y > 0) {
                                heroBtn.setLocation((int) x + step, (int) y - step);
                            }
                        } else if (keyDown) {
                            x = point.getX();
                            y = point.getY();
                            if (x + 40 < 700 && y + 60 < 700) {
                                heroBtn.setLocation((int) x + step, (int) y + step);
                            }
                        } else {
                            x = point.getX();
                            y = point.getY();
                            if (x + 40 < 700) {
                                heroBtn.setLocation((int) x + step, (int) y);
                            }
                        }
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 子弹线程类
     */
    class BulletThread implements Runnable {

        public BulletThread(JButton jjb) {
            jb = jjb;
        }

        JButton jb;

        private boolean first = true;

        public void run() {
            while (gameAlive) {
                start();
            }
        }

        public void start() {
            // 弹珠位置x
            int zx = 0;
            // 弹珠位置y
            int zy = 0;

            // 定义子弹与物体之间的步长
            int zzx = 0;
            int zzy = 0;
            while (true) {
                if (gameAlive) {
                    int direction = (int) (Math.random() * 4 + 1);
                    // 四个if随即从四个边发射子弹
                    if (direction == 1) {
                        zx = 0;
                        zy = (int) (Math.random() * 701);
                    }
                    if (direction == 2) {
                        zx = (int) (Math.random() * 701);
                        zy = 0;
                    }
                    if (direction == 3) {
                        zx = 700;
                        zy = (int) (Math.random() * 701);
                    }
                    if (direction == 4) {
                        zx = (int) (Math.random() * 701);
                        zy = 700;
                    }
                    // 初始化子弹，有了就不在加了
                    if (first) {
                        jPanel.add(jb, null);
                        first = false;
                    }
                    jb.setBounds(new Rectangle(zx, zy, 10, 10));
                    // 定义子弹与物体之间的步长
                    zzx = (int) (((x + 15) - zx) / 30);
                    zzy = (int) (((y + 15) - zy) / 30);
                }
                while (gameAlive) {
                    try {
                        zx += zzx;
                        zy += zzy;
                        jb.setLocation(zx, zy);
                        if (zx + 5 > x & zx + 5 < x + 30 & zy + 5 > y & zy + 5 < y + 30) {
                            heroBtn.setIcon(new ImageIcon(deadGifUrl));
                            gameAlive = false;
                            first = true;
                            restartBtn.setVisible(true);
                            jLabel.setVisible(true);
                            lastTime = new Date().getTime();
                            Date gametime = new Date(lastTime - firsTime);
                            int min = 0;
                            int sec = 0;
                            min = gametime.getMinutes();
                            sec = gametime.getSeconds();
                            String endtime = "";
                            if (min != 0) {
                                endtime = min + "分  " + sec + "秒";
                            } else {
                                endtime = sec + "秒";
                            }
                            jLabel.setText("                          GAME OVER!!! \n用时：" + endtime);
                            break;
                        }
                        // 超出边线停止循环
                        if (zx > 700 | zy > 700 | zx < 0 | zy < 0) {
                            break;
                        }
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
