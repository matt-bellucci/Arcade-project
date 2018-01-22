package fr.insarcade.javafx.games.shellshock;

import fr.insarcade.controller.ControllerData;
import fr.insarcade.javafx.core.Arcade;
import fr.insarcade.javafx.core.ArcadeGame;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;


/**
 *
 * @author Matthieu Bellucci
 */
public class GameShellShock extends ArcadeGame {
    
    public final static int NB_LINES = 10;
    public final static double PI = 3.14;
    public final static double SPEED_LOADING = 100;
    public final static double BULLET_DAMAGE = 20;
    public Map map; 
    private Tank tank1;
    private Tank tank2;
    private double timer;
    private Bullet bullet;
    private boolean isBulletShot;
    
            
    
    
    
    public GameShellShock(){
        super("ShellShock", "Matthieu Bellucci");
        
    }

    @Override
    public void resetGame() {
        
        map = new Map(NB_LINES);
        tank1 = spawnTank(50, map, "left");
        tank2 = spawnTank(950, map, "right");
        timer = 0;
        isBulletShot = false;
        bullet = new Bullet();
        tank1.processTankPoints();
        tank2.processTankPoints();
    }

    @Override
    public void updateGame(double interpolation, ControllerData controller) {
        switch(getState()) {
            case 1 : //deplacement et tir du joueur 1
                timer = 0;
                moveTank1(controller, interpolation, tank1);
                bullet.setBulletPos(tank1.getCannonEnd());
                if (controller.isJoystick1_ACTION()){
                    tank1.incBulletSpeed(SPEED_LOADING * interpolation);                   
                }
                else{
                    if (tank1.getBulletSpeed() > 0){
                        isBulletShot = true;
                    }
                }
                if (isBulletShot){
                    double speedx = tank1.getBulletSpeed() * cos(tank1.getCannonAngle() - tank1.getLine().getDiffAngle());
                    double speedy = tank1.getBulletSpeed() * sin(tank1.getCannonAngle() - tank1.getLine().getDiffAngle());
                    bullet.setBulletSpeed(speedx, speedy);
                    isBulletShot = false; 
                    tank1.resetBulletSpeed();
                    bullet.setBulletPos(tank1.getCannonEnd());
                    setState(2);
                }
                
                tank1.processTankPoints();
                break;
            case 2 :  // Si le joueur 1 tire
                //tank1.processTankPoints();
                bullet.moveBullet(timer);
                timer += 2*interpolation;
                if (collides(bullet.getBulletShape(), tank2.getTankShape())){
                    tank2.damaged(BULLET_DAMAGE);
                    if (tank2.getTankHealth() <= 0){
                        gameIsFinish(winner.Player1, false);
                    }
                    setState(3);
                }
                else if((collides(bullet.getBulletShape(), map.getMapShape())) || (bullet.getBulletPos().getX() > Arcade.getWindowWidth()) || bullet.getBulletPos().getX() < 0){
                    setState(3);
                }    
                
                //tank2.processTankPoints();
                
                break;
            case 3 : 
                
                timer = 0;
                moveTank2(controller, interpolation, tank2);
                bullet.setBulletPos(tank2.getCannonEnd());
                if (controller.isJoystick2_ACTION()){
                    tank2.incBulletSpeed(SPEED_LOADING * interpolation);                   
                }
                else{
                    if (tank2.getBulletSpeed() > 0){
                        isBulletShot = true;
                    }
                }
                if (isBulletShot){
                    double speedx = tank2.getBulletSpeed() * cos(tank2.getCannonAngle() - tank2.getLine().getDiffAngle());
                    double speedy = tank2.getBulletSpeed() * sin(tank2.getCannonAngle() - tank2.getLine().getDiffAngle());
                    if ((tank2.getCannonAngle() - tank2.getLine().getDiffAngle()) < (PI/2)){
                        bullet.setBulletSpeed(speedx, speedy);                        
                    }
                    else{
                        bullet.setBulletSpeed(speedx, speedy);                       
                    }

                    isBulletShot = false; 
                    tank2.resetBulletSpeed();
                    bullet.setBulletPos(tank2.getCannonEnd());
                    setState(4);
                }
                
                //tank1.processTankPoints();
                tank2.processTankPoints();                
                break;
            case 4 :
                //tank2.processTankPoints();
                //tank1.processTankPoints();
                bullet.moveBullet(timer);
                timer += 2*interpolation;
                if (collides(bullet.getBulletShape(), tank1.getTankShape())){
                    tank1.damaged(BULLET_DAMAGE);
                    if (tank1.getTankHealth() <= 0){
                        gameIsFinish(winner.Player2, false);
                    }
                    setState(1);
                }
                else if((collides(bullet.getBulletShape(), map.getMapShape())) || (bullet.getBulletPos().getX() > Arcade.getWindowWidth()) || bullet.getBulletPos().getX() < 0){
                    tank1.resetFuel();
                    tank2.resetFuel();
                    setState(1);
                }               
                
                break;
                
               
        }
       
    }

    @Override
    public void renderGame(GraphicsContext drawContext) {
        switch(getState()) {
            case 1 :
                map.drawMap(drawContext);
                //map.debugMap(drawContext);

                tank1.drawTank(drawContext, Color.BLUE);
                tank2.drawTank(drawContext, Color.RED);
                showDebug(drawContext, tank1);
                break;
            case 2 :
                map.drawMap(drawContext);
                //map.debugMap(drawContext);

                tank1.drawTank(drawContext, Color.BLUE);
                tank2.drawTank(drawContext, Color.RED);
                bullet.drawBullet(drawContext, Color.BLUE);
                showDebug(drawContext, tank1);
                
                break;
            case 3 :
                map.drawMap(drawContext);
                tank1.drawTank(drawContext, Color.BLUE);
                tank2.drawTank(drawContext, Color.RED);
                showDebug(drawContext, tank2);   
                break;
            case 4 :
                map.drawMap(drawContext);
                //map.debugMap(drawContext);

                tank1.drawTank(drawContext, Color.BLUE);
                tank2.drawTank(drawContext, Color.RED);
                bullet.drawBullet(drawContext, Color.RED);
                showDebug(drawContext, tank1);               
                break;
                
                
        }
        
    }
    
    public void showDebug(GraphicsContext drawContext, Tank tank){
        String debug0 = "xmid " + tank.getMiddle().getX();
        String debug00 = "ymid " + tank.getMiddle().getY();
        String debug1 = "x1 " + tank.getTankPoints(0)[0];
        String debug2 = "y1 " + tank.getTankPoints(0)[1];
        String debug3 = "x2 " + tank.getTankPoints(1)[0];
        String debug4 = "y2 " + tank.getTankPoints(1)[1];
        String debug5 = "x3 " + tank.getTankPoints(2)[0];
        String debug6 = "y3 " + tank.getTankPoints(2)[1];
        String debug7 = "x4 " + tank.getTankPoints(3)[0];
        String debug8 = "y4 " + tank.getTankPoints(3)[1];
        String debug9 = "infX " + tank.getLine().getInfimum().getX();
        String debug10 = "infY " + tank.getLine().getInfimum().getY();
        String debug11 = "Yint " + tank.getLine().getYIntercept();
        String debug12 = "supX " + tank.getLine().getSupremum().getX();
        String debug13 = "supY " + tank.getLine().getSupremum().getX();
        String debug14 = "diffPerp " + tank.getLine().getDiffPerp1();
        String debug15 = "diffLine " + tank.getLine().getDiffLine1();
        String debug16 = "CannonCenter x, y " + tank.getCannonCenter().getX() + " " + tank.getCannonCenter().getY();
        String debug17 = "CannonEnd x, y " + tank.getCannonEnd().getX() + " " + tank.getCannonEnd().getY();
        String debug18 = "CannonAngle " + (tank.getCannonAngle()-tank.getLine().getDiffAngle());
        String debug19 = "collision tank ? " + collides(tank1.getTankShape(), tank2.getTankShape());
        String debug20 = "timer " + timer;
        String debug21 = "bullet x, y " + bullet.getBulletPos().getX() + " " + bullet.getBulletPos().getY();
        String debug22 = "gravity " + bullet.getGravity();
        String debug23 = "fuel" + tank.getFuel();
        drawContext.setFill(Color.BLACK);
        drawContext.fillText(debug0, 500, 50);
        drawContext.fillText(debug00, 500, 75);
        drawContext.fillText(debug1, 500, 100);
        drawContext.fillText(debug2, 500, 125);
        drawContext.fillText(debug3, 500, 150);
        drawContext.fillText(debug4, 500, 175);
        drawContext.fillText(debug5, 500, 200);
        drawContext.fillText(debug6, 500, 225);
        drawContext.fillText(debug7, 500, 250);
        drawContext.fillText(debug8, 500, 275);
        drawContext.fillText(debug9, 500, 300);
        drawContext.fillText(debug10, 500, 325);
        drawContext.fillText(debug11, 500, 350);
        drawContext.fillText(debug12, 500, 375);
        drawContext.fillText(debug13, 500, 400);
        drawContext.fillText(debug14, 500, 425);
        drawContext.fillText(debug15, 500, 450);
        drawContext.fillText(debug16, 500, 475);
        drawContext.fillText(debug17, 500, 500);
        drawContext.fillText(debug18, 500, 525);
        drawContext.fillText(debug19, 500, 550);
        drawContext.fillText(debug20, 500, 575);
        drawContext.fillText(debug21, 500, 600);
        drawContext.fillText(debug22, 500, 625);
        drawContext.fillText(debug23, 500, 650);
        
        
        
    }
    
    public void changeLine(Tank tank){
        if ((tank.getMiddle().getX() < tank.getLine().getInfimum().getX()) && (tank.getLineNumber() > 0)){
            tank.setLine(tank.getLineNumber() - 1, map);                       
        }
        
        else if ((tank.getMiddle().getX() > tank.getLine().getSupremum().getX()) && (tank.getLineNumber() < (NB_LINES - 1))){
            tank.setLine(tank.getLineNumber() + 1, map);
        }
        
        
    }
        
    public void moveTank1(ControllerData controller, double interpolation, Tank tank){
        
        double x = tank.getMiddle().getX();
        double prevX = x;
        double angle = tank.getCannonAngle();
        double prevAngle = angle;
        if ((controller.isJoystick1_RIGHT()) && (tank.getFuel() > 0)){
            x += tank.getSpeed() * interpolation;
            tank.consumeFuel();
        }
        if ((controller.isJoystick1_LEFT()) && (tank.getFuel() > 0)){ 
            x -= tank.getSpeed() * interpolation;
            tank.consumeFuel();
        }
        if (controller.isJoystick1_UP()){
            angle += tank.getRotSpeed() * interpolation;
            if (angle > PI){
                angle = prevAngle;
            }
            tank.setCannonAngle(angle);
            
        }
        if (controller.isJoystick1_DOWN()){
            angle -= tank.getRotSpeed() * interpolation;
            if (angle < 0){
                angle = prevAngle;
            }
            tank.setCannonAngle(angle);
            
        }        
        tank.getMiddle().setX(x);            
        if (isTankOutside(tank, map)){
                tank.getMiddle().setX(prevX);
        }
        changeLine(tank);
        tank.getMiddle().setY(tank.getLine().computePoint(tank.getMiddle().getX()));
        
            
        }
    public void moveTank2(ControllerData controller, double interpolation, Tank tank){
        
        double x = tank.getMiddle().getX();
        double prevX = x;
        double angle = tank.getCannonAngle();
        double prevAngle = angle;
        if ((controller.isJoystick2_RIGHT()) && (tank.getFuel() > 0)){
            x += tank.getSpeed() * interpolation;
            tank.consumeFuel();
        }
        if ((controller.isJoystick2_LEFT()) && (tank.getFuel() > 0)){ 
            x -= tank.getSpeed() * interpolation;
            tank.consumeFuel();
        }
        if (controller.isJoystick2_UP()){
            angle += tank.getRotSpeed() * interpolation;
            if (angle > PI){
                angle = prevAngle;
            }
            tank.setCannonAngle(angle);
            
        }
        if (controller.isJoystick2_DOWN()){
            angle -= tank.getRotSpeed() * interpolation;
            if (angle < 0){
                angle = prevAngle;
            }
            tank.setCannonAngle(angle);
            
        }        
        tank.getMiddle().setX(x);            
        if (isTankOutside(tank, map)){
                tank.getMiddle().setX(prevX);
        }
        changeLine(tank);
        tank.getMiddle().setY(tank.getLine().computePoint(tank.getMiddle().getX()));
        
            
        }
    
        
        public boolean isTankOutside(Tank tank, Map map){
            boolean resultInf = false; //resultInf est vrai si le tank sort du cadre à gauche
            boolean resultSup = false; // resultSup est vrai si le tank sort du cadre à droite
            if (tank.getLineNumber() == 0){
                
                
                if (map.getLine(0).getSlope() < 0){
                
                    resultInf = ( (tank.getMiddle().getX() - tank.getLine().getDiffLine1() - tank.getLine().getDiffPerp1() ) < map.getLine(0).getInfimum().getX() );
                
                }
                
                else if (map.getLine(0).getSlope() > 0){
                    resultInf = ( (tank.getMiddle().getX() - tank.getLine().getDiffLine1() ) < map.getLine(0).getInfimum().getX() );
                }
                
                else{
                    resultInf = (tank.getMiddle().getX() < map.getLine(0).getInfimum().getX());
                }
                   
            }
            
            if (tank.getLineNumber() == (NB_LINES - 1)){
                
                
                if (map.getLine(NB_LINES - 1).getSlope() < 0){
                
                    resultSup = ( (tank.getMiddle().getX() + tank.getLine().getDiffLine1() ) > map.getLine(NB_LINES - 1).getSupremum().getX() );
                
                }
                
                else if (map.getLine(NB_LINES - 1).getSlope() > 0){
                    resultInf = ( (tank.getMiddle().getX() + tank.getLine().getDiffLine1() + tank.getLine().getDiffPerp1() ) > map.getLine(NB_LINES - 1).getSupremum().getX() );
                }
                
                else{
                    resultSup = (tank.getMiddle().getX() > map.getLine(NB_LINES - 1).getSupremum().getX());
                }
                
            }
            
            return (resultInf || resultSup);
            
            
        }
        
        public Tank spawnTank(double x, Map map, String pos){
            
            int lMin = 0;
            int lMax = NB_LINES;
            int lMid = (int) (NB_LINES / 2);
            // On cherche la ligne correspondant à l'abscisse
            while ( (x > map.getLine(lMid).getSupremum().getX()) || (x < map.getLine(lMid).getInfimum().getX()) ){
                
            
                if ( x < map.getLine(lMid).getInfimum().getX() ){
                    lMax = lMid;                   
                }
                else if ( x > map.getLine(lMid).getSupremum().getX() ){
                    lMin = lMid;
                }
                lMid = (int) (lMax + lMin) / 2;
                
            }
            // On s'assure que le tank n'apparait pas hors de la map
            double y = map.getLine(lMid).computePoint(x);
            Point2D start = new Point2D(x,y);
            Tank nTank = new Tank(start, map, lMid);
            
            if (isTankOutside(nTank, map)){
                x += 10;
                nTank = spawnTank(x, map, pos);
            }
            if ("right".equals(pos)){
               nTank.setCannonAngle(PI);
            }
            else{
                nTank.setCannonAngle(0);
            }
            return nTank;
            
        }
        
        public boolean collides(Shape shape1, Shape shape2){
            Shape intersect = Shape.intersect(shape1, shape2); 
            boolean collisionDetected  = !intersect.getBoundsInLocal().isEmpty();
            return collisionDetected;
        }
        
        public void drawBar(double var, double max_var, double width, double height, GraphicsContext drawContext, double x, double y, Color color){
            drawContext.setFill(Color.BLACK);
            drawContext.fillRect(x, y, width, height);
            drawContext.setFill(Color.WHITE);
            drawContext.fillRect(x + 1, y + 1, width - 2, height - 2);
            drawContext.setFill(color);
            drawContext.fillRect(x + 1, y + 1, (width - 2) * (var/max_var), height - 2);          
        }
    

            
    
    
    
    
    }
    

