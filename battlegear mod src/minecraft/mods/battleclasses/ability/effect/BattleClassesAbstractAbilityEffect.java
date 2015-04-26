package mods.battleclasses.ability.effect;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public abstract class BattleClassesAbstractAbilityEffect {
	
	protected BattleClassesAbstractAbilityActive parentAbility;
	
	/**
	 * School of the effect value. Used to select corresponding power and critical data, determine dispellability.
	 */
	protected EnumBattleClassesAbilitySchool school;
	
	BattleClassesAbstractAbilityEffect(EnumBattleClassesAbilitySchool school) {
		this.school = school;
	}
	
	public void setParentAbility(BattleClassesAbstractAbilityActive ability) {
		this.parentAbility = ability;
	}
	
	public EnumBattleClassesAbilitySchool getAbilitySchool() {
		return this.school;
	}
	
	public abstract void performByOwnerOnTarget(EntityLivingBase owner, EntityLivingBase target);
	
	public abstract EnumBattleClassesAbilityIntent getIntent();
	
}
