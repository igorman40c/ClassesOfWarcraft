package mods.battleclasses.ability;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battlegear2.Battlegear;


public class BattleClassesWeaponSkill extends BattleClassesAbstractAbilityCooldownHolder {

	public BattleClassesWeaponSkill(int parAbilityID) {
		super(parAbilityID);
		// TODO Auto-generated constructor stub
	}
	
	public BattleClassesWeaponSkill(int parAbilityID, BattleClassesPlayerHooks parPlayerHooks, boolean isMainHand) {
		super(parAbilityID);
		this.setPlayerHooks(parPlayerHooks);
		this.mainHand = isMainHand;
	}
	
	public boolean mainHand;

	@Override
	public void setPlayerHooks(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
		parPlayerHooks.mainCooldownMap.put(this.getCooldownHashCode(), this);
	}

	public static final int MELEE_SWING_COOLDOWN = 1;
	
	@Override
	public float getCooldownDuration() {
		// CHECK WEAPON SPEED !! TO DO
		return MELEE_SWING_COOLDOWN;
	}
	
	public void setToHalfCooldown() {
		//this.setCooldown(this.getCooldownDuration()/2, false);
	}

}
