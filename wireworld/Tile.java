package wireworld;

public abstract class Tile
{
	public static final double WIDTH = 16.0;
	public static final double HEIGHT = 16.0;
	public static final double MASS = 1.0;

	public static final int AIR_TILE_ID = 0;
	public static final int TILE_ID = 1;
	public static final int SPIKE_ID = 2;
	public static final int BREAK_TILE_ID = 3;
	public static final int SPRING_ID = 4;
	public static final int DOOR_ID = 5;
	public static final int LAVA_ID = 6;
	
	public static final double SPRING_SPEED = -25;
	
	public boolean canChange;
	public boolean solid;
	   
	public Tile(boolean b)
	{
		solid = b;
		canChange = true;
	}
	
	public abstract int getID();
}