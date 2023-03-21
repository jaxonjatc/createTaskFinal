package com.example.createtask;

import javafx.scene.paint.Color;

public class Enemy extends Bullet{
    private boolean isAlive;

    public Enemy(double v, double startingX, double startingY, Color color) {
        super(v, startingX, startingY, color);
        isAlive = true;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
