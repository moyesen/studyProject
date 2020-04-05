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
 * �㵯��С��Ϸ
 *
 * @author mosen
 * @date 2020-04-04 16:17
 **/
public class Game extends JFrame {
    /**
     * ��Ϸ��ʼʱ��
     */
    private long firsTime;
    /**
     * ��Ϸ����ʱ��
     */
    private long lastTime;
    /**
     * ��Ϸ���
     */
    private JPanel jPanel = null;

    /**
     * ��Ϸ�����˽�ɫ
     */
    private JButton heroBtn = null;

    /**
     * ��������
     */
    private boolean keyUp;

    /**
     * ������ȥ
     */
    private boolean keyDown;

    /**
     * �����
     */
    private boolean keyLeft;

    /**
     * �ҷ����
     */
    private boolean keyRight;

    /**
     * ������ҵ����߲�������ֵԽ���ƶ��ٶ�Խ��
     */
    private int step = 3;

    /**
     * ������λ�ã�x,y��
     */
    private Point point;

    /**
     * ������λ��x
     */
    private double x = 0.0;

    /**
     * ������λ��y
     */
    private double y = 0.0;

    /**
     * �������ӵ��ĸ���
     */
    private int bulletNum = 70;

    /**
     * ��Ϸ�Ƿ����
     */
    private boolean gameAlive = true;

    /**
     * ��Ϸ������ʾ��ǩ����ʾʱ����Ϣ
     */
    private JLabel jLabel = null;

    /**
     * ���¿�ʼ��ť
     */
    private JButton restartBtn = null;

    /**
     * �����б�
     */
    private ArrayList<JButton> btnList = new ArrayList<>();

    /**
     * �����߳��б�
     */
    private ArrayList<Thread> threadList = new ArrayList<>();

    /**
     * ���ź�ɫͼ��
     */
    private URL aliveGifUrl = this.getClass().getResource("game_play_alive_red.gif");

    /**
     * ��սʧ��ʱ��ɫͼ��
     */
    private URL deadGifUrl = this.getClass().getResource("game_play_dead_blue.gif");

    public Game() {
        super();
        initialize();
    }


    /**
     * ���¿�ʼ��ť
     *
     * @return
     */
    private JButton getRestartBtn() {
        if (restartBtn == null) {
            restartBtn = new JButton();
            restartBtn.setBounds(new Rectangle(478, 361, 164, 51));
            restartBtn.setText("���¿�ʼ");
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
     * ��Ϸ����
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
     * ��ʼ��Ϸ,׼��70�����飬�㵯���˽�ɫ
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
     * ��ʼ�����߳�
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
     * ��ʼ�����ڽ���
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
        this.setTitle("�㵯��С��Ϸ��");
        this.setVisible(true);

        Image image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("fireCloud.jpg"));
        ImageIcon img = new ImageIcon(image);
        //Ҫ���õı���ͼƬ
        JLabel imgLabel = new JLabel();
        imgLabel.setIcon(img);
        //������ͼ���ڱ�ǩ�
        this.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
        imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        // ���ñ�����ǩ��λ��
        Container contain = this.getContentPane();
        ((JPanel) contain).setOpaque(false);
        this.setBackground(new Color(1, 1, 1));
    }

    /**
     * ��ʼ����Ϸ���
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
     * �㵯���˰�ť��ʼ��
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
                    // ���������
                    if (e.getKeyCode() == 37) {
                        keyLeft = true;
                    }
                    // �������ϼ�
                    if (e.getKeyCode() == 38) {
                        keyUp = true;
                    }
                    // �������Ҽ�
                    if (e.getKeyCode() == 39) {
                        keyRight = true;
                    }
                    // �������¼�
                    if (e.getKeyCode() == 40) {
                        keyDown = true;
                    }
                }
            });
        }
        return heroBtn;
    }

    /**
     * �㵯�����߳�
     */
    class HeroThread implements Runnable {
        public void run() {
            while (true) {
                while (gameAlive) {
                    point = heroBtn.getLocation();
                    // ����
                    if (keyUp) {
                        if (keyLeft) {
                            // ͬʱ�����
                            x = point.getX();
                            y = point.getY();
                            if (x > 0 && y > 0) {
                                heroBtn.setLocation((int) x - step, (int) y - step);
                            }
                        } else if (keyRight) {
                            // ͬʱ���Ҽ�
                            x = point.getX();
                            y = point.getY();
                            if (x + 40 < 700 && y > 0) {
                                heroBtn.setLocation((int) x + step, (int) y - step);
                            }
                        } else {
                            // ֻ���ϼ�
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
     * �ӵ��߳���
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
            // ����λ��x
            int zx = 0;
            // ����λ��y
            int zy = 0;

            // �����ӵ�������֮��Ĳ���
            int zzx = 0;
            int zzy = 0;
            while (true) {
                if (gameAlive) {
                    int direction = (int) (Math.random() * 4 + 1);
                    // �ĸ�if�漴���ĸ��߷����ӵ�
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
                    // ��ʼ���ӵ������˾Ͳ��ڼ���
                    if (first) {
                        jPanel.add(jb, null);
                        first = false;
                    }
                    jb.setBounds(new Rectangle(zx, zy, 10, 10));
                    // �����ӵ�������֮��Ĳ���
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
                                endtime = min + "��  " + sec + "��";
                            } else {
                                endtime = sec + "��";
                            }
                            jLabel.setText("                          GAME OVER!!! \n��ʱ��" + endtime);
                            break;
                        }
                        // ��������ֹͣѭ��
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
