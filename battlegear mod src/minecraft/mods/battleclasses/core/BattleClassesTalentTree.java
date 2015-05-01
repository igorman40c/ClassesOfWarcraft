package mods.battleclasses.core;

import java.util.ArrayList;

import mods.battleclasses.ability.talent.BattleClassesAbstractTalent;

public class BattleClassesTalentTree {
	
	protected BattleClassesTalentMatrix parentTalentMatrix;
	
	public void setOwnerTalentMatrix(BattleClassesTalentMatrix parTalentMatrix) {
		this.parentTalentMatrix = parTalentMatrix;
		
		for(BattleClassesAbstractTalent talent : talentList) {
			talent.setParentTree(this);
			talent.setPlayerHooks(parTalentMatrix.playerHooks);
		}
	}
	
	public String name = "";
	
	public void setName(String parName) {
		name = parName;
	}
	
	public BattleClassesTalentMatrix getParentTalentMatrix() {
		return this.parentTalentMatrix;
	}
	
	public ArrayList<BattleClassesAbstractTalent> talentList = new ArrayList<BattleClassesAbstractTalent>();
	
	/**
	 * Returns the index of the talent tree
	 * @return
	 */
	public int getIndexOfTree() {
		if(this.parentTalentMatrix != null) {
			return this.parentTalentMatrix.talentTrees.indexOf(this);
		}		
		return 0;
	}
	
	public int getPointsOnTree() {
		int n = 0;
		for(BattleClassesAbstractTalent talentAbility : this.talentList ) {
			n += talentAbility.getCurrentState();
    	}
		return n;
	}
	
	public void spendTalentPoints(int points) {
		for(BattleClassesAbstractTalent talentAbility : this.talentList ) {
    		while(!talentAbility.isAlreadyLearned() && points != 0) {
    			this.parentTalentMatrix.learnTalent(talentAbility);
    			points--;
    		}
    	}
	}
	
	/**
	 * Returns the resource path of background image of this talent tree 
	 * @return
	 */
	public String getResoureLocationString() {
		if(this.parentTalentMatrix != null) {
			int i = getIndexOfTree() + 1;
			return "textures/talents/backgrounds/talent_background_"
					+ (this.parentTalentMatrix.playerHooks.playerClass.getPlayerClass().toString()).toLowerCase()
					+ "_" + i + ".png";
		}
		return "";
	}

}
