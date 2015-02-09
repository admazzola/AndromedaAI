package pack;

import bwapi.Game;
import bwapi.Unit;

/*  
 * 'Randomizes' arbitrary parameters and records 'points of interest' from those parameters
 * For example, this class will define a certain build order and will record the time it took to first spawn zerglings, or the K/D ratio of tactics.. etc
 * 
 * This data will be used to create the 'random' arbitrary parameters in the future so they will be more estimated, closer and closer to optimal values
 * 
 */

public class MutagenicStrategyManager {
	
	
	Game game;
	
	long timestampGameStart;
	
	
	
	int parameters[] = new int[100];
	

	public MutagenicStrategyManager(Game game) {
		this.game=game;
		timestampGameStart = System.currentTimeMillis();
		
		init();
	}

	private void init() {
		parameters[StrategyParameters.NUM_SCOUTS] = 4;
		
	}

	public int getParam(int index)
	{
		return parameters[index];
	}
	
	
	public void gameEnded(boolean won) {
	
		log("Game ended after "+ (System.currentTimeMillis() - timestampGameStart)/1000 + " seconds");
		
	}
	
	/*
	 * PARAMETERS:
	 * 
	 * tech build order (pool at 5? 9? against which races?)
	 * number of scouting parties
	 * size of scouting parties
	 * when to attack
	 * units to counter which other units
	 * 
	 * 
	 * 
	 */
	
	
	
	
	
	void log(Object o)
	{
		System.out.println(o);
	}

	public void unitDied(Unit unit) {
		
		if( unit.getPlayer() == game.self() )
		{
			
			
		}else{
			
			
			
		}
		
	}
	
	

}
