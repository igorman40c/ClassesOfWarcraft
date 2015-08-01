package mods.battleclasses.core;

import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battlegear2.Battlegear;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import org.lwjgl.Sys;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class CooldownClock {

	public static final float COOLDOWN_INITIALIZER = 3600;

	public final String cooldownID;
	public IMainCooldownMap parentCooldownMapper;
	private boolean enabled = true;
	private BattleClassesAbstractAbility parentActiveAbility;
	
	private CooldownClock() {
		this.cooldownID = "";
		this.resetClock();
	}
	
	public CooldownClock(String id) {
		this.cooldownID = id;
		this.resetClock();
	}
	
	public CooldownClock(String id, float duration, EnumBattleClassesCooldownType type) {
		this.cooldownID = id;
		this.setDefaultDuration(duration);
		this.setDefaultType(type);
		this.resetClock();
	}
	
	public CooldownClock(String id, IMainCooldownMap parameterCDcenter) {
		this.cooldownID = id;
		this.resetClock();
		this.registerInCooldownCenter(parameterCDcenter);
	}
	
	public CooldownClock(String id, float duration, EnumBattleClassesCooldownType type, IMainCooldownMap parameterCDcenter) {
		this.cooldownID = id;
		this.setDefaultDuration(duration);
		this.setDefaultType(type);
		this.resetClock();
		this.registerInCooldownCenter(parameterCDcenter);
	}
	
	public void resetClock() {
		setTime = BattleClassesUtils.getCurrentTimeInSeconds() - COOLDOWN_INITIALIZER;
	}
	//----------------------------------------
	public void registerInCooldownCenter(IMainCooldownMap parameterCDcenter) {
		if(parameterCDcenter == null) {
			BattleClassesUtils.ErrorLog("FATAL ERROR! Trying to register CooldownClock object into NULL ICooldownCenter");
			return;
		}
		if(parameterCDcenter.getCooldownMap().get(this.getCooldownHashCode()) != null) {
			if(parameterCDcenter.getCooldownMap().get(this.getCooldownHashCode()) != this) {
				BattleClassesUtils.ErrorLog("FATAL ERROR! ID of CooldownClock already registred for a different CooldownClock!");
				return;
			}
			else {
				BattleClassesUtils.ErrorLog("WARNING! Trying to register CooldownClock which is already registred!");
				return;
			}
		}
		this.parentCooldownMapper = parameterCDcenter;
		this.parentCooldownMapper.getCooldownMap().put(this.getCooldownHashCode(), this);
	}
	
	public void unregisterFromCooldownCenter() {
		if(this.parentCooldownMapper == null) {
			BattleClassesUtils.ErrorLog("FATAL ERROR! Trying to unregister CooldownClock object from NULL ICooldownCenter!");
			return;
		}
		if(this.parentCooldownMapper.getCooldownMap().get(this.getCooldownHashCode()) == null) {
			BattleClassesUtils.ErrorLog("FATAL ERROR! Trying to unregister unregistred CooldownClock!");
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
	public void startDefaultCooldown() {
		this.startCooldown(this.getDefaultDuration(), false, this.getDefaultType());
	}
	
	/**
	 * Sets the clock to forced cooldown using the default values
	 */
	public void startDefaultCooldownForced() {
		this.startCooldown(this.getDefaultDuration(), true, this.getDefaultType());
	}
	
	/**
	 * Sets the clock to cooldown using the given parameters
	 * @param duration - duration of the cooldown time in seconds
	 * @param forced - boolean value which decides to override the previously set cooldown (when duration > getCooldownRemaining()) or not!
	 * @param type - type of the cooldown
	 */
	public void startCooldown(float duration, boolean forced, EnumBattleClassesCooldownType type) {
		if(!enabled) {
			return;
		}
		if( duration > this.getRemainingDuration() || forced) {
			this.setTime = BattleClassesUtils.getCurrentTimeInSeconds();
			
			//Effecting duration with multipliers
			if(type == EnumBattleClassesCooldownType.CooldownType_ABILITY 
					&& this.parentActiveAbility != null 
					&& parentCooldownMapper != null) {
				duration *= parentCooldownMapper.getCooldownMultiplierForAbility(parentActiveAbility);
			}
			
			this.lastUsedDuration = duration;
			this.lastUsedType = type;
			/*
			System.out.println(String.format("Did set cd of: %s, setTime: %f", this.getCooldownID(), this.setTime));
			System.out.println(String.format("Sys.getTime: %d, Sys.getTimerResolution: %d", Sys.getTime(), Sys.getTimerResolution()));
			*/
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
		this.resetClock();
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
		return getRemainingDuration() > 0;
	}
	
	/**
	 * Return the remaining time of the currently ongoing cooldown of this clock in seconds
	 * @return - (float) time remaining in seconds
	 */
	public double getRemainingDuration() {
		double timeRemaining = getSetTime() + getLastUsedDuration() - BattleClassesUtils.getCurrentTimeInSeconds();
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
	
	public double getSetTime() {
		return setTime;
	}

	//----------------------------------------
	/** Stores the point in time, in which the last cooldown has been set on this clock*/
	protected double setTime;
	protected float lastUsedDuration;
	protected EnumBattleClassesCooldownType lastUsedType = EnumBattleClassesCooldownType.CooldownType_UNKNOWN;
	
	protected String getCooldownID() {
		return (this.parentActiveAbility!=null) ? this.parentActiveAbility.getAbilityID() : this.cooldownID;
	}

	protected String getCooldownHashCode() {
		return this.getCooldownID();
	}
		
	protected EntityPlayer getOwnerPlayer() {
		return this.parentCooldownMapper.getCooldownCenterOwner();
	}
	
	/**
	 * Helper method for deserialization to apply cooldown data.
	 * @param t
	 */
	void setSetTime(double t) {
		if(BattleClassesUtils.getCurrentTimeInSeconds() < t) {
			this.resetClock();
			return;
		}
		setTime = t;
	}
	
	void setLastUsedType(EnumBattleClassesCooldownType type) {
		this.lastUsedType = type;
	}
	
	void setLastUsedDuration(float duration) {
		this.lastUsedDuration = duration;
	}
	
	public void setParentAbility(BattleClassesAbstractAbility parentActiveAbility) {
		this.parentActiveAbility = parentActiveAbility;
	}

}
