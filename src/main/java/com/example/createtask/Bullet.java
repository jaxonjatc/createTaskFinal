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




    public Bullet(double v, double startingX, double startingY) {
        super(v);
        radius = v;
        this.startingX = startingX;
        this.startingY = startingY;

        setLayoutX(startingX);
        setLayoutY(startingY);

        setFill(Color.BLACK);
    }


    public double getRealPositionX(){
        return startingX + getTranslateX();
    }

    public double getRealPositionY(){
        return startingY + getTranslateY();
    }


    public boolean isTouching(double top, double left, double right, double bottom){



        return (getRealPositionX() -radius >= left && getRealPositionX()+radius <= right) && (getRealPositionY()-radius >= top && getRealPositionY()+radius <= bottom);

    }


}
