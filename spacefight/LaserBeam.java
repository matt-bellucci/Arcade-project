/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.spacefight;

import javafx.geometry.BoundingBox;

/**
 *
 * @author Matthieu Bellucci
 */
public class LaserBeam {
    
    private static final double SPEED = 500;
    private static final int LASER_WIDTH = 25;
    private static final int LASER_HEIGHT = 5;
    private static final int LASER_INIT_X = 100;
    private static final int LASER_INIT_Y = 100;
    private double speed, laserx, lasery;
    
    private int laserDirection;
    
    public LaserBeam(){
        speed = SPEED;
        laserDirection = 1;
        laserx = LASER_INIT_X;
        lasery = LASER_INIT_Y;
    }
    public LaserBeam(double x, double y, int direction ){
 
        speed = SPEED;
        laserDirection = direction;
        laserx = x;
        lasery = y;
        
    }
    
    public double getSpeed(){
        return speed;
    }

    public double getLaserx(){
        return laserx;
    }
    
    public double getLasery(){
        return lasery;
    }
    
    public int getLaserDirection(){
        return laserDirection;
    }
    
    public double getLaserWidth(){
        return LASER_WIDTH;
    }
    
    public double getLaserHeight(){
        return LASER_HEIGHT;
    }
    
    public BoundingBox getLaserBox(){
        return new BoundingBox(laserx, lasery, LASER_WIDTH, LASER_HEIGHT);
    }
    
    public void moveLaserBeam(double interpolation){
        switch(laserDirection){
            case 1 :
                
                       
                laserx += speed * interpolation; 
                break;
                     
            case 2 :
                lasery -= speed * interpolation;
                break;
                        
            case 3 :
                laserx -= speed * interpolation;
                break;
                        
            case 4 :
                lasery += speed * interpolation;
                break;
                        
            default :
                break;
            
        }
    }
    
    
}
    
    
