package wireworld;

import java.util.*;

public class Model
{
	public HashSet<String> keys; // keyboard keys  
	public String mapName;
	public Map map;
	public Player player;
	public ArrayList<Entity> entities;
	public ArrayList<String> maps;
	    
	public Model(String m)
	{
		keys = new HashSet<String>();
		player = new Player(this);
		entities = new ArrayList<Entity>();
		entities.add(player);
		maps = new ArrayList<String>();
		mapName = m;
		map = new Map(this, mapName);
	}
	 
	public void update()
	{
		if (!player.paused) {
			map.update();
		}
		player.control(keys);
		if (!player.paused) {
			for (int i = 0; i < entities.size(); ++i) {
				if (entities.get(i) != player) {
					entities.get(i).update();
				}
			}
			player.update();
		}
	}
 
	public boolean isSolid(Entity caller, double x, double y)
	{
		if (map.isSolid(x,y))
		{
			return true;
		}
		for (Entity e: entities) {
			if (e != caller && e.isSolidTo(caller)) {
				if (e.intersects(x,y)) {
					return true;
				}
			}
		}
		return false;
	}
 
	public double distanceToObstacle(Entity caller, double x, double y, double xs, double ys)
	{
		double distance = map.distanceToObstacle(x,y,xs,ys);
		for (Entity e: entities) {
			if (e != caller && e.isSolidTo(caller) && e.inLine(x,y,xs,ys)) {
				double eDist = e.distance(x,y,xs,ys);
				if (eDist < distance) {
					distance = eDist;
				}
			}
		}
		return distance;
	}
	
	public void removeEntity(Entity e)
	{
		for (int i = 0; i < entities.size(); ++i) {
			if (entities.get(i) == e) {
				entities.remove(i);
				return;
			}
		}
	}
	
	public void reset() 
	{
		entities.clear();
		player = new Player(this);
		entities.add(player);
		map = new Map(this, mapName);
	}
}