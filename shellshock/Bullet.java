/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.shellshock;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author matt
 */
public class Bullet {
    private final static double SIZE = 5;
    private final static double GRAVITY = 20;
    private double initSpeedx;
    private double initSpeedy;
    private double x;
    private double y;
    private Point2D initPos;
    
    public Bullet(){
        initSpeedx = 0;
        initSpeedy = 0;
        initPos = new Point2D();
    }
    
    public Bullet(Point2D initPos, double speedx, double speedy){
        initSpeedx = speedx;
        initSpeedy = speedy;
        this.initPos = initPos;
        x = initPos.getX();
        y = initPos.getY();
    }
    
    public Point2D getBulletPos(){
        Point2D pos = new Point2D(x, y);
        return pos;
    }
    
    
    public double getBulletSpeedX(){
        return initSpeedx;
    }
    public double getBulletSpeedY(){
        return initSpeedy;
    }
    public double getGravity(){
        return GRAVITY;
    }
    public void setBulletSpeed(double speedx, double speedy){
        initSpeedx = speedx;
        initSpeedy = speedy;
    }
    public void setBulletPos(Point2D pos){
        x = pos.getX();
        y = pos.getY();
        initPos = pos;
    }
    
    
    public void moveBullet(double timer){  
        x = initSpeedx * timer + initPos.getX();
        y = initPos.getY() - initSpeedy * timer + (GRAVITY * (timer * timer)) / 2;

    }
    
    public Circle getBulletShape(){
        Circle bulletShape = new Circle();
        bulletShape.setCenterX(x);
        bulletShape.setCenterY(y);
        bulletShape.setRadius(SIZE);
        return bulletShape;
        
    }
    
    public void drawBullet(GraphicsContext drawContext, Color color){
        drawContext.setFill(color);
        drawContext.fillRect(x-SIZE, y+SIZE, SIZE, SIZE);
    }
            
    
}
