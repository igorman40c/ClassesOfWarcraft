package mods.battleclasses.core;

import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battlegear2.Battlegear;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class CooldownClock {

	public static final float COOLDOWN_INITIALIZER = 3600;

	public final int cooldownID;
	public ICooldownMapHolder parentCooldownMapper;
	private boolean enabled = true;
	
	private CooldownClock() {
		this.cooldownID = 0;
		this.initCooldownHolder();
	}
	
	public CooldownClock(int id) {
		this.cooldownID = id;
		this.initCooldownHolder();
	}
	
	public CooldownClock(int id, float duration, EnumBattleClassesCooldownType type) {
		this.cooldownID = id;
		this.setDefaultDuration(duration);
		this.setDefaultType(type);
		this.initCooldownHolder();
	}
	
	public CooldownClock(int id, ICooldownMapHolder parameterCDcenter) {
		this.cooldownID = id;
		this.initCooldownHolder();
		this.registerInCooldownCenter(parameterCDcenter);
	}
	
	public CooldownClock(int id, float duration, EnumBattleClassesCooldownType type, ICooldownMapHolder parameterCDcenter) {
		this.cooldownID = id;
		this.setDefaultDuration(duration);
		this.setDefaultType(type);
		this.initCooldownHolder();
		this.registerInCooldownCenter(parameterCDcenter);
	}
	
	public void initCooldownHolder() {
		setTime = BattleClassesUtils.getCurrentTimeInSeconds() - COOLDOWN_INITIALIZER;
	}
	//----------------------------------------
	public void registerInCooldownCenter(ICooldownMapHolder parameterCDcenter) {
		if(parameterCDcenter == null) {
			BattleClassesUtils.Log("FATAL ERROR! Trying to register CooldownClock object into NULL ICooldownCenter", LogType.CORE);
			return;
		}
		if(parameterCDcenter.getCooldownMap().get(this.getCooldownHashCode()) != null) {
			if(parameterCDcenter.getCooldownMap().get(this.getCooldownHashCode()) != this) {
				BattleClassesUtils.Log("FATAL ERROR! ID of CooldownClock already registred for a different CooldownClock!", LogType.CORE);
				return;
			}
			else {
				BattleClassesUtils.Log("WARNING! Trying to register CooldownClock which is already registred!", LogType.CORE);
				return;
			}
		}
		this.parentCooldownMapper = parameterCDcenter;
		this.parentCooldownMapper.getCooldownMap().put(this.getCooldownHashCode(), this);
	}
	
	public void unregisterFromCooldownCenter() {
		if(this.parentCooldownMapper == null) {
			BattleClassesUtils.Log("FATAL ERROR! Trying to unregister CooldownClock object from NULL ICooldownCenter!", LogType.CORE);
			return;
		}
		if(this.parentCooldownMapper.getCooldownMap().get(this.getCooldownHashCode()) == null) {
			BattleClassesUtils.Log("FATAL ERROR! Trying to unregister unregistred CooldownClock!", LogType.CORE);
			return;
		}
		this.parentCooldownMapper.getCooldownMap().remove(this.getCooldownHashCode());
	}
	//----------------------------------------
	/** Default cooldown duration parameter */
	protected float defaultDuration = 0.0F;
	/**
	 * Default cooldown duration parameter setter
	 * @param d - duration
	 */
	public void setDefaultDuration(float d) {
		this.defaultDuration = d;
	}
	/**
	 * Default cooldown duration parameter getter
	 * @return - duration
	 */
	public float getDefaultDuration() {
		return this.defaultDuration;
	}
	//----------------------------------------
	/** Default cooldown type parameter */
	protected EnumBattleClassesCooldownType defaultType;
	/**
	 * Default cooldown type parameter setter
	 * @param type - type
	 */
	public void setDefaultType(EnumBattleClassesCooldownType type) {
		this.defaultType = type;
	}
	/**
	 * Default cooldown type parameter getter
	 * @return - type
	 */
	public EnumBattleClassesCooldownType getDefaultType() {
		return this.defaultType;
	}
	//----------------------------------------

	/**
	 * Sets the clock to cooldown using the default values
	 */
	public void setCooldownDefault() {
		this.setCooldown(this.getDefaultDuration(), false, this.getDefaultType());
	}
	
	/**
	 * Sets the clock to forced cooldown using the default values
	 */
	public void setCooldownDefaultForced() {
		this.setCooldown(this.getDefaultDuration(), true, this.getDefaultType());
	}
	
	/**
	 * Sets the clock to cooldown using the given parameters
	 * @param duration - duration of the cooldown time in seconds
	 * @param forced - boolean value which decides to override the previously set cooldown (when duration > getCooldownRemaining()) or not!
	 * @param type - type of the cooldown
	 */
	public void setCooldown(float duration, boolean forced, EnumBattleClassesCooldownType type) {
		if(!enabled) {
			return;
		}
		if( duration > this.getCooldownRemaining() || forced) {
			this.setTime = BattleClassesUtils.getCurrentTimeInSeconds();
			this.lastUsedDuration = duration;
			this.lastUsedType = type;
			if(this.getOwnerPlayer() instanceof EntityPlayerMP) {
				EntityPlayerMP entityPlayerMP = (EntityPlayerMP) this.getOwnerPlayer();
				if(entityPlayerMP != null) {
					BattleClassesUtils.Log("Sending class cooldown set to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
					FMLProxyPacket p = new BattleClassesPacketCooldownSet(this.getOwnerPlayer(), this.getCooldownHashCode(),
							this.getLastUsedDuration(), forced, this.getLastUsedType()).generatePacket();
					BattleClassesMod.packetHandler.sendPacketToPlayerWithSideCheck(p, entityPlayerMP);
				}
			}
		}
	}
	
	/**
	 * Removes the previously set cooldown
	 */
	public void cancelCooldown() {
		this.initCooldownHolder();
		if(this.getOwnerPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP) this.getOwnerPlayer();
			if(entityPlayerMP != null) {
				BattleClassesUtils.Log("Sending class cooldown set to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
				FMLProxyPacket p = new BattleClassesPacketCooldownSet(this.getOwnerPlayer(), this.getCooldownHashCode(),
						this.getLastUsedDuration(), false, EnumBattleClassesCooldownType.CooldownType_CANCEL).generatePacket();
				BattleClassesMod.packetHandler.sendPacketToPlayerWithSideCheck(p, entityPlayerMP);
			}
		}
	}
	
	/**
	 * Returns the boolean value of "is there any active cooldown going on this clock?"
	 * @return - (boolean) value of (getCooldownRemaining() > 0) 
	 */
	public boolean isOnCooldown() {
		return getCooldownRemaining() > 0;
	}
	
	/**
	 * Return the remaining time of the currently ongoing cooldown of this clock in seconds
	 * @return - (float) time remaining in seconds
	 */
	public float getCooldownRemaining() {
		float timeRemaining = getSetTime() + getLastUsedDuration() - BattleClassesUtils.getCurrentTimeInSeconds();
		if(timeRemaining < 0 ) {
			timeRemaining = 0;
		}
		return timeRemaining;
	}
	
	/**
	 * Returns the type of the last used cooldown
	 * @return - type of the last used cooldown
	 */
	public EnumBattleClassesCooldownType getLastUsedType() {
		return lastUsedType;
	}
	
	public float getLastUsedDuration() {
		return this.lastUsedDuration;
	}	
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean parBool) {
		this.enabled = parBool;
	}
	
	public float getSetTime() {
		return setTime;
	}

	//----------------------------------------
	/** Stores the point in time, in which the last cooldown has been set on this clock*/
	protected float setTime;
	protected float lastUsedDuration;
	protected EnumBattleClassesCooldownType lastUsedType = EnumBattleClassesCooldownType.CooldownType_UNKNOWN;

	protected int getCooldownHashCode() {
		return this.cooldownID;
	}
		
	protected EntityPlayer getOwnerPlayer() {
		return this.parentCooldownMapper.getCooldownCenterOwner();
	}
	
	void setSetTime(float t) {
		setTime = t;
	}
	
	void setLastUsedType(EnumBattleClassesCooldownType type) {
		this.lastUsedType = type;
	}
	
	void setLastUsedDuration(float duration) {
		this.lastUsedDuration = duration;
	}

}
