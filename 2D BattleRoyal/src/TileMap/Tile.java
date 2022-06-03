package TileMap;

import java.awt.image.BufferedImage;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Tile implements Serializable
{
	private transient BufferedImage image;
	private int type;
	
	//tile types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	
	/**
     * Constructs a new {@code Tile}
     * @param image piece of map
     * @param type of image
     */
	public Tile(BufferedImage image, int type)
	{
		this.image = image;
		this.type = type;
	}
	
	/**
     * get image of Tile
     * @return image
     */
	public BufferedImage getImage() { return image; }
	/**
     * get type of Tile
     * @return 1 or 0
     */
	public int getType() { return type;}
}
