/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.shellshock;

import fr.insarcade.javafx.core.Arcade;
import static fr.insarcade.javafx.games.shellshock.Tank.TANK_HEIGHT1;
import static fr.insarcade.javafx.games.shellshock.Tank.TANK_HEIGHT2;
import static fr.insarcade.javafx.games.shellshock.Tank.TANK_LENGTH;
import static java.lang.Math.atan;
import static java.lang.Math.sqrt;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Matthieu
 */
public class Line {
    private Point2D infimum;
    private Point2D supremum;
    private double slope;
    private double yIntercept;
    private final double diffPerp1; //C'est l'écart qu'il y a entre l'abscisse d'un point connu et celle
                        // d'un point perpendiculaire à la ligne, dont leur distance est TANK_HEIGHT1
    private final double diffPerp2; // écart entre l'abscisse d'un point sur la ligne inférieure du rectangle contenant le canon et  d'un point sur la ligne au dessus
    private final double diffLine1; // C'est l'écart qu'il y a entre l'abscisse du point du milieu, qui est actualisé à chaque boucle et les point sur la même ligne que le point du milieu
    private final double diffLine2; // de meme pour que diffLine1, mais cette fois la distance entre deux points correspond à la longueur du rectangle supérieur
    private final double diffAngle; // angle entre l'axe des abscisses et la droite
    private final double diffYIntercept1; // donne l'écart entre la droite passant par les points inférieurs du tank et celle passant par les points supérieurs du tank
    private final double diffYIntercept2; // de meme mais pour la partie contenant le canon, ecart entre paraLine1 et paraLine2
    
    public Line(){
        infimum = new Point2D(0,0);
        supremum = new Point2D(1,1);
        slope = (supremum.getY() - infimum.getY())/(supremum.getX() - infimum.getX());
        yIntercept = infimum.getY() - (slope * infimum.getX());
        diffPerp1 = TANK_HEIGHT1 * sqrt(1 / ( 1 + ( 1 / ( slope * slope ) ) ) );
        diffPerp2 = diffPerp1 * (TANK_HEIGHT2/TANK_HEIGHT1);
        diffLine1 = (TANK_LENGTH / 2) * sqrt(1 / (1 + slope * slope));
        diffLine2 = (TANK_LENGTH / 3) * sqrt(1 / (1 + slope * slope));
        diffYIntercept1 = sqrt((TANK_HEIGHT1 * TANK_HEIGHT1) + (diffPerp1 * diffPerp1));
        diffYIntercept2 = sqrt(TANK_HEIGHT2 * TANK_HEIGHT2 + diffPerp2 * diffPerp2);
        diffAngle = atan(slope);

    }
    
    public Line(Point2D inf, Point2D sup){
        infimum = inf;
        supremum = sup;
        slope = (supremum.getY() - infimum.getY())/(supremum.getX() - infimum.getX());
        yIntercept = infimum.getY() - (slope * infimum.getX());
        diffPerp1 = TANK_HEIGHT1 * sqrt(1 / ( 1 + ( 1 / ( slope * slope ) ) ) );
        diffPerp2 = diffPerp1 * (TANK_HEIGHT2/TANK_HEIGHT1);
        diffLine1 = (TANK_LENGTH / 2) * sqrt(1 / (1 + slope * slope));
        diffLine2 = (TANK_LENGTH / 3) * sqrt(1 / (1 + slope * slope));
        diffYIntercept1 = sqrt((TANK_HEIGHT1 * TANK_HEIGHT1) + (diffPerp1 * diffPerp1));
        diffYIntercept2 = sqrt(TANK_HEIGHT2 * TANK_HEIGHT2 + diffPerp2 * diffPerp2);
        diffAngle = atan(slope);
        
    }
    
    public Point2D getInfimum(){
        return infimum;
    }
    
    public Point2D getSupremum(){
        return supremum;
    }
    
    public double getSlope(){
        return slope;
    }
    
    public double getYIntercept(){
        return yIntercept;
    }
    
    public double getDiffPerp1(){
        return diffPerp1;
    }
    
    public double getDiffLine1(){
        return diffLine1;
    }
    public double getDiffPerp2(){
        return diffPerp2;
    }
    
    public double getDiffLine2(){
        return diffLine2;
    }
    
    public double getDiffYIntercept1(){
        return diffYIntercept1;
    }
    
    public double getDiffYIntercept2(){
        return diffYIntercept2;
    }
    
    public double getDiffAngle(){
        return diffAngle;
    }
    
    
    public double computePoint(double x){
        double y = x * slope + yIntercept;
        return y;
    }
    
    public void setInfimum(double xInf, double yInf){
        infimum.setLocation(xInf, yInf);
        slope = (supremum.getY() - infimum.getY())/(supremum.getX() - infimum.getX());
        yIntercept = infimum.getY() - (slope * infimum.getX());
    }
    
    public void setSupremum(double xSup, double ySup){
        supremum.setLocation(xSup, ySup);
        slope = (supremum.getY() - infimum.getY())/(supremum.getX() - infimum.getX());
        yIntercept = infimum.getY() - (slope * infimum.getX());
    }
    
    public void setLine(Point2D inf, Point2D sup){
        infimum = inf;
        supremum = sup;
        slope = (supremum.getY() - infimum.getY())/(supremum.getX() - infimum.getX());
        yIntercept = infimum.getY() - (slope * infimum.getX());
        
    }

  
    
    public Line translateLine(double h){ //permet d'obtenir une Line dont l'ordonnée à l'origine est décalée de h
        Line tempLine = new Line(this.getInfimum(), this.getSupremum());
        tempLine.yIntercept -= h;
        return tempLine;
    }
    
    public void drawLine(GraphicsContext drawContext){
        double[] xPoints = new double[4];
        double[] yPoints = new double[4];
        xPoints[0] = infimum.getX();
        yPoints[0] = infimum.getY();
        xPoints[1] = supremum.getX();
        yPoints[1] = supremum.getY();
        xPoints[2] = supremum.getX();
        yPoints[2] = Arcade.getWindowHeight();
        xPoints[3] = infimum.getX();
        yPoints[3] = Arcade.getWindowHeight();
        
        drawContext.fillPolygon(xPoints, yPoints, 4);
        
    }
    
}
