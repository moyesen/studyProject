package com.flower.mangame;

/**
 * @author mosen
 * @date 2020-04-04 16:17
 **/
public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
        game.reset();
        game.startBulletThread();
    }
}
