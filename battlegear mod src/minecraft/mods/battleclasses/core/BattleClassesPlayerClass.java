package mods.battleclasses.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enumhelper.EnumBattleClassesCooldownType;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battleclasses.enumhelper.EnumBattleClassesWieldAccess;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.BattlegearAnimationPacket;
import mods.battlegear2.utils.EnumBGAnimations;

public class BattleClassesPlayerClass implements ICooldownHolder {
	
	public static final int TOTAL_CLASSES = EnumBattleClassesPlayerClass.values().length;
	public static IIcon classIcons[];
	
	public BattleClassesSpellBook spellBook;
	public BattleClassesTalentMatrix talentMatrix;
		
	protected BattleClassesPlayerHooks playerHooks;
	protected EnumBattleClassesPlayerClass playerClass;
	
	//BattleClassesAbstractAbilityPassive
	
	public BattleClassesPlayerClass(BattleClassesPlayerHooks parPlayerHooks, EnumBattleClassesPlayerClass parPlayerClass) {
		this.playerHooks = parPlayerHooks;
		this.initCooldownHolder();
		parPlayerHooks.mainCooldownMap.put(this.getCooldownHashCode(), this);
		this.spellBook = new BattleClassesSpellBook(parPlayerHooks);
		this.talentMatrix = new BattleClassesTalentMatrix(parPlayerHooks);
		
		this.setPlayerClass(parPlayerClass);
	}
	/*
	public void switchToPlayerClass(EnumBattleClassesPlayerClass parPlayerClass) {
		this.setPlayerClass(parPlayerClass);

		FMLProxyPacket p = new BattleClassesPacketPlayerClassSnyc(playerHooks.getOwnerPlayer(), playerClass).generatePacket();
		
		if(playerHooks.getOwnerPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerHooks.getOwnerPlayer();
			if(entityPlayerMP != null) {
				BattleClassesUtils.Log("Sending class switch sync to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
				Battlegear.packetHandler.sendPacketToPlayer(p, entityPlayerMP);
			}
		}
		
		BattleClassesUtils.Log(playerHooks.getOwnerPlayer().getDisplayName() + " switched to class: " + parPlayerClass.toString(), LogType.CORE);
		
		this.setToCooldownForced();
	}
	*/
	protected void setPlayerClass(EnumBattleClassesPlayerClass parPlayerClass) {
		this.playerClass = parPlayerClass;
		this.initClassContent();
	}
	
	protected void initClassContent() {
		this.spellBook.initWithAbilities(getClassAbilities());
		this.talentMatrix.initWithTalentTrees(getClassTalents());
		//TODO
		//this.setWeaponAccessByClass(this.playerClass);
	}
	
	public LinkedHashMap<Integer, BattleClassesAbstractAbilityActive> getClassAbilities() {
		LinkedHashMap<Integer, BattleClassesAbstractAbilityActive> abilities = new LinkedHashMap<Integer, BattleClassesAbstractAbilityActive>();
		//No addition on void class
		return abilities;
	}
	
	public ArrayList<BattleClassesTalentTree> getClassTalents() {
		ArrayList<BattleClassesTalentTree> talentTrees = new ArrayList<BattleClassesTalentTree>();
		//No addition on void class
		return talentTrees;
	}
	
	public void setWeaponAccessByClass(EnumBattleClassesPlayerClass parPlayerClass) {
		this.playerHooks.weaponHitHandler.initAccessSet();
		
		switch (parPlayerClass) {
			case PlayerClass_NONE : {
				this.playerHooks.weaponHitHandler.accessSet.add(EnumBattleClassesWieldAccess.WeaponAccess_DUALWIELD);
				this.playerHooks.weaponHitHandler.accessSet.add(EnumBattleClassesWieldAccess.WeaponAccess_SHIELD);
			}
				break;
			case MAGE : {
				//
			}
				break;
			case PRIEST : {
				//
			}
				break;
			case WARLOCK : {
				//
			}
				break;
			case ROGUE : {
				this.playerHooks.weaponHitHandler.accessSet.add(EnumBattleClassesWieldAccess.WeaponAccess_DUALWIELD);
			}
				break;
			case HUNTER : {
				//
			}
				break;
			case PALADIN : {
				this.playerHooks.weaponHitHandler.accessSet.add(EnumBattleClassesWieldAccess.WeaponAccess_SHIELD);
			}
				break;
			case WARRIOR : {
				this.playerHooks.weaponHitHandler.accessSet.add(EnumBattleClassesWieldAccess.WeaponAccess_DUALWIELD);
				this.playerHooks.weaponHitHandler.accessSet.add(EnumBattleClassesWieldAccess.WeaponAccess_SHIELD);
			}
				break;
		default:
			break;
		}
	}
	
	public EnumBattleClassesPlayerClass getPlayerClass() {
		return this.playerClass;
	}
	
	public static IIcon getClassIcon(EnumBattleClassesPlayerClass parPlayerClass) {
		return classIcons[parPlayerClass.ordinal()];
	}
	
	public ResourceLocation getIconResourceLocation() {
		return new ResourceLocation("battleclasses", "textures/sharedicons/classes/" + this.playerClass.toString() + ".png");
	}
	
	// -------------------- ICooldownHolder implementation --------------------
	
	public static final float CLASS_SWITCH_COOLDOWN_VALUE = 3.0F;
	public static final int CLASS_SWITCH_COOLDOWN_HASHCODE = 1399;

	protected float cooldownDuration = CLASS_SWITCH_COOLDOWN_VALUE;
	
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
		this.setCooldown(this.getCooldownDuration(), false, EnumBattleClassesCooldownType.CooldownType_CLASS_SWITCH);
	}

	@Override
	public void setToCooldownForced() {
		this.setCooldown(this.getCooldownDuration(), true, EnumBattleClassesCooldownType.CooldownType_CLASS_SWITCH);
	}
	
	public void setCooldown(float duration, boolean forced, EnumBattleClassesCooldownType type) {
		if( duration > this.getCooldownRemaining() || forced) {
			this.setTime = BattleClassesUtils.getCurrentTimeInSeconds();
			this.setDuration = duration;
			setLastUsedCooldownType(type);
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
		return this.CLASS_SWITCH_COOLDOWN_HASHCODE;
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
