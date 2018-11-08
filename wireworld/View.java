package wireworld;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class View extends JPanel
{
	public Model model;
	public int width;
	public int height;
 
	private HashMap<String, BufferedImage> images;
 
	public View(Model m)
	{
		model = m;
  
		images = new HashMap<String, BufferedImage>();
		BufferedImage image;
		// Tiles:
		try
		{
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/standframe.png"));
			images.put("WIREMAN_STAND_0_LEFT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/standframer.png"));
			images.put("WIREMAN_STAND_0_RIGHT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/arms1.png"));
			images.put("WIREMAN_WALK_0_LEFT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/arms2.png"));
			images.put("WIREMAN_WALK_1_LEFT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/arms3.png"));
			images.put("WIREMAN_WALK_2_LEFT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/arms4.png"));
			images.put("WIREMAN_WALK_3_LEFT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/arms5.png"));
			images.put("WIREMAN_WALK_4_LEFT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/arms6.png"));
			images.put("WIREMAN_WALK_5_LEFT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/arms7.png"));
			images.put("WIREMAN_WALK_6_LEFT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/arms8.png"));
			images.put("WIREMAN_WALK_7_LEFT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/arms9.png"));
			images.put("WIREMAN_WALK_8_LEFT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/arms10.png"));
			images.put("WIREMAN_WALK_9_LEFT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/armsr1.png"));
			images.put("WIREMAN_WALK_0_RIGHT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/armsr2.png"));
			images.put("WIREMAN_WALK_1_RIGHT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/armsr3.png"));
			images.put("WIREMAN_WALK_2_RIGHT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/armsr4.png"));
			images.put("WIREMAN_WALK_3_RIGHT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/armsr5.png"));
			images.put("WIREMAN_WALK_4_RIGHT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/armsr6.png"));
			images.put("WIREMAN_WALK_5_RIGHT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/armsr7.png"));
			images.put("WIREMAN_WALK_6_RIGHT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/armsr8.png"));
			images.put("WIREMAN_WALK_7_RIGHT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/armsr9.png"));
			images.put("WIREMAN_WALK_8_RIGHT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/armsr10.png"));
			images.put("WIREMAN_WALK_9_RIGHT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/jump.png"));
			images.put("WIREMAN_JUMP_0_LEFT", image);
			image = ImageIO.read(Wireworld.class.getResourceAsStream("/graphics/wireman/jumpr.png"));
			images.put("WIREMAN_JUMP_0_RIGHT", image);
		}
		catch (Exception e)
		{
		}
	}
	 
	public void refresh(int w, int h)
	{
		repaint();
		width = w;
		height = h;
	}
	 
	@Override
	public void paintComponent(Graphics graphics)
	{
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0,0,width,height);  
		drawMap(graphics);
		for (Entity e: model.entities) {
			if (e != model.player) {
				if (e.getType().equals("Platform")) {
					drawPlatform(graphics, (Platform)e);
				}
				if (e.getType().equals("Block")) {
					drawBlock(graphics, (Block)e);
				}
				if (e.getType().equals("Fallspike")) {
					drawFallspike(graphics, (Block)e);
				}
				if (e.getType().equals("Lava")) {
					drawLava(graphics, (Lava)e);
				}
			}
		}
		drawCharacter(graphics, model.player);
		drawPauseMenu(graphics, model.player);
	}
	 
	public void drawCharacter(Graphics graphics, Character character)
	{
		int scrollX = (int)model.player.x + model.player.scrollX;
		int scrollY = (int)model.player.y + model.player.scrollY;
		int offsetX =(int) Math.round((width / 2) + ((character.x - scrollX) * model.player.zoom));
		int offsetY = (int)Math.round((height / 2) + ((character.y - scrollY) * model.player.zoom));
		int positionX1 = (int)Math.round(offsetX - ((character.width / 2) * model.player.zoom));
		int positionY1 = (int)Math.round(offsetY - ((character.height / 2) * model.player.zoom));
		int positionX2 = (int)Math.round(offsetX + ((character.width / 2) * model.player.zoom));
		int positionY2 = (int)Math.round(offsetY + ((character.height / 2) * model.player.zoom));
		  
		String imageName = character.spriteSet + "_" + character.frame;
		if (character.direction)
			imageName += "_RIGHT";
		else
			imageName += "_LEFT";
			  
		graphics.drawImage(images.get(imageName),positionX1, positionY1, positionX2, positionY2, 0, 0, (int)character.width, (int)character.height, null);
	}
	 
	public void drawPlatform(Graphics graphics, Platform p)
	{
		int scrollX = (int)model.player.x + model.player.scrollX;
		int scrollY = (int)model.player.y + model.player.scrollY;
		int offsetX =(int) Math.round((width / 2) + ((p.x - scrollX) * model.player.zoom));
		int offsetY = (int)Math.round((height / 2) + ((p.y - scrollY) * model.player.zoom));
		int positionX1 = (int)Math.round(offsetX - ((p.width / 2) * model.player.zoom));
		int positionY1 = (int)Math.round(offsetY - ((p.height / 2) * model.player.zoom));
		 
		graphics.setColor(Color.BLACK);
		graphics.drawRect(positionX1, positionY1, (int)(p.width * model.player.zoom), (int)(p.height * model.player.zoom));
	}
	 
	public void drawBlock(Graphics graphics, Block b)
	{
		int scrollX = (int)model.player.x + model.player.scrollX;
		int scrollY = (int)model.player.y + model.player.scrollY;
		int offsetX =(int) Math.round((width / 2) + ((b.x - scrollX) * model.player.zoom));
		int offsetY = (int)Math.round((height / 2) + ((b.y - scrollY) * model.player.zoom));
		int positionX1 = (int)Math.round(offsetX - ((b.width / 2) * model.player.zoom));
		int positionY1 = (int)Math.round(offsetY - ((b.height / 2) * model.player.zoom));
		 
		graphics.setColor(Color.BLACK);
		graphics.drawRect(positionX1, positionY1, (int)(b.width * model.player.zoom), (int)(b.height * model.player.zoom));
	}
	 
	public void drawLava(Graphics graphics, Lava b)
	{
		int scrollX = (int)model.player.x + model.player.scrollX;
		int scrollY = (int)model.player.y + model.player.scrollY;
		int offsetX =(int) Math.round((width / 2) + ((b.x - scrollX) * model.player.zoom));
		int offsetY = (int)Math.round((height / 2) + ((b.y - scrollY) * model.player.zoom));
		int positionX1 = (int)Math.round(offsetX - ((b.width / 2) * model.player.zoom));
		int positionY1 = (int)Math.round(offsetY - ((b.height / 2) * model.player.zoom));
		 
		graphics.setColor(Color.RED);
		graphics.drawRect(positionX1, positionY1, (int)(b.width * model.player.zoom), (int)(b.height * model.player.zoom));
	}
	 
	public void drawFallspike(Graphics graphics, Block b)
	{
		int scrollX = (int)model.player.x + model.player.scrollX;
		int scrollY = (int)model.player.y + model.player.scrollY;
		int offsetX =(int) Math.round((width / 2) + ((b.x - scrollX) * model.player.zoom));
		int offsetY = (int)Math.round((height / 2) + ((b.y - scrollY) * model.player.zoom));
		int positionX1 = (int)Math.round(offsetX - ((b.width / 2) * model.player.zoom));
		int positionY1 = (int)Math.round(offsetY - ((b.height / 2) * model.player.zoom));
		int positionX2 = (int)Math.round(offsetX + ((b.width / 2) * model.player.zoom));
		int positionY2 = (int)Math.round(offsetY + ((b.height / 2) * model.player.zoom));
		 
		graphics.setColor(Color.BLACK);
		graphics.drawRect(positionX1, positionY1, (int)(b.width * model.player.zoom), (int)(b.height / 2.0 * model.player.zoom));
		graphics.drawLine(positionX1, positionY1 + (int)(b.height / 2.0 * model.player.zoom), positionX1 + (int)(b.width * (1/4.0) * model.player.zoom), positionY2);
		graphics.drawLine(positionX1 + (int)(b.width / 2.0 * model.player.zoom), positionY1 + (int)(b.height / 2.0 * model.player.zoom), positionX1 + (int)(b.width * (1/4.0) * model.player.zoom), positionY2);
		graphics.drawLine(positionX1 + (int)(b.width / 2.0 * model.player.zoom), positionY1 + (int)(b.height / 2.0 * model.player.zoom), positionX1 + (int)(b.width * (3/4.0) * model.player.zoom), positionY2);
		graphics.drawLine(positionX2, positionY1 + (int)(b.height / 2.0 * model.player.zoom), positionX1 + (int)(b.width * (3/4.0) * model.player.zoom), positionY2);
	}
	 
	public void drawMap(Graphics graphics)
	{
		int scrollX = (int)model.player.x + model.player.scrollX;
		int scrollY = (int)model.player.y + model.player.scrollY;
		int horizontalTiles = (int)Math.round(width / (Tile.WIDTH * model.player.zoom)) + 2;
		int verticalTiles = (int)Math.round(height / (Tile.HEIGHT * model.player.zoom)) + 2;
		int scrollXTiles = (int)Math.round(scrollX / Tile.WIDTH);
		int scrollYTiles = (int)Math.round(scrollY / Tile.HEIGHT);
		int offsetX = (int)Math.round((scrollXTiles * Tile.WIDTH) - scrollX);
		int offsetY = (int)Math.round((scrollYTiles * Tile.HEIGHT) - scrollY);
		  
		for (int i = scrollYTiles - Math.round(verticalTiles / 2) - 1; i < scrollYTiles + Math.round(verticalTiles / 2) + 1; ++i)
		{
			for (int j = scrollXTiles - Math.round(horizontalTiles / 2) - 1; j < scrollXTiles + Math.round(horizontalTiles / 2) + 1; ++j)
			{
				Tile tile, northTile, eastTile, southTile, westTile;
				if (i >= 0 && i < model.map.tiles.length && j >= 0 && j < model.map.tiles[0].length) {
					tile = model.map.tiles[i][j];
				}
				else {
					tile = model.map.borderTile;
				}
				if (i - 1 >= 0 && i - 1 < model.map.tiles.length && j >= 0 && j < model.map.tiles[0].length) {
					northTile = model.map.tiles[i - 1][j];
				}
				else {
					northTile = model.map.borderTile;
				}
				if (i + 1 >= 0 && i + 1 < model.map.tiles.length && j >= 0 && j < model.map.tiles[0].length) {
					southTile = model.map.tiles[i + 1][j];
				}
				else {
					southTile = model.map.borderTile;
				}
				if (i >= 0 && i < model.map.tiles.length && j - 1 >= 0 && j - 1 < model.map.tiles[0].length) {
					westTile = model.map.tiles[i][j - 1];
				}
				else {
					westTile = model.map.borderTile;
				}
				if (i >= 0 && i < model.map.tiles.length && j + 1 >= 0 && j + 1 < model.map.tiles[0].length) {
					eastTile = model.map.tiles[i][j + 1];
				}
				else {
					eastTile = model.map.borderTile;
				}
				   
				int positionX = (int)Math.round((width / 2) + ((((j - scrollXTiles) * Tile.WIDTH) + offsetX) * model.player.zoom));
				int positionY = (int)Math.round((height / 2) + ((((i - scrollYTiles) * Tile.HEIGHT) + offsetY) * model.player.zoom));
				   
				drawTile(graphics, tile, positionX, positionY, (int)Math.round(positionX + (Tile.WIDTH * model.player.zoom)) - 1,
				(int)Math.round(positionY + (Tile.HEIGHT * model.player.zoom)) - 1, northTile, eastTile, southTile, westTile);
			}
		}
	}
	 
	public void drawTile(Graphics graphics, Tile tile, int x1, int y1, int x2, int y2, Tile northTile, Tile eastTile, Tile southTile, Tile westTile)
	{
		if (tile.getID() == Tile.AIR_TILE_ID) {
			if (northTile.getID() == Tile.DOOR_ID) {
				graphics.drawLine(x1,y2,x2,y2);
				graphics.drawLine(x1,y1,x1,y2);
				graphics.drawLine(x2,y1,x2,y2);
			}
		}
		if (tile.getID() == Tile.TILE_ID || tile.getID() == Tile.BREAK_TILE_ID) {
			graphics.setColor(Color.BLACK);
			graphics.drawLine(x1,y1,x2,y1);
			graphics.drawLine(x1,y1 + (int)((y2-y1) / 2),x2,y1 + (int)((y2-y1) / 2));
			graphics.drawLine(x1,y1,x1,y2);
			graphics.drawLine(x1 + (int)((x2-x1) / 2),y1,x1 + (int)((x2-x1) / 2),y2);
			if (southTile.getID() != Tile.TILE_ID && southTile.getID() != Tile.BREAK_TILE_ID) {
				graphics.drawLine(x1,y2,x2,y2);
			}
			if (eastTile.getID() != Tile.TILE_ID && eastTile.getID() != Tile.BREAK_TILE_ID) {
				graphics.drawLine(x2,y1,x2,y2);
			}
		}
		if (tile.getID() == Tile.SPIKE_ID) {
			graphics.setColor(Color.BLACK);
			graphics.drawLine(x1,y2,x1 + (int)((x2-x1) / 4),y1);
			graphics.drawLine(x1 + (int)((x2-x1) / 2),y2,x1 + (int)((x2-x1) / 4),y1);
			graphics.drawLine(x1 + (int)((x2-x1) / 2),y2,x1 + (int)((x2-x1) * (3/4.0)),y1);
			graphics.drawLine(x2,y2,x1 + (int)((x2-x1) * (3/4.0)),y1);
		}
		if (tile.getID() == Tile.SPRING_ID) {
			graphics.setColor(Color.BLACK);
			graphics.drawLine(x1,y1,x2,y1);
			graphics.drawLine(x1,y1,x1,y2);
			if (southTile.getID() != Tile.TILE_ID && southTile.getID() != Tile.BREAK_TILE_ID) {
				graphics.drawLine(x1,y2,x2,y2);
			}
			graphics.drawLine(x2,y1,x2,y2);
			graphics.drawLine(x1,y1 + (int)((y2-y1) * 1/3.0),x2,y1 + (int)((y2-y1) * 1/3.0));
			graphics.drawLine(x1,y1 + (int)((y2-y1) * 2/3.0),x2,y1 + (int)((y2-y1) * 2/3.0));
		}
		if (tile.getID() == Tile.DOOR_ID) {
			graphics.setColor(Color.BLACK);
			graphics.drawLine(x1,y1,x2,y1);
			graphics.drawLine(x1,y1,x1,y2);
			graphics.drawLine(x2,y1,x2,y2);
		}
		if (tile.getID() == Tile.LAVA_ID) {
			graphics.setColor(Color.RED);
			graphics.drawLine(x1,y1,x2,y2);
			/*double westLevel = 0.0;
			if (westTile.solid) westLevel = tile.level;
			if (westTile.id == Tile.LAVA_ID) westLevel = westTile.level;
			double eastLevel = 0.0;
			if (eastTile.solid) eastLevel = tile.level;
			if (eastTile.id == Tile.LAVA_ID) eastLevel = eastTile.level;
			if (westLevel != 0) westLevel = (westLevel + tile.level) / 2.0;
			if (eastLevel != 0) eastLevel = (eastLevel + tile.level) / 2.0;
			if (northTile.id != Tile.LAVA_ID || tile.level < 1.0) {
				graphics.drawLine(x1,y2 - (int)(westLevel * (y2-y1)),x1 + (int)((x2-x1) / 2),y2 - (int)(tile.level * (y2-y1)));
				graphics.drawLine(x2,y2 - (int)(eastLevel * (y2-y1)),x1 + (int)((x2-x1) / 2),y2 - (int)(tile.level * (y2-y1)));
			}
			if (westTile.id != Tile.LAVA_ID) {
				graphics.drawLine(x1,y2 - (int)(westLevel * (y2-y1)),x1,y2);
			}
			if (eastTile.id != Tile.LAVA_ID) {
				graphics.drawLine(x2,y2 - (int)(eastLevel * (y2-y1)),x2,y2);
			}
			if (southTile.id != Tile.LAVA_ID) {
				graphics.drawLine(x1,y2,x2,y2);
			}*/
		}
	}
	 
	public void drawPauseMenu(Graphics graphics, Player player)
	{
		if (player.paused) {
			int boxWidth = 300;
			int lineSpace = 20;
			int charWidth = 20;
			int line = -1;
			graphics.setColor(Color.WHITE);
			graphics.fillRect((int)(width / 2 - boxWidth / 2), (int)(height / 2 - lineSpace * 2), boxWidth, (int)(lineSpace * 5));
			graphics.setColor(Color.BLACK);
			graphics.drawRect((int)(width / 2 - boxWidth / 2), (int)(height / 2 - lineSpace * 2), boxWidth, (int)(lineSpace * 5));
			if (!player.dead) {
				graphics.drawString("Paused", (int)(width / 2 - boxWidth / 2) + charWidth * 1, (int)(height / 2) + (line * lineSpace));
			}
			else {
				graphics.drawString("Game Over", (int)(width / 2 - boxWidth / 2) + charWidth * 1, (int)(height / 2) + (line * lineSpace));
			}
			line += 1;
			if (!player.dead) {
				graphics.drawString("Press Esc to resume", (int)(width / 2 - boxWidth / 2) + charWidth * 1, (int)(height / 2) + (line * lineSpace));
				line += 1;
			}
			graphics.drawString("Press r to restart", (int)(width / 2 - boxWidth / 2) + charWidth * 1, (int)(height / 2) + (line * lineSpace));
			line += 1;
			graphics.drawString("Press m to return to the menu", (int)(width / 2 - boxWidth / 2) + charWidth * 1, (int)(height / 2) + (line * lineSpace));
			line += 1;
		}
	}
}