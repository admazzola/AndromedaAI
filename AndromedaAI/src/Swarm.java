import java.util.ArrayList;

import bwapi.Position;


public class Swarm {

	
	ArrayList<SuperUnit> units = new ArrayList<SuperUnit>();

	public ArrayList<SuperUnit> getUnits() {
		return units;
	}

	public Position getNextPosition() {
		
		return new Position(0,0);
	}

	public void add(SuperUnit mySuperUnit) {
		units.add(mySuperUnit);
		
	}
	
	
}
