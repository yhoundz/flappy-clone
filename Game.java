import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.util.ArrayList;
import java.util.EnumSet;

public class Game{
    public static void main(String[] args){
        JFrame s = new JFrame();
		drawPanel m = new drawPanel();
		s.setSize(m.getSize());
		s.add(m); 
		s.setTitle("fiappy bird");
	    s.addKeyListener(m);
	    s.addMouseListener(m);
		s.setVisible(true);
		s.setResizable(false);
		s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.setLayout(null);
		System.setProperty("sun.java2d.opengl", "true");
    }
}

class drawPanel extends JPanel implements ActionListener, MouseListener, KeyListener{

	private enum state {PAUSED, DEAD, MENU, PLAYING}
	private state currState;
	private Player player;
	private Timer timer;
	private int score;
	private Spawner spawner;
	private boolean isKeyPressed;
	private JLabel scoreLabel;
	private Font customFont;
	private JLabel menuText;
	private JLabel gameOver;
	private int highscore;
	private int col;
	private JLabel pauseLabel;
	private JCheckBox hardModeToggle;

    drawPanel(){
    	highscore = 0;
    	this.setBounds(new Rectangle(0,0,640,960));
		start();
		this.requestFocusInWindow();
	}

	public void start(){
		timer = new Timer(10, this);
        setSize(640, 960);
        setVisible(true);
		player = new Player();
		score = 0;
		spawner = new Spawner();
		isKeyPressed = false;
		currState = state.MENU;
		col = 150;
		hardModeToggle = new JCheckBox("Hardmode");
		this.add(hardModeToggle);
		hardModeToggle.setFocusable(false);
		
		customFont = loadFont();

		menuText = new JLabel("<html><div style = 'text-align:center;'><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>PRESS SPACE TO JUMP</div></html>");
		addJLabel(menuText, 40);
		setHardMode();

		if(gameOver != null){
			this.remove(gameOver);
		}

		stop();
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(new Color(21, 193, 212));
		g.fillRect(0,0,960,960);
		player.paintComponent(g,player.getX(),player.getY());
		if(currState != state.MENU)
			spawner.paintComponent(g);
		g.setColor(new Color(235, 215, 169));
		g.fillRect(0, 940 - 60, 640, 60);
		
		if(currState == state.PAUSED || currState == state.DEAD){
			g.setColor(new Color(col,col,col,128));
			g.fillRect(0,0,960,960);
			
		}
	}

	public void setHardMode(){
		hardModeToggle.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(hardModeToggle.isSelected()){
					spawner.setHardMode(true);
				}
				else{
					spawner.setHardMode(false);
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e){
		if(currState != state.MENU){
			player.fall();
			if(player.getY() > 800){
				player.setY(800);
				player.setyVel(0);
				die();
			}
			if(player.getY() < 0){
				player.setY(0);
				player.setyVel(0);
			}
			
			for(int i = 0; i < spawner.returnPipes().size(); i++){
				if(player.getCollider().intersects(spawner.returnPipes().get(i).getScoreCollider()) && !spawner.returnPipes().get(i).isScoreCollected()){
					spawner.returnPipes().get(i).setCollectedScore(true);
					score++;
					updateScore();
				}
				for(int j = 0; j < spawner.returnPipes().get(i).getColliders().length; j++){
					if(player.getCollider().intersects(spawner.returnPipes().get(i).getColliders()[j])){
						die();
					}
				}
			}

			for(Pipe pipe : spawner.returnPipes()){
				pipe.run();
			}

			repaint();
		}
	}

	public void mouseClicked(MouseEvent e){
		if(currState != state.DEAD && currState != state.PAUSED){
			if(currState == state.MENU){
				this.remove(hardModeToggle);
				addScoreLabel();
				resume();
				currState = state.PLAYING;
				this.remove(hardModeToggle);
				this.remove(menuText);
			}
			player.jump();
		}
		if(currState == state.DEAD){
			start();
		}
	}

	public void addScoreLabel(){
		scoreLabel = new JLabel("<html><div style = 'text-align:center;'>" + score + "</div><html>");
		addJLabel(scoreLabel, 70);
	}

    public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){
		unPause();
	}

	public void mouseExited(MouseEvent e){
		pause();
	}
	
	public void pause(){
		if(currState == state.PLAYING){
			currState = state.PAUSED;
			this.remove(scoreLabel);
			pauseLabel = new JLabel("PAUSED");
			addJLabel(pauseLabel, 70);
			stop();
		}
	}
	
	public void unPause(){
		if(currState == state.PAUSED){
			this.remove(pauseLabel);
			this.add(scoreLabel);
			resume();
			currState = state.PLAYING;
		}
	}

	public void keyPressed(KeyEvent e){
		if(currState == state.MENU || currState == state.PLAYING){
			if(e.getKeyCode() == KeyEvent.VK_SPACE && !isKeyPressed){
				if(currState == state.MENU){
					addScoreLabel();
					resume();
					currState = state.PLAYING;
					this.remove(hardModeToggle);
					this.remove(menuText);
				}
				player.jump();
				isKeyPressed = true;
			}
		}
		if(currState == state.DEAD){
			start();
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			if(currState == state.PLAYING){
				pause();
			}
			else if(currState == state.PAUSED){
				unPause();
			}
		}
	}
	public void keyReleased(KeyEvent e)	{
		isKeyPressed = false;
	}
	public void keyTyped(KeyEvent e){}

	public void stop(){
		timer.stop();
		spawner.killSwitch();
	}

	public void resume(){
		if((currState == state.PAUSED || currState == state.MENU) && currState != state.DEAD)
			timer.start();
			spawner.restore();
			repaint();
	}

	public void updateScore(){
		scoreLabel.setText("<html><div style = 'text-align:center;'>" + score + "</div><html>");
	}

	public Font loadFont(){
        try{
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("sprites/ka1.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

            return font;
        }
        catch(IOException | FontFormatException e){
            return new Font("Arial", Font.PLAIN, 96);
        }
    }
    
    public void die(){
    	currState = state.DEAD;
		stop();
		if(score > highscore){
			highscore = score;
		}
		this.remove(scoreLabel);
		gameOver = new JLabel("<html><div style = 'text-align:center;'><br/><br/>GAME OVER<br/>SCORE: " + score + "<br/>HIGHSCORE: " + highscore + "</div></html>");
		addJLabel(gameOver, 60);
		score = 0;
    }

	public void addJLabel(JLabel label, int fontSize){
		label.setFont(customFont);
		this.add(label);
		label.setSize(label.getPreferredSize());
		label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), fontSize));
		label.setForeground(Color.BLACK);
		repaint();
	}
}

