import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.util.ArrayList;

public class Player{
    private int gravity;
    private int yVel;
    private int jumpForce;
    private BufferedImage sprite;
    private int x,y;
    private Rectangle collider;

    public Player() {
        gravity = 1;
        yVel = 0;
        jumpForce = 20;
        updateCollisions();
        x = 216;
        y = 320;

        try{
            sprite = ImageIO.read(new File("sprites/player_sprite_scaled_7x_pngcrushed.png"));
        }
        catch(IOException e){
            System.out.println("image not found");
        }
        
    }
    
    public void paintComponent(Graphics g, int x, int y){
        this.x = x;
        this.y = y;
        g.drawImage(sprite,this.x,this.y,null);
        updateCollisions();
    }

    public int getGravity(){
        return gravity;
    }

    public void setGravity(int g){
        gravity = g;
    }

    public int getyVel(){
        return yVel;
    }

    public void setyVel(int vel){
        yVel = vel;
    }

    public int getJumpForce(){
        return jumpForce;
    }

    public void fall(){
        yVel += gravity;
        y += yVel;
    }

    public int getY(){
        return y;
    }

    public void setY(int gf){
        y = gf;
    }

    public Rectangle getCollider(){
        return collider;
    }

    public void updateCollisions(){
        collider = new Rectangle(x + 3, y + 40, 110, 66);
    }

    public void jump(){
		yVel = 0;
		yVel -= jumpForce;
	}
	
	public int getX(){
		return x;
	}
}
