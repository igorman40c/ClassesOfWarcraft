package mods.battleclasses.gui.controlls;

import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.gui.tab.BattleClassesTabSpellbook;
import net.minecraft.util.ResourceLocation;

public class BattleClassesGuiButtonAbilityNode extends BattleClassesGuiButton
{
	public BattleClassesAbstractAbilityActive ability;
	public BattleClassesGuiButtonAbilityNode(BattleClassesAbstractAbilityActive parAbility) {
		super(parAbility.getAbilityID(), BattleClassesTabSpellbook.resource);
		this.ability = parAbility;
	}
	
	

}
