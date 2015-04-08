package mods.battleclasses.ability.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.core.BattleClassesAttributes;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.potion.BattleClassesPotionEffectValueOverTime;

public class BattleClassesAbilityEffectValueOverTime extends BattleClassesAbilityEffectPotion implements IValueEffect {

	protected BattleClassesAbstractAbilityEffectInstantValue effectInstantValue;
	
	BattleClassesAbilityEffectValueOverTime(BattleClassesAbstractAbilityActive ability) {
		super(ability);
		// TODO Auto-generated constructor stub
	}
	
	protected int tickCount = 1;

	@Override
	public void performValueEffect(BattleClassesAttributes attributesForParentAbility, 
			float critChance, float partialMultiplier, EntityLivingBase owner, EntityLivingBase target) {
		target.addPotionEffect(new BattleClassesPotionEffectValueOverTime(
				this.getPotionByStoredID(), this.durationInTicksForSeconds(), 1,
				attributesForParentAbility, critChance, this.tickCount, this.effectInstantValue, 
				this.parentAbility, owner));
	}
	
	@Override
	public EnumBattleClassesAbilityIntent getIntent() {
		return this.effectInstantValue.getIntent();
	}

}
