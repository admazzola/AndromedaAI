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

	public void addToSwarm(SuperUnit mySuperUnit) {
		Swarm swarm = getSwarmFor(mySuperUnit.getUnit().getType());
		mySuperUnit.setSwarm(swarm);
		swarm.add(mySuperUnit);
		
	}

	private Swarm getSwarmFor(UnitType type) {
		
		
		
		return swarms.get(0); //temporary...
	}

	public void update() {
		Swarm mySwarm =  swarms.get(0);
		
		for(SuperUnit superunit:mySwarm.getUnits())
		{
			Unit unit = superunit.getUnit();
			
			unit.attack(mySwarm.getNextPosition() );
			
			
		}
		
	}
	 
	 
	 
	 
}
