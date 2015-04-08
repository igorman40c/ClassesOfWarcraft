package mods.battleclasses.ability.effect;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public abstract class BattleClassesAbstractAbilityEffect {
	
	protected BattleClassesAbstractAbilityActive parentAbility;
	
	BattleClassesAbstractAbilityEffect(BattleClassesAbstractAbilityActive ability) {
		this.parentAbility = ability;
	}
	
	public abstract void performByOwnerOnTarget(EntityLivingBase owner, EntityLivingBase target);
	
	public abstract EnumBattleClassesAbilityIntent getIntent();
	
}
