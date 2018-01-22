package fr.insarcade.javafx.games.shellshock;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import static javafx.scene.shape.Shape.union;

/**
 *
 * @author Matthieu
 */
public class Tank {
    private static final double PI = 3.14;
    public static final double TANK_HEIGHT1 = 40;
    public static final double TANK_HEIGHT2 = 15;
    public static final double TANK_LENGTH = 100;
    private static final double TANK_SPEED = 100;
    private static final double CANNON_LENGTH = 50;
    private static final double ROTATION_SPEED = 0.2;
    private static final double HEALTH_WIDTH = 75;
    private static final double HEALTH_HEIGHT = 10;
    private static final double MAX_HEALTH = 100;
    private static final double MAX_FUEL = 100;
    private static final double FUEL_CONSUMPTION = 0.5;
    private static final double FUEL_WIDTH = 75;
    private static final double FUEL_HEIGHT = 5; 
    private static final double MAX_BULLET_SPEED = 250;
    private static final double BULLET_SPEED_HEIGHT = 8;
    private final Point2D middle;
    private Line currLine;
    private int lineNumber;
    private final double [] tankPointsX1;
    private final double [] tankPointsY1;
    private final double [] tankPointsX2;
    private final double [] tankPointsY2;
    private Point2D cannonCenter;
    private Point2D cannonEnd;
    private double cannonAngle;
    private Line cannon;
    private double[] cannonPointsX;
    private double[] cannonPointsY;
    private double health;
    private double fuel;
    private double bulletSpeed;
    private Line paraLine1;
    private Line paraLine2;
    
    public Tank(){
        middle = new Point2D(TANK_LENGTH/2, 0);
        currLine = new Line();
        paraLine1 = new Line();
        paraLine2 = new Line();
        tankPointsX1 = new double[4];
        tankPointsY1 = new double[4];
        tankPointsX2 = new double[4];
        tankPointsY2 = new double[4];
        cannonPointsX = new double[4];
        cannonPointsY = new double[4];        
        lineNumber = 0;
        cannonCenter = new Point2D();
        cannonEnd = new Point2D();
        cannonAngle = 0;
        cannon = new Line();
        health = MAX_HEALTH;
        fuel = MAX_FUEL;
        bulletSpeed = 0;
    }
    
    public Tank(Point2D startPoint, Map map, int lineNumber){
        middle = startPoint;
        currLine = map.getLine(lineNumber);
        paraLine1 = map.getParaLine1(lineNumber);
        paraLine2 = map.getParaLine2(lineNumber);
        tankPointsX1 = new double[4];
        tankPointsY1 = new double[4];
        tankPointsX2 = new double[4];
        tankPointsY2 = new double[4];
        cannonPointsX = new double[4];
        cannonPointsY = new double[4];
        cannonCenter = new Point2D();
        cannonEnd = new Point2D();
        cannonAngle = 0;
        cannon = new Line();
        this.lineNumber = lineNumber;
        health = MAX_HEALTH;
        fuel = MAX_FUEL;
        bulletSpeed = 0;

    }
    
    public double[] getTankPoints(int i){
        double[] temp = new double[4];           
        temp[0] = tankPointsX1[i];
        temp[1] = tankPointsY1[i];
        temp[2] = tankPointsX2[i];
        temp[3] = tankPointsY2[i];
 
        return temp;       
    }
    
    
    public Point2D getMiddle(){
        return middle;
    }
    
    public Line getLine(){
        return currLine;
    }
    
    public double getSpeed(){
        return TANK_SPEED;
    }
    
    public int getLineNumber(){
        return lineNumber;
    }
    public double getCannonAngle(){
        return cannonAngle;
    }
    public double getRotSpeed(){
        return ROTATION_SPEED;
    }
    public void setCannonAngle(double newAngle){
        cannonAngle = newAngle;
    }
    public Point2D getCannonCenter(){
        return cannonCenter;
    }
    public Point2D getCannonEnd(){
        return cannonEnd;
    }
    public double getTankHealth(){
        return health;
    }
    public void damaged(double damage){
        health -= damage;
    }
    public double getFuel(){
        return fuel;
    }
    public void consumeFuel(){
        fuel -= FUEL_CONSUMPTION;
    }
    public void resetFuel(){
        fuel = MAX_FUEL;
    }
    public void incBulletSpeed(double increment){
        if (bulletSpeed < MAX_BULLET_SPEED){
            bulletSpeed += increment;
        }
        
        
    }
    public double getBulletSpeed(){
        return bulletSpeed;
    }
    public void resetBulletSpeed(){
        bulletSpeed = 0;
    }

    public void setLine(int lineNumber, Map map){
        currLine = map.getLine(lineNumber);
        paraLine1 = map.getParaLine1(lineNumber);
        paraLine2 = map.getParaLine2(lineNumber);
        this.lineNumber = lineNumber;
        
    }
    
    public boolean isOnLine(){
        return ((middle.getX() > currLine.getInfimum().getX()) && (middle.getX() < currLine.getSupremum().getX()));
    }
    
    public void processTankPoints(){ //calcule le rectangle delimitant le corps du tank, partant du point en bas à droite, jusq'au point en bas à gauche
        //Calcul du rectangle sous le canon
        //Point en bas à droite
        tankPointsX1[0] = middle.getX() + currLine.getDiffLine1();
        tankPointsY1[0] = currLine.computePoint(tankPointsX1[0]);
        
        //Point en haut à droite
        tankPointsX1[1] = tankPointsX1[0];
        if (currLine.getSlope() < 0){
            tankPointsX1[1] -= currLine.getDiffPerp1();    
        }
        else if (currLine.getSlope() > 0) {
            tankPointsX1[1] += currLine.getDiffPerp1();
        }
        tankPointsY1[1] = paraLine1.computePoint(tankPointsX1[1]);
        
       // Point en bas à gauche       
       tankPointsX1[3] = middle.getX() - currLine.getDiffLine1();
       tankPointsY1[3] = currLine.computePoint(tankPointsX1[3]);

       
       //Point en haut à gauche
       tankPointsX1[2] = tankPointsX1[3];
       if (currLine.getSlope() < 0){
           tankPointsX1[2] -= currLine.getDiffPerp1();
           
       }
       else if (currLine.getSlope() > 0){
           tankPointsX1[2] += currLine.getDiffPerp1();
       }
       tankPointsY1[2] = paraLine1.computePoint(tankPointsX1[2]);

       
       //calcul du rectangle contenant le canon
       
       //Point en bas à droite
        tankPointsX2[0] = tankPointsX1[1] - currLine.getDiffLine2();
        tankPointsY2[0] = paraLine1.computePoint(tankPointsX2[0]);
        
        //Point en haut à droite
        tankPointsX2[1] = tankPointsX2[0];
        if (currLine.getSlope() < 0){
            tankPointsX2[1] -= currLine.getDiffPerp2();    
        }
        else if (currLine.getSlope() > 0) {
            tankPointsX2[1] += currLine.getDiffPerp2();
        }
        tankPointsY2[1] = paraLine2.computePoint(tankPointsX2[1]);
        
       // Point en bas à gauche       
       tankPointsX2[3] = tankPointsX1[2] + currLine.getDiffLine2();
       tankPointsY2[3] = paraLine1.computePoint(tankPointsX2[3]);

       
       //Point en haut à gauche  
       tankPointsX2[2] = tankPointsX2[3];
       if (currLine.getSlope() < 0){
           tankPointsX2[2] -= currLine.getDiffPerp2();
           
       }
       else if (currLine.getSlope() > 0){
           tankPointsX2[2] += currLine.getDiffPerp2();
       }
       tankPointsY2[2] = paraLine2.computePoint(tankPointsX2[2]);
       
       cannonCenter.setX(tankPointsX2[3] + (currLine.getDiffLine2()/2));
       cannonCenter.setY(currLine.translateLine(currLine.getDiffYIntercept1() + currLine.getDiffYIntercept2()/2).computePoint(cannonCenter.getX()));
       
       double endX = cannonCenter.getX() + CANNON_LENGTH;
       //double endY = currLine.translateLine(currLine.getDiffYIntercept1() + currLine.getDiffYIntercept2()/2).computePoint(endX);
       double endY = cannonCenter.getY();
       endX = endX - CANNON_LENGTH * (1 - cos(cannonAngle - currLine.getDiffAngle()));
       endY = endY - CANNON_LENGTH * sin(cannonAngle - currLine.getDiffAngle());
       
       cannonEnd.setLocation(endX, endY);
       
       if (cannonAngle > PI/2 ){
           cannon.setLine(cannonEnd, cannonCenter);
       }
       else {
           cannon.setLine(cannonCenter, cannonEnd);
       }
       double radius = TANK_HEIGHT2 / 3;
       cannonPointsX[0] = cannonCenter.getX() + radius * sin(cannonAngle - currLine.getDiffAngle());
       cannonPointsY[0] = cannonCenter.getY() + radius * cos(cannonAngle - currLine.getDiffAngle());
       cannonPointsX[1] = cannonEnd.getX() + radius * sin(cannonAngle - currLine.getDiffAngle());
       cannonPointsY[1] = cannonEnd.getY() + radius * cos(cannonAngle - currLine.getDiffAngle());
       cannonPointsX[2] = cannonEnd.getX() - radius * sin(cannonAngle - currLine.getDiffAngle());
       cannonPointsY[2] = cannonEnd.getY() - radius * cos(cannonAngle - currLine.getDiffAngle());
       cannonPointsX[3] = cannonCenter.getX() - radius * sin(cannonAngle - currLine.getDiffAngle());
       cannonPointsY[3] = cannonCenter.getY() - radius * cos(cannonAngle - currLine.getDiffAngle());
       
       
       
        
        
        
        
 
    }

    public Shape getTankShape(){
        Polygon lowerRect = new Polygon();
        Polygon upperRect = new Polygon();
        for (int i = 0; i <= 3; i++){
            lowerRect.getPoints().add(tankPointsX1[i]);
            lowerRect.getPoints().add(tankPointsY1[i]);
            upperRect.getPoints().add(tankPointsX2[i]);
            upperRect.getPoints().add(tankPointsY2[i]);
        }
        Shape tankPoly = union(upperRect, lowerRect);
        return tankPoly;
        
        
    }
        
    
    public void drawTank(GraphicsContext drawContext, Color c){

        drawContext.setFill(c);
        drawContext.fillPolygon(tankPointsX1, tankPointsY1, 4); 
        drawContext.fillPolygon(tankPointsX2, tankPointsY2, 4);

        drawContext.fillPolygon(cannonPointsX, cannonPointsY, 4);

       double lowerWidth = (HEALTH_WIDTH / MAX_HEALTH) * health;
       double xRect = getCannonCenter().getX() - (HEALTH_WIDTH + 1)/2;
       double yRect = getCannonCenter().getY() - 50;
       // Affichage de la barre de santé
       drawBar(health, MAX_HEALTH, HEALTH_WIDTH, HEALTH_HEIGHT, drawContext, xRect, yRect, Color.DARKGREEN);
       
       // Affiche de la barre de fuel
       drawBar(fuel, MAX_FUEL, FUEL_WIDTH, FUEL_HEIGHT, drawContext, xRect, yRect + HEALTH_HEIGHT, Color.YELLOW);
        if (bulletSpeed > 0){
            drawBar(bulletSpeed, MAX_BULLET_SPEED, HEALTH_WIDTH, BULLET_SPEED_HEIGHT, drawContext, xRect, yRect + HEALTH_HEIGHT + FUEL_HEIGHT, Color.CORAL);
        }
            
        }
    public void drawBar(double var, double max_var, double width, double height, GraphicsContext drawContext, double x, double y, Color color){
       drawContext.setFill(Color.BLACK);
       drawContext.fillRect(x, y, width, height);
       drawContext.setFill(Color.AQUA);
       drawContext.fillRect(x + 1, y + 1, width - 2, height - 2);
       drawContext.setFill(color);
       drawContext.fillRect(x + 1, y + 1, (width - 2) * (var/max_var), height - 2);          
        
    }
    }
     

    
    
