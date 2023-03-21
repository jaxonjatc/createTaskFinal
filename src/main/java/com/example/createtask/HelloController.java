package com.example.createtask;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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



    /*

    Things to still implement

    Easy
    -Make methods take inputs rather than use global variables
    -Clean up code
    -Waves of enemies of increasing difficulty (quantity and speed)
    -Score counter
    -possible Better collision detection
    (Up the tick speed and just multiply the odds of spawning
    (create variable for default odds))


    Medium
    -Weapon selection
    Make the bullet spawner take rotation as a parameter
    give it that parameter in the click method
    change the click method to do it based on which button was clicked before each new game
    -Add background art and make enemies/ammo/character more interesting looking

    Hard
    -Make it work on any screen size


     */

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

    Random random = new Random();

    int odds = 8;

    boolean gameOver = true;


    LinkedList<Bullet> bullets = new LinkedList<>();
    LinkedList<Enemy> enemies = new LinkedList<>();

    public void printPos(Circle bullet){
        System.out.println(" Layout X: " + bullet.getTranslateX() + " Layout Y: " + bullet.getTranslateY());
    }
    public void removeNext(){
        if (bullets.size() > 0) {
            viewPort.getChildren().remove(bullets.getFirst());
            bullets.removeFirst();
        }


        }

    public void onClick() throws InterruptedException {
        double newX = diffX;
        double newY = diffY;
//        System.out.println("Y: " + diffY);
//        System.out.println("X: " + diffX);
//        System.out.println(rotation);

        boolean done = false;

        while(!done){
            newX = newX*1.5;
            newY = newY*1.5;

            if (Math.sqrt(Math.pow(newX, 2) + Math.pow(newY, 2)) > 750){
                done = true;
            }
        }

    bullets.addLast(new Bullet(15, barrelPos[0]+6, barrelPos[1]+31, Color.BLUE));
    viewPort.getChildren().add(bullets.getLast());

        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.millis(3500));
        translateTransition.setInterpolator(Interpolator.LINEAR);
        translateTransition.setNode(bullets.getLast());






        translateTransition.setByX(newX);
        translateTransition.setByY(newY);
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

        gameOver = false;





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
            double oldDiffX = diffX;
            double oldDiffY = diffY;

            diffY = mousePosY - barrelPos[1];
            diffX = mousePosX - barrelPos[0];

            if (diffY > 30) {
                diffY = 30;
            }

            if (diffY > -25 && Math.abs(diffX) < 90){
                diffX = oldDiffX;
                diffY = oldDiffY;
            }


            if (diffX != 0){
                rotation = Math.toDegrees(Math.atan(diffY / diffX)) + 90;

                barrel.setRotate(rotation);

            }




        });




    }
    public void removeNextEnemy(){
        if (enemies.size() > 0) {
            viewPort.getChildren().remove(enemies.getFirst());
            enemies.removeFirst();
        }

    }
    public void spawnEnemy(){

    double xSpawn = random.nextDouble(50, 550);
    double ySpawn = random.nextDouble(25, 100);

        enemies.addLast(new Enemy(15, xSpawn, ySpawn, Color.RED));
        viewPort.getChildren().add(enemies.getLast());

        TranslateTransition enemyTranslation = new TranslateTransition();
        enemyTranslation.setDuration(Duration.millis(3500));
        enemyTranslation.setInterpolator(Interpolator.LINEAR);
        enemyTranslation.setNode(enemies.getLast());








        enemyTranslation.setToX(300 - xSpawn);
        enemyTranslation.setToY(505 - ySpawn);
        enemyTranslation.setOnFinished(e -> removeNextEnemy());
        enemyTranslation.setCycleCount(0);


        //Setting auto reverse value to false
        enemyTranslation.setAutoReverse(false);

        //Playing the animation
        enemyTranslation.play();
    }

    public void setBarrel(){

        //Position relative to circle
        barrelPos[0] = ((mousePosX-150) / 4.166666) + 258;
        barrelPos[1] = (Math.abs((((mousePosX-150) / 4.166666) + 258)-300) / 1.8) + 430;
        barrel.setLayoutX(((mousePosX-150) / 4.166666) + 258);
        barrel.setLayoutY(barrelPos[1]);






    }

    public void bulletHit(){
        for (Bullet bullet:
             bullets) {
            for (Enemy enemy :
                    enemies) {

                if(bullet.isTouchingCircle(enemy.getRealPositionX(), enemy.getRealPositionY(), enemy.getRadius())){
                    enemy.setAlive(false);
                    viewPort.getChildren().remove(enemy);
                }


            }

        }
    }

    public void tickTimer() {
        if (!gameOver) {
            bulletHit();
            if (random.nextInt(0, odds) == 0) {
                spawnEnemy();
                odds = 8;
            } else {
                odds--;
            }
            gameOver = isDead();
        }
    }

    public boolean isDead(){
        for (Enemy enemy :
                enemies) {
            if (enemy.getRealPositionY() > 485 && enemy.getIsAlive()){
                destroyAll();
                startButton.setVisible(true);
                startButton.setDisable(false);
                startButton.setText("Game Over!\nTry again?");
                return true;
            }
        }
        return false;
    }

    public void destroyAll(){
        for (Bullet bullet :
                bullets) {
            viewPort.getChildren().remove(bullet);
        }
        for (Enemy enemy :
                enemies) {
            viewPort.getChildren().remove(enemy);
        }
        viewPort.setOnMouseMoved(null);

    enemies.clear();
    bullets.clear();
    }

}