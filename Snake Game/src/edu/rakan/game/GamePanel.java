package edu.rakan.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;
	//Render
	private Graphics2D g2d;
	private BufferedImage image;

	//Game Loop
	private Thread thread;
	private boolean running;
	private long targetTime;

	//Game Stuff
	private final int SIZE = 10;
	private Entity head,apple,rock;
	private ArrayList<Entity> snake;
	private int score;
	
	//Movement
	private int dx,dy;
	//key input
	private boolean up,down,right,left,start;
	public GamePanel(){
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	@Override
	public void addNotify() {
		super.addNotify(); 
		thread = new Thread(this);
		thread.start();

	}

	public void setFPS(int fps) {
		targetTime = 1000/ fps;

	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		int k = e.getKeyCode();
		
		if(k==KeyEvent.VK_UP) up = true;
		if(k==KeyEvent.VK_DOWN) down = true;
		if(k==KeyEvent.VK_LEFT) left = true;
		if(k==KeyEvent.VK_RIGHT) right = true;
		if(k==KeyEvent.VK_ENTER) start = true;


	}
	@Override
	public void keyReleased(KeyEvent e) {
		
		int k = e.getKeyCode();

		if(k==KeyEvent.VK_UP) up = false;
		if(k==KeyEvent.VK_DOWN) down = false;
		if(k==KeyEvent.VK_LEFT) left = false;
		if(k==KeyEvent.VK_RIGHT) right = false;
		if(k==KeyEvent.VK_ENTER) start = false;


	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		if(running)return;
		init();
		long startTime;
		long elapsed;
		long wait;

		while(running){
			startTime = System.nanoTime();
			
			update();
			requestRender();
			
			elapsed = System.nanoTime()- startTime;
			wait = targetTime - elapsed / 1000000;
			if(wait > 0){
				try{
					Thread.sleep(wait);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		running = true;
		setUplevel();
		setFPS(10); //the speed of the snake
		

	}
	
	private void setUplevel(){
		snake = new ArrayList<Entity>();
		head = new Entity(SIZE);
		head.setPosition(WIDTH/ 2, HEIGHT/ 2);
		snake.add(head);
		
		//Size of the snake is 3
		for(int i = 1;i < 3;i++){
			Entity e = new Entity(SIZE);
			e.setPosition(head.getX()+(i*SIZE), head.getY());
			snake.add(e);
		}
		
		apple = new Entity(SIZE);
		rock = new Entity(SIZE);

		setApple();
		setRock();

		score = 0;
	}
	
	
	public void setApple(){
		int x = (int)(Math.random()*(WIDTH - SIZE));
		int y = (int)(Math.random()*(HEIGHT - SIZE));
		x = x - (x % SIZE);
		y = y - (y % SIZE);

		apple.setPosition(x, y);
	
	}
	
	public void setRock(){
		int x = (int)(Math.random()*(WIDTH - SIZE));
		int y = (int)(Math.random()*(HEIGHT - SIZE));
		x = x - (x % SIZE);
		y = y - (y % SIZE);

		rock.setPosition(x, y);
	
	}
	
	private void requestRender() {
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image,0,0,null);
		g.dispose();
		
	}
	private void update() {
		if(up && dy == 0){
			dy = -SIZE;
			dx = 0;
		}
		if(down && dy == 0){
			dy = SIZE;
			dx = 0;
		}
		if(left && dx == 0){
			dy = 0;
			dx = -SIZE;
		}
		if(right && dx == 0){
			dy = 0;
			dx = SIZE;
		}
		
		if(dx != 0 || dy !=0) {
		
			for (int i = snake.size() - 1;i > 0;i--){
				snake.get(i).setPosition(
						snake.get(i - 1).getX(),
						snake.get(i - 1).getY()
						
						);	
				}
			head.move(dx, dy);
		
		}
		
		//if head of snake collide the apple
		// then increment the score to 1
		if (apple.isCollsion(head)){
			score++;
			setApple();
			
			Entity e = new Entity(SIZE);
			e.setPosition(-200,-100);
			snake.add(e);
			
		}
		
		if (rock.isCollsion(head)){
			score--;
			setRock();
			
			Entity e = new Entity(SIZE);
			e.setPosition(-200,-100);
			snake.remove(snake.size() - 1);;
			
		}
		
		if(head.getX() < 0) head.setX(WIDTH);
		if(head.getY() < 0) head.setY(HEIGHT);
		if(head.getX()> WIDTH) head.setX(0);
		if(head.getY()> HEIGHT) head.setY(0);

	}
	
	//set up the color of entity
	public void render(Graphics2D g2d){
		g2d.clearRect(0, 0, WIDTH, HEIGHT);
		g2d.setColor(Color.GREEN);
		for(Entity e : snake){
			e.render(g2d);
		}
			g2d.setColor(Color.RED);
			apple.render(g2d);
			g2d.setColor(Color.YELLOW);
			rock.render(g2d);

			
			g2d.setColor(Color.WHITE);
			g2d.drawString("Score: " + score, 10, 10);

	}
	

}
