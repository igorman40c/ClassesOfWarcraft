package mods.battleclasses.ability.talent;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BattleClassesTalentAbilityActiveContainer extends BattleClassesAbstractTalentAbilityContainer {

	public BattleClassesTalentAbilityActiveContainer(int parAbilityID, int parTalentLevel, BattleClassesAbstractAbilityActive parAbility) {
		super(parAbilityID, parTalentLevel, parAbility);
		this.setUnlocalizedName("universal.abilityactivecontainer");
	}
	
	public BattleClassesAbstractAbilityActive getContainedAbility() {
		return (BattleClassesAbstractAbilityActive) this.containedAbility;
	}
	
	@SideOnly(Side.CLIENT)    
    public ResourceLocation getIconResourceLocation() {
    	return getContainedAbility().getIconResourceLocation();
    }
	
	@SideOnly(Side.CLIENT)
	public String getAbilityIconName() {
		return getContainedAbility().getAbilityIconName();
	}
	
	@Override
	public String getTranslatedName() {
		return super.getTranslatedName() + ": " + this.getContainedAbility().getTranslatedName();
	}
	
	@Override
	public String getTranslatedDescription() {
		return this.getContainedAbility().getTranslatedDescription();
	}

}
