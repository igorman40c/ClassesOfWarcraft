package mods.battleclasses.ability.active;

import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.enums.EnumBattleClassesAbilityDirectTargetRequirement;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;

public class BattleClassesAbilityTestCasted extends BattleClassesAbstractAbilityActive {
	
	public BattleClassesAbilityTestCasted() {
		super();
		initStuff();
	}
	
	public BattleClassesAbilityTestCasted(String abilityName) {
		super();
		initStuff();
		this.setUnlocalizedName(abilityName);
	}
		
	public void initStuff() {
		this.setCastingType(EnumBattleClassesAbilityCastingType.CASTED);
		this.baseCastTime = 3;
		this.getCooldownClock().setDefaultDuration(6.0F);
		
		int pick = new Random().nextInt(EnumBattleClassesAbilitySchool.values().length);
		this.school = EnumBattleClassesAbilitySchool.values()[pick];
		
		pick = new Random().nextInt(EnumBattleClassesAbilityDirectTargetRequirement.values().length);
		this.targetRequirementType = EnumBattleClassesAbilityDirectTargetRequirement.values()[pick];
		
		pick = new Random().nextInt(EnumBattleClassesAbilityIntent.values().length);
		this.intent = EnumBattleClassesAbilityIntent.values()[pick];
	}

	@Override
	public boolean releaseEffects(EntityLivingBase targetEntity, int tickCount) {
		BattleClassesUtils.Log("BANG BANG Performed effect", LogType.CORE);
		this.playReleaseSound();
		this.playerHooks.getOwnerPlayer().addPotionEffect(new PotionEffect(34, 10*20, 0, false));
		return true;
	}
	
	@Override
	public String getTranslatedName() {
		return "Testing school:" + school + " intent:" + intent + " target:" + targetRequirementType;
	}
}
