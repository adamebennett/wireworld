package wireworld;

import java.awt.geom.Point2D;
import java.util.Random;

public class Lava extends Block
{
	Tile source;
	
	public Lava(Model m, double d, Tile t)
	{
		super(m);
		mass = d;
		width = Tile.WIDTH * mass - 1;
		height = Tile.HEIGHT * mass - 1;
		layer = Entity.LAYER_CLUTTER;
		source = t;
	}
 
	public String getType()
	{
		return "Lava";
	}
	
	public void interactWithMap()
	{
		Point2D.Double[] corners = getCorners();
		for (int i = 0; i < corners.length; ++i) {
			Tile tile = model.map.getTile(corners[i].x, corners[i].y + Entity.ADJACENT_DISTANCE);
			if (tile.solid) {
				Tile t = model.map.getTile(corners[i].x, corners[i].y);
				if (t.canChange && !t.solid && t != source && t.getID() != Tile.LAVA_ID) {
					Tile lavaTile = new BasicTile(Tile.LAVA_ID, false);
					model.map.replaceTile(corners[i].x, corners[i].y, lavaTile);
					//lavaTile.level = mass;
					mass = 0;
					model.removeEntity(this);
					return;
				}
			}
			/*if (tile.getID() == Tile.LAVA_ID && tile.level >= 1.0) {
				Tile t = model.map.getTile(corners[i].x, corners[i].y);
				if (t.canChange && !t.solid && t != source && t.getID() != Tile.LAVA_ID) {
					Tile lavaTile = new BasicTile(Tile.LAVA_ID, false);
					model.map.replaceTile(corners[i].x, corners[i].y, lavaTile);
					//lavaTile.level = mass;
					mass = 0;
					model.removeEntity(this);
					return;
				}
				else {
					if (ySpeed > -4) {
						ySpeed -= Entity.GRAVITY_ACCELERATION;
					}
					else {
						ySpeed = -4;
					}
					if (xSpeed == 0) {
						xSpeed = new Random().nextDouble() * 0.5 - 0.25;
					}
				}
			}
			tile = model.map.getTile(corners[i].x, corners[i].y);
			if (tile.getID() == Tile.LAVA_ID && tile != source && tile.canChange) {
				if (tile.canChange && !tile.solid && tile != source) {
					if (tile.level + mass <= 1.0) {
						//tile.level += mass;
						model.removeEntity(this);
						return;
					}
					else {
						mass -= 1.0 - tile.level;
						//tile.level = 1.0;
					}
				}
			}*/
		}
	}
}