package com.example.createtask;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

public class HelloController {

    @FXML
    AnchorPane viewPort;
    @FXML
    Button startButton;
    @FXML
    Rectangle barrel;

    @FXML
    Label label;

    double mousePosX;
    double mousePosY;
    double[] barrelPos = new double[2];

    double diffY;
    double diffX;

    byte barrelOffset = 6;

    double rotation = 90;


    LinkedList<Circle> bullets = new LinkedList<>();







public void printPos(Circle bullet){
    System.out.println(" Layout X: " + bullet.getTranslateX() + " Layout Y: " + bullet.getTranslateY());
}
public void removeNext(){
    viewPort.getChildren().remove(bullets.getFirst());
    bullets.removeFirst();


    }

    public void onClick() throws InterruptedException {


    bullets.addLast(new Bullet(15, barrelPos[0], barrelPos[1]));
    viewPort.getChildren().add(bullets.getLast());

        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.millis(20000));
        translateTransition.setInterpolator(Interpolator.LINEAR);
        translateTransition.setNode(bullets.getLast());



        double newRotation;
        System.out.println("Rotation" + (rotation + 90));
        if ((rotation+90) <= 0){
            newRotation = -(rotation +90);
        }else{
            newRotation = -rotation +90;
        }
        System.out.println(newRotation);
        System.out.println("Translate to X: " + (Math.sin(Math.toRadians(newRotation))) * 600);
        System.out.println("Translate to Y: " + Math.cos(Math.toRadians(newRotation)) * -600);

        translateTransition.setByX((Math.sin(Math.toRadians(newRotation))) * 600);
        System.out.println("Sin = " + (Math.sin(Math.toRadians(newRotation))));
        translateTransition.setByY(Math.cos(Math.toRadians(newRotation)) * -600);
        translateTransition.setOnFinished(e -> removeNext());
        translateTransition.setCycleCount(0);


        //Setting auto reverse value to false
        translateTransition.setAutoReverse(false);

        //Playing the animation
        translateTransition.play();



    }

    public void onStartClick(){
        startButton.setDisable(true);
        startButton.setVisible(false);

        viewPort.setOnMouseMoved(event -> {

            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();

/*
            if (mousePosX >= 258 && mousePosX <= 330){
                setBarrel();

            }

            */
            if (mousePosX >= 150 && mousePosX <= 450){
                setBarrel();

            }


            //Rotation based on mouse

            diffY = mousePosY - barrelPos[1];
            diffX = mousePosX - barrelPos[0];

            if (diffX != 0){
                rotation = Math.toDegrees(Math.atan(diffY / diffX)) - 90;
                if (rotation < 90 || (rotation > 270 && rotation < 360)) {
                    barrel.setRotate(rotation);
                }else{
                    barrel.setRotate(90);
                }
            }else{
                barrel.setRotate(0);
            }

            //barrel.setRotate((mousePosX - 300) / 3.33333);


        });




    }

    //430 - 450



    public void setBarrel(){

        //Position relative to circle
        barrelPos[0] = ((mousePosX-150) / 4.166666) + 258;
        barrelPos[1] = (Math.abs((((mousePosX-150) / 4.166666) + 258)-300) / 1.8) + 430;
        barrel.setLayoutX(((mousePosX-150) / 4.166666) + 258);
        barrel.setLayoutY(barrelPos[1]);






    }
}