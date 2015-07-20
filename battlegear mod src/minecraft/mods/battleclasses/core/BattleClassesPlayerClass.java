package mods.battleclasses.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.enums.EnumBattleClassesWieldAccess;
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
		
	public BattleClassesPlayerClass(BattleClassesPlayerHooks parPlayerHooks, EnumBattleClassesPlayerClass parPlayerClass) {
		this.playerHooks = parPlayerHooks;
		this.cooldownClock = new CooldownClock(CLASS_SWITCH_COOLDOWN_HASHCODE, CLASS_SWITCH_COOLDOWN_DURATION, EnumBattleClassesCooldownType.CooldownType_CLASS_SWITCH, parPlayerHooks);
		this.spellBook = new BattleClassesSpellBook(parPlayerHooks);
		this.talentMatrix = new BattleClassesTalentMatrix(parPlayerHooks);
		
		this.setPlayerClass(parPlayerClass);
	}

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
	
	public List<BattleClassesAbstractAbilityActive> getClassAbilities() {
		ArrayList<BattleClassesAbstractAbilityActive> abilities = new ArrayList<BattleClassesAbstractAbilityActive>();
		//No addition on void class
		return abilities;
	}
	
	public List<BattleClassesTalentTree> getClassTalents() {
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

	public ArrayList<EnumBattleClassesAttributeType> getDefaultAttributesToDisplay() {
		ArrayList<EnumBattleClassesAttributeType> attributes = new ArrayList<EnumBattleClassesAttributeType>();
		attributes.add(EnumBattleClassesAttributeType.HEALTH);
		attributes.add(EnumBattleClassesAttributeType.VANILLA_ARMOR);
		return attributes;
	}
	
	public ArrayList<EnumBattleClassesAttributeType> getPrimaryAttributesToDisplay() {
		ArrayList<EnumBattleClassesAttributeType> attributes = new ArrayList<EnumBattleClassesAttributeType>();
		
		attributes.add(EnumBattleClassesAttributeType.STRENGTH);
		attributes.add(EnumBattleClassesAttributeType.AGILITY);
		attributes.add(EnumBattleClassesAttributeType.SPELLPOWER_ARCANE);
		attributes.add(EnumBattleClassesAttributeType.SPELLPOWER_FIRE);
		attributes.add(EnumBattleClassesAttributeType.SPELLPOWER_FROST);
		attributes.add(EnumBattleClassesAttributeType.SPELLPOWER_HOLY);
		attributes.add(EnumBattleClassesAttributeType.SPELLPOWER_SHADOW);
		
		return attributes;
	}
	
	public ArrayList<EnumBattleClassesAttributeType> getSecondaryAttributesToDisplay() {
		ArrayList<EnumBattleClassesAttributeType> attributes = new ArrayList<EnumBattleClassesAttributeType>();
		
		attributes.add(EnumBattleClassesAttributeType.MELEE_ATTACK_DAMAGE);
		attributes.add(EnumBattleClassesAttributeType.RANGED_ATTACK_DAMAGE);
		attributes.add(EnumBattleClassesAttributeType.CRITICAL_RATING);
		attributes.add(EnumBattleClassesAttributeType.HASTE_RATING);
		attributes.add(EnumBattleClassesAttributeType.ARMOR_PENETRATION);
		
		return attributes;
	}
	
	protected void registerAbilties(List<BattleClassesAbstractAbility> abilities) {
		for(BattleClassesAbstractAbility ability : abilities) {
			BattleClassesAbstractAbility.registerAbility(ability);
		}
	}
	
	// -------------------- ICooldownOwner implementation --------------------
	
	public static final float CLASS_SWITCH_COOLDOWN_DURATION = 3.0F;
	public static final String CLASS_SWITCH_COOLDOWN_HASHCODE = "class_switch";

	@Override
	public CooldownClock getCooldownClock() {
		return this.cooldownClock;
	}
}
