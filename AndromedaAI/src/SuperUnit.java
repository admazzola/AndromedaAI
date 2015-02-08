import java.util.ArrayList;

import bwapi.Unit;
import bwapi.UnitType;


public class SuperUnit{
	Unit unit;
	
	Swarm mySwarm;
	
	UnitType unitTraining = null;
	
	public SuperUnit(Unit unit) {
		this.unit=unit;
	}

	public UnitType getUnitTraining() {
		return unitTraining;
	}

	public boolean train(UnitType type){
		unitTraining= type;
		
		return unit.train(type);
	}

	public Unit getUnit() {
		return unit;
	}

	public void setSwarm(Swarm swarm) {
		mySwarm= swarm;
		
	}

	public Swarm getSwarm() {
		
		return mySwarm;
	}
	
}
