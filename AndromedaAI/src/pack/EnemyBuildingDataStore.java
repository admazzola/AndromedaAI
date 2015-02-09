package pack;

import bwapi.Position;
import bwapi.Unit;

public class EnemyBuildingDataStore {
	
	Unit building;
	Position lastPosition;
	
	public EnemyBuildingDataStore(Unit unit, Position position) {
		building = unit;
		lastPosition = position;
	}

	public Position getLastPosition() {
		return lastPosition;
	}
	
	
}
