package mods.battleclasses.ability;

import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.BattleClassesTalentTree;

public class BattleClassesAbstractTalent extends BattleClassesAbstractAbilityPassive {

	public BattleClassesAbstractTalent(int parAbilityID, int parTalentLevel) {
		super(parAbilityID);
		talentLevel = parTalentLevel;
	}
	
	protected BattleClassesTalentTree parentTree;
	
	int talentLevel = 0;
	int currentState = 0;
	
	/**
	 * Should only be used by PacketTalentSync while applying talentStateMap
	 * @param state
	 */
	public void setCurrentState(int state) {
		this.currentState = state;
		this.onStateChanged();
	}
	
	public int getCurrentState() {
		return this.currentState;
	}
	
	public int getMaximalState() {
		return 1;
	}
	
	public boolean incrementState() {
		if(this.currentState < this.getMaximalState() ) {
			currentState++;
			this.onStateChanged();
			return true;
		}
		return false;
	}
	
	public void resetState() {
		currentState = 0;
		this.onStateChanged();
	}
	
	public void setParentTree(BattleClassesTalentTree parParentTree) {
		this.parentTree = parParentTree;
	}
	
	public BattleClassesTalentTree getParentTree() {
		return this.parentTree;
	}
	
	@Override
	public void setPlayerHooks(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
		//TODO
	}
	
    @SideOnly(Side.CLIENT)    
    public ResourceLocation getIconResourceLocation() {
    	return new ResourceLocation("battleclasses", getTalentIconPath() + getAbilityIconName());
    }

	
	/*
	public BattleClassesTalentTree getParentTree() {
		for(BattleClassesTalentTree talentTree : this.playerHooks.playerClass.talentMatrix.talentTrees) {
			if(talentTree.talentList.contains(this)) {
				return talentTree;
			}
		}
		BattleClassesUtils.Log("FATAL ERROR! AbstractTalent.getParentTree returning null. Prepare for crash!", LogType.CORE);
		return null;
	}
	*/
	
	public void onStateChanged() {
		//TODO
		//ADD OR REMOVE THIS AMPLIFY/MODIFY PROVIDER FROM CENTRAL HASHTABLE
		//ADD OR REMOVE CONTAINED ABILITY TO SPELLBOOK
	}
	
	public boolean isLitOnUI() {
		return isAvailableToLearn() || isAlreadyLearned();
	}
	
	
	public boolean isAvailableToLearn() {
		if(!this.playerHooks.playerClass.talentMatrix.hasPointsToSpend()) {
			return false;
		}
		if(this.isAlreadyLearned()) {
			return false;
		}
		return (this.talentLevel == 0) ||  (this.getPreviousTalentTreeElement().isAlreadyLearned());
	}
	
	public BattleClassesAbstractTalent getPreviousTalentTreeElement() {
		return this.parentTree.talentList.get(this.parentTree.talentList.indexOf(this) - 1);
	}
	
	public boolean isAlreadyLearned() {
		return this.getCurrentState() == this.getMaximalState();
	}
	
    @SideOnly(Side.CLIENT)
    public String getTalentIconPath() {
    	return "textures/talents/icons/";
    }
	
    @SideOnly(Side.CLIENT)
	public String getAbilityIconName() {
		return "talent_icon_" 
				+ (this.playerHooks.playerClass.getPlayerClass().toString()).toLowerCase()
				+ "_"
				+ (this.parentTree.getIndexOfTree())
				+ "_"
				+ (this.talentLevel)
				+ ".png";
	}

}
