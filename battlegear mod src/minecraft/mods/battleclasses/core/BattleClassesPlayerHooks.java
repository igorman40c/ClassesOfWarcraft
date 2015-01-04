package mods.battleclasses.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractAbilityPassive;
import mods.battleclasses.core.classes.BattleClassesPlayerClassMage;
import mods.battleclasses.enumhelper.EnumBattleClassesAmplifierApplyType;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battlegear2.Battlegear;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;


public class BattleClassesPlayerHooks implements ICooldownMapHolder {
	
	protected EntityPlayer ownerPlayer;
	
	public BattleClassesPlayerClass playerClass;
	public BattleClassesWeaponHitHandler weaponHitHandler;
	public HashMap<Integer, CooldownClock> mainCooldownMap;
	
	public BattleClassesPlayerHooks(EntityPlayer parOwnerPlayer) {
		this.ownerPlayer = parOwnerPlayer;
		mainCooldownMap = new HashMap<Integer, CooldownClock>();
		
		playerClass = new BattleClassesPlayerClass(this, EnumBattleClassesPlayerClass.PlayerClass_NONE);
		weaponHitHandler = new BattleClassesWeaponHitHandler(this);
	}
	
	public void switchToPlayerClass(EnumBattleClassesPlayerClass parPlayerClass) {
		/*
		if(this.playerClass != null) {
			this.playerClass.cooldownClock.unregisterFromCooldownCenter();
		}
		*/
		mainCooldownMap.clear();
		switch(parPlayerClass) {
			case MAGE: {
				this.playerClass = new BattleClassesPlayerClassMage(this);
			}
				break;
				/*
			case PlayerClass_PRIEST:
				break;
			case PlayerClass_WARLOCK:
				break;
			case PlayerClass_ROGUE:
				break;
			case PlayerClass_HUNTER:
				break;
			case PlayerClass_PALADIN:
				break;
			case PlayerClass_WARRIOR:
				break;
				*/
			default: {
				this.playerClass = new BattleClassesPlayerClass(this, parPlayerClass);
			}
				break;
		
		}
		
		FMLProxyPacket p = new BattleClassesPacketPlayerClassSnyc(this.ownerPlayer, parPlayerClass).generatePacket();
		
		if(this.getOwnerPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP) this.ownerPlayer;
			if(entityPlayerMP != null) {
				BattleClassesUtils.Log("Sending class switch sync to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
				BattleClassesMod.packetHandler.sendPacketToPlayer(p, entityPlayerMP);
			}
		}
		
		BattleClassesUtils.Log(this.ownerPlayer.getDisplayName() + " switched to class: " + parPlayerClass.toString(), LogType.CORE);
		
		playerClass.getCooldownClock().setCooldownDefaultForced();
	}
	
	public EntityPlayer getOwnerPlayer() {
		return this.ownerPlayer;
	}
	
	
	//AMPLIFIERS
	/** Should be called every time when there is a change in: Armor worn OR Talent points OR Potion effects OR onPlayerSpawn  */
	public void onAttributeSourcesChanged() {
		BattleClassesAttributes totalAttributes = this.getTotalAttributes(0);
		//par1EntityLivingBase.setAbsorptionAmount(par1EntityLivingBase.getAbsorptionAmount() - (float)(4 * (par3 + 1)));
		//SharedMonsterAttributes.maxHealth
		
		//Refreshing health based on totalAttributes
		float relativeHealth = this.getOwnerPlayer().getHealth() / this.getOwnerPlayer().getMaxHealth();
		AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString("5D6F0BA2-1186-46AC-B896-C61C5CEE99CC"), "potion.healthBoost", 10.0F, 0);
		IAttributeInstance iattributeinstance = this.ownerPlayer.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
		iattributeinstance.removeAllModifiers();
		iattributeinstance.applyModifier(attributemodifier);
		
		this.getOwnerPlayer().setHealth(relativeHealth * this.getOwnerPlayer().getMaxHealth());
	}
	
	private ArrayList<IAmplifyProvider> getAmplifiersFromPotionEffects(EnumBattleClassesAmplifierApplyType applyType) {
		ArrayList<IAmplifyProvider> amplifiers = new ArrayList<IAmplifyProvider>();
		//TODO
		return amplifiers;
	}
	
	private ArrayList<IAmplifyProvider> getAmplifiersFromPassiveAbilities(EnumBattleClassesAmplifierApplyType applyType) {
		ArrayList<IAmplifyProvider> amplifiers = new ArrayList<IAmplifyProvider>();
		for(BattleClassesAbstractAbilityPassive passiveAbility : this.playerClass.spellBook.getPassiveAbilitiesInArray()) {
			if(passiveAbility instanceof IAmplifyProvider) {
				IAmplifyProvider amplifier = (IAmplifyProvider)passiveAbility;
				if(amplifier.getApplyType() == applyType) {
					amplifiers.add(amplifier);
				}
			}
		}
		return amplifiers;
	}
	
	public static float DEFAULT_PLAYER_HP = 20;
	
	public BattleClassesAttributes getDefaultAttributes() {
		BattleClassesAttributes baseAttributes = new BattleClassesAttributes();
		//baseAttributes.stamina = this.getOwnerPlayer().getMaxHealth();
		baseAttributes.stamina = DEFAULT_PLAYER_HP;
		return baseAttributes;
	}
	
	public BattleClassesAttributes getItemAttributes() {
		BattleClassesAttributes itemAttributes = new BattleClassesAttributes();
		//TODO
		return itemAttributes;
	}
	
	private BattleClassesAttributes getBaseAttributeBonuses(int targetAbilityID) {
		BattleClassesAttributes baseAttributeBonuses = new BattleClassesAttributes();
		ArrayList<IAmplifyProvider> bonuses = this.getAmplifiersFromPassiveAbilities(EnumBattleClassesAmplifierApplyType.BASE_ATTRIBUTE_BONUS);
		for(IAmplifyProvider bonus : bonuses) {
			baseAttributeBonuses.add(bonus.getAttributes(targetAbilityID));
		}
		return baseAttributeBonuses;
	}
	
	private BattleClassesAttributes getBaseAttributeAmplifiers(int targetAbilityID) {
		BattleClassesAttributes baseAttributeAmplifiers = new BattleClassesAttributes(1);
		ArrayList<IAmplifyProvider> amplifiers = this.getAmplifiersFromPassiveAbilities(EnumBattleClassesAmplifierApplyType.BASE_ATTRIBUTE_AMPLIFIER);
		for(IAmplifyProvider amplifier : amplifiers) {
			baseAttributeAmplifiers.multiply(amplifier.getAttributes(targetAbilityID));
		}
		return baseAttributeAmplifiers;
	}
	
	private BattleClassesAttributes getTotalBasedAttributeBonuses(int targetAbilityID, BattleClassesAttributes totalAttributes) {
		BattleClassesAttributes totalAttributeBonuses = new BattleClassesAttributes();
		ArrayList<IAmplifyProvider> bonuses = this.getAmplifiersFromPassiveAbilities(EnumBattleClassesAmplifierApplyType.TOTAL_BASED_ATTRIBUTE_BONUS);
		for(IAmplifyProvider bonus : bonuses) {
			totalAttributeBonuses.add(bonus.getAttributes(targetAbilityID, totalAttributes));
		}
		return totalAttributeBonuses;
	}
	
	public BattleClassesAttributes getTotalAttributes(int targetAbilityID) {
		BattleClassesAttributes totalAttributes = new BattleClassesAttributes();
		totalAttributes.add(this.getDefaultAttributes());
		totalAttributes.add(this.getItemAttributes());
		totalAttributes.add(this.getBaseAttributeBonuses(targetAbilityID));
		totalAttributes.multiply(this.getBaseAttributeAmplifiers(targetAbilityID));
		totalAttributes.add(this.getTotalBasedAttributeBonuses(targetAbilityID, totalAttributes));
		
		return totalAttributes;
	}

	@Override
	public HashMap<Integer, CooldownClock> getCooldownMap() {
		return this.mainCooldownMap;
	}

	@Override
	public EntityPlayer getCooldownCenterOwner() {
		return this.getOwnerPlayer();
	}
	
    public NBTTagList writeToNBT(NBTTagList par1nbtTagList) {
    	
    	return par1nbtTagList;
    }
	
    public void readFromNBT(NBTTagList nbtTagList) {
    	
    }
	
	
}
