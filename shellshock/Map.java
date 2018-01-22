package fr.insarcade.javafx.games.shellshock;

import fr.insarcade.javafx.core.Arcade;
import static fr.insarcade.javafx.games.shellshock.GameShellShock.NB_LINES;
import static java.lang.Math.ceil;
import java.util.Random;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Matthieu
 */
public class Map {
    private final static int MAX_LINES = 1000;
    private final static double SLOPE_MAX = 0.75;
    private final int interval;
    private final Line[] map;
    private final Line[] paraLines1;
    private final Line[] paraLines2;
    private double[] xPoints;
    private double[] yPoints;
        
    
    public Map(){
        interval = (int) ceil(Arcade.getWindowWidth() / MAX_LINES);
        map = new Line[MAX_LINES];
        paraLines1 = new Line[MAX_LINES];
        paraLines2 = new Line[MAX_LINES];

    }
    
    public Map(int nbLines){
        interval = (int) ceil(Arcade.getWindowWidth() / nbLines);
        map = generateMap(nbLines);
        paraLines1 = new Line[MAX_LINES];
        paraLines2 = new Line[MAX_LINES];
        for (int i = 0; i < nbLines; i++){
            paraLines1[i] = map[i].translateLine(map[i].getDiffYIntercept1());
            paraLines2[i] = map[i].translateLine(map[i].getDiffYIntercept2() + map[i].getDiffYIntercept1());
        }
    }
    
    private Line[] generateMap(int nbLines){
        
        Line[] tempMap = new Line[nbLines];
        xPoints = new double[nbLines + 1];
        yPoints = new double[nbLines + 1];
        Point2D[] mapPoints = new Point2D[nbLines + 1];
        Random rand = new Random();
        double random;
        
        xPoints[0] = 0;
        yPoints[0] =  rand.nextDouble() * (Arcade.getWindowHeight() / 4) + (3 * Arcade.getWindowHeight() / 4);
        mapPoints[0] = new Point2D(xPoints[0], yPoints[0]);
                
        for (int i = 1; i <= nbLines; i++){
            xPoints[i] = xPoints[i - 1] + interval;
            random = rand.nextDouble();
            yPoints[i] = yPoints[i-1] + (random * ( 2 * SLOPE_MAX * interval) - SLOPE_MAX * interval);
            if (yPoints[i] > Arcade.getWindowHeight()){
                yPoints[i] = Arcade.getWindowHeight() - 10;
            }
            else if (yPoints[i] < Arcade.getWindowHeight() / 4){
                yPoints[i] = Arcade.getWindowHeight() / 4;
            }
                
            /* 
            rand.nextDouble() génère un nombre entre 0 et 1, on veut que y[i] suive la relation |y[i] - y[i-1]| / interval < SLOPE_MAX.
            On prend donc un nombre aléatoire entre 0 et 2 * SLOPE_MAX*interval + y[i - 1] puis on décale cette valeur de SLOPE_MAX * interval + y[i-1] 
            de telle manière que le nombre appartienne à [ - (SLOPE_MAX * interval + y[i-1]) ; SLOPE_MAX * interval + y[i-1] ] et donc y[i] se voit
            imposer la condition que la pente de la ligne est entre -SLOPE_MAX et SLOPE_MAX.              
            */
            xPoints[nbLines] = Arcade.getWindowWidth();
            mapPoints[i] = new Point2D(xPoints[i], yPoints[i]);
            tempMap[i-1] = new Line(mapPoints[i-1], mapPoints[i]);

        }
            
            
        
        return tempMap;
            
        }
    public Line getLine(int i){
        return map[i];
    } 
    public Line getParaLine1(int i){
        return paraLines1[i];
    }
    
    public Line getParaLine2(int i){
        return paraLines2[i];
    }
    
    public Line[] getLineArray(){
        return map;
    }
    public Polygon getMapShape(){
        Polygon mapShape = new Polygon();
        mapShape.getPoints().add( (double) -2);
        mapShape.getPoints().add( (double) -5000);
        mapShape.getPoints().add( (double) 0);
        mapShape.getPoints().add( (double) 0);
        for (int i = 0; i < xPoints.length; i++){
            mapShape.getPoints().add(xPoints[i]);
            mapShape.getPoints().add(yPoints[i]);
        }
        mapShape.getPoints().add( (double) Arcade.getWindowWidth());
        mapShape.getPoints().add( (double) -5000);
        mapShape.getPoints().add( (double) Arcade.getWindowWidth() + 2);
        mapShape.getPoints().add( (double) Arcade.getWindowHeight());
        mapShape.getPoints().add( (double) 0);
        mapShape.getPoints().add( (double) Arcade.getWindowHeight());
        return mapShape;       
        
    }
        
    public void debugMap(GraphicsContext drawContext){
        String debug1 = "x0, yo " + this.getLine(0).getInfimum().getX() + " , " + this.getLine(0).getInfimum().getY();
        String debug2 = "x1, y1 " + this.getLine(1).getSupremum().getX() + " , " + this.getLine(0).getSupremum().getY() + " slope : " + this.getLine(0).getSlope();
        String debug25 = "x1', y1' " + this.getLine(1).getInfimum().getX() + " , " + this.getLine(1).getInfimum().getY();
        String debug3 = "x2, y2 " + this.getLine(2).getInfimum().getX() + " , " + this.getLine(2).getInfimum().getY();
        String debug4 = "interval " + this.interval;
        String debug5 = "x3, y3 " + this.getLine(2).getSupremum().getX() + " , " + this.getLine(2).getSupremum().getY() + " , slope : " + this.getLine(2).getSlope();
        drawContext.fillText(debug1, 500, 250);
        drawContext.fillText(debug2, 500, 275);
        drawContext.fillText(debug25, 500, 287.5);
        drawContext.fillText(debug3, 500, 300);
        drawContext.fillText(debug5, 500, 325);
        drawContext.fillText(debug4, 500, 350);
        
        
    }
    public void drawMap(GraphicsContext drawContext){
        drawContext.setFill(Color.AQUA);
        drawContext.fillRect(0, 0, Arcade.getWindowWidth(), Arcade.getWindowHeight());
        drawContext.setFill(Color.GREEN);
        for (int i = 0; i < NB_LINES; i++){
            this.getLine(i).drawLine(drawContext);                          
        }       
    }
}
    

