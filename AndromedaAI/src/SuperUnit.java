import bwapi.Unit;
import bwapi.UnitType;


public class SuperUnit{
	Unit unit;

	
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
	
}
