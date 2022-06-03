package ServState;

import java.awt.Point;
import java.io.Serializable;

@SuppressWarnings("serial")
public class StartPoint implements Serializable
{
	protected Point startPos;
	protected String teamName;
	static public int numOfMem;
	
	/**
     * Constructs a new {@code StartPoint}
     * @param p starting point of team
     * @param t name of team
     */
	public StartPoint(Point p, String t)
	{
		this.startPos = p;
		this.teamName = t;
	}
	/**
     * Getter for Point of start
     * @return startPos starting position of team
     */
	public Point getPoint() { return startPos; }
	/**
     * Getter for name of team
     * @return teamName name of team
     */
	public String getName() { return teamName; }
}
