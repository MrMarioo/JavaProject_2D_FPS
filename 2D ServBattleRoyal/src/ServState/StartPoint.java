package ServState;

import java.awt.Point;
import java.io.Serializable;

public class StartPoint implements Serializable
{
	protected Point startPos;
	protected String teamName;
	static public int numOfMem;
	
	public StartPoint(Point p, String t)
	{
		this.startPos = p;
		this.teamName = t;
	}
	public Point getPoint() { return startPos; }
	public String getName() { return teamName; }
}
