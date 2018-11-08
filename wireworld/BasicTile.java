package wireworld;

public class BasicTile extends Tile
{	
	public int id;
	   
	public BasicTile(int i, boolean b)
	{
		super(b);
		id = i;
	}
	
	public int getID() {
		return id;
	}
}