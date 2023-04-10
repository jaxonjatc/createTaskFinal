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
    Label scoreLabel;
    @FXML
    Label waveLabel;

    int waveNumber = 1;


    int score = 0;

    double mousePosX;
    double mousePosY;
    double[] barrelPos = new double[2];

    double diffY;
    double diffX;

    byte barrelOffset = 6;

    double rotation = 90;

    Random random = new Random();

    int odds = 100;

    boolean gameOver = true;

    double deathTime = 0;


    LinkedList<Bullet> bullets = new LinkedList<>();
    LinkedList<Enemy> enemies = new LinkedList<>();

    public void printPos(Circle bullet){
        System.out.println(" Layout X: " + bullet.getTranslateX() + " Layout Y: " + bullet.getTranslateY());
    }
    public void removeNextBullet(Bullet bullet){
        if (bullets.size() > 0) {
            viewPort.getChildren().remove(bullet);
            bullets.remove(bullet);
        }
    }

    public void onClick() throws InterruptedException {
        if (!gameOver){
            shootBullet();
        }
    }



    public void onStartClick(){
        if (System.currentTimeMillis() - deathTime > 750) {

            System.out.println(enemies.toString());
            System.out.println(bullets.toString());

            score = 0;
            waveNumber = 1;

            removeHomeScreen();

            gameOver = false;

            viewPort.setOnMouseMoved(event -> {

                mousePosX = event.getSceneX();
                mousePosY = event.getSceneY();

                if (mousePosX >= 150 && mousePosX <= 450) {
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

                if (diffY > -25 && Math.abs(diffX) < 90) {
                    diffX = oldDiffX;
                    diffY = oldDiffY;
                }

                if (diffX != 0) {
                    rotation = Math.toDegrees(Math.atan(diffY / diffX)) + 90;

                    barrel.setRotate(rotation);

                }

            });
        }
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

                if(bullet.isTouchingCircle(enemy.getRealPositionX(), enemy.getRealPositionY(), enemy.getRadius()) && bullet.getIsAlive() && enemy.getIsAlive()){

                    enemy.setAlive(false);
                    viewPort.getChildren().remove(enemy);

                    bullet.setAlive(false);
                    viewPort.getChildren().remove(bullet);
                    score++;
                    scoreLabel.setText("Score - " + score);

                    System.out.println("bullet hit");

                    changeWave();

                }


            }

        }
    }

    public void tickTimer() {
        if (!gameOver) {
            bulletHit();
            if (random.nextInt(0, odds) == 0) {
                if (random.nextInt(8-waveNumber) == 0){
                    spawnEnemy(true);
                }else{
                    spawnEnemy(false);
                }
                odds = 100 - (waveNumber * 15);
            } else {
                odds--;
            }
            gameOver = isDead();
        }
    }

    public boolean isDead(){
        for (Enemy enemy :
                enemies) {
            if ((enemy.getRealPositionY() > 485 && enemy.getIsAlive())){


                destroyAll();
                endGameScreen();


                deathTime = System.currentTimeMillis();
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

    public void changeWave(){
        switch (score) {
            case 10 -> waveNumber = 2;
            case 20 -> waveNumber = 3;
            case 40 -> waveNumber = 4;
            case 60 -> waveNumber = 5;
            case 100 -> waveNumber = 6;
        }

        if (waveNumber == 6){
            waveLabel.setText("SURVIVE");
        }else {
            waveLabel.setText("Wave " + waveNumber);
        }
    }


    public void shootBullet(){

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

        Bullet bullet = new Bullet(15, barrelPos[0]+6, barrelPos[1]+31, Color.BLUE);

        bullets.addLast(bullet);
        viewPort.getChildren().add(bullet);

        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.millis(3500));
        translateTransition.setInterpolator(Interpolator.LINEAR);
        translateTransition.setNode(bullet);

        translateTransition.setByX(newX);
        translateTransition.setByY(newY);
        translateTransition.setOnFinished(e -> removeNextBullet(bullet));
        translateTransition.setCycleCount(0);

        //Setting auto reverse value to false
        translateTransition.setAutoReverse(false);

        //Playing the animation
        translateTransition.play();
    }

    public void spawnEnemy(boolean isHard){
//Sets default speed and color
        Color color = Color.RED;
        int speed = 3100;

        //Changes the default color and speed if the argument passed is true
        if (isHard){
            color = Color.PURPLE;
            speed = 1900;
        }

        //Sets the spawn position to a random point right above the screen
        double xSpawn = random.nextDouble(-50, 650);
        double ySpawn = random.nextDouble(-75, -25);


        //Creates an enemy object and adds it to the enemies list and the viewport
        Enemy enemy = new Enemy(15, xSpawn, ySpawn, color);

        enemies.addLast(enemy);
        viewPort.getChildren().add(enemy);

        //Creates an animation for the enemy and sets all the attributes of the animation

        TranslateTransition enemyTranslation = new TranslateTransition();
        enemyTranslation.setDuration(Duration.millis(speed));
        enemyTranslation.setInterpolator(Interpolator.LINEAR);
        enemyTranslation.setNode(enemy);

        enemyTranslation.setToX(300 - xSpawn);
        enemyTranslation.setToY(505 - ySpawn);
        enemyTranslation.setOnFinished(e -> removeNextEnemy(enemy));
        enemyTranslation.setCycleCount(0);

        //Plays the animation
        enemyTranslation.play();
    }

    public void removeNextEnemy(Enemy enemy){
        if (enemies.size() > 0) {
            viewPort.getChildren().remove(enemy);
            enemies.remove(enemy);
            System.out.println("Removed");
        }

    }


    public void removeHomeScreen(){
        scoreLabel.setText("Score - " + score);
        waveLabel.setText("Wave " + waveNumber);

        startButton.setDisable(true);
        startButton.setVisible(false);
    }

    public void endGameScreen(){
        startButton.setVisible(true);
        startButton.setDisable(false);
        startButton.setText("Game Over!\nTry again?");
    }

}