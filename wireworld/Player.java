package wireworld;

import java.util.*;
import java.awt.geom.Point2D;

public class Player extends Character
{
	public static final int WALK_SPEED = 8;
	public static final int JUMP_SPEED = -12;
	public static final int NUM_WALK_FRAMES = 10;
	
	public HashMap<String, String> controls;
	public boolean paused;
	private boolean pauseDelay;
	public double zoom;
	public int scrollX;
	public int scrollY;
	public boolean restart;
	public boolean returnToMenu;
	public boolean dead;
	
	public Player(Model m)
	{
		super(m);
		paused = true;
		zoom = 1.0;
		scrollX = 0;
		scrollY = 0;
		spriteSet = "WIREMAN_STAND";
		width = 18;
		height = 27;
		mass = 2.0;
		direction = true;
		frame = 0;
		layer = Entity.LAYER_MAIN;
		xSpeed = 0;
		ySpeed = 0;
		controls = defaultControls();
		restart = false;
		returnToMenu = false;
		dead = false;
	}
 
	public void control(HashSet<String> keys)
	{
		boolean walking = false;
		boolean pausing = false;
		for (String k: keys) {
			if (controls.containsKey(k)) {
				if (controls.get(k).contains("pause") && !pauseDelay && !dead) {
					paused = !paused;
					pausing = true;
					pauseDelay = true;
				}
				if (paused && controls.get(k).contains("menu")) {
					returnToMenu = true;
				}
				if (paused && controls.get(k).contains("restart")) {
					restart = true;
				}
				if (!paused && controls.get(k).contains("moveLeft")) {
					walking = true;
					spriteSet = "WIREMAN_WALK";
					frame = (frame + 1) % NUM_WALK_FRAMES;
					direction = false;
					if (xSpeed > -Player.WALK_SPEED && xSpeed <= 0) {
						xSpeed = -Player.WALK_SPEED;
					}
					else if (xSpeed > 0) {
						xSpeed -= Player.WALK_SPEED;
					}
				}
				if (!paused && controls.get(k).contains("moveRight")) {
					walking = true;
					spriteSet = "WIREMAN_WALK";
					frame = (frame + 1) % NUM_WALK_FRAMES;
					direction = true;
					if (xSpeed < Player.WALK_SPEED && xSpeed >= 0) {
						xSpeed = Player.WALK_SPEED;
					}
					else if (xSpeed < 0) {
						xSpeed += Player.WALK_SPEED;
					}
				}
				if (!paused && controls.get(k).contains("jump")) {
					if (detectBoundaries(0, Entity.ADJACENT_DISTANCE)) {
						ySpeed += Player.JUMP_SPEED;
						spriteSet = "WIREMAN_JUMP";
						frame = 0;
					}
				}
			}
		}
		if (!paused && !walking) {
			if (Math.abs(xSpeed) <= Math.abs(Player.WALK_SPEED)) {
				spriteSet = "WIREMAN_STAND";
				frame = 0;
				xSpeed = 0;
			}
		}
		if (!paused && !detectBoundaries(0, Entity.ADJACENT_DISTANCE)) {
			spriteSet = "WIREMAN_JUMP";
			frame = 0;
		}
		if (!pausing) pauseDelay = false;
	}
 
	private static HashMap<String, String> defaultControls()
	{
		HashMap<String, String> c = new HashMap<String, String>();
		c.put("27","pause");
		c.put("82","restart");
		c.put("77","menu");
		c.put("37","moveLeft");
		c.put("39","moveRight");
		c.put("38","jump");
		return c;
	}
	
	public void interactWithMap()
	{
		super.interactWithMap();
		Point2D.Double[] corners = getCorners();
		for (int i = 0; i < corners.length; ++i) {
			Tile tile = model.map.getTile(corners[i].x, corners[i].y);
			if (tile.getID() == Tile.SPIKE_ID) {
				dead = true;
				paused = true;
			}
			else if (tile.getID() == Tile.LAVA_ID) {
				dead = true;
				paused = true;
			}
			for (Entity e: model.entities) {
				if (e.getType() == "Fallspike") {
					if (e.intersects(corners[i].x, corners[i].y - ADJACENT_DISTANCE)) {
						dead = true;
						paused = true;
					}
				}
				if (e.getType() == "Lava") {
					if (e.intersects(corners[i].x, corners[i].y)) {
						dead = true;
						paused = true;
					}
				}
			}
		}
	}
}