/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.spacefight;

import fr.insarcade.javafx.core.Arcade;
import javafx.geometry.BoundingBox;

/**
 *
 * @author Matthieu Bellucci
 */
public class Spaceship {
    private static final int DEFAULT_HEIGHT = 150;
    private static final int DEFAULT_WIDTH = 120;
    private int shipHeight;
    private int shipWidth;
    private String shipColor;
    protected double shipx,shipy; //Ship's coordinates
    private int shipState; // 1 : points  right 
                           // 2 : points  up 
                           // 3 : points left
                           // 4 : points down
    private int healthPoints;
    
    public Spaceship(){
         shipHeight = DEFAULT_HEIGHT;
         shipWidth = DEFAULT_WIDTH;
         shipColor = "BLACK";
         shipx = Arcade.getWindowWidth()/4;
         shipy = Arcade.getWindowHeight()/2;
         shipState = 1;
         healthPoints = 100;
         
         
    }
    public Spaceship(int height, int width, String color, double xinit, double yinit, int initState, int initHP){
      shipHeight = height;
      shipWidth = width;
      shipColor = color;
      shipx = xinit;
      shipy = yinit;
      shipState = initState;
      healthPoints = initHP;
    }
    public int getShipHeight(){
        return shipHeight;
    }
    
   public int getShipWidth(){
        return shipWidth;
    }
    
   public String getShipColor(){
       return shipColor;
   }
   public double getShipx(){
       return shipx;
   }
   public double getShipy(){
       return shipy;
   }
   
   public int getShipState(){
       return shipState;
   }
   
   
   public int getShipHealth(){
       return healthPoints;
   }
   
   public BoundingBox getShipBox(){
       return new BoundingBox(shipx, shipy, shipWidth, shipHeight);
   }
   
   public void setShipState(int newState){
       shipState = newState;
   }
   
   public void setHealth(int newHealth){
       healthPoints = newHealth;
   }
   
   protected void rotateShip(){
       if ((shipState == 2) || (shipState == 4)){
           
           shipHeight = DEFAULT_WIDTH;
           shipWidth = DEFAULT_HEIGHT;  
       }
       else{
           shipHeight = DEFAULT_HEIGHT;
           shipWidth = DEFAULT_WIDTH;
       }
       
       
   }
   
}
