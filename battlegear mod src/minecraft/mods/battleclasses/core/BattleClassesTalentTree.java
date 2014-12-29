package mods.battleclasses.core;

import java.util.ArrayList;

import mods.battleclasses.ability.BattleClassesAbstractTalent;

public class BattleClassesTalentTree {
	
	protected BattleClassesTalentMatrix ownerTalentMatrix;
	
	public void setOwnerTalentMatrix(BattleClassesTalentMatrix parTalentMatrix) {
		this.ownerTalentMatrix = parTalentMatrix;
		
		for(BattleClassesAbstractTalent talent : talentList) {
			talent.setParentTree(this);
			talent.setPlayerHooks(parTalentMatrix.playerHooks);
		}
	}
	
	public String name = "";
	
	public void setName(String parName) {
		name = parName;
	}
	
	public BattleClassesTalentMatrix getOwnerTalentMatrix() {
		return this.ownerTalentMatrix;
	}
	
	public ArrayList<BattleClassesAbstractTalent> talentList = new ArrayList<BattleClassesAbstractTalent>();
	
	/**
	 * Returns the index of the talent tree
	 * @return
	 */
	public int getIndexOfTree() {
		if(this.ownerTalentMatrix != null) {
			return this.ownerTalentMatrix.talentTrees.indexOf(this);
		}		
		return 0;
	}
	
	/**
	 * Returns the resource path of background image of this talent tree 
	 * @return
	 */
	public String getResoureLocationString() {
		if(this.ownerTalentMatrix != null) {
			int i = getIndexOfTree() + 1;
			return "textures/talents/backgrounds/talent_background_"
					+ (this.ownerTalentMatrix.playerHooks.playerClass.getPlayerClass().toString()).toLowerCase()
					+ "_" + i + ".png";
		}
		return "";
	}

}
