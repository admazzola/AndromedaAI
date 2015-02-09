package pack;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pack.Swarm.SwarmStrategy;
import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;


public class Swarm {

	Position nextPosition = null;
	
	ArrayList<Unit> units = new ArrayList<Unit>();
	
	SwarmStrategy strategy = SwarmStrategy.ATTACK;
	

	public ArrayList<Unit> getUnits() {
		return units;
	}

	public Position getNextPosition() {
		
		return nextPosition;
	}
	
	public Position getCurrentPosition() {
		
		if(!units.isEmpty()){
			return units.get(0).getPosition();
		}
		
		return null;
	}

	public void add(Unit myUnit) {
		units.add(myUnit);
		
	}

	int nextSubListToSelect = 0;
	public void selectNextSubList(Game game) {
		
		int lowerBound = nextSubListToSelect*12;
		int upperBound = (nextSubListToSelect+1) * 12;
		
		if(  upperBound  > getSize() )
		{
			upperBound = getSize();
		}
		
		game.getSelectedUnits().addAll( getUnits().subList(lowerBound, upperBound));
		
		int maxSubLists = (int) Math.floor(getSize() / 12f);	
		
		nextSubListToSelect++;
		
		if(nextSubListToSelect > maxSubLists)
		{
			nextSubListToSelect = maxSubLists;
		}
		
		
	}

	private int getSize() {
		return getUnits().size();		
	}

	
	public void update(long tpf)
	{
		cleanUp(tpf);
		sendCommands(tpf);
		
	}
	
	
	private void cleanUp(long tpf) {
		for(Unit unit: unitsToRemove)
		{
			getUnits().remove(unit);
			
			System.out.println("Removeda "+ unit + " from " + this);
		}
		unitsToRemove.clear();
	}

	public void sendCommands(long tpf) {

		if(getNextPosition()!=null /*&& !alreadyInPosition()*/ )
		{
			for(Unit unit: getUnits())
			{
				if(unit.isIdle())
				{
					unit.attack( getNextPosition() );	
				}
			}
		}
		
		
	}

	private boolean alreadyInPosition() {//crashes!
		return getNextPosition().getApproxDistance(getCurrentPosition()) <= 3;
	}

	public void setTargetPosition(Position position) {
		
		nextPosition = new Position( position.getX(), position.getY());//clone
		
	}

	
	public enum SwarmStrategy
	{
		SCOUT,
		DEFEND,
		ATTACK,
		
		;
		
	}


	public void setStrategy(SwarmStrategy strat) {
		strategy = strat;
		
	}

	
	
	Set<Unit> unitsToRemove = new HashSet<Unit>();
	public void queueUnitRemoval(Unit unit) {
		unitsToRemove.add(unit);

		
	}
	
	
	
	
	
}
