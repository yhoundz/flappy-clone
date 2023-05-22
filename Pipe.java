import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.util.ArrayList;

public class Pipe{
    private static BufferedImage sprite;
    private Rectangle[] colliders;
    private int x;
    private int vectorPos;
    private int speed;
    private int normSpeed;
    private int hmSpeed;
    private Rectangle scoreColliderRectangle;
    private boolean scoreCollected;

    public Pipe(){
        reset();
        colliders = new Rectangle[]{new Rectangle(0 + x,0 + vectorPos,182,669), new Rectangle(0 + x,1004 + vectorPos,182,669)};
        scoreColliderRectangle = new Rectangle(181 + x, 670 + vectorPos, 1, 333);
        normSpeed = 10;
        hmSpeed = 17;

        setNorm();
        scoreCollected = false;
    }

    public static BufferedImage getSprite() {
        if (sprite == null) {
            try {
                sprite = ImageIO.read(new File("sprites/pipe.png"));
            } catch (IOException e) {
                System.out.println("image not found");
            }
        }
        return sprite;
    }

    public void draw(Graphics g){
        g.drawImage(getSprite(),this.x, vectorPos,null);
        g.setColor(Color.RED);
        updateColliders();
    }

    public int getSpeed(){
        return speed;
    }

    public void setNorm(){
        speed = normSpeed;
    }

    public void setHM(){
        speed = hmSpeed;
    }

    public int getX(){
        return x;
    }
    
    public void reset(){
    	vectorPos = (int)(Math.random() * (270) + 270) * -1;
    	x = 660;
    }

    public Rectangle[] getColliders(){
        return colliders;
    }

    public Rectangle getScoreCollider(){
        return scoreColliderRectangle;
    }

    public void updateColliders(){
        colliders[0].setLocation(0 + x, vectorPos);
        colliders[1].setLocation(0 + x, 1004 + vectorPos);
        scoreColliderRectangle.setLocation(181 + x, 670 + vectorPos);
    }

    public void killSwitch(){
        speed = 0;
    }

    public void restore(){
        setNorm();
    }

    public void setCollectedScore(boolean f){
        scoreCollected = f;
    }

    public boolean isScoreCollected(){
        return scoreCollected;
    }

    public void run(){
        x -= speed;
    }
}