package com.example.createtask;

import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.Vector;

public class Bullet extends Circle {
    private double startingX;
    private double startingY;

    private double radius;
    private boolean isAlive;





    public Bullet(double v, double startingX, double startingY, Color color) {
        super(v);
        radius = v;
        this.startingX = startingX;
        this.startingY = startingY;

        setLayoutX(startingX);
        setLayoutY(startingY);

        setFill(color);
        isAlive = true;
    }


    public double getRealPositionX(){
        return startingX + getTranslateX();
    }

    public double getRealPositionY(){
        return startingY + getTranslateY();
    }


    public boolean isTouchingSquare(double top, double left, double right, double bottom){



        return (getRealPositionX() -radius >= left && getRealPositionX()+radius <= right) && (getRealPositionY()-radius >= top && getRealPositionY()+radius <= bottom);

    }

    public boolean isTouchingCircle(double x, double y, double radius){

        return Math.abs(getRealPositionX() - x) < radius && Math.abs(getRealPositionY() - y) < 2.05*radius;

    }

    public boolean getIsAlive() {
        return isAlive;
    }


    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
