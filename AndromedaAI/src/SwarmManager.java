import java.util.ArrayList;

import bwapi.Game;
import bwapi.Unit;
import bwapi.UnitType;


public class SwarmManager {

	Game game;
	 ArrayList<Swarm> swarms = new ArrayList<Swarm>();

	public SwarmManager(Game game) {
		this.game=game;
		
		if(swarms.isEmpty())
		{
			swarms.add(new Swarm());
		}
	}

	public void addToSwarm(Unit unit) {
		Swarm swarm = getSwarmFor(unit.getType());
		
		swarm.add(unit);
		
	}

	private Swarm getSwarmFor(UnitType type) {
		
		
		
		return swarms.get(0); //temporary...
	}

	public void update() {
		Swarm mySwarm =  swarms.get(0);
		
		for(Unit unit:mySwarm.getUnits())
		{
			
			
			unit.attack(mySwarm.getNextPosition() );
			
			
		}
		
	}

	public boolean unitSwarmless(Unit unit) {
		
		return !swarms.get(0).units.contains(unit);
	}
	 
	 
	 
	 
}
