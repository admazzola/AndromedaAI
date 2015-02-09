package pack;
import java.util.ArrayList;
import java.util.HashSet;

import pack.Swarm.SwarmStrategy;
import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;


public class ArmyManager {
	
	 ArrayList<ArmyBuildHelper> buildHelper = new ArrayList<ArmyBuildHelper>();
	 
	
	 ArrayList<Swarm> swarms = new ArrayList<Swarm>();
	 
	
	 
	 
	 HashSet<BaseInformation> bases = new HashSet<BaseInformation>();
	 
	 HashSet<EnemyBuildingDataStore> enemyBuildingMemory = new HashSet<EnemyBuildingDataStore>();
	 
	 
	 Game game;
		
	 public ArmyManager(Game game) {
		 this.game=game;
			
			if(swarms.isEmpty())
			{
				swarms.add(new Swarm());
			}
			
			init();
			
			
		}

		private void init() {



			for (BaseLocation b : BWTA.getBaseLocations()) {
				// If this is a possible start location,
				if (b.isStartLocation()) {
					// do something. For example send some unit to attack that position:
					BaseInformation newBaseInfo = new BaseInformation( b );
					bases.add( newBaseInfo );
					System.out.println("added new base " + newBaseInfo);
				}
			}
			
			
			
			buildHelper.add(new ArmyBuildHelper(UnitType.Zerg_Zergling, UnitType.Zerg_Spawning_Pool, 16));
			buildHelper.add(new ArmyBuildHelper(UnitType.Zerg_Hydralisk, UnitType.Zerg_Spawning_Pool, 10));
		}

		public  ArrayList<ArmyBuildHelper> getBuildHelper() {
			return buildHelper;
		}
		
		

		public void update(long tpf) {
			
			
						
			
			
			for(BaseInformation base : bases) {			
				
				if( base.isExplored() == false && base.hasScout() == false)
				{
					for(Unit unit : getNonWorkerUnits())
					{

						if(unitIsFighter(unit.getType())){
							if(unitSwarmless(unit))
							{							
								System.out.println(" assigned scout!");
								addToNewScoutSwarm(unit,base);
								
								base.setScout(unit);								
								
								break;
							}
						}
					}
					
				}
			}
						
			
			
			for(Unit unit : getNonWorkerUnits())
			{
			
				
				if(unitIsFighter(unit.getType())){
					if(unitSwarmless(unit))
					{
						
						addToSwarm(unit);
													
					}
					
					
				}
				
				
			}
			
			updateEnemyInformation(tpf);
			
			
			
			updateSwarms(tpf);
		
			
		}



		private UnitManager getUnitManager() {
			return AndromedaAI.getUnitManager();
			
		}

		private boolean unitIsFighter(UnitType type) {
			
			return type == UnitType.Zerg_Zergling
					|| type == UnitType.Zerg_Hydralisk
					|| type == UnitType.Zerg_Mutalisk
					|| type == UnitType.Zerg_Lurker;
		}

		private ArrayList<Unit> getNonWorkerUnits() {
			return AndromedaAI.getUnitManager().allMyUnits;
			
		}


		public void addToSwarm(Unit unit) {
			Swarm swarm = getSwarmFor(unit.getType());
			
			swarm.add(unit);
			
		}
		
		private void addToNewScoutSwarm(Unit unit, BaseInformation baseToAssignScout) {
			Swarm scoutswarm = new Swarm();
			scoutswarm.setTargetPosition(  baseToAssignScout.getPosition()  );
			scoutswarm.add(unit);
			scoutswarm.setStrategy(SwarmStrategy.SCOUT);
			swarms.add(scoutswarm);
		}
		
		
		

		private Swarm getSwarmFor(UnitType type) {
			
			
			
			return swarms.get(0); //temporary...
		}


		

		private void updateEnemyInformation(long tpf) {

			//always loop over all currently visible enemy units (even though this set is usually empty)
			for (Unit u : game.enemy().getUnits()) {
				//if this unit is in fact a building
				if (u.getType().isBuilding()) {
					//check if we have it's position in memory and add it if we don't
					if (!enemyBuildingMemory.contains(u))
						{
							enemyBuildingMemory.add(new EnemyBuildingDataStore(u , u.getPosition()  )  );
						}
					
					
					
					
				}
			}
			
		}

		private void updateTargetPosition(long counter) {  

			/*
			for(BaseInformation baseInfo : bases) {
				System.out.println("set next base to explore to " + baseInfo);
				nextBaseToExplore = baseInfo;
				break;
			}*/
			
			
			
			
			
			
		}

		
		
		
		private void updateSwarms(long tpf) {

			Swarm myFirstSwarm =  swarms.get(0);
			
			if(  getBuildingPositionToAttack() !=null)
			{
				myFirstSwarm.setTargetPosition( getBuildingPositionToAttack());		
			}
			
			
			for(Swarm swarm : swarms)
			{
				swarm.update(tpf);
				
			}
			
			//myFirstSwarm.selectNextSubList(game);
			//UnitCommand command = new UnitCommand();
			//game.issueCommand(game.getSelectedUnits(), UnitCommandType.Move);
			
			
			
			
		}

		public boolean unitSwarmless(Unit unit) {
			
			for(Swarm swarm : swarms)
			{
				if(swarm.units.contains(unit) )
				{
					return false;
										
				}
								
			}
			
			return true;
			
			
		}

		public void unitDied(Unit unit) {  //this is threaded!

			for(Swarm swarm : swarms)
			{
				if(swarm.getUnits().contains(unit))
				{
					

					swarm.queueUnitRemoval(unit);
					
				}
			}
			
			
		}
		 
		 
		 public Position getBuildingPositionToAttack()
		 {
			 for(EnemyBuildingDataStore building : enemyBuildingMemory)
			 {
				return building.getLastPosition();
				 
				 
			 }
			 
			 return null;
		 }
		 
}
