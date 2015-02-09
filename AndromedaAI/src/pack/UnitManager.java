package pack;
import java.util.ArrayList;

import bwapi.Game;
import bwapi.Unit;
import bwapi.UnitType;


public class UnitManager {
	
	    
	  public   ArrayList<Unit> allMyUnits = new ArrayList<Unit>();
	  
	  public  ArrayList<Unit> allMyLarvae = new ArrayList<Unit>();
	  public   ArrayList<Unit> allMyEggs = new ArrayList<Unit>();
	  public  ArrayList<Unit> allMyWorkers = new ArrayList<Unit>();
	  
	  Game game;

	public UnitManager(Game game) {
		this.game=game;
	}

	long counter = 0;

	public void update(long tpf) {
		counter+=tpf;
		


		if(getLarvae()!=null)
		{
        
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
        
		}
		
		if(counter > 1000)
		{
			counter=0;
			
			for (Unit myWorker : allMyWorkers) {
		        
	        	if(countGasHarvesters() < 3 && getUnitCount(UnitType.Zerg_Extractor) > 0 ) //gets laggy ? switchesa lot? maybe the swarm movin is laggy
	        	{
	        		System.out.println("trying to gather gas!!!!");
	        		if(!myWorker.isGatheringGas()){   //this could cause issues with many refineies?
	        			myWorker.gather( getNearbyRefinery(myWorker)   );
	        			break;
	        		}
	        	}
	       	
	        }
			
		}
		
        
        
        
        for (Unit myWorker : allMyWorkers) {
                	

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

		 for (Unit myUnit : allMyUnits) {
	        	
	        	if(myUnit.getType().isRefinery()){
	        		return myUnit;
	        	}
		 }
		 
		return null;
	}

	int countGasHarvesters() {
		int count = 0;

	    for (Unit myWorker : allMyWorkers) {
        	
        	if(myWorker.isGatheringGas())
        	{
        		count++;
        	}
	    }
        			
		return count;
	}

	private int getUnitCount(UnitType type) {
		int count= 0;
		
		 for (Unit unit : allMyUnits) {
			 	if(unit.getType().equals(type))
			 	{
			 		count++;
			 	}
			 
		 }
		
		return count;
	}

	private Unit getLarvae() {
        for (Unit myLarvae : allMyLarvae) {
        	return myLarvae;
        }
        
	return null;
}

	//should build a list of workers and larvae to reduce computation time
    
    int getWorkerCount() {
    			
		return allMyWorkers.size();
	}

	Unit getAvailableDrone() {


        
        for (Unit myUnit : allMyWorkers) {
        
       //try to get an idle worker
        	if ( myUnit.isIdle()) {
        	
        		return myUnit;
        	}
        
        }
        
        for (Unit myUnit : allMyWorkers) {
        	

        	//try to get any worker
                        	
            		return myUnit;
            	            
            }
        
        
		return null;
	}
	

	public Unit getUnitOfType(UnitType type) {
	
		for(Unit unit : allMyUnits)
		{
			
			if (unit.getType().equals(type)) {
				return unit;
			}
		}
		
		
		return null;
	}


	boolean haveUnit(UnitType type) {	
    	
		return getUnitOfType(type) != null;
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
		for (Unit myEgg : allMyEggs) {
			//Unit myUnit = myLarvae.getUnit(); 
    		
    			if( myEgg.getBuildType().equals(type )   ){
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
    
/*
    private Unit findSuperUnit(Unit unit) {
		
   
    	for(int i=0;i<allMyUnits.size();i++)
    	{
    		if(unit.equals(allMyUnits.get(i).getUnit()) ){
    			return allMyUnits.get(i);
    		}
    	}
    	
    	System.err.println("Found no super unit to destroy!");
		return new SuperUnit(unit);
	}

*/



	public void registerUnit(Unit unit) {



    //    SuperUnit superunit = findSuperUnit(unit);
        
        removeUnitFromAllLists(unit  );
        
        if(unit.getType() == UnitType.Zerg_Larva)
        {        	
        	allMyLarvae.add(unit);        	
        }else  if(unit.getType() == UnitType.Zerg_Egg)
        {        	
        	allMyEggs.add(unit);
        }else  if(unit.getType() == UnitType.Zerg_Drone)
        {
        	System.out.println("added worker " + unit);
        	allMyWorkers.add(unit);
        }
        
        
        allMyUnits.add(unit);
        System.out.println("added allunit " + unit);
       
        
        
	}





	private void removeUnitFromAllLists(Unit unit) {
		allMyLarvae.remove(unit);
		allMyEggs.remove(unit);
		allMyWorkers.remove(unit);
		allMyUnits.remove(unit);
	}





	public void unRegisterUnit(Unit unit) {
		
		removeUnitFromAllLists(unit);
		
	}






}
