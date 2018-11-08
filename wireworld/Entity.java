package wireworld;

import java.awt.geom.Point2D;

public abstract class Entity
{
	public static final int GRAVITY_ACCELERATION = 1;
	public static final double ADJACENT_DISTANCE = 4;
	
	public static final int LAYER_NO_COLLISIONS = -1;
	public static final int LAYER_MAIN = 0;
	public static final int LAYER_CLUTTER = 1;

	public Model model;
	public int layer;
	public double x;
	public double y;
	public double xSpeed;
	public double ySpeed;
	public double xTermSpeed;
	public double yTermSpeed;
	public double mass;
 
	public Entity(Model m)
	{
		model = m;
		xTermSpeed = 25;
		yTermSpeed = 25;
		layer = Entity.LAYER_MAIN;
	}
 
	public abstract String getType();
 
	public void update()
	{
		interactWithMap();
		gravity();
		move();
	}
 
	public Point2D.Double[] getCorners()
	{
		Point2D.Double[] corners = new Point2D.Double[1];
		corners[0] = new Point2D.Double(x,y);
		return corners;
	}
 
	public void gravity()
	{
		if (!detectBoundaries(0, ySpeed + Entity.GRAVITY_ACCELERATION)) {
			ySpeed += Entity.GRAVITY_ACCELERATION;
			if (ySpeed > yTermSpeed) {
				ySpeed = yTermSpeed;
			}
		}
	}
 
	public void move()
	{
		double xDir = 1.0;
		if (xSpeed < 0) xDir = -1.0;
		double oldXSpeed = xSpeed;
		double remainingXSpeed = Math.abs(oldXSpeed);
		while (remainingXSpeed > 0) {
			xSpeed = Math.min(Tile.WIDTH, remainingXSpeed) * xDir;
			remainingXSpeed -= Tile.WIDTH;
			if (!detectBoundaries(xSpeed, 0)) {
				x += xSpeed;
			}
			else {
				if (xSpeed != 0) {
					double dist = getRemainingDistance(xSpeed, 0);
					if (Math.abs(dist) <= Math.abs(xSpeed)) {
						x += dist;
					}
					oldXSpeed = 0;
				}
			}
		}
		xSpeed = oldXSpeed;
		
		double yDir = 1.0;
		if (ySpeed < 0) yDir = -1.0;
		double oldYSpeed = ySpeed;
		double remainingYSpeed = Math.abs(oldYSpeed);
		while (remainingYSpeed > 0) {
			ySpeed = Math.min(Tile.HEIGHT, remainingYSpeed) * yDir;
			remainingYSpeed -= Tile.HEIGHT;
			if (!detectBoundaries(0, ySpeed)) {
				y += ySpeed;
			}
			else {
				if (ySpeed != 0) {
					double dist = getRemainingDistance(0, ySpeed);
					if (Math.abs(dist) <= Math.abs(ySpeed)) {
						y += dist;
					}
					oldYSpeed = 0;
				}
			}
		}
		ySpeed = oldYSpeed;
	}
	
	public boolean isSolidTo(Entity e) {
		if (this == e) {
			return false;
		}
		if (layer == Entity.LAYER_NO_COLLISIONS) {
			return false;
		}
		return (layer == e.layer);
	}
 
	public boolean detectBoundaries(double xs, double ys)
	{
		Point2D.Double[] corners = getCorners();
		for (int i = 0; i < corners.length; ++i) {
			if (model.isSolid(this, corners[i].x + xs, corners[i].y + ys)) {
				return true;
			}
		}
		return false;
	}
 
	public double getRemainingDistance(double xs, double ys)
	{
		Point2D.Double[] corners = getCorners();
		double[] distances = new double[corners.length];
		for (int i = 0; i < corners.length; ++i) {
			distances[i] = model.distanceToObstacle(this, corners[i].x, corners[i].y, xs, ys);
		}
		double distance = distances[0];
		for (int i = 1; i < distances.length; ++i) {
			if (Math.abs(distances[i]) < Math.abs(distance)) {
				distance = distances[i];
			}
		}
		if (ys > 0) {
			distance -= 1;
		}
		if (ys < 0) {
			distance += 1;
		}
		if (xs > 0) {
			distance -= 3;
		}
		if (xs < 0) {
			distance += 3;
		}
		return distance;
	}
 
	public boolean intersects(double px, double py)
	{
		return (px == x && py == y);
	}
 
	public boolean inLine(double px, double py, double dx, double dy)
	{
		if (dx != 0) {
			if (py == y) {
				return true;
			}
		}
		if (dy != 0) {       
			if (px == x) {
				return true;
			}
		}
		return false;
	}
 
	public double pointInLine(double px, double py, double dx, double dy)
	{
		if (dx != 0) {
			return x;
		}
		if (dy != 0) {
			return y;
		}
		return 0;
	}
 
	public double distance(double px, double py, double dx, double dy)
	{
		if (dx < 0) {
			return -Math.abs(px - pointInLine(px,py,dx,dy));
		}
		if (dx > 0) {
			return Math.abs(px - pointInLine(px,py,dx,dy));
		}
		if (dy < 0) {
			return -Math.abs(py - pointInLine(px,py,dx,dy));
		}
		if (dy > 0) {
			return Math.abs(py - pointInLine(px,py,dx,dy));
		}
		return 0;
	}
	
	public boolean isAdjacent(Entity e, double dx, double dy)
	{
		Point2D.Double[] corners = getCorners();
		for (int i = 0; i < corners.length; ++i) {
			if (e.intersects(corners[i].x + dx, corners[i].y + dy)) {
				return true;
			}
		}
		return false;
	}
	
	public void interactWithMap()
	{
		Point2D.Double[] corners = getCorners();
		for (int i = 0; i < corners.length; ++i) {
			Tile tile = model.map.getTile(corners[i].x, corners[i].y + Entity.ADJACENT_DISTANCE);
			if (tile.getID() == Tile.SPRING_ID) {
				ySpeed = Tile.SPRING_SPEED;
				if (Math.abs(ySpeed) > Math.abs(yTermSpeed)) ySpeed = -yTermSpeed;
			}
			if (tile.getID() == Tile.BREAK_TILE_ID) {
				if (mass >= Tile.MASS) {
					double triggerY = corners[i].y + Entity.ADJACENT_DISTANCE;				
					tile = model.map.getTile(corners[i].x, triggerY);
					while (tile.getID() == Tile.BREAK_TILE_ID) {		
						if (tile.canChange) {
							tile = new BasicTile(Tile.AIR_TILE_ID, false);
							model.map.replaceTile(corners[i].x, triggerY, tile);
							for (int a = 0; a < 2; ++a) {
								for (int b = 0; b < 2; ++b) {
									Block block = new Block(model);
									block.layer = Entity.LAYER_CLUTTER;
									block.width = Tile.WIDTH / 2.0;
									block.height = Tile.HEIGHT / 2.0;
									block.mass = 0.25;
									block.x = ((Math.floor(corners[i].x / Tile.WIDTH) * Tile.WIDTH) + block.width / 2.0) + block.width * a;
									block.y = ((Math.floor(triggerY / Tile.HEIGHT) * Tile.HEIGHT) + block.height / 2.0) + block.height * b;
									block.width -= 1.0;
									block.height -= 1.0;
									model.entities.add(block);			
								}
							}		
						}
						else {
							break;
						}
						triggerY += Tile.HEIGHT;
						tile = model.map.getTile(corners[i].x, triggerY);
					}
				}
			}
		}
	}
}