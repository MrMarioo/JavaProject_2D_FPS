package GameState;

import java.util.ArrayList;

import Main.Client;

@SuppressWarnings("unused")
public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	
	protected Client client;
	
	public static final int NUMGAMESTATES = 3;
	public static final int MENUSTATE = 0;
	public static final int LOBBYSTATE = 1;
	public static final int LEVEL1STATE = 2;
	
	 /**
     * Constructs a new {@code GameStateManager}
     * @see #loadState(int state) 
     */
	public GameStateManager() {
		
		gameStates = new GameState[NUMGAMESTATES];
		
		currentState = MENUSTATE;
		loadState(currentState);
		
	}
	
	/**
     * load state
     * @param state value of choosed game state
     */
	private void loadState(int state) {
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		if(state == LOBBYSTATE)
			gameStates[state] = new LobbyState(this);
		if(state == LEVEL1STATE)
			gameStates[state] = new Level1State(this, client);
	}
	
	/**
     * set null to array of game states 
     * @param state value of choosed game state
     */
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	/**
     * set choosed state
     * @param state value of choosed game state
     */
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	/**
     * set client socket to game state
     * @param client a class to connect with server
     */
	public void setClient(Client client) {this.client = client;}
	/**
     * Update values on current state
     */
	public void update() {
		try {
			gameStates[currentState].update();
		} catch(Exception e) {}
	}
	/**
     * Draw object, strings on the frame 
     * @param g the specified frame Graphics
     */
	public void draw(java.awt.Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		} catch(Exception e) {}
	}
	/**
     * Function to detect key press
     * @param k value of key from keyboard
     */
	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
	}
	/**
     * Function to detect key release
     * @param k value of key from keyboard
     */
	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}
	/**
     * Function to get coordinate of mouse on screen
     * @param x coordinate of x mouse on screen
     * @param y coordinate of y mouse on screen
     */
	public void mouseMoved(int x, int y)
	{
		gameStates[currentState].mouseMoved(x, y);
	}
	/**
     * Function to detect mouse press
     */
	public void mousePressed()
	{
		gameStates[currentState].mousePressed();
	}
	
	
}









