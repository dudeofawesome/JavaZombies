/**
 * @author Louis Orleans
 * @date 10 September 2012
 * @homepage http://orleans.pl/spaceZombies
*/

import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class frame extends JFrame implements KeyListener, ActionListener, WindowListener{
	//constants
	private final double VERSION = 0.44;

	private final int EXPLOSION = 0;
	private final int SMOKE = 1;
	private final int FIRE = 2;
	private final int EXHAUST = 3;
	private final int POWERUPDUST = 4;

	private final int LASER = 0;
	private final int HEALTH = 1;
	private final int NUKE = 2;
	private final int TWOSHOT = 3;
	private final int SHOTGUN = 4;
	private final int SHIELD = 5;

	private static int FREQUENCYOFNEWCHARS = 30;
	private final static int DELAY = 33;
	private static int MOUSEXDISPLACEMENT = 0;
	private static int MOUSEYDISPLACEMENT = 0;
	private final static int MARGINFORERROR = 3;

	private static LinkedList<character> characters = new LinkedList<character>();	
	private static LinkedList<bullet> bullets = new LinkedList<bullet>();
	private static LinkedList<particle> particles = new LinkedList<particle>();
	private static LinkedList<powerup> powerups = new LinkedList<powerup>();
	private boolean gameOver = true;
	private boolean gamePaused = false;//true;
	private int totalScore = 0;
	private int addNewCharacterCounter = FREQUENCYOFNEWCHARS;
	private int smokeCounter = 0;
	private Laser laser = new Laser(-10,-10,-11,-11);
	private int twoshotLife = 200;
	private int twoshotAlive = 0;
	private int shotgunLife = 200;
	private int shotgunAlive = 0;
	private int shieldLife = 400;
	private int shieldAlive = 0;

	private TimerTask task = new TimerTask() {
		public void run() {
			if(gamePaused != true){
				if(gameOver != true){
					movePlayer();
					moveEnemies();
					testCollision();
					addNewCharacterCounter -= 1;
					if(addNewCharacterCounter <= 0){
						addEnemy();
						addNewCharacterCounter = FREQUENCYOFNEWCHARS;
					}
				}
				moveBullets();
				moveParticles();
				movePowerups();
				repaint();
			}
			else{
				repaint();
			}
		}
	};

	//interface stuffs
	private static JPanel panel = new JPanel();
	private static JButton btnStart = new JButton("Start");

	private static Action enterAction;

	public frame(){
		super();
		//btnStart.addActionListener(this);
		// frame f = new frame();
		// f.addActionListener(this);
	}
	
	public void movePlayer(){
		//move your dot based on its assigned velocity
		// characters.get(0).move(MOUSEXDISPLACEMENT,MOUSEYDISPLACEMENT);
		characters.get(0).move(100,100);
		
		if(smokeCounter >= 2){

			particles.add(new particle(EXHAUST,characters.get(0).x,characters.get(0).y,1));

			if(characters.get(0).health <= 75){
				particles.add(new particle(SMOKE,characters.get(0).x,characters.get(0).y));
			}
			if(characters.get(0).health <= 50){
				particles.add(new particle(SMOKE,characters.get(0).x,characters.get(0).y));
				particles.add(new particle(SMOKE,characters.get(0).x,characters.get(0).y));
			}
			if(characters.get(0).health <= 25){
				particles.add(new particle(SMOKE,characters.get(0).x,characters.get(0).y));
				particles.add(new particle(SMOKE,characters.get(0).x,characters.get(0).y));
				particles.add(new particle(SMOKE,characters.get(0).x,characters.get(0).y));
				particles.add(new particle(FIRE,characters.get(0).x,characters.get(0).y));
				particles.add(new particle(FIRE,characters.get(0).x,characters.get(0).y));
			}
			smokeCounter = 0;
		}
		else{
			smokeCounter ++;
		}
	}
	
	public void moveEnemies(){
		//move the other entities towards your position
		for(int i = 1;i < characters.size();i++){
			characters.get(i).move(characters.get(0).x,characters.get(0).y);
			for(int j = 1;j < characters.size();j++){
			 	if(i != j && circleCircleCollision(characters.get(i).x,characters.get(i).y,characters.get(i).diameter,characters.get(j).x,characters.get(j).y,characters.get(j).diameter,-3)){
			 		characters.get(i).moveBack();
			 	}
			}
		}
	}

	public void moveBullets(){
		for(int i = 1;i < bullets.size();i++){
			//changes position
			bullets.get(i).move();
		}
	}

	public void moveParticles(){
		int i = 0;
		while(i < particles.size()){
			if(particles.get(i).move()){
				i++;
			}
			else{
				particles.remove(i);
			}
		}
	}

	public void movePowerups(){
		int i = 0;
		while(i < powerups.size()){
			if(Math.floor(Math.random() * 5) == 2){
				particles.add(new particle(POWERUPDUST,powerups.get(i).x,powerups.get(i).y));
			}
			if(powerups.get(i).move()){
				i++;
			}
			else{
				powerups.remove(i);
			}
		}
	}

	public void testCollision(){
		//test for enemies touching you
		for(int i = 1;i < characters.size();i++){
			if(circleCircleCollision(characters.get(0).x,characters.get(0).y,characters.get(0).diameter,characters.get(i).x,characters.get(i).y,characters.get(i).diameter,0)){
				boolean shieldActive = false;
				for(int j = 0;j < characters.get(0).collectedPowerups.size();j++){
					if(characters.get(0).collectedPowerups.get(j) == SHIELD){
						shieldActive = true;
					}
				}
				if(shieldActive){
					int numOfParts = (int) (Math.random() * 20 + 20);
					for(int k = 0; k < numOfParts;k++){
						particles.add(new particle(EXPLOSION,characters.get(i).x,characters.get(i).y));
					}
					characters.remove(i);
					//@debug
					new AePlayWave(getClass().getResource("/sounds/enemyExplode.wav").toString().split(":")[1]).start();
				}
				else{
					characters.get(0).health -= 25;
					int numOfParts = (int) (Math.random() * 20 + 20);
					for(int k = 0; k < numOfParts;k++){
						particles.add(new particle(EXPLOSION,characters.get(i).x,characters.get(i).y));
					}
					characters.remove(i);
					if(characters.get(0).health <= 0){
						//game over
						gameOver = true;

						btnStart.setVisible(true);

						numOfParts = (int) (Math.random() * 100 + 1000);
						for(int k = 0; k < numOfParts;k++){
							particles.add(new particle(EXPLOSION,characters.get(0).x,characters.get(0).y,10));
						}

						characters.remove(0);

						//@debug
						new AePlayWave(getClass().getClassLoader().getResourceAsStream("/sounds/youExplode.wav").toString().split(":")[1]).start();
					}
				}
			}
		}

		//test for you collecting powerups
		int i = 0;
		while(i < powerups.size()){
			if(circleCircleCollision(characters.get(0).x,characters.get(0).y,characters.get(0).diameter,powerups.get(i).x,powerups.get(i).y,powerups.get(i).diameter,MARGINFORERROR)){
				if(powerups.get(i).type == HEALTH){
					characters.get(0).health += 25;
				}
				else if(powerups.get(i).type == NUKE){
					totalScore += characters.size() * 100;
					character player = characters.get(0);
					characters.clear();
					//@debug
					new AePlayWave(getClass().getResource("/sounds/enemyExplode.wav").toString().split(":")[1]).start();

					characters.add(player);
					int numOfParts = (int) (Math.random() * 100 + 400);
					for(int k = 0; k < numOfParts;k++){
						particles.add(new particle(EXPLOSION,characters.get(0).x,characters.get(0).y,50));
					}
				}
				else{
					int numberOfPowerups = characters.get(0).collectedPowerups.size();
					boolean powerupNotTaken = true;
					for(int j = 0;j < characters.get(0).collectedPowerups.size();j++){
						if(characters.get(0).collectedPowerups.get(j) == powerups.get(i).type){
							powerupNotTaken = false;
						}
					}
					if(powerupNotTaken){
						characters.get(0).collectedPowerups.add(powerups.get(i).type);
					}
					else{
						switch(powerups.get(i).type){
							case SHIELD:
								shieldAlive = 0;
							break;
							case SHOTGUN:
								shotgunAlive = 0;
							break;
							case TWOSHOT:
								twoshotAlive = 0;
							break;
							case LASER:
								laser.alive = 0;
							break;
						}
					}
				}

				int numOfParts = (int) (Math.random() * 10 + 35);
				for(int j = 0; j < numOfParts;j++){
					particles.add(new particle(EXPLOSION,powerups.get(i).x,powerups.get(i).y,10));
				}

				powerups.remove(i);

				totalScore += 300;
			}
			else{
				i++;
			}
		}

		//test for bullets touching enemies
		i = 0;
		while(i < bullets.size()){// - 1){
			boolean increase = true;
			int j = 1;
			while(j < characters.size()){
				if(j < characters.size() && i < bullets.size() && circleCircleCollision(bullets.get(i).x,bullets.get(i).y,bullets.get(i).diameter,characters.get(j).x,characters.get(j).y,characters.get(j).diameter,MARGINFORERROR)){
					//make particles
					int numOfParts = (int) (Math.random() * 20 + 20);
					for(int k = 0; k < numOfParts;k++){
						particles.add(new particle(EXPLOSION,characters.get(j).x,characters.get(j).y));
					}

					int randomNumber = (int) (Math.random() * 35);
					if(randomNumber == 3){
					//@debug
					// if(0 == 0){
						//add new powerup
						switch((int) (Math.random() * 7)){
							case 0:
								powerups.add(new powerup(LASER,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
							break;
							case 1:
								powerups.add(new powerup(NUKE,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
							break;
							case 2:
								powerups.add(new powerup(TWOSHOT,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
							break;
							case 3:
								powerups.add(new powerup(SHOTGUN,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
							break;
							case 4: case 5:
								powerups.add(new powerup(HEALTH,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
							break;
							case 6:
								powerups.add(new powerup(SHIELD,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
							break;
						}
					}

					//remove bullet and character
					characters.remove(j);
					bullets.remove(i);

					//@debug
					// System.out.println(getClass().getResource("sounds/enemyExplode.wav").toString().split(":")[0] + "..." + getClass().getResource("sounds/enemyExplode.wav").toString().split(":")[1] + "..." + getClass().getResource("sounds/enemyExplode.wav").toString().split(":")[2]);
					if(this.getClass().getResource("/sounds/enemyExplode.wav").toString().split(":")[1].equals("file")){
						new AePlayWave(this.getClass().getResource("/sounds/enemyExplode.wav").toString().split(":")[2]).start();
					}
					else{
						new AePlayWave(this.getClass().getResource("/sounds/enemyExplode.wav").toString().split(":")[1]).start();
					}

					increase = false;

					//give 100 points for killing enemy
					totalScore += 100;

					//add an enemy
					if(laser.alive == 0 && twoshotAlive == 0 && shotgunAlive == 0){
						addEnemy();
					}
				}
				else{
					//i++;
					j++;
				}
			}
			if(increase == true){
				i++;
			}
		}

		//test for laser touching enemies
		for(i = 0;i < characters.get(0).collectedPowerups.size();i++){
			if(characters.get(0).collectedPowerups.get(i) == LASER){
				int j = 1;
				while(j < characters.size()){
					if(circleLineCollision(characters.get(j).x,characters.get(j).y,characters.get(j).diameter,laser.x1,laser.y1,laser.x2,laser.y2,MARGINFORERROR)){
						int numOfParts = (int) (Math.random() * 20 + 20);
						for(int k = 0; k < numOfParts;k++){
							particles.add(new particle(EXPLOSION,characters.get(j).x,characters.get(j).y));
						}

						//remove bullet and character
						characters.remove(j);

						//@debug
						new AePlayWave(getClass().getResource("/sounds/enemyExplode.wav").toString().split(":")[1]).start();

						//give 150 points for killing enemy
						totalScore += 150;
					}
					else{
						j++;
					}
				}
			}
		}

		//test to make sure bullet is still on screen
		for(i = 0;i < bullets.size();i++){
			if(bullets.get(i).x < 0 || bullets.get(i).x > getWidth() ||
				bullets.get(i).y < 0 || bullets.get(i).y > getHeight()){
				//remove bullet
				bullets.remove(i);
			}
		}
	}

	public boolean circleCircleCollision(int _x1,int _y1,int _diam1,int _x2,int _y2,int _diam2,int _marginForError){
		double _distance = Math.sqrt(Math.pow((_x1 - _diam1 / 2) - (_x2 - _diam2 / 2),2) + Math.pow((_y1 - _diam1 / 2) - (_y2 - _diam2 / 2), 2));
		if(_distance <= (_diam1 / 2) + (_diam2 / 2) + _marginForError){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean circleLineCollision(int _cX,int _cY,int _diam,int _lX1,int _lY1,int _lX2,int _lY2,int _marginForError){
		//turn vectors into component form
		int _lX = _lX2 - _lX1;
		int _lY = _lY2 - _lY1;
		_cX = _cX - _lX1;
		_cY = _cY - _lY1;

		double _CdotL = _cX * _lX + _cY * _lY; //C dot L
		double _magC = Math.hypot(_cX,_cY); //magnitude of C
		double _magL = Math.hypot(_lX,_lY); //magnitude of L
		//make sure that the enemy is in front of player
		//A dot B = mag(A) * mag(B) * cos(Theda)
		//cos(Theda) is positive for angles <= PI/2  ie. enemy in front of player
		if(_CdotL / (_magC * _magL) >= 0){
			//get distance to shortest point on line
			//reference: http://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line
			double _distance = Math.abs(_cX * _lY - _cY * _lX) / _magL;
			if(_distance <= (_diam / 2) + _marginForError){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}

	public void shootBullet(){
		int _mouseX = MouseInfo.getPointerInfo().getLocation().x + MOUSEXDISPLACEMENT, _mouseY = MouseInfo.getPointerInfo().getLocation().y + MOUSEYDISPLACEMENT;
		boolean laserShooting = false;
		boolean twoshot = false;
		boolean shotgun = false;

		for(int i = 0;i < characters.get(0).collectedPowerups.size();i++){
			if(characters.get(0).collectedPowerups.get(i) == LASER){
				laserShooting = true;
			}
			else if(characters.get(0).collectedPowerups.get(i) == TWOSHOT){
				if(twoshotAlive < twoshotLife){
					twoshotAlive += 1;
					twoshot = true;
				}
				else{
					characters.get(0).collectedPowerups.remove(i);
					twoshotAlive = 0;
				}
			}
			else if(characters.get(0).collectedPowerups.get(i) == SHOTGUN){
				if(shotgunAlive < shotgunLife){
					shotgunAlive += 1;
					shotgun = true;
				}
				else{
					characters.get(0).collectedPowerups.remove(i);
					shotgunAlive = 0;
				}
			}
		}
		if(laserShooting){
			if(laser.alive <= laser.life){
				laser.move(characters.get(0).x + characters.get(0).diameter / 2 ,characters.get(0).y + characters.get(0).diameter / 2 ,_mouseX,_mouseY,getWidth(),getHeight());
				laser.alive++;
			}
			else{
				for(int i = 0;i < characters.get(0).collectedPowerups.size();i++){
					if(characters.get(0).collectedPowerups.get(i) == LASER){
						characters.get(0).collectedPowerups.remove(i);
						laser.alive = 0;
					}
				}
			}
		}
		else{
			double _vX,_vY;
			double _angleTo;

			//remove 1 point for using ammo
			totalScore -= 1;

			//get angle to cursor
			_angleTo = Math.atan2((_mouseY - characters.get(0).y),(_mouseX - characters.get(0).x));

			//split it up into x and y
			_vX = Math.cos(_angleTo) * 15;
			_vY = Math.sin(_angleTo) * 15;
			bullets.add(new bullet(characters.get(0).x + characters.get(0).diameter / 2,characters.get(0).y + characters.get(0).diameter / 2,2,_vX,_vY,Color.WHITE));

			if(twoshot){
				for(int i = 0;i < 6;i++){

					_angleTo += Math.PI;

					//split it up into x and y
					_vX = Math.cos(_angleTo) * 15;
					_vY = Math.sin(_angleTo) * 15;
					bullets.add(new bullet(characters.get(0).x + characters.get(0).diameter / 2,characters.get(0).y + characters.get(0).diameter / 2,2,_vX,_vY,Color.WHITE));
				}
			}
			if(shotgun){
				_angleTo -= 3 * Math.PI / 12;
				for(int i = 0;i < 6;i++){

					_angleTo += Math.PI / 12;

					//split it up into x and y
					_vX = Math.cos(_angleTo) * 15;
					_vY = Math.sin(_angleTo) * 15;
					bullets.add(new bullet(characters.get(0).x + characters.get(0).diameter / 2,characters.get(0).y + characters.get(0).diameter / 2,2,_vX,_vY,Color.WHITE));
				}
			}
		}
	}

	public void addEnemy(){
		//find a place to put enemy
		int _width = getWidth();
		int _height = getHeight();
		int _circum = (_width * 2) + (_height * 2);
		int _placeOnEdge = (int) (Math.random() * _circum);
		int _xLoc = 0;
		int _yLoc = 0;
		if(_placeOnEdge > _width){
			if(_placeOnEdge > _width + _height){
				if(_placeOnEdge > _width + _height + _width){
					_yLoc = _height - (_placeOnEdge - _width - _height - _width);
				}
				else{
					_xLoc = _width - (_placeOnEdge - _width - _height);
					_yLoc = _height;
				}
			}
			else{
				_yLoc = _placeOnEdge - _width;
				_xLoc = _width;
			}
		}
		else{
			_xLoc = _placeOnEdge;
		}

		int _diameter = (int) (Math.random() * 5 + 9);
		// int _totalV = (int) ((16 - _diameter) * 0.85);
		int _totalV = (int) ((24 - _diameter) / 3);
		// int _totalV = 3;
		//use constructor from character.java to add in the enemy
		characters.add(new character(_xLoc,_yLoc,_diameter,_totalV,Color.RED));
	}

	public void paint(Graphics g){
		Graphics2D g2D = (Graphics2D) g;

		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D ig = image.createGraphics();

		// Anti-aliasing
		ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//draw particles
		for(int i = 0; i < particles.size(); i++){
			//String _color = particles.get(i).color + "," + particles.get(i).opacity;
			ig.setColor(new Color(particles.get(i).color.getRed(),particles.get(i).color.getGreen(),particles.get(i).color.getBlue(),particles.get(i).opacity));
			// ig.setColor(particles.get(i).color);
			ig.setStroke(new BasicStroke((float) particles.get(i).lineWidth));
			ig.drawArc(particles.get(i).x,particles.get(i).y,particles.get(i).diameter,particles.get(i).diameter,0,360);
		}
		//draw player
		ig.setColor(characters.get(0).color);
		ig.setStroke(new BasicStroke(3F));
		ig.drawArc(characters.get(0).x,characters.get(0).y,characters.get(0).diameter,characters.get(0).diameter,0,360);

		//draw zombies
		for(int i = 1;i < characters.size(); i++){
			ig.setColor(characters.get(i).color);
			ig.setStroke(new BasicStroke(3F));
			ig.drawArc(characters.get(i).x,characters.get(i).y,characters.get(i).diameter,characters.get(i).diameter,0,360);
		}
		//draw bullets
		for(int i = 0;i < bullets.size(); i++){
			ig.setColor(bullets.get(i).color);
			ig.setStroke(new BasicStroke(1F));
			ig.drawArc(bullets.get(i).x,bullets.get(i).y,bullets.get(i).diameter,bullets.get(i).diameter,0,360);
		}
		//draw laser
		for(int i = 0;i < characters.get(0).collectedPowerups.size();i++){
			if(characters.get(0).collectedPowerups.get(i) == LASER){
				ig.setColor(laser.color);
				ig.setStroke(new BasicStroke(laser.width));
				ig.drawLine(laser.x1,laser.y1,laser.x2,laser.y2);
			}
		}
		//draw powerups
		for(int i = 0;i < powerups.size();i++){
			ig.drawImage(powerups.get(i).sprite,powerups.get(i).x - powerups.get(i).diameter / 2,powerups.get(i).y - powerups.get(i).diameter / 2,powerups.get(i).diameter,powerups.get(i).diameter,null);
		}
		//draw shield
		for(int i = 0;i < characters.get(0).collectedPowerups.size();i++){
			if(characters.get(0).collectedPowerups.get(i) == SHIELD){
				if(gamePaused != true && gameOver != true){
					shieldAlive += 1;
				}
				ig.setColor(new Color(18,255,0,70));
				ig.setStroke(new BasicStroke(3F));
				ig.fillOval(characters.get(0).x - characters.get(0).diameter / 2 - 13,characters.get(0).y - characters.get(0).diameter / 2 - 13,50,50);
				ig.setColor(new Color(18,255,0,100));
				ig.drawArc(characters.get(0).x - characters.get(0).diameter / 2 - 13,characters.get(0).y - characters.get(0).diameter / 2 - 13,50,50,0,360 - 360 * shieldAlive / shieldLife);
				if(shieldAlive > shieldLife){
					characters.get(0).collectedPowerups.remove(i);
					shieldAlive = 0;
				}
			}
		}

		//draw score
		ig.setColor(Color.GRAY);
		ig.setFont(new Font("Arial", Font.BOLD, 30));
		ig.drawString(totalScore + "",getWidth() - 200,getHeight() - 50);

		if(gamePaused == false && gameOver == false){
			shootBullet();
		}
		else if(gamePaused == true){
			ig.setColor(Color.WHITE);
			ig.setStroke(new BasicStroke(5F));
			ig.setFont(new Font("Arial", Font.BOLD, 40));
			ig.drawString("Game Paused",80,150);
			ig.setFont(new Font("Arial", Font.BOLD, 20));
			ig.drawString("Version: " + VERSION,80,190);
		}
		else{
			//draw game over message if game over
			ig.setColor(Color.RED);
			ig.setStroke(new BasicStroke(5F));
			ig.setFont(new Font("Arial", Font.BOLD, 40));
			ig.drawString("Game Over",80,150);
			ig.setFont(new Font("Arial", Font.BOLD, 20));
			ig.drawString("Version: " + VERSION,80,190);
			ig.setColor(new Color(200,0,0,200));
			ig.setFont(new Font("Arial", Font.BOLD, 30));
			ig.drawString("Click to Retry",getWidth() / 2 - 85,getHeight() / 2 - 10);

			if(particles.size() == 0){

			}
		}
		// g2D.drawImage(oldImage, 0, 0, this);
		g2D.drawImage(image, 0, 0, this);
	}

	public void keyPressed(KeyEvent e){

		int key = e.getKeyCode();

		if(key == 27){
			if(gamePaused != true && gameOver != true){
				gamePaused = true;
			}
			else{
				gamePaused = false;
			}
		}
	}

	public void keyReleased(KeyEvent e){

	}

	public void keyTyped(KeyEvent e){

	}

	public void windowDeactivated(WindowEvent e){
		if(gameOver == false){
			gamePaused = true;
		}
	}

	public void windowActivated(WindowEvent e){

	}

	public void windowDeiconified(WindowEvent e){

	}

	public void windowIconified(WindowEvent e){
		if(gameOver == false){
			gamePaused = true;
		}
	}

	public void windowClosed(WindowEvent e){

	}

	public void windowClosing(WindowEvent e){

	}

	public void windowOpened(WindowEvent e){

	}

	public void init() {
		TimerTask task = new TimerTask() {
			public void run() {
				if(gamePaused != true){
					if(gameOver != true){
						movePlayer();
						moveEnemies();
						testCollision();
						addNewCharacterCounter -= 1;
						if(addNewCharacterCounter <= 0){
							addEnemy();
							addNewCharacterCounter = FREQUENCYOFNEWCHARS;
						}
					}
					moveBullets();
					moveParticles();
					movePowerups();
					repaint();
				}
				else{
					repaint();
				}
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 0, DELAY);
	}
	
	public static void main(String[] args)
	{
		final frame f = new frame();

		//make player
		characters.add(new character(350,350,15,7,Color.CYAN));

		f.getContentPane().setBackground(Color.BLACK);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.init();
		f.addKeyListener(f);
		f.addWindowListener(f);
		f.setSize(700,700);
		f.setVisible(true);

		f.getContentPane().addMouseListener(new MouseAdapter(){
			@Override
			public void mouseReleased(MouseEvent e){
				// frame f1 = new frame();
				f.restartGame();
			}
			public void mouseMoved(MouseEvent e){
				MOUSEXDISPLACEMENT = e.getX();
				MOUSEYDISPLACEMENT = e.getY();
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		if(gameOver){
			requestFocus();

			// btnStart.setVisible(false);
			// btnStart.invalidate();

			//reset game
			characters.clear();
			characters.add(new character(350,350,15,7,Color.CYAN));

			bullets.clear();
			particles.clear();
			powerups.clear();

			gameOver = false;
			gamePaused = false;
			totalScore = 0;
			addNewCharacterCounter = FREQUENCYOFNEWCHARS;
			smokeCounter = 0;
			twoshotLife = 200;
			twoshotAlive = 0;
			shotgunLife = 200;
			shotgunAlive = 0;
			shieldLife = 400;
			shieldAlive = 0;

			Timer timer = new Timer();
			timer.schedule(task, 0, DELAY);
		}
	}

	public void restartGame(){
		if(gameOver){
			requestFocus();
			// btnStart.setVisible(false);
			// btnStart.invalidate();
			//reset game
			characters.clear();
			characters.add(new character(350,350,15,7,Color.CYAN));
			bullets.clear();
			particles.clear();
			powerups.clear();
			gameOver = false;
			gamePaused = false;
			totalScore = 0;
			addNewCharacterCounter = FREQUENCYOFNEWCHARS;
			smokeCounter = 0;
			twoshotLife = 200;
			twoshotAlive = 0;
			shotgunLife = 200;
			shotgunAlive = 0;
			shieldLife = 400;
			shieldAlive = 0;
		}
		else if(gamePaused){
			gamePaused = false;
		}
	}
}