import java.util.ArrayList;

import bwapi.Game;
import bwapi.Unit;
import bwapi.UnitType;


public class ArmyManager {
	
	 ArrayList<ArmyBuildHelper> buildHelper = new ArrayList<ArmyBuildHelper>();
	 
	
	SwarmManager swarmManager;
	 
	 Game game;
		
	 public ArmyManager(Game game) {
			this.game = game;
			
			init();
			
			
		}

		private void init() {
			swarmManager = new SwarmManager(game);
			buildHelper.add(new ArmyBuildHelper(UnitType.Zerg_Zergling, UnitType.Zerg_Spawning_Pool, 16));
			buildHelper.add(new ArmyBuildHelper(UnitType.Zerg_Hydralisk, UnitType.Zerg_Spawning_Pool, 10));
		}

		public  ArrayList<ArmyBuildHelper> getBuildHelper() {
			return buildHelper;
		}

		public void update() {
			for(SuperUnit mySuperUnit : getNonWorkerUnits())
			{
				Unit unit = mySuperUnit.getUnit();
				
				if(unitIsFighter(unit.getType())){
					if(mySuperUnit.getSwarm() == null)
					{
						
						swarmManager.addToSwarm(mySuperUnit);
						
					}
					
					
				}
				
				
			}
			
			swarmManager.update();
		
			
		}

		private boolean unitIsFighter(UnitType type) {
			
			return type == UnitType.Zerg_Zergling
					|| type == UnitType.Zerg_Hydralisk
					|| type == UnitType.Zerg_Mutalisk
					|| type == UnitType.Zerg_Lurker;
		}

		private ArrayList<SuperUnit> getNonWorkerUnits() {
			return AndromedaAI.getUnitManager().allMyUnits;
			
		}


}
