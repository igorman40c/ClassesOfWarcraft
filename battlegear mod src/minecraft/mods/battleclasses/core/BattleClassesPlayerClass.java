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

public class BattleClassesPlayerClass implements ICooldownOwner {
	
	public static final int TOTAL_CLASSES = EnumBattleClassesPlayerClass.values().length;
	public static IIcon classIcons[];
	
	public BattleClassesSpellBook spellBook;
	public BattleClassesTalentMatrix talentMatrix;
		
	protected BattleClassesPlayerHooks playerHooks;
	protected EnumBattleClassesPlayerClass playerClass;
	
	protected CooldownClock cooldownClock;
	
	//BattleClassesAbstractAbilityPassive
	
	public BattleClassesPlayerClass(BattleClassesPlayerHooks parPlayerHooks, EnumBattleClassesPlayerClass parPlayerClass) {
		this.playerHooks = parPlayerHooks;
		this.cooldownClock = new CooldownClock(CLASS_SWITCH_COOLDOWN_HASHCODE, CLASS_SWITCH_COOLDOWN_DURATION, EnumBattleClassesCooldownType.CooldownType_CLASS_SWITCH, parPlayerHooks);
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
				BattleClassesMod.packetHandler.sendPacketToPlayer(p, entityPlayerMP);
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
			case NONE : {
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
	
	// -------------------- ICooldownOwner implementation --------------------
	
	public static final float CLASS_SWITCH_COOLDOWN_DURATION = 3.0F;
	public static final int CLASS_SWITCH_COOLDOWN_HASHCODE = 1399;

	@Override
	public CooldownClock getCooldownClock() {
		return this.cooldownClock;
	}
}
