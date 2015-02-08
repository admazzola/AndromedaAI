import java.util.ArrayList;

import bwapi.Position;
import bwapi.Unit;


public class Swarm {

	
	ArrayList<Unit> units = new ArrayList<Unit>();

	public ArrayList<Unit> getUnits() {
		return units;
	}

	public Position getNextPosition() {
		
		return new Position(0,0);
	}

	public void add(Unit myUnit) {
		units.add(myUnit);
		
	}
	
	
}
