package mods.battleclasses.ability;

import mods.battleclasses.gui.BattleClassesGuiHelper;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BattleClassesTalentAbilityContainer extends BattleClassesAbstractTalent {

	public BattleClassesTalentAbilityContainer(int parAbilityID, int parTalentLevel, BattleClassesAbstractAbilityActive parAbility) {
		super(parAbilityID, parTalentLevel);
		containedAbility = parAbility;
	}
	
	BattleClassesAbstractAbilityActive containedAbility;
	
	public void onStateChanged() {
		super.onStateChanged();
		if(this.currentState == 0) {
			this.playerHooks.playerClass.spellBook.unLearnAbility(containedAbility);
		}
		else {
			this.playerHooks.playerClass.spellBook.learnAbility(containedAbility);
		}
	}
	
	@SideOnly(Side.CLIENT)    
    public ResourceLocation getIconResourceLocation() {
    	return BattleClassesGuiHelper.getAbilityIconResourceLocation(containedAbility.getAbilityID());
    }
	
	@SideOnly(Side.CLIENT)
	public String getAbilityIconName() {
		return containedAbility.getAbilityIconName();
	}

}
