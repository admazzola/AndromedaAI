import java.util.ArrayList;

import bwapi.*;
import bwta.BWTA;

public class AndromedaAI extends DefaultBWListener {

    private Mirror mirror = new Mirror();

    private Game game;

    private Player self;
    
    /*
     BuildOrderHelper[] buildOrder = new BuildOrderHelper[]{
    		
    		new BuildOrderHelper(UnitType.Zerg_Spawning_Pool,8)
    		
    }; */
     
     
   ArrayList<SuperUnit> allMyNonWorkerUnits = new ArrayList<SuperUnit>();
   ArrayList<SuperUnit> allMyLarvae = new ArrayList<SuperUnit>();
   ArrayList<SuperUnit> allMyEggs = new ArrayList<SuperUnit>();
   ArrayList<SuperUnit> allMyWorkers = new ArrayList<SuperUnit>();
    
    BuildingManager buildManager;
     
  

    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitDiscover(Unit unit)
    {
    	 System.out.println("Discovered unit " + unit.getType()); //enemy unit discovery
    	 
    	 
    }
    
    @Override
    public void onUnitCreate(Unit unit) {
    	
        System.out.println("New unit " + unit.getType());
        
      if(  unit.getPlayer() == self ){
        
        SuperUnit superunit = new SuperUnit(unit);
        
        
        if(unit.getType() == UnitType.Zerg_Larva)
        {
        	
        	allMyLarvae.add(superunit);
        }else  if(unit.getType() == UnitType.Zerg_Egg)
        {        	
        	allMyEggs.add(superunit);
        }else  if(unit.getType() == UnitType.Zerg_Drone)
        {
        	
        	allMyWorkers.add(superunit);
        }else{
        	
        	allMyNonWorkerUnits.add(superunit);
        }
      }
        //add to lists!?
        
    }
    
    @Override
    public void onUnitMorph(Unit unit)
    {
    	 if(  unit.getPlayer() == self ){
    	 System.out.println("New morph " + unit.getType());
    	 SuperUnit superunit = findSuperUnit(unit);
    	 
    	 if(unit.getType() == UnitType.Zerg_Drone)
         {    		
    		 allMyLarvae.remove(superunit);
         	allMyWorkers.add(superunit);
         }else if(unit.getType() == UnitType.Zerg_Egg)
         {
        	 allMyLarvae.remove(superunit);
        	 allMyEggs.add(superunit);
         }else{
        	 allMyEggs.remove(superunit);
         	allMyNonWorkerUnits.add(superunit);
         }
    	 }
    }
    
    
    @Override
	public void onUnitDestroy(Unit unit) {
    	 if(  unit.getPlayer() == self ){
    	 SuperUnit superunit = findSuperUnit(unit);
    	
         
         if(unit.getType() == UnitType.Zerg_Larva)
         {
         	allMyLarvae.remove(superunit);
         }
         else if(unit.getType() == UnitType.Zerg_Egg)
         {
        	 allMyEggs.remove(superunit);
         }
         
         else if(unit.getType() == UnitType.Zerg_Drone)
         {
         	allMyWorkers.remove(superunit);
         }
         else
         {
        	 allMyNonWorkerUnits.remove(superunit);
         }
    	 }
    }

    private SuperUnit findSuperUnit(Unit unit) { //hopefully this work?
		
    	for(int i=0;i<allMyLarvae.size();i++)
    	{
    		if(unit.equals(allMyLarvae.get(i).getUnit()) ){
    			
    			return allMyLarvae.get(i);
    		}
    	}
    	
    	for(int i=0;i<allMyEggs.size();i++)
    	{
    		if(unit.equals(allMyEggs.get(i).getUnit()) ){
    			return allMyEggs.get(i);
    		}
    	}
    	
    	for(int i=0;i<allMyWorkers.size();i++)
    	{
    		if(unit.equals(allMyWorkers.get(i).getUnit()) ){
    			return allMyWorkers.get(i);
    		}
    	}
    	
    	for(int i=0;i<allMyNonWorkerUnits.size();i++)
    	{
    		if(unit.equals(allMyNonWorkerUnits.get(i).getUnit()) ){
    			return allMyNonWorkerUnits.get(i);
    		}
    	}
    	
    	System.err.println("Found no super unit to destroy!");
		return null;
	}



	@Override
    public void onStart() {
    	
    	
        game = mirror.getGame();
        self = game.self();

        
        game.setLocalSpeed(20); //tourney speed
        
        
        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
        
        buildManager = new BuildingManager(game);

    }
    
  //  private HashSet enemyBuildingMemory = new HashSet();
    

    @Override
    public void onFrame() {
        game.setTextSize(10);
        game.drawTextScreen(10, 10, "AndyAI playing as " + self.getName() + " - " + self.getRace());

        StringBuilder units = new StringBuilder("My units:\n");

        
        for (SuperUnit myLarvae : allMyLarvae) {
        	//Unit myLarvae = anyLarvae.getUnit();
        	
        	
            	
            	 if (canAfford(UnitType.Zerg_Overlord) && needMoreOverlords()) {            		 
            		 myLarvae.train(UnitType.Zerg_Overlord);
            		 break;
            	 }
            	
            	if(canAfford(UnitType.Zerg_Drone) && needMoreWorkers()){            	
            		myLarvae.train(UnitType.Zerg_Drone);    
            	}
                
            
        }
        
      //  System.out.println( "num workers" + allMyWorkers.size());
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
        
        
        
        //iterate through my units
        for (SuperUnit anyUnit : allMyNonWorkerUnits) {
        	Unit myUnit = anyUnit.getUnit();
            units.append(myUnit.getType()).append(" ").append(myUnit.getTilePosition()).append("\n");

        }
        
        
        

        //draw my units on screen
        game.drawTextScreen(10, 25, units.toString());
        
              
        for( BuildOrderHelper helper : buildManager.getBuildOrder()  )
        {
        	
        	if(!haveUnit(helper.getBuildingType() ) && getWorkerCount() >= helper.getNumWorkersNeeded() ){
        		
        		if(canAfford(helper.getBuildingType())){
        			
        			Unit builder = getAvailableDrone();
        			
        			TilePosition buildTile = 
            				buildManager.getBuildTile(builder, helper.getBuildingType(), self.getStartLocation());
        			
        			if (buildTile != null) {
        			
        				builder.build(buildTile, helper.getBuildingType());
        				break;
        			}
        			
        			
        		}
        		
        		
        	}
        	
        	
        }
       
                
                    
        
        
    }



	//should build a list of workers and larvae to reduce computation time
    
    private int getWorkerCount() {
    			
		return allMyWorkers.size();
	}

	private Unit getAvailableDrone() {


        
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

	private boolean haveUnit(UnitType type) {
    	for (Unit myUnit : self.getUnits()) {
    		if (myUnit.getType() == type) {
    			return true;
    		}
    	}
		return false;
	}

	private boolean canAfford(UnitType type) {
		
		return self.minerals() >= type.mineralPrice() && self.gas() >= type.gasPrice();
	}
	
	
	public boolean needMoreOverlords()
	{
		
		
		return (self.supplyTotal() - self.supplyUsed() < 2) && beingTrainedCount(UnitType.Zerg_Overlord) == 0;		
		
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
    	
    	
    	   for( BuildOrderHelper helper : buildManager.getBuildOrder()  )
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
    

	
    
    public static void main(String[] args) {
        new AndromedaAI().run();
    }
}