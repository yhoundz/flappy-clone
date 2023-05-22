import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.util.ArrayList;

public class Spawner implements ActionListener{
    private Timer spawnDelay;
    private ArrayList<Pipe> pipes;
    private int currPipe;
    private boolean inHardMode;

    public Spawner(){
        pipes = new ArrayList<Pipe>();
        spawnDelay = new Timer(2000,this);
        spawnDelay.setInitialDelay(2000);
        spawnDelay.setDelay(1000);
        currPipe = 0;
        inHardMode = false;
    }

    public void paintComponent(Graphics g){
        if(!spawnDelay.isRunning()){
            spawnDelay.start();
        }
        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }
    }

    public void spawn() {
        currPipe = (pipes.size() == 0 || currPipe == 0) ? 0 : currPipe % pipes.size();
        if (pipes.size() < 3) {
            pipes.add(new Pipe());
            if(inHardMode){
                pipes.get(pipes.size() - 1).setHM();
            }
        } else {
            Pipe currentPipe = pipes.get(currPipe);
            if (currentPipe.getX() < -180) {
                currentPipe.reset();
                currentPipe.setCollectedScore(false);
                currPipe++;
            }
        }
    }

    public ArrayList<Pipe> returnPipes(){
        return pipes;
    }

    public boolean getHardMode(){
        return inHardMode;
    }

    public void setHardMode(boolean j){
        inHardMode = j;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        spawn();
    }

    public void killSwitch(){
        spawnDelay.stop();
        for(int i = 0; i < pipes.size(); i++){
            pipes.get(i).killSwitch();
        }
    }

    public void restore(){
        spawnDelay.start();
        for(int i = 0; i < pipes.size(); i++){
            pipes.get(i).restore();
            if(getHardMode()){
                pipes.get(i).setHM();
            }
        }
    }
}