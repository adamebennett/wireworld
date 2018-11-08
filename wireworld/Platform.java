package wireworld;

import java.awt.geom.Point2D;

public class Platform extends Entity
{
	public double width;
	public double height;
 
	public Platform(Model m, double xs, double ys)
	{
		super(m);
		xSpeed = xs;
		ySpeed = ys;
		width = Tile.WIDTH * 2.0 - 1;
		height = Tile.HEIGHT - 1;
		mass = 2.1;
		layer = Entity.LAYER_MAIN;
	}
 
	public String getType()
	{
		return "Platform";
	}
 
	public Point2D.Double[] getCorners()
	{
		Point2D.Double[] corners = new Point2D.Double[8];
		corners[0] = new Point2D.Double(x - width / 2,y - height / 2);
		corners[1] = new Point2D.Double(x + width / 2,y - height / 2);
		corners[2] = new Point2D.Double(x + width / 2,y + height / 2 - 1);
		corners[3] = new Point2D.Double(x - width / 2,y + height / 2 - 1);
		corners[4] = new Point2D.Double(x,y - height / 2);
		corners[5] = new Point2D.Double(x,y - height / 2);
		corners[6] = new Point2D.Double(x + width / 2,y);
		corners[7] = new Point2D.Double(x - width / 2,y);
		return corners;
	}
 
	public void gravity()
	{
	}
 
	public void move()
	{
		if (!moveEntities((ySpeed > 0))) return;
		if (!detectBoundaries(xSpeed, 0)) {
			x += xSpeed;
		}
		else {
			xSpeed = -xSpeed;
		}
		if (!detectBoundaries(0, ySpeed)) {
			y += ySpeed;
		}
		else {
			ySpeed = -ySpeed;
		}
	}
	
	public boolean moveEntities(boolean movedDown)
	{
		double verticalAllowance = 0;
		if (movedDown) verticalAllowance = Math.abs(ySpeed);
		for (Entity e: model.entities) {
			if (e != this && e.mass < mass) {
				if (ySpeed != 0 && (e.isAdjacent(this, 0, Entity.ADJACENT_DISTANCE + verticalAllowance)
				|| e.isAdjacent(this, 0, -Entity.ADJACENT_DISTANCE))) {
					double oldY = y;
					if (ySpeed > 0 && !detectBoundaries(0, ySpeed)) {
						y += ySpeed;
					}
					if (!e.detectBoundaries(0, ySpeed)) {
						e.y += ySpeed;
					}
					else {
						y = oldY;
						ySpeed = -ySpeed;
						return false;
					}
					y = oldY;
				}
				if (xSpeed != 0 && (e.isAdjacent(this, 0, Entity.ADJACENT_DISTANCE))
				|| (xSpeed > 0 && e.isAdjacent(this, -Entity.ADJACENT_DISTANCE, 0))
				|| (xSpeed < 0 && e.isAdjacent(this, Entity.ADJACENT_DISTANCE, 0))) {
					if (!e.detectBoundaries(xSpeed, 0)) {
						e.x += xSpeed;
					}
				}
			}
		}
		return true;
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
	
	public void interactWithMap()
	{
	}
}