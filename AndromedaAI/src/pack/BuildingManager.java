package pack;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import bwapi.*;

public class BuildingManager {

	ArrayList<BuildOrderHelper> buildOrder = new ArrayList<BuildOrderHelper>();

	Game game;

	public BuildingManager(Game game) {
		this.game = game;

		init();

	}

	Map<UnitType, UnitType> buildingMorphsFrom = new HashMap<UnitType, UnitType>();

	private void init() {

		

		buildOrder.add(new BuildOrderHelper(UnitType.Zerg_Spawning_Pool, 5));
		
		
		buildOrder.add(new BuildOrderHelper(UnitType.Zerg_Extractor, 7));

		buildOrder.add(new BuildOrderHelper(UnitType.Zerg_Lair, 10));
		
		buildOrder.add(new BuildOrderHelper(UnitType.Zerg_Spire, 16));
		
		
		

		buildingMorphsFrom.put(UnitType.Zerg_Lair, UnitType.Zerg_Hatchery);

	}

	// Returns a suitable TilePosition to build a given building type near
	// specified TilePosition aroundTile, or null if not found. (builder
	// parameter is our worker)
	public TilePosition getBuildTile(Unit builder, UnitType buildingType, TilePosition aroundTile) {
		TilePosition ret = null;
		int maxDist = 3;
		int stopDist = 40;

		// Refinery, Assimilator, Extractor
		if (buildingType.isRefinery()) {
			for (Unit n : game.neutral().getUnits()) {
				if ((n.getType() == UnitType.Resource_Vespene_Geyser)
						&& (Math.abs(n.getTilePosition().getX() - aroundTile.getX()) < stopDist)
						&& (Math.abs(n.getTilePosition().getY() - aroundTile.getY()) < stopDist))
					return n.getTilePosition();
			}
		}

		while ((maxDist < stopDist) && (ret == null)) {
			for (int i = aroundTile.getX() - maxDist; i <= aroundTile.getX() + maxDist; i++) {
				for (int j = aroundTile.getY() - maxDist; j <= aroundTile.getY() + maxDist; j++) {
					if (game.canBuildHere(builder, new TilePosition(i, j), buildingType, false)) {
						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : game.getAllUnits()) {
							if (u.getID() == builder.getID())
								continue;
							if ((Math.abs(u.getTilePosition().getX() - i) < 4)
									&& (Math.abs(u.getTilePosition().getY() - j) < 4))
								unitsInWay = true;
						}
						if (!unitsInWay) {
							return new TilePosition(i, j);
						}
						// creep for Zerg
						if (buildingType.requiresCreep()) {
							boolean creepMissing = false;
							for (int k = i; k <= i + buildingType.tileWidth(); k++) {
								for (int l = j; l <= j + buildingType.tileHeight(); l++) {
									if (!game.hasCreep(k, l))
										creepMissing = true;
									break;
								}
							}
							if (creepMissing)
								continue;
						}
					}
				}
			}
			maxDist += 2;
		}

		if (ret == null)
			game.printf("Unable to find suitable build position for " + buildingType.toString());
		return ret;
	}

	public ArrayList<BuildOrderHelper> getBuildOrder() {
		return buildOrder;
	}

	public BuildOrderHelper getCurrentBuildOrder() {

		for (BuildOrderHelper helper : getBuildOrder()) {

			if (!AndromedaAI.getUnitManager().haveUnit(helper.getBuildingType())) {
				return helper;

			}
		}

		return null;
	}

	public void update(long tpf) {

		updateBuildingProgression(tpf);

	}

	long buildCounter = 0;

	private void updateBuildingProgression(long tpf) {
		buildCounter += tpf;

		if (buildCounter > 1000) {

			buildCounter = 0;

			BuildOrderHelper helper = getCurrentBuildOrder();

			if (helper != null) {
				
				
				if( helper.buildingType == UnitType.Zerg_Hatchery)
				{
					
					//find good spot!
					
				}else if (!buildingMorphsFrom.containsKey(helper.buildingType)) 
				{

					if (getUnitManager().getWorkerCount() >= helper.getNumWorkersNeeded()) {

						if (getUnitManager().canAfford(helper.getBuildingType())) {

							Unit builder = getUnitManager().getAvailableDrone();

							TilePosition buildTile = getBuildTile(builder, helper.getBuildingType(), game.self()
									.getStartLocation());

							if (buildTile != null) {

								builder.build(buildTile, helper.getBuildingType());

							}
						}
					}

				} else {
					Unit morpher = getUnitManager().getUnitOfType(buildingMorphsFrom.get(helper.buildingType));

					if (getUnitManager().canAfford(helper.getBuildingType())) {
						morpher.morph(helper.buildingType);

					}

				}
			}
		}
	}

	private UnitManager getUnitManager() {
		return AndromedaAI.getUnitManager();
	}

}
