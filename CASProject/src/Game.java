import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements KeyListener{

	
	public static int INIT_X = 100;
	public static int INIT_Y = 100;
	public static int INIT_X2 = 600;
	public static int INIT_Y2 = 600;
	public Character character;
	public Character enemy;
	public Target target;	
	private static int WINDOWWIDTH = 1200;
	private static int WINDOWHEIGHT = 800;
	private static int IMGWIDTH = 100;
	
	private static int HITS_TO_WIN = 3;
	
	public int x;
	public int y;
	public int xVelocity;
	public int yVelocity;
	public int x1;
	public int y1;
	public int xVelocity1;
	public int yVelocity1;
	public int velocity;
	public int acceleration;
	public boolean collision;
	public int collisionCount;
	public int collisionCount1;
	
	
	public static int rounds;
	public static boolean roundOver;

	public static ArrayList<Bullet> bullets = new ArrayList<Bullet>();

	
	public Game() {
		super();
		
		roundOver = false;
		velocity = 2;
		acceleration = 1;
		collision = false;
		collisionCount = 0;
		

		x = INIT_X;
		y = INIT_Y;
		x1 = INIT_X2;
		y1 = INIT_Y2;
		target = new Target(1000, 300);

		
		
        addKeyListener(this);
		this.setBackground(Color.white);
        this.setBounds(0, 0, WINDOWWIDTH , WINDOWHEIGHT);
	}

	public void initialize(int ship) {
		character = new Character(0, ship);
		enemy = new Character(1, ship);
		roundOver = false;
		collisionCount = 0;
		collisionCount1 = 0;
		x = INIT_X;
		y = INIT_Y;
		x1 = INIT_X2;
		y1 = INIT_Y2;
		bullets.clear();

	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		movement();
		movement2();
		g.drawImage(character.getImg(), x, y, character.getSizeX(), character.getSizeY(), this);
		g.drawImage(enemy.getImg(), x1, y1, enemy.getSizeX(), enemy.getSizeY(), this);

		character.setPos(x, y);
		enemy.setPos(x1, y1);

		
		g.drawImage(target.getImg(), target.getX(), target.getY(), target.getSizeX(), target.getSizeY(), this);
		g.drawString("Times player1 is hit " + collisionCount, 100, 50);
		g.drawString("Times player2 is hit " + collisionCount1, 100, 80);

		
		if(bullets.size() > 0) {
			for(int i = 0; i<bullets.size(); i++) {
				bullets.get(i).moveBullet();
				g.drawImage(bullets.get(i).getImg(), bullets.get(i).getX(), bullets.get(i).getY(),  bullets.get(i).getSizeX(), bullets.get(i).getSizeY(), this);
				
				
				collisionDetect(i);
				if((collisionCount == HITS_TO_WIN || collisionCount1 == HITS_TO_WIN) && !roundOver) {
					rounds++;
					roundOver = true;
				}
				if (collision) {
					bullets.remove(i);
					break;
				}
				
				if(bullets.get(i).getX() > WINDOWWIDTH) {
					bullets.remove(i);
					break;
				}
				
			}
		}
		System.out.println(rounds);
		repaint();
	}

	
	
	public void resetRound() {
		roundOver = false;
		collisionCount = 0;
		collisionCount1 = 0;
		x = INIT_X;
		y = INIT_Y;
		x1 = INIT_X2;
		y1 = INIT_Y2;
		bullets.clear();
	}
	
	public void collisionDetect(int i) {
		Rectangle bulletHitbox = bullets.get(i).bounds();
		Rectangle characterHitBox = character.bounds();
		Rectangle enemyHitBox = enemy.bounds();

		if(bulletHitbox.intersects(characterHitBox)) {
			collision = true;
			collisionCount ++;
		}
		else if (bulletHitbox.intersects(enemyHitBox)) {
			collision = true;
			collisionCount1 ++;
		}
		else { 
			collision = false;
		}
	}
	
	
	public void movement() {
		if(x < 0) {
			x = 1;
		}
		else if(x > WINDOWWIDTH-IMGWIDTH) {
			x = WINDOWWIDTH-IMGWIDTH;
		}
		else {
			x += xVelocity;
		}
		
		
		if(y<0) {
			y=1;
		}
		else if(y>WINDOWHEIGHT-IMGWIDTH) {
			y = WINDOWHEIGHT-IMGWIDTH;
		}
		else {
			y += yVelocity;
		}
		
	} 

	public void movement2() {
		if(x1 < 0) {
			x1 = 1;
		}
		else if(x1 > WINDOWWIDTH-IMGWIDTH) {
			x1 = WINDOWWIDTH-IMGWIDTH;
		}
		else {
			x1 += xVelocity1;
		}
		
		
		if(y1<0) {
			y1=1;
		}
		else if(y1>WINDOWHEIGHT-IMGWIDTH) {
			y1 = WINDOWHEIGHT-IMGWIDTH;
		}
		else {
			y1 += yVelocity1;
		}
		
	} 
	@Override
	public void keyTyped(KeyEvent e) {
		if(Math.abs(xVelocity) < Math.abs(velocity)) {
			if(e.getKeyChar() == 'd') {
				xVelocity += acceleration;
			}
			else if(e.getKeyChar() == 'a') {
				xVelocity -= acceleration;
			}
		}
		
		if(Math.abs(yVelocity) < Math.abs(velocity)) {
			if(e.getKeyChar() == 'w') {
				yVelocity -= acceleration;
			}
			else if(e.getKeyChar() == 's') {
				yVelocity += acceleration;
			}
		}
		
		
		
		
		//Second character
		if(Math.abs(xVelocity1) < Math.abs(velocity)) {
			if(e.getKeyChar() == 'l') {
				xVelocity1 += acceleration;
			}
			else if(e.getKeyChar() == 'j') {
				xVelocity1 -= acceleration;
			}
		}
		
		if(Math.abs(yVelocity1) < Math.abs(velocity)) {
			if(e.getKeyChar() == 'i') {
				yVelocity1 -= acceleration;
			}
			else if(e.getKeyChar() == 'k') {
				yVelocity1 += acceleration;
			}
		}
		
		
		
		if(e.getKeyChar() == 'f') {
			bullets.add(new Bullet(x + IMGWIDTH/2 + 50, y + IMGWIDTH/2 - 10, true));
		}
		
		if(e.getKeyChar() == 'h') {
			bullets.add(new Bullet(x1-50, y1 + IMGWIDTH/2 - 10, false));
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyChar() == 'd' || e.getKeyChar() == 'a') {
			xVelocity = 0;
		}
		if(e.getKeyChar() == 'w' || e.getKeyChar() == 's') {
			yVelocity = 0;
		}
		
		if(e.getKeyChar() == 'l' || e.getKeyChar() == 'j') {
			xVelocity1 = 0;
		}
		if(e.getKeyChar() == 'i' || e.getKeyChar() == 'k') {
			yVelocity1 = 0;
		}
		
	}
	
	
	public void setFocus() {
		this.setFocusable(true);
		this.requestFocusInWindow();
	}
	
}
