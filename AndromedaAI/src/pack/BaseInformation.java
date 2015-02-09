package pack;

import bwapi.Position;
import bwapi.Unit;
import bwta.BaseLocation;

public class BaseInformation {

	BaseLocation location;
	
	Unit scout = null;
	
	boolean explored = false;
	boolean hasEnemyBuildings = false;
	
	public BaseInformation(BaseLocation b) {
		location = b;
	}

	public Position getPosition()
	{
		return location.getPosition();
	}
	
	
	
	public String toString()
	{
		return "" + getPosition();
		
	}

	public boolean isExplored() {
		
		return explored;
	}

	public boolean hasScout() {
		
		return scout != null;
	}

	public void setScout(Unit unit) {
		 scout = unit;
		
	}
	
	
}
