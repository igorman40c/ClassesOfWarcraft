package mods.battleclasses.ability;

import java.util.Random;

import net.minecraft.entity.EntityLiving;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.enumhelper.EnumBattleClassesAbilitySchool;
import mods.battleclasses.enumhelper.EnumBattleClassesAbilityDirectTargetRequirement;
import mods.battleclasses.enumhelper.EnumBattleClassesAbilityIntent;

public class BattleClassesAbilityTest extends BattleClassesAbstractAbilityActive {
	
	public static final int TEST_SPELL_ID = 777;

	public BattleClassesAbilityTest() {
		super(TEST_SPELL_ID);
		this.castTime = 3;
		this.channeled = true;
		this.channelTickCount = 3;
		initStuff();
	}
	
	public BattleClassesAbilityTest(int id) {
		super(id);
		initStuff();
	}
	
	public void initStuff() {
		this.castTime = 3;
		this.getCooldownClock().setDefaultDuration(6.0F);
		
		int pick = new Random().nextInt(EnumBattleClassesAbilitySchool.values().length);
		this.school = EnumBattleClassesAbilitySchool.values()[pick];
		
		pick = new Random().nextInt(EnumBattleClassesAbilityDirectTargetRequirement.values().length);
		this.targetType = EnumBattleClassesAbilityDirectTargetRequirement.values()[pick];
		
		pick = new Random().nextInt(EnumBattleClassesAbilityIntent.values().length);
		this.intent = EnumBattleClassesAbilityIntent.values()[pick];
		
		if(this.abilityID == 100) {
			this.channeled = true;
			this.channelTickCount = 3;
			this.school = EnumBattleClassesAbilitySchool.SPELL_ARCANE;
		}
	}

	@Override
	public boolean performEffect(EntityLiving targetEntity, int tickCount) {
		BattleClassesUtils.Log("BANG BANG Performed effect", LogType.CORE);
		return true;
	}
	
	public String getName() {
		return "Testing school:" + school + " intent:" + intent + " target:" + targetType;
	}
}
