package mods.battleclasses.ability.active;

import java.util.Random;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.enums.EnumBattleClassesAbilityCastingType;
import mods.battleclasses.enums.EnumBattleClassesAbilityDirectTargetRequirement;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class BattleClassesAbilityTestChanneled extends BattleClassesAbstractAbilityActive {
	
	public BattleClassesAbilityTestChanneled(int id) {
		super(id);
		initStuff();
	}
	
	public void initStuff() {
		this.castTime = 3;
		this.setCastingType(EnumBattleClassesAbilityCastingType.CHANNELED);
		this.channelTickCount = 3;
		this.school = EnumBattleClassesAbilitySchool.SPELL_ARCANE;
		this.getCooldownClock().setDefaultDuration(6.0F);
	}
	
	@Override
	public boolean releaseEffects(EntityLivingBase targetEntity, int tickCount) {
		BattleClassesUtils.Log("BANG BANG Performed effect", LogType.CORE);
		this.playReleaseSound();
		this.playerHooks.getOwnerPlayer().addPotionEffect(new PotionEffect(34, 10*20, 0, false));
		return true;
	}
	
	public String getName() {
		return "Testing school:" + school + " intent:" + intent + " target:" + targetRequirementType;
	}
}
