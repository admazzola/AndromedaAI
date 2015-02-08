import java.util.ArrayList;

import bwapi.Game;
import bwapi.Unit;
import bwapi.UnitType;


public class UnitManager {
	
	    
	  public   ArrayList<SuperUnit> allMyUnits = new ArrayList<SuperUnit>();
	  public  ArrayList<SuperUnit> allMyLarvae = new ArrayList<SuperUnit>();
	  public   ArrayList<SuperUnit> allMyEggs = new ArrayList<SuperUnit>();
	  public  ArrayList<SuperUnit> allMyWorkers = new ArrayList<SuperUnit>();
	  
	  Game game;

	public UnitManager(Game game) {
		this.game=game;
	}





	public void update() {


    
        
        for(ArmyBuildHelper buildHelper :  AndromedaAI.getArmyManager().getBuildHelper()  )
        {
        	if(canAfford(buildHelper.getType()) && getUnitCount(buildHelper.getPrereq() ) > 0 && getUnitCount(buildHelper.getType()) < buildHelper.getTargetPopulation())
        		{
        			getLarvae().train( buildHelper.getType() );
        		}
        	
        }
        
        if (canAfford(UnitType.Zerg_Overlord) && needMoreOverlords()) { 
        	getLarvae().train(UnitType.Zerg_Overlord);
        	
        }
   	
  
        if(canAfford(UnitType.Zerg_Drone) && needMoreWorkers()){            	
        	getLarvae().train(UnitType.Zerg_Drone);    
        }
   	
        
        for (SuperUnit anyWorker : allMyWorkers) {
        	Unit myWorker = anyWorker.getUnit();
        
        	if(countGasHarvesters() < 3 && getUnitCount(UnitType.Zerg_Extractor) > 0 ) //need to have more gas harvesters.. maybe
        	{
        		System.out.println("trying to gather gas!!!!");
        		if(!myWorker.isGatheringGas()){   //this could cause issues with many refineies?
        			myWorker.gather( getNearbyRefinery(myWorker)   );
        			break;
        		}
        	}
       	
        }
        
        for (SuperUnit anyWorker : allMyWorkers) {
        	Unit myWorker = anyWorker.getUnit();
                	

            //if it's a drone and it's idle, send it to the closest mineral patch
            if (myWorker.isIdle()) {
                Unit closestMineral = null;

                //find the closest mineral
                for (Unit neutralUnit : game.neutral().getUnits()) {
                    if (neutralUnit.getType().isMineralField()) {
                        if (closestMineral == null || myWorker.getDistance(neutralUnit) < myWorker.getDistance(closestMineral)) {
                            closestMineral = neutralUnit;
                        }
                    }
                }

                //if a mineral patch was found, send the drone to gather it
                if (closestMineral != null) {
                	myWorker.gather(closestMineral, false);
                }
            }
            
            
            
            
        }
        
        
		
	}

	
	


	private Unit getNearbyRefinery(Unit myWorker) {

		 for (SuperUnit anyUnit : allMyUnits) {
	        	Unit myUnit = anyUnit.getUnit();
	        	if(myUnit.getType().isRefinery()){
	        		return myUnit;
	        	}
		 }
		 
		return null;
	}

	int countGasHarvesters() {
		int count = 0;

	    for (SuperUnit anyWorker : allMyWorkers) {
        	Unit myWorker = anyWorker.getUnit();
        	if(myWorker.isGatheringGas())
        	{
        		count++;
        	}
	    }
        			
		return count;
	}

	private int getUnitCount(UnitType type) {
		int count= 0;
		
		 for (SuperUnit myUnit : allMyUnits) {
			 	Unit unit = myUnit.getUnit();
			 	if(unit.getType().equals(type))
			 	{
			 		count++;
			 	}
			 
		 }
		
		return count;
	}

	private SuperUnit getLarvae() {
        for (SuperUnit myLarvae : allMyLarvae) {
        	return myLarvae;
        }
        
	return null;
}

	//should build a list of workers and larvae to reduce computation time
    
    int getWorkerCount() {
    			
		return allMyWorkers.size();
	}

	Unit getAvailableDrone() {


        
        for (SuperUnit myWorker : allMyWorkers) {
        	Unit myUnit = myWorker.getUnit();
       //try to get an idle worker
        	if ( myUnit.isIdle()) {
        	
        		return myUnit;
        	}
        
        }
        
        for (SuperUnit myWorker : allMyWorkers) {
        	Unit myUnit = myWorker.getUnit();

        	//try to get any worker
                        	
            		return myUnit;
            	            
            }
        
        
		return null;
	}

	boolean haveUnit(UnitType type) {
		for(SuperUnit mySuperUnit : allMyUnits)
		{
			Unit unit = mySuperUnit.getUnit();
			if (unit.getType().equals(type)) {
    			return true;
    		}
		}
		
    	
		return false;
	}

	boolean canAfford(UnitType type) {
		
		return game.self().minerals() >= type.mineralPrice() && game.self().gas() >= type.gasPrice();
	}
	
	
	public boolean needMoreOverlords()
	{
		
		
		return (game.self().supplyTotal() - game.self().supplyUsed() < 2) && beingTrainedCount(UnitType.Zerg_Overlord) == 0;		
		
	}

	private int beingTrainedCount(UnitType type) {
		int count =0;
		for (SuperUnit myEgg : allMyEggs) {
			//Unit myUnit = myLarvae.getUnit(); 
    		
    			if( type.equals(myEgg.getUnitTraining()) ){
    				count++;
    			}
    		
		}
		
		return count;
	}



	public boolean needMoreWorkers()
    {
    		
		BuildOrderHelper nextBuildingOrder = null;
    	
    	
    	   for( BuildOrderHelper helper : AndromedaAI.getBuildManager().getBuildOrder()  )
           {
           		if(!haveUnit(helper.getBuildingType() )  ){
           			nextBuildingOrder = helper;
           				break;
           		}
           
           	
           	}
           	
    	   if(nextBuildingOrder != null)
    	   {
    		 
    		   return getWorkerCount() < nextBuildingOrder.getNumWorkersNeeded();
    	   }
           	 
           	
           	
    	return false;
    	
    	
    }
    

    private SuperUnit findSuperUnit(Unit unit) {
		
   
    	for(int i=0;i<allMyUnits.size();i++)
    	{
    		if(unit.equals(allMyUnits.get(i).getUnit()) ){
    			return allMyUnits.get(i);
    		}
    	}
    	
    	System.err.println("Found no super unit to destroy!");
		return new SuperUnit(unit);
	}





	public void registerUnit(Unit unit) {



        SuperUnit superunit = findSuperUnit(unit);
        
        removeUnitFromAllLists(superunit  );
        
        if(unit.getType() == UnitType.Zerg_Larva)
        {        	
        	allMyLarvae.add(superunit);        	
        }else  if(unit.getType() == UnitType.Zerg_Egg)
        {        	
        	allMyEggs.add(superunit);
        }else  if(unit.getType() == UnitType.Zerg_Drone)
        {
        	System.out.println("added worker " + unit);
        	allMyWorkers.add(superunit);
        }
        
        
        allMyUnits.add(superunit);
        System.out.println("added allunit " + unit);
       
        
        
	}





	private void removeUnitFromAllLists(SuperUnit superunit) {
		allMyLarvae.remove(superunit);
		allMyEggs.remove(superunit);
		allMyWorkers.remove(superunit);
		allMyUnits.remove(superunit);
	}





	public void unRegisterUnit(Unit unit) {
		 SuperUnit superunit = findSuperUnit(unit);
		removeUnitFromAllLists(superunit);
		
	}


}
