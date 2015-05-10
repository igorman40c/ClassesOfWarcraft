package mods.battleclasses.potion;

import mods.battleclasses.client.IDescriptionProvider;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.StatCollector;

public class BattleClassesPotionMovementModifier extends BattleClassesPotionAbilityEffect implements IDescriptionProvider {

	protected float speedModifier = 0;
	protected boolean appliedOnCaster = false;
	
	BattleClassesPotionMovementModifier(int id, boolean badEffect, int liquidColorCode, float speedModifier, boolean appliedOnCaster) {
		super(id, badEffect, liquidColorCode);
		this.speedModifier = speedModifier;
		String UUID = new String("7107DE5E-7CE8-4030-940E-514C1F160" + String.format("%03d", id));
		this.func_111184_a(SharedMonsterAttributes.movementSpeed, UUID, speedModifier, 2);
		this.appliedOnCaster = appliedOnCaster;
	}

	@Override
	public String getTranslatedDescription() {
		int speedModifierPercentage = (int) (100F * Math.abs(speedModifier));
		String targetString = StatCollector.translateToLocal("pcpotion.speedmodifier." + 
				((this.appliedOnCaster) ? "caster" : "target"));
		String descriptionString = StatCollector.translateToLocal("pcpotion.speedmodifier" + 
				((this.speedModifier < 0) ? "decrease" : "increase")); 
		descriptionString.replace("%1$s", targetString);
		descriptionString.replace("%2$s", speedModifierPercentage + "%");
		
		return null;
	}

}
