package wireworld;

import java.awt.geom.Point2D;

public class Fallspike extends Block
{
	boolean triggered;
 
	public Fallspike(Model m)
	{
		super(m);
		triggered = false;
		layer = Entity.LAYER_MAIN;
	}
 
	public String getType()
	{
		return "Fallspike";
	}
 
	public void update()
	{
		super.update();
		triggerFall();
	}
 
	public void gravity()
	{
		if (triggered) {
			if (!detectBoundaries(0, ySpeed + Entity.GRAVITY_ACCELERATION)) {
				ySpeed += Entity.GRAVITY_ACCELERATION;
				if (ySpeed > yTermSpeed) {
					ySpeed = yTermSpeed;
				}
			}
		}
	}
	
	public void triggerFall()
	{
		for (Entity e: model.entities) {
			if (e.getType().equals("Character") && e.inLine(x, y, 0, 1)) {
				double triggerY = y;
				Tile tile = model.map.getTile(x, triggerY);
				while (tile.solid == false) {
					triggerY += Tile.HEIGHT;
					tile = model.map.getTile(x, triggerY);
					if (e.y <= triggerY) {
						triggered = true;
						return;
					}
				}
			}
		}
	}
}