package wireworld;

import java.awt.geom.Point2D;

public class Block extends Entity
{
	public double width;
	public double height;
 
	public Block(Model m)
	{
		super(m);
		layer = Entity.LAYER_MAIN;
		width = Tile.WIDTH - 1.0;
		height = Tile.HEIGHT - 1.0;
		mass = Tile.MASS;
	}
 
	public String getType()
	{
		return "Block";
	}
 
	public Point2D.Double[] getCorners()
	{
		Point2D.Double[] corners = new Point2D.Double[8];
		corners[0] = new Point2D.Double(x - width / 2,y - height / 2.0);
		corners[1] = new Point2D.Double(x + width / 2,y - height / 2.0);
		corners[2] = new Point2D.Double(x + width / 2,y + height / 2.0);
		corners[3] = new Point2D.Double(x - width / 2,y + height / 2.0);
		corners[4] = new Point2D.Double(x,y + height / 2);
		corners[5] = new Point2D.Double(x,y - height / 2);
		corners[6] = new Point2D.Double(x + width / 2,y);
		corners[7] = new Point2D.Double(x - width / 2,y);
		return corners;
	}
 
	public boolean intersects(double px, double py)
	{
		if (px >= x - width / 2 && px <= x + width / 2) {
			if (py >= y - height / 2 && py <= y + height / 2) {
				return true;
			}
		}
		return false;
	}
 
	public boolean inLine(double px, double py, double dx, double dy)
	{    
		if (dx != 0) {
			if (py >= y - height / 2 && py <= y + height / 2) {
				return true;
			}
		}
		if (dy != 0) {       
			if (px >= x - width / 2 && px <= x + width / 2) {
				return true;
			}
		}
		return false;
	}
 
	public double pointInLine(double px, double py, double dx, double dy)
	{
		if (dx < 0) {
			return x + width / 2;
		}
		if (dx > 0) {
			return x - width / 2;
		}
		if (dy > 0) {
			return y - height / 2;
		}
		if (dy < 0) {
			return y + height / 2;
		}
		return 0;
	}
}