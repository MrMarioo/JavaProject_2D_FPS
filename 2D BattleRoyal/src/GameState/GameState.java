package GameState;

public abstract class GameState {
	/**
     * Class to define state of program
     */
	protected GameStateManager gsm;

	/**
     * Function for init current state
     */
	public abstract void init() ;
	/**
     * Function for update state
     */
	public abstract void update() ;
	/**
     * Draw object, strings on the frame 
     * @param g the specified frame Graphics
     */
	public abstract void draw(java.awt.Graphics2D g);
	/**
     * Function to detect key press
     * @param k value of key from keyboard
     */
	public abstract void keyPressed(int k);
	/**
     * Function to detect key release
     * @param k value of key from keyboard
     */
	public abstract void keyReleased(int k);
	/**
     * Function to get coordinate of mouse on screen
     * @param x coordinate of x mouse on screen
     * @param y coordinate of y mouse on screen
     */
	public abstract void mouseMoved(int x, int y);
	/**
     * Function to detect mouse press
     */
	public abstract void mousePressed();
}	
