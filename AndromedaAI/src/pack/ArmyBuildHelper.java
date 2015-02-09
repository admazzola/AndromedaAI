package pack;
import bwapi.UnitType;

   public  class ArmyBuildHelper
    {
    	int targetPopulation;
    	UnitType type;
    	UnitType prereq;
    	
    	ArmyBuildHelper(UnitType type, UnitType prereq, int n){
    		targetPopulation= n;
    		this.type = type;
    		this.prereq=prereq;
    	}

		public int getTargetPopulation() {
			return targetPopulation;
		}

		public UnitType getType() {
			return type;
		}
    	
		public UnitType getPrereq() {
			return prereq;
		}
    }
	
	
