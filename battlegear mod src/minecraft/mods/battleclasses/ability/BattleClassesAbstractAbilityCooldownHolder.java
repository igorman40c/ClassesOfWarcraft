package mods.battleclasses.ability;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.ICooldownHolder;
import mods.battleclasses.enumhelper.EnumBattleClassesCooldownType;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battlegear2.Battlegear;

public abstract class BattleClassesAbstractAbilityCooldownHolder extends BattleClassesAbstractAbility implements ICooldownHolder {
	
	public BattleClassesAbstractAbilityCooldownHolder(int parAbilityID) {
		super(parAbilityID);
		this.initCooldownHolder();
	}
	
	@Override
	public void setPlayerHooks(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
	}

	// -------------------- ICooldownHolder implementation --------------------
	

	protected float cooldownDuration = 0.0F;
	
	public void setCooldownDuration(float d) {
		this.cooldownDuration = d;
	}

	private float setTime;
	private float setDuration;
	
	@Override
	public void initCooldownHolder() {
		setTime = BattleClassesUtils.getCurrentTimeInSeconds() - COOLDOWN_INITIALIZER;
	}
	
	@Override
	public float getCooldownDuration() {
		return this.cooldownDuration;
	}

	@Override
	public void setToCooldown() {
		this.setCooldown(this.getCooldownDuration(), false, EnumBattleClassesCooldownType.CooldownType_ABILITY);
	}

	@Override
	public void setToCooldownForced() {
		this.setCooldown(this.getCooldownDuration(), true, EnumBattleClassesCooldownType.CooldownType_ABILITY);
	}
	
	public void setCooldown(float duration, boolean forced, EnumBattleClassesCooldownType type) {
		if( duration > this.getCooldownRemaining() || forced) {
			this.setTime = BattleClassesUtils.getCurrentTimeInSeconds();
			this.setDuration = duration;
			this.setLastUsedCooldownType(type);
			if(playerHooks.getOwnerPlayer() instanceof EntityPlayerMP) {
				EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerHooks.getOwnerPlayer();
				if(entityPlayerMP != null) {
					BattleClassesUtils.Log("Sending class cooldown set to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
					FMLProxyPacket p = new BattleClassesPacketCooldownSet(playerHooks.getOwnerPlayer(), this.getCooldownHashCode(),
							this.getSetDuration(), forced, this.getLastUsedCooldownType()).generatePacket();
					Battlegear.packetHandler.sendPacketToPlayerWithSideCheck(p, entityPlayerMP);
				}
			}
		}
	}


	@Override
	public float getCooldownRemaining() {
		float timeRemaining = getSetTime() + getSetDuration() - BattleClassesUtils.getCurrentTimeInSeconds();
		if(timeRemaining < 0 ) {
			timeRemaining = 0;
		}
		return timeRemaining;
	}

	@Override
	public boolean isOnCooldown() {
		return getCooldownRemaining() > 0;
	}

	@Override
	public int getCooldownHashCode() {
		return this.getAbilityID();
	}
	
	@Override
	public float getSetDuration() {
		return this.setDuration;
	}

	@Override
	public float getSetTime() {
		return setTime;
	}

	@Override
	public void setSetTime(float t) {
		setTime = t;
	}
	
	public void cancelCooldown() {
		this.initCooldownHolder();
		if(playerHooks.getOwnerPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerHooks.getOwnerPlayer();
			if(entityPlayerMP != null) {
				BattleClassesUtils.Log("Sending class cooldown set to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
				FMLProxyPacket p = new BattleClassesPacketCooldownSet(playerHooks.getOwnerPlayer(), this.getCooldownHashCode(),
						this.getSetDuration(), false, EnumBattleClassesCooldownType.CooldownType_CANCEL).generatePacket();
				Battlegear.packetHandler.sendPacketToPlayerWithSideCheck(p, entityPlayerMP);
			}
		}
	}
	
	protected EnumBattleClassesCooldownType lastUsedCoodownType;
	
	public void setLastUsedCooldownType(EnumBattleClassesCooldownType type) {
		this.lastUsedCoodownType = type;
	}
	
	public EnumBattleClassesCooldownType getLastUsedCooldownType() {
		return lastUsedCoodownType;
	}
}
