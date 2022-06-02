package EntityProperties;

import java.io.Serializable;

import TileMap.TileMap;

@SuppressWarnings("serial")
public class ObjectTileStuff implements Serializable
{

	//tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
		
	/**
     * Constructs a new {@code ObjectTileStuff}
     * @param     tm TileMap of Object
     */
	public ObjectTileStuff(TileMap tm)
	{
		this.tileMap = tm;
		this.tileSize = tm.getTileSize();
	}
	
	/**
     * Setter tilemap of object
     * @param     tm  TileMap class
     */ 
	public void setTM(TileMap tm)
	{
		this.tileMap = tm;
	}
	/**
     * Setter
     * set ymap to object
     * @param     y coord of player on map
     */
	public void setMapPositon()
	{
		xmap = tileMap.getX();
		ymap = tileMap.getY();
	}
	
	/**
     * Getter
     * Get tile size of tile map
     * @return    {@code tileSize}
     */
	public int getTileSize() {	return tileSize;	}
	/**
     * Getter
     * Get tile map
     * @return    {@code tileMap}
     */
	public TileMap getTileMap() { return tileMap; 	}
	/**
     * Getter
     * Get for x coord object on map
     * @return    {@code xmap}
     */
	public double getXmap() { return xmap; }
	/**
     * Getter
     * Get for y coord object on map
     * @return    {@code ymap}
     */
	public double getYmap() { return ymap; }
		
}
