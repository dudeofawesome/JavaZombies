/**
 * @author Louis Orleans
 * @date 10 September 2012
*/

import java.awt.Color;

public class bullet {
	public int x,y,diameter;
	public double vX,vY;
	public Color color;
	bullet(int _xLoc,int _yLoc,int _diam,double _vX,double _vY,Color _color){
		x = _xLoc;
		y = _yLoc;
		diameter = _diam;
		vX = _vX;
		vY = _vY;
		color = _color;
	}
	
	public void move(){
		x += vX;
		y += vY;
	}
}
