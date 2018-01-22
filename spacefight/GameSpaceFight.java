/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.spacefight;

import fr.insarcade.controller.ControllerData;
import fr.insarcade.javafx.core.Arcade;
import fr.insarcade.javafx.core.ArcadeGame;
import java.math.BigDecimal;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 *
 * @author Matthieu Bellucci
 */
public class GameSpaceFight extends ArcadeGame {
    
    private static final int MAX_LASER = 1000; //nombre de lasers max par vaisseau, à diminuer pour augmenter les fps si beaucoup de tirs
    private static final double RELOAD_TIME = 1;
    private static final int RELOAD_LENGTH = 20; //diametre du cercle sur le vaisseau
    private static final int SHIP_SHOOTING_POINT14 = 95; //the coordinate of the shooting point on the image for states 1 and 4
    private static final int SHIP_SHOOTING_POINT23 = 24; // the coordinate of the shooting point on the image for states 2 and 3
    private static final int SHIP_SPEED = 400; 
    private static final double INVINCIBILITY_TIME = 1.5; //temps d'invincibilité après qu'un vaisseau ait été touché
    private static final BigDecimal BLINKING_TIME = new BigDecimal("0.1"); //en BigDecimal pour des calculs exacts, intervalle entre 2 clignotements lorsque le vaisseau est touché
    private static final int LASER_DAMAGE = 10; 
    private static final Image[] IMAGE_ARRAY = new Image[8];
    private static final Boolean debug = false; //pour afficher les variables

    private Spaceship Player1;
    private Spaceship Player2;
    private int shootCountP1;
    private int shootCountP2;
    private LaserBeam [] player1Laser;
    private LaserBeam [] player2Laser;
    private double antiflood1;
    private double antiflood2;
    private double timer1;
    private double timer2;
     
    
    
    
    
    public GameSpaceFight(){
        super("SpaceFight", "Matthieu Bellucci");
        for (int i = 0; i <= 3; i++){
            IMAGE_ARRAY[i] = new Image("/res/GameSpaceFight/redShip" + (i+1) + ".png");
        }
        for (int i = 4; i <= 7; i++){
            IMAGE_ARRAY[i] = new Image("/res/GameSpaceFight/blueShip" + (i-3) + ".png");
        }
}
    
    @Override
    public void resetGame(){
        Player1 = new Spaceship();
        Player1.shipx = 100;
        Player1.shipy = Arcade.getWindowHeight()/2-50;
        Player1.setShipState(1);
        Player1.setHealth(100);
        player1Laser = new LaserBeam[MAX_LASER];
        
        Player2 = new Spaceship();
        Player2.shipx = Arcade.getWindowWidth()-220;
        Player2.shipy = Arcade.getWindowHeight()/2-50;
        Player2.setShipState(3);
        Player2.setHealth(100);
        player2Laser = new LaserBeam[MAX_LASER];
        timer1 = 0;
        timer2 = 0;
        antiflood1 = RELOAD_TIME;
        antiflood2 = RELOAD_TIME;
        shootCountP1 = 0;
        shootCountP2 = 0;
    }
    

    @Override
    public void updateGame(double interpolation, ControllerData controller) {
        
        switch(getState()) {
//            case 0:
//                Player1.shipx = Arcade.getWindowWidth()/4;
//                Player1.shipy = Arcade.getWindowHeight()/2;
//                Player1.setShipState(1);
//                Player1.setHealth(100);
//                
//                Player2.shipx = Arcade.getWindowWidth()*0.75;
//                Player2.shipy = Arcade.getWindowHeight()/2;
//                Player2.setShipState(3);
//                Player2.setHealth(100);
//                timer1 = 0;
//                timer2 = 0;
//                shootCountP1 = 0;
//                shootCountP2 = 0;
//                
//                setState(1);
//                
//                break;
            case 1:
                antiflood1 += interpolation;
                antiflood2 += interpolation;
                timer1 += interpolation;
                timer2 += interpolation;
                
                movePlayer1(interpolation, controller);
                movePlayer2(interpolation, controller);
                
                if (shootCountP1 > 0){
                    for (int i = 0; i <= shootCountP1 - 1; i++){
                        player1Laser[i].moveLaserBeam(interpolation);
                    }
                }
                
                if (shootCountP2 > 0){
                    for (int i = 0; i <= shootCountP2 - 1; i++){
                        player2Laser[i].moveLaserBeam(interpolation);
                    }
                }
               
                
                if ((controller.isJoystick1_ACTION()) && (antiflood1 > RELOAD_TIME)){
                    switch(Player1.getShipState()){ //1 cas par direction possible
                        case 1 :
                            
                            player1Laser[shootCountP1] = new LaserBeam(Player1.getShipx() + SHIP_SHOOTING_POINT14, Player1.getShipy() + Player1.getShipHeight()/2, Player1.getShipState());
                            break;
                        case 2 :
                        
                            player1Laser[shootCountP1] = new LaserBeam(Player1.getShipx() + Player1.getShipWidth()/2, Player1.getShipy() + SHIP_SHOOTING_POINT23, Player1.getShipState());
                            break;
                        
                        case 3 :
                            player1Laser[shootCountP1] = new LaserBeam(Player1.getShipx() + SHIP_SHOOTING_POINT23, Player1.getShipy() + Player1.getShipHeight()/2, Player1.getShipState());
                            break;
                        
                        case 4 :
                            player1Laser[shootCountP1] = new LaserBeam(Player1.getShipx() + Player1.getShipWidth()/2, Player1.getShipy() + SHIP_SHOOTING_POINT14, Player1.getShipState());
                            break;
                            
                        default :
                            break;
                            
                    }
                    if (shootCountP1 < MAX_LASER - 1){
                        shootCountP1 ++;    
                    }
                    else{
                        shootCountP1 = 0;
                    }
                    
                    antiflood1 = 0;
                    } 
                
                if ((controller.isJoystick2_ACTION()) && (antiflood2 > RELOAD_TIME)){
                    switch(Player2.getShipState()){
                        case 1 :
                            
                            player2Laser[shootCountP2] = new LaserBeam(Player2.getShipx() + Player2.getShipWidth(), Player2.getShipy() + Player2.getShipHeight()/2, Player2.getShipState());
                            break;
                        case 2 :
                        
                            player2Laser[shootCountP2] = new LaserBeam(Player2.getShipx() + Player2.getShipWidth()/2, Player2.getShipy(), Player2.getShipState());
                            break;
                        
                        case 3 :
                            player2Laser[shootCountP2] = new LaserBeam(Player2.getShipx(), Player2.getShipy() + Player2.getShipHeight()/2, Player2.getShipState());
                            break;
                        
                        case 4 :
                            player2Laser[shootCountP2] = new LaserBeam(Player2.getShipx() + Player2.getShipWidth()/2, Player2.getShipy() + Player2.getShipHeight(), Player2.getShipState());
                            break;
                            
                        default :
                            break;
                            
                    }
                    if (shootCountP2 < MAX_LASER - 1){
                        shootCountP2 ++;    
                    }
                    else{
                        shootCountP2 = 0;
                    }
                    
                    antiflood2 = 0;
                    } 
                
                //===========================================================
                // Gestion des collisions laser/vaisseau
                
                int j = 0;
                while((timer1 > INVINCIBILITY_TIME) && (j <= shootCountP2 - 1) && (shootCountP2 > 0)){
                    
                    if (player2Laser[j].getLaserBox().intersects(Player1.getShipBox())){
                        Player1.setHealth(Player1.getShipHealth() - LASER_DAMAGE);
                        timer1 = 0;
                    }
                        j ++;
                        
                                        
            }
                j = 0;
                while((timer2 > INVINCIBILITY_TIME) && (j <= shootCountP1 - 1) && (shootCountP1 > 0)){
                    if (player1Laser[j].getLaserBox().intersects(Player2.getShipBox())){
                        Player2.setHealth(Player2.getShipHealth() - LASER_DAMAGE);
                        timer2 = 0;                                                
                    }
                    j ++;
            }
            //==================================================================================
            
                if ((Player1.getShipHealth() <=0 ) && (Player2.getShipHealth() > 0)){
                    gameIsFinish(winner.Player2, false);
                }
                if ((Player1.getShipHealth() > 0 ) && (Player2.getShipHealth() <= 0)){
                    gameIsFinish(winner.Player1, false);
                }
                
                break; 
            
        }
    }

    @Override
    public void renderGame(GraphicsContext drawContext) {
        String debug1 = "Nb de tirs J1 : " + shootCountP1;
        String debug2 = "Nb de tirs J2 : " + shootCountP2;
        String debug3 = "timer 1 : " + timer1;
        String Hull1 = "Player 1 HP = " + Player1.getShipHealth();
        String Hull2 = "Player 2 HP = " + Player2.getShipHealth();
        
        
        switch(getState()){
            case 1:
                
                drawContext.setFill(Color.BLACK);
                drawContext.fillRect(0, 0, Arcade.getWindowWidth(), Arcade.getWindowHeight());
                drawContext.setFill(Color.GREEN);
                if (debug) {
                    drawContext.fillText(debug1, 900, 10); //nb de lasers dans tableau du joueur 1
                    drawContext.fillText(debug2, 900, 20); //nb de lasers dans tableau du joueur 2
                    drawContext.fillText(debug3, 500, 30); // timer d'invincibilité du joueur 1
                }
                drawContext.setFont(Arcade.FONT_20());
                drawContext.fillText(Hull1, 20, Arcade.getWindowHeight() - 40);
                drawContext.fillText(Hull2, Arcade.getWindowWidth() - 180, Arcade.getWindowHeight() - 40);
                drawContext.fillRect(20, Arcade.getWindowHeight() - 28, Player1.getShipHealth(), 16);
                drawContext.fillRect(Arcade.getWindowWidth() - 108, Arcade.getWindowHeight() - 28, Player2.getShipHealth(), 16);
                
                //===================================================
                //          Clignotement des vaisseaux 
                if (timer1 > INVINCIBILITY_TIME){        
                drawPlayer(Player1, drawContext, shootCountP1, antiflood1, player1Laser, "red",  Color.DARKRED);
                }
                else{
                    BigDecimal i = new BigDecimal("0"); //utilisation des BigDecimal pour avoir les valeurs exactes
                    while (timer1 >= i.doubleValue()){
                        i = i.add(BLINKING_TIME);
                        
                    }
                    if (debug){
                        String debug4 = "i = " + i;
                        drawContext.fillText(debug4, 500, 40);
                    }
                    
                    if (isEven(i.divide(BLINKING_TIME).intValueExact())){
                        drawPlayer(Player1, drawContext, shootCountP1, antiflood1, player1Laser, "red",  Color.DARKRED);
                    }
                      
                }
                if (timer2 > INVINCIBILITY_TIME){        
                drawPlayer(Player2, drawContext, shootCountP2, antiflood2, player2Laser, "blue", Color.CHARTREUSE);
                }
                else{
                    BigDecimal j = new BigDecimal("0");
                    while (timer2 >= j.doubleValue()){
                        j = j.add(BLINKING_TIME);
                    }
                    if (isEven(j.divide(BLINKING_TIME).intValueExact())){
                        drawPlayer(Player2, drawContext, shootCountP2, antiflood2, player2Laser, "blue", Color.CHARTREUSE);
                    }
                      
                }
                
                //============================================================
                
                break;
        }
    }
    
    private void drawPlayer(Spaceship Player, GraphicsContext drawContext, int shootCount, double antiflood, LaserBeam[] playerLaser, String shipColor,  Paint laserColor){
        Player.rotateShip();
        drawContext.setFill(Color.BLACK);
        if (shipColor == "blue"){
            drawContext.drawImage(IMAGE_ARRAY[Player.getShipState() + 3], Player.getShipx(), Player.getShipy());            
        }
        if (shipColor == "red"){
            drawContext.drawImage(IMAGE_ARRAY[Player.getShipState() - 1], Player.getShipx(), Player.getShipy());
        }
        //Affichage du carré de rechargement
        drawContext.fillRect(Player.getShipx() + Player.getShipWidth()/2 - RELOAD_LENGTH/2 - 2, Player.getShipy() + Player.getShipHeight()/2 - RELOAD_LENGTH/2 - 2, RELOAD_LENGTH + 4 , RELOAD_LENGTH + 4);
        drawContext.setFill(laserColor);
        if (shootCount > 0){
                    for (int i = 0; i <= shootCount - 1; i++){
                        switch(playerLaser[i].getLaserDirection()){
                            case 1 :
                            case 3 :
                                drawContext.fillRect(playerLaser[i].getLaserx(), playerLaser[i].getLasery(), playerLaser[i].getLaserWidth(), playerLaser[i].getLaserHeight());
                                break;
                            case 2 :
                            case 4 :
                                drawContext.fillRect(playerLaser[i].getLaserx(), playerLaser[i].getLasery(), playerLaser[i].getLaserHeight(), playerLaser[i].getLaserWidth());
                                break;
                            default : 
                                break;
                        }
                    }
                }
        if (antiflood < RELOAD_TIME){
                    drawContext.fillRect(Player.getShipx() + Player.getShipWidth()/2 - RELOAD_LENGTH/2 , Player.getShipy() + Player.getShipHeight()/2 - RELOAD_LENGTH/2, RELOAD_LENGTH, (antiflood/RELOAD_TIME) * RELOAD_LENGTH);
                }
                if (antiflood >= RELOAD_TIME){
                    drawContext.fillRect(Player.getShipx() + Player.getShipWidth()/2 - RELOAD_LENGTH/2 , Player.getShipy() + Player.getShipHeight()/2 - RELOAD_LENGTH/2, RELOAD_LENGTH, RELOAD_LENGTH);
                }            
    }
    private void movePlayer1(double interpolation, ControllerData controller){
        
        if (controller.isJoystick1_LEFT()){
                    Player1.shipx -= interpolation*SHIP_SPEED;
                    Player1.setShipState(3);
                    if (Player1.shipx <= 0){ Player1.shipx += interpolation*SHIP_SPEED;}
                }
                if (controller.isJoystick1_RIGHT()){
                    Player1.shipx += interpolation*SHIP_SPEED;
                    Player1.setShipState(1);
                    if ((Player1.getShipx() + Player1.getShipWidth())>= Arcade.getWindowWidth()){ Player1.shipx-= interpolation*SHIP_SPEED;}
                }
                if (controller.isJoystick1_UP()){
                    Player1.shipy -= interpolation*SHIP_SPEED;
                    Player1.setShipState(2);
                    if (Player1.shipy <= 0){ Player1.shipy+= interpolation*SHIP_SPEED;}
                }
                if (controller.isJoystick1_DOWN()){
                    Player1.shipy += interpolation*SHIP_SPEED;
                    Player1.setShipState(4);
                    if ((Player1.shipy + Player1.getShipHeight())>= Arcade.getWindowHeight()){ Player1.shipy-= interpolation*SHIP_SPEED;}
                }
        
    }
    private void movePlayer2(double interpolation, ControllerData controller){
        
        if (controller.isJoystick2_LEFT()){
                    Player2.shipx -= interpolation*SHIP_SPEED;
                    Player2.setShipState(3);
                    if (Player2.shipx <= 0){ Player2.shipx += interpolation*SHIP_SPEED;}
                }
                if (controller.isJoystick2_RIGHT()){
                    Player2.shipx += interpolation*SHIP_SPEED;
                    Player2.setShipState(1);
                    if ((Player2.getShipx() + Player2.getShipWidth())>= Arcade.getWindowWidth()){ Player2.shipx-= interpolation*SHIP_SPEED;}
                }
                if (controller.isJoystick2_UP()){
                    Player2.shipy -= interpolation*SHIP_SPEED;
                    Player2.setShipState(2);
                    if (Player2.shipy <= 0){ Player2.shipy+= interpolation*SHIP_SPEED;}
                }
                if (controller.isJoystick2_DOWN()){
                    Player2.shipy += interpolation*SHIP_SPEED;
                    Player2.setShipState(4);
                    if ((Player2.shipy + Player2.getShipHeight())>= Arcade.getWindowHeight()){ Player2.shipy-= interpolation*SHIP_SPEED;}
                }
        
    }
    
    private boolean isEven(double i){
        return (i%2 == 0);
    }
    
        
    
    
//    private boolean detectCollision(LaserBeam laser, Spaceship player){
//        boolean collision;
//        boolean doXcollide;
//        boolean doYcollide;
//        double playerX = player.getShipx();
//        double playerY = player.getShipy();
//        int playerH = player.getShipHeight();
//        int playerW = player.getShipWidth();
//        double laserX = laser.getLaserx();
//        double laserY = laser.getLasery();
//
//        
//        switch(laser.getLaserDirection()){
//            case 1 : 
//                doXcollide = (laserX + LASER_WIDTH >= playerX) && (laserX + LASER_WIDTH <= playerX + playerW);
//                doYcollide = (((laserY >= playerY) && (laserY <=  playerY + playerH)) || (((laserY + LASER_HEIGHT >= playerY) && (laserY + LASER_HEIGHT <= playerY + playerH))));              
//                break;
//            
//            case 2 : 
//                doXcollide = (((laserX >= playerX) && (laserX <= playerX + playerW)) || ((laserX + LASER_HEIGHT >= playerX) && (laserX + LASER_HEIGHT <= playerX + playerW)));
//                doYcollide = ((laserY >= playerY) && (laserY <=  playerY + playerH));                
//                break;
//                
//            case 3 :
//                doXcollide = ((laserX >= playerX) && (laserX <= (playerX + playerW)));
//                doYcollide = (((laserY >= playerY) && (laserY <=  playerY + playerH)) || (((laserY + LASER_HEIGHT >= playerY) && (laserY + LASER_HEIGHT <= playerY + playerH))));               
//                break;
//                
//            case 4 : 
//                doXcollide = (((laserX >= playerX) && (laserX <= playerX + playerW)) || ((laserX + LASER_HEIGHT >= playerX) && (laserX + LASER_HEIGHT <= playerX + playerW)));
//                doYcollide = ((laserY + LASER_WIDTH >= playerY) && (laserY + LASER_WIDTH <=  playerY + playerH)); 
//                break;
//                
//            default : 
//                doXcollide = false;
//                doYcollide = false;
//                break;
//        }
//        
//        collision = ((doXcollide) && (doYcollide));
//        return collision;
//        
//    }
}

