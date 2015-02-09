package pack;
import bwapi.UnitType;

   public  class BuildOrderHelper
    {
    	int numWorkers;
    	UnitType buildingType;
    	
    	BuildOrderHelper(UnitType type, int n){
    		numWorkers= n;
    		buildingType = type;
    	}

		public int getNumWorkersNeeded() {
			return numWorkers;
		}

		public UnitType getBuildingType() {
			return buildingType;
		}
    	
    	
    }
	
	
