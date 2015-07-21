package mods.battleclasses.ability.active;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.CooldownClock;
import mods.battleclasses.core.ICooldownOwner;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battlegear2.Battlegear;

public abstract class BattleClassesAbstractAbilityCooldownHolder extends BattleClassesAbstractAbility implements ICooldownOwner {
	
	public BattleClassesAbstractAbilityCooldownHolder() {
		super();
		this.cooldownClock = new CooldownClock(this.getAbilityID(), defaultCooldownDuration, EnumBattleClassesCooldownType.CooldownType_ABILITY);
		this.cooldownClock.setParentAbility(this);
	}
		
	public void onLearn() {
		this.cooldownClock.registerInCooldownCenter(this.getPlayerHooks());
	}
	
	public void onUnLearn() {
		this.cooldownClock.unregisterFromCooldownCenter();
	}

	protected float defaultCooldownDuration = 0.0F;
	protected CooldownClock cooldownClock;
	
	public CooldownClock getCooldownClock() {
		return this.cooldownClock;
	}

}