package wireworld;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

public class Map
{
	public Model model;
	public Tile[][] tiles;
	public Tile borderTile;
    
	public Map(Model m)
	{
		model = m;
		borderTile = new BasicTile(Tile.TILE_ID, true);
		borderTile.canChange = false;
		tiles = new Tile[40][40];
		for (int i = 0; i < tiles.length; ++i) {
			for (int j = 0; j < tiles[0].length; ++j) {
				if (i > 38 || (j < 5 && i > 15) || j > 39)
					tiles[i][j] = new BasicTile(Tile.TILE_ID, true);
				else
					tiles[i][j] = new BasicTile(Tile.AIR_TILE_ID, false);
			}
		}
	}
    
	public Map(Model m, String path)
	{
		model = m;
		load(path);
	}
 
	public Tile getTile(double x, double y)
	{
		int i = (int)Math.floor(y / Tile.HEIGHT);
		int j = (int)Math.floor(x / Tile.WIDTH);
		Tile tile;
		if (i >= 0 && i < tiles.length && j >= 0 && j < tiles[0].length) {
			tile = tiles[i][j];
		}
		else {
			tile = borderTile;
		}
		return tile;
	}
 
	public void replaceTile(double x, double y, Tile tile)
	{
		int i = (int)Math.floor(y / Tile.HEIGHT);
		int j = (int)Math.floor(x / Tile.WIDTH);
		if (i >= 0 && i < tiles.length && j >= 0 && j < tiles[0].length) {
			if (tiles[i][j].canChange) {
				tiles[i][j] = tile;
			}
		}
	}
 
	public boolean isSolid(double x, double y)
	{
		int i = (int)Math.floor(y / Tile.HEIGHT);
		int j = (int)Math.floor(x / Tile.WIDTH);
		Tile tile;
		if (i >= 0 && i < tiles.length && j >= 0 && j < tiles[0].length) {
			tile = tiles[i][j];
		}
		else {
			tile = borderTile;
		}
		return tile.solid;
	}
 
	public double distanceToObstacle(double x, double y, double xs, double ys)
	{
		int i = (int)Math.floor(y / Tile.HEIGHT);
		int j = (int)Math.floor(x / Tile.WIDTH);
		int a = 0;
		int b = 0;
		if (ys > 0) a = 1;
		if (ys < 0) a = -1;
		if (xs > 0) b = 1;
		if (xs < 0) b = -1;
		Tile tile = new BasicTile(Tile.AIR_TILE_ID, false);
		while (i < tiles.length + 2 && i >= -2 && j < tiles[0].length + 2 && j >= -2 && !tile.solid) {
			if (i >= 0 && i < tiles.length && j >= 0 && j < tiles[0].length) {
				tile = tiles[i][j];
			}
			else {
				tile = borderTile;
			}
			i += a;
			j += b;
		}
		double distance;
		if (a != 0) {
			distance = Math.abs(((i - a) * Tile.HEIGHT) - y);
			return distance * a;
		}
		if (b != 0) {
			distance = Math.abs(((j - b) * Tile.WIDTH) - x);
			return distance * b;
		}
		return 0;
	}
	
	public void update()
	{
		for (int i = 0; i < tiles.length; ++i) {
			for (int j = 0; j < tiles[0].length; ++j) {
				/*if (tiles[i][j].id == Tile.LAVA_ID) {
					if (i+1 < tiles.length) {
						if (!tiles[i+1][j].solid && tiles[i+1][j].id != Tile.LAVA_ID) {
							if (tiles[i][j].level > 0) {
								Lava lava = new Lava(model, 0.25, tiles[i][j]);
								lava.x = j * Tile.WIDTH + Tile.WIDTH * 0.5;
								lava.y = i * Tile.HEIGHT + Tile.HEIGHT * 0.5;
								model.entities.add(lava);
								tiles[i][j].level -= 0.25;
							}
						}
					}
					double leftPressure = 0.0;
					double rightPressure = 0.0;					
					if (j-1 >= 0) {
						if (!tiles[i][j-1].solid) {
							if (tiles[i][j-1].id == Tile.LAVA_ID) {
								if (tiles[i][j-1].level <= tiles[i][j].level) {
									rightPressure = Math.abs(tiles[i][j].level - tiles[i][j-1].level);
								}
							}
							else {
								rightPressure = 1.0;
							}
						}
					}
					if (j+1 < tiles[0].length) {
						if (!tiles[i][j+1].solid) {
							if (tiles[i][j+1].id == Tile.LAVA_ID) {
								if (tiles[i][j+1].level <= tiles[i][j].level) {
									leftPressure = Math.abs(tiles[i][j].level - tiles[i][j+1].level);
								}
							}
							else {
								leftPressure = 1.0;
							}
						}
					}
					int dir = 0;
					if (leftPressure < rightPressure) {
						if (new Random().nextDouble() < leftPressure) {
							dir = -1;
						}
					}
					else if (rightPressure < leftPressure) {
						if (new Random().nextDouble() < rightPressure) {
							dir = 1;
						}
					}
					else if (rightPressure == leftPressure) {
						if (new Random().nextDouble() < leftPressure + rightPressure) {
							if (new Random().nextDouble() < 0.5) {
								dir = -1;
							}
							else {
								dir = 1;
							}
						}
					}
					if (leftPressure != 0 || rightPressure != 0) {
						//System.out.println("LP: " + leftPressure + ", RP: " + rightPressure + ", lvl: " + tiles[i][j].level);
					}
					if (dir == -1) {
						if (j-1 >= 0) {
							if (!tiles[i][j-1].solid) {
								if (tiles[i][j-1].id != Tile.LAVA_ID || (tiles[i][j-1].id == Tile.LAVA_ID && tiles[i][j].level >= tiles[i][j-1].level)) {
									//System.out.println("l");
									Lava lava = new Lava(model, 0.25, tiles[i][j]);
									lava.x = (j-1) * Tile.WIDTH + Tile.WIDTH * 0.5;
									lava.y = i * Tile.HEIGHT + Tile.HEIGHT * 0.5;
									model.entities.add(lava);
									tiles[i][j].level -= 0.25;								
								}
							}
						}
					}
					if (dir == 1) {
						if (j+1 < tiles[0].length) {
							if (!tiles[i][j+1].solid) {
								if (tiles[i][j+1].id != Tile.LAVA_ID || (tiles[i][j+1].id == Tile.LAVA_ID && tiles[i][j].level >= tiles[i][j+1].level)) {
									//System.out.println("r");
									Lava lava = new Lava(model, 0.25, tiles[i][j]);
									lava.x = (j+1) * Tile.WIDTH + Tile.WIDTH * 0.5;
									lava.y = i * Tile.HEIGHT + Tile.HEIGHT * 0.5;
									model.entities.add(lava);
									tiles[i][j].level -= 0.25;								
								}
							}
						}
					}
					if (tiles[i][j].level <= 0) {
						tiles[i][j] = new Tile(Tile.AIR_TILE_ID, false);
					}
				}*/
			}
		}
	}
 
	public void load(String path)
	{
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(Wireworld.class.getResourceAsStream(path)));
			String line;
			boolean readingTiles = false;
			int width = 0;
			int height = 0;
			int i = 0;
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("//")) {
					if (line.startsWith("width=")) {
						width = Integer.parseInt(line.substring(line.indexOf("=") + 1));
					}
					else if (line.startsWith("height=")) {
						height = Integer.parseInt(line.substring(line.indexOf("=") + 1));
					}
					else if (line.startsWith("border=")) {
						if (line.substring(line.indexOf("=") + 1).equals("solid")) {
							borderTile = new BasicTile(Tile.TILE_ID, true);
							borderTile.canChange = false;
						}
						else {
							borderTile = new BasicTile(Tile.AIR_TILE_ID, false);
							borderTile.canChange = false;
						}
					}
					else if (line.equals("(")) {
						tiles = new Tile[height][width];
						readingTiles = true;
					}
					else if (line.equals(")")) {
						readingTiles = false;
					}
					else if (readingTiles) {
						String row = line.substring(1, line.length() - 1);
						for (int j = 0; j < row.length(); ++j) {
							if (row.charAt(j) == ' ') {
								tiles[i][j] = new BasicTile(Tile.AIR_TILE_ID, false);
							}
							else if (row.charAt(j) == 'w') {
								model.player.x = j * Tile.WIDTH + model.player.height / 2;
								model.player.y = i * Tile.HEIGHT + model.player.width / 2;
								tiles[i][j] = new BasicTile(Tile.AIR_TILE_ID, false);
							}
							else if (row.charAt(j) == 't') {
								tiles[i][j] = new BasicTile(Tile.TILE_ID, true);
							}
							else if (row.charAt(j) == '^') {
								tiles[i][j] = new BasicTile(Tile.SPIKE_ID, false);
							}
							else if (row.charAt(j) == '-') {
								tiles[i][j] = new BasicTile(Tile.AIR_TILE_ID, false);
								Platform plat = new Platform(model, -4, 0);
								plat.x = j * Tile.WIDTH + plat.width / 2;
								plat.y = i * Tile.HEIGHT + plat.height / 2;
								model.entities.add(plat);
							}
							else if (row.charAt(j) == '|') {
								tiles[i][j] = new BasicTile(Tile.AIR_TILE_ID, false);
								Platform plat = new Platform(model, 0, 4);
								plat.x = j * Tile.WIDTH + plat.width / 2;
								plat.y = i * Tile.HEIGHT + plat.height / 2;
								model.entities.add(plat);
							}
							else if (row.charAt(j) == 'x') {
								tiles[i][j] = new BasicTile(Tile.BREAK_TILE_ID, true);
							}
							else if (row.charAt(j) == '!') {
								tiles[i][j] = new BasicTile(Tile.SPRING_ID, true);
							}
							else if (row.charAt(j) == 'v') {
								tiles[i][j] = new BasicTile(Tile.AIR_TILE_ID, false);
								Fallspike block = new Fallspike(model);
								block.x = j * Tile.WIDTH + block.width / 2;
								block.y = i * Tile.HEIGHT + block.height / 2;
								model.entities.add(block);
							}
							else if (row.charAt(j) == 'd') {
								tiles[i][j] = new BasicTile(Tile.DOOR_ID, false);
							}
							else if (row.charAt(j) == '~') {
								tiles[i][j] = new BasicTile(Tile.LAVA_ID, false);
								//tiles[i][j].level = 1.0;
							}
							else {
								tiles[i][j] = new BasicTile(Tile.AIR_TILE_ID, false);
							}
						}
						++i;
					}
				}
			}
			reader.close();
		}
		catch (Exception e) {
		}
	}
}