/**
 * @author Louis Orleans
 * @date 10 September 2012
*/

import java.awt.Color;

public class particle{

	private final int EXPLOSION = 0;
	private final int SMOKE = 1;
	private final int FIRE = 2;
	private final int EXHAUST = 3;
	private final int POWERUPDUST = 4;

	public int x = 0;
	public int y = 0;
	public double dir = 0;
	public double vel = 1;
	public int life = 100;
	public int alive = 0;
	public int diameter = 3;
	public int lineWidth = 3;
	public int type = EXPLOSION;
	public double randomness = .1;
	public int opacity = 255;
	public Color color = Color.RED;
	public Color[] colors = {new Color(209,0,0),new Color(219,29,0),new Color(222,52,4),new Color(232,99,5),new Color(240,138,5),new Color(252,179,20),new Color(255,213,25),new Color(255,255,46),new Color(255,255,102),new Color(255,255,176)};

	particle(int _type,int _x,int _y){
		//not type dependent settings
		x = _x;
		y = _y;

		type = _type;

		//type dependent settings
		if(type == EXPLOSION){
			color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 3 + .3;
			diameter = (int) (Math.random() * 5 + 1);
			lineWidth = diameter;
			opacity = (int) (Math.random() * 100 + 140);
		}
		else if(type == SMOKE){
			colors[0] = new Color(12,12,12);
			colors[1] = new Color(20,20,20);
			colors[2] = new Color(25,25,25);
			colors[3] = new Color(30,30,30);
			colors[4] = new Color(41,41,41);
			colors[5] = new Color(60,60,60);
			colors[6] = new Color(80,80,80);
			colors[7] = new Color(100,100,100);
			colors[8] = new Color(120,120,120);
			colors[9] = new Color(148,148,148);
			color = colors[(int) (Math.random() * 9)];
			// color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 4.0 + 1;
			diameter = (int) (Math.random() * 10 + 1);
			lineWidth = diameter;
			opacity = (int) (Math.random() * 20 + 100);
		}
		else if(type == FIRE){
			colors[0] = new Color(214,39,0);
			colors[1] = new Color(242,44,0);
			colors[2] = new Color(242,69,0);
			colors[3] = new Color(255,106,0);
			colors[4] = new Color(255,157,0);
			colors[5] = new Color(255,174,0);
			colors[6] = new Color(255,179,0);
			colors[7] = new Color(255,236,28);
			colors[8] = new Color(255,245,135);
			colors[9] = new Color(255,251,204);
			// color = colors[(int) (Math.random() * 9)];
			color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 4.0 + .1;
			diameter = (int) (Math.random() * 4 + 1);
			lineWidth = diameter;
			opacity = (int) (Math.random() * 50 + 150);
		}
		else if(type == EXHAUST){
			colors[0] = new Color(55,0,100);
			colors[1] = new Color(40,0,75);
			colors[2] = new Color(33,0,60);
			colors[3] = new Color(23,0,41);
			colors[4] = new Color(16,0,29);
			color = colors[(int) (Math.random() * 5)];
			// color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 4.0 + .1;
			diameter = (int) (Math.random() * 3 + 1);
			alive = 70;
			lineWidth = diameter;
			opacity = (int) (Math.random() * 8 + 93);
		}
		else if(type == POWERUPDUST){
			colors[0] = new Color(55,0,100);
			colors[1] = new Color(40,0,75);
			colors[2] = new Color(33,0,60);
			colors[3] = new Color(23,0,41);
			colors[4] = new Color(16,0,29);
			color = colors[(int) (Math.random() * 5)];
			// color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 4.0 + .1;
			diameter = (int) (Math.random() * 5 + 3);
			alive = 70;
			lineWidth = diameter;
			opacity = (int) (Math.random() * 50 + 150);
		}
	}

	particle(int _type,int _x,int _y,int _vel){
		//not type dependent settings
		x = _x;
		y = _y;

		type = _type;

		//type dependent settings
		if(type == EXPLOSION){
			color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 5 + _vel;
			diameter = (int) (Math.random() * 5 + 1);
			lineWidth = diameter;
			opacity = (int) (Math.random() * 100 + 140);
		}
		else if(type == SMOKE){
			colors[0] = new Color(12,12,12);
			colors[1] = new Color(20,20,20);
			colors[2] = new Color(25,25,25);
			colors[3] = new Color(30,30,30);
			colors[4] = new Color(41,41,41);
			colors[5] = new Color(60,60,60);
			colors[6] = new Color(80,80,80);
			colors[7] = new Color(100,100,100);
			colors[8] = new Color(120,120,120);
			colors[9] = new Color(148,148,148);
			// color = colors[(int) (Math.random() * 9)];
			color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 5 + _vel;
			diameter = (int) (Math.random() * 10 + 1);
			lineWidth = diameter;
			opacity = (int) (Math.random() * 20 + 100);
		}
		else if(type == FIRE){
			colors[0] = new Color(214,39,0);
			colors[1] = new Color(242,44,0);
			colors[2] = new Color(242,69,0);
			colors[3] = new Color(255,106,0);
			colors[4] = new Color(255,157,0);
			colors[5] = new Color(255,174,0);
			colors[6] = new Color(255,179,0);
			colors[7] = new Color(255,236,28);
			colors[8] = new Color(255,245,135);
			colors[9] = new Color(255,251,204);
			// color = colors[(int) (Math.random() * 9)];
			color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 5.0 + _vel;
			diameter = (int) (Math.random() * 4 + 1);
			lineWidth = diameter;
			opacity = (int) (Math.random() * 50 + 150);
		}
		else if(type == EXHAUST){
			colors[0] = new Color(55,0,100);
			colors[1] = new Color(40,0,75);
			colors[2] = new Color(33,0,60);
			colors[3] = new Color(23,0,41);
			colors[4] = new Color(16,0,29);
			color = colors[(int) (Math.random() * 5)];
			// color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 2.0 + _vel;
			diameter = (int) (Math.random() * 3 + 1);
			alive = 70;
			lineWidth = diameter;
			opacity = (int) (Math.random() * 8 + 93);
		}
		else if(type == POWERUPDUST){
			colors[0] = new Color(55,0,100);
			colors[1] = new Color(40,0,75);
			colors[2] = new Color(33,0,60);
			colors[3] = new Color(23,0,41);
			colors[4] = new Color(16,0,29);
			color = colors[(int) (Math.random() * 5)];
			// color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 2.0 + _vel;
			diameter = (int) (Math.random() * 5 + 3);
			alive = 70;
			lineWidth = diameter;
			opacity = (int) (Math.random() * 50 + 150);
		}
	}

	public boolean move(){
		if(alive > life){
			return false;
		}
		else{
			int _dX = (int) (Math.cos(dir) * vel);
			int _dY = (int) (Math.sin(dir) * vel);
			x += _dX;
			y += _dY;
			dir += Math.random() * 0.6 - 0.3;
			vel *= 0.99;
			if(opacity - 1 > 0){
				opacity --;
			}
			if(type == EXPLOSION || type == FIRE){
				switch(alive){
					case 10:
						color = colors[8];
					break;
					case 20:
						color = colors[7];
					break;
					case 30:
						color = colors[6];
						if(lineWidth > 1){
							lineWidth -= 1;
						}
					break;
					case 40:
						color = colors[5];
					break;
					case 50:
						color = colors[4];
					break;
					case 60:
						color = colors[3];
						if(lineWidth > 1){
							lineWidth -= 1;
						}
					break;
					case 70:
						color = colors[2];
					break;
					case 80:
						color = colors[1];
					break;
					case 90:
						color = colors[0];
						if(lineWidth > 1){
							lineWidth -= 1;
						}
					break;
				}
			}
			else{
				switch(alive){
					case 30:
						if(lineWidth > 1){
							lineWidth -= 1;
						}
					break;
					case 60:
						if(lineWidth > 1){
							lineWidth -= 1;
						}
					break;
					case 90:
						if(lineWidth > 1){
							lineWidth -= 1;
						}
					break;
				}
			}

			alive ++;
			return true;
		}
	}
}