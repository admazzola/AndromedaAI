package pack;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bwapi.*;
import bwta.BWTA;

public class AndromedaAI extends DefaultBWListener {

    private Mirror mirror = new Mirror();

    private Game game;

    private Player self;
    

    static MutagenicStrategyManager stratManager;
    
    static BuildingManager buildManager;
     
    static ArmyManager armyManager;
    
    static UnitManager unitManager;

    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitDiscover(Unit unit)
    {
    	 System.out.println("Discovered unit " + unit.getType()); //enemy unit discovery
    	 
    	 
    }
    
   // @Override
   // public void onUnitComplete(Unit unit){
    	
    	//onUnitCreate(unit);
   // }
    
    @Override
    public void onUnitCreate(Unit unit) {
    	
        System.out.println("New unit " + unit.getType());
        
        if(  unit.getPlayer() == self ){
        	
        	getUnitManager().registerUnit(unit);
        
        }
        
        
      
        //add to lists!?
        
    }
    
    @Override
    public void onUnitMorph(Unit unit)
    {
    	
    	   if(  unit.getPlayer() == self ){
           	
           	getUnitManager().registerUnit(unit);
           
           }
    	   
    	   /*
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
         }
    	 
    	 
    	 if(!allMyUnits.contains(superunit))
    	 {
    	 allMyUnits.add(superunit);
    	 }
    	 
        
    	 } */
    }
    
    
    @Override
	public void onUnitDestroy(Unit unit) {
    	

 	   if(  unit.getPlayer() == self ){
 		 
        	getUnitManager().unRegisterUnit(unit);
        
        }
 	   
 	  stratManager.unitDied(unit);
 	  
 	  armyManager.unitDied(unit);
 	   
    	/*
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
         //else
         //{
        	 allMyUnits.remove(superunit);
         //}
    	 }*/
    }


	@Override
    public void onStart() {
    	
    	
        game = mirror.getGame();
        self = game.self();

        
        game.setLocalSpeed(20); //tourney speed..lower is faster
        
        
        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
        
        buildManager = new BuildingManager(game);
        armyManager = new ArmyManager(game);
        unitManager = new UnitManager(game);
        
        stratManager = new MutagenicStrategyManager(game);
        
    }
    
  //  private HashSet enemyBuildingMemory = new HashSet();
   
	@Override
	public void onEnd(boolean won)
	{
		stratManager.gameEnded(won);
	}
	
	
	
	long last_millis = System.currentTimeMillis();

    @Override
    public void onFrame() {
    	
    	    	
    	long tpf = System.currentTimeMillis() - last_millis;
    	last_millis = System.currentTimeMillis();
    	
    	
    
        game.setTextSize(10);
        game.drawTextScreen(10, 10, "AndromedaAI - " + tpf  + " - " + self.getRace());

        //StringBuilder units = new StringBuilder("My units:\n");

        //draw my units on screen
        game.drawTextScreen(10, 30, "workers:" + getUnitManager().getWorkerCount());
        game.drawTextScreen(10, 50, "gas harvesters:" + getUnitManager().countGasHarvesters());
        
        game.drawTextScreen(10, 70, "num swarms:" + getArmyManager().swarms.size());
        
        game.drawTextScreen(10, 90, "num enemy buildings:" + getArmyManager().enemyBuildingMemory.size() );
        
        if(buildManager.getCurrentBuildOrder()!=null)
        	game.drawTextScreen(10, 110, "tech:" +  buildManager.getCurrentBuildOrder().getBuildingType());
        
        
        
        armyManager.update(tpf);
        
        unitManager.update(tpf);
    
        
        buildManager.update(tpf);
        
        	
        	
        
       
                
                    
        
        
    }


	
    
    public static void main(String[] args) {
        new AndromedaAI().run();
    }

	public static UnitManager getUnitManager() {
		return unitManager;
	}

	public static ArmyManager getArmyManager() {
		// TODO Auto-generated method stub
		return armyManager;
	}

	public static BuildingManager getBuildManager() {
		// TODO Auto-generated method stub
		return buildManager;
	}
}