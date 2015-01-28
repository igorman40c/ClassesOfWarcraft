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
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.items.IAttributeProvider;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battlegear2.Battlegear;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;


public class BattleClassesPlayerHooks implements ICooldownMapHolder {
	
	protected EntityPlayer ownerPlayer;
	
	public BattleClassesPlayerClass playerClass;
	public BattleClassesWeaponHitHandler weaponHitHandler;
	public HashMap<Integer, CooldownClock> mainCooldownMap;
	
	public BattleClassesPlayerHooks(EntityPlayer parOwnerPlayer) {
		this.ownerPlayer = parOwnerPlayer;
		mainCooldownMap = new HashMap<Integer, CooldownClock>();
		
		playerClass = new BattleClassesPlayerClass(this, EnumBattleClassesPlayerClass.NONE);
		weaponHitHandler = new BattleClassesWeaponHitHandler(this);
		//this.refreshBaseAttributes();
	}
	
	public void switchToPlayerClass(EnumBattleClassesPlayerClass parPlayerClass) {
		EnumBattleClassesPlayerClass previousClass = this.playerClass.getPlayerClass();
		this.applyPlayerClass(parPlayerClass);
		
		FMLProxyPacket p = new BattleClassesPacketPlayerClassSnyc(this.ownerPlayer, parPlayerClass).generatePacket();
		if(this.getOwnerPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP) this.ownerPlayer;
			if(entityPlayerMP != null) {
				BattleClassesUtils.Log("Sending class switch sync to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
				BattleClassesMod.packetHandler.sendPacketToPlayer(p, entityPlayerMP);
			}
		}
		
		if(previousClass != EnumBattleClassesPlayerClass.NONE) {
			playerClass.getCooldownClock().setCooldownDefaultForced();
		}
	}
	
	protected void applyPlayerClass(EnumBattleClassesPlayerClass parPlayerClass) {
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
		BattleClassesUtils.Log(this.ownerPlayer.getDisplayName() + " switched to class: " + parPlayerClass.toString(), LogType.CORE);
	}
	
	public EntityPlayer getOwnerPlayer() {
		return this.ownerPlayer;
	}
	
	
	protected BattleClassesAttributes baseAttributes;
	protected BattleClassesAttributes displayedAttributes;
	//AMPLIFIERS
	/** Should be called every time when there is a change in: Weapon equiped OR Armor worn OR Talent points OR Potion effects OR onPlayerSpawn  */
	public void onAttributeSourcesChanged() {
		refreshBaseAttributes();
		BattleClassesAttributes totalAttributes = this.getTotalAttributesForAbility(0);
		
		//par1EntityLivingBase.setAbsorptionAmount(par1EntityLivingBase.getAbsorptionAmount() - (float)(4 * (par3 + 1)));
		//SharedMonsterAttributes.maxHealth
		
		//Refreshing health based on totalAttributes
		float relativeHealth = this.getOwnerPlayer().getHealth() / this.getOwnerPlayer().getMaxHealth();
		AttributeModifier newHealthModifier = new AttributeModifier(UUID.fromString("5D6F0BA2-1186-46AC-B896-BCA001EE0001"), "bcattribute.healthBoost", totalAttributes.health, 0);
		IAttributeInstance iattributeinstance = this.ownerPlayer.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
		AttributeModifier oldHealthModifier = iattributeinstance.getModifier(UUID.fromString("5D6F0BA2-1186-46AC-B896-BCA001EE0001"));
		if(oldHealthModifier != null) {
			iattributeinstance.removeModifier(oldHealthModifier);
		}
		iattributeinstance.applyModifier(newHealthModifier);
		
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
		baseAttributes.health = DEFAULT_PLAYER_HP;
		//baseAttributes.armor = this.getOwnerPlayer().getArm
		return baseAttributes;
	}
	
	public BattleClassesAttributes getItemAttributes() {
		BattleClassesAttributes itemAttributes = new BattleClassesAttributes();
		if(this.ownerPlayer.inventory != null) {
			for(ItemStack armorItemStack : this.ownerPlayer.inventory.armorInventory) {
				if(armorItemStack != null && armorItemStack.getItem() instanceof IAttributeProvider) {
					itemAttributes.add(((IAttributeProvider)armorItemStack.getItem()).getAttributes());
				}
			}
		}
		ItemStack mainHandItemStack = BattleClassesUtils.getMainhandItemStack(ownerPlayer);
		ItemStack offHandItemStack = BattleClassesUtils.getOffhandItemStack(ownerPlayer);
		if(mainHandItemStack != null && mainHandItemStack.getItem() instanceof IAmplifyProvider) {
			itemAttributes.add(((IAttributeProvider)mainHandItemStack.getItem()).getAttributes());
		}
		if(offHandItemStack != null && offHandItemStack.getItem() instanceof IAmplifyProvider) {
			itemAttributes.add(((IAttributeProvider)offHandItemStack.getItem()).getAttributes());
		}
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
	
	protected void refreshBaseAttributes() {
		BattleClassesAttributes attributes = new BattleClassesAttributes();
		attributes.add(this.getDefaultAttributes());
		attributes.add(this.getItemAttributes());
		this.baseAttributes = attributes;
		this.displayedAttributes = new BattleClassesAttributes();
		this.displayedAttributes.add(baseAttributes);
		this.displayedAttributes.add(this.getTotalBasedAttributeBonuses(0, this.displayedAttributes));
	}
	
	public BattleClassesAttributes getDisplayedAttributes() {
		if(this.displayedAttributes == null) {
			this.refreshBaseAttributes();
		}
		return this.displayedAttributes;
	}
	
	public BattleClassesAttributes getTotalAttributesForAbility(int targetAbilityID) {
		BattleClassesAttributes totalAttributes = new BattleClassesAttributes();
		totalAttributes.add(this.baseAttributes);
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
	
	public static final String NBT_TAGNAME_COMPOUNDNAME_KEY = "Name";
	public static final String NBT_TAGNAME_COMPOUNDNAME_VALUE = "BattleClasses";
	public static final String NBT_TAGNAME_PLAYERCLASS = "BC_PlayerClass"; 
	public static final String NBT_TAGNAME_TALENT_TREE_STATES = "BC_TalentTreeStates";
	public static final String NBT_TAGNAME_CD_MAP = "BC_Cooldowns";
	public static final String NBT_TAGNAME_CD_KEY = "CD_Key";
	public static final String NBT_TAGNAME_CD_SETTIME = "CD_SetTime";
	public static final String NBT_TAGNAME_CD_LASTDURATION = "CD_LastDuration";
	public static final String NBT_TAGNAME_CD_LASTTYPE = "CD_LastType";
	
	public NBTTagCompound writeTagCompound() {
    	NBTTagCompound tagCompound = new NBTTagCompound();
    	//Saving name of the tagCompound
    	tagCompound.setString(NBT_TAGNAME_COMPOUNDNAME_KEY, NBT_TAGNAME_COMPOUNDNAME_VALUE);
    	
    	//Saving Player BattleClass
    	tagCompound.setInteger(NBT_TAGNAME_PLAYERCLASS, this.playerClass.getPlayerClass().ordinal());
    	
    	//Saving Talent Matrix
    	tagCompound.setIntArray(NBT_TAGNAME_TALENT_TREE_STATES, this.playerClass.talentMatrix.getPointsOnTrees());
    	
    	//Saving main CooldownClock map
    	NBTTagList cooldownClocks = new NBTTagList();
    	ArrayList<Integer> cooldownClockKeys = new ArrayList<Integer>(mainCooldownMap.keySet());
    	for(int i = 0; i < cooldownClockKeys.size(); ++i) {
    		int cooldownClockKey = cooldownClockKeys.get(i);
    		CooldownClock cooldownClock = mainCooldownMap.get(cooldownClockKey);
    		
    		NBTTagCompound cooldownClockTagCompound = new NBTTagCompound();
    		cooldownClockTagCompound.setFloat(NBT_TAGNAME_CD_SETTIME, cooldownClock.getSetTime());
    		cooldownClockTagCompound.setFloat(NBT_TAGNAME_CD_LASTDURATION, cooldownClock.getLastUsedDuration());
    		cooldownClockTagCompound.setInteger(NBT_TAGNAME_CD_LASTTYPE, cooldownClock.getLastUsedType().ordinal());
    		cooldownClockTagCompound.setInteger(NBT_TAGNAME_CD_KEY, cooldownClockKey);
    		cooldownClocks.appendTag(cooldownClockTagCompound);
    	}
    	tagCompound.setTag(NBT_TAGNAME_CD_MAP, cooldownClocks);
    	
    	return tagCompound;
	}
	
    public NBTTagList writeToNBT(NBTTagList nbtTagList) {
    	nbtTagList.appendTag(this.writeTagCompound());
    	return nbtTagList;
    }
    
    public void readTagCompound(NBTTagCompound nbttagcompound) {
    	//Loading Player BattleClass
    	int classCode = nbttagcompound.getInteger(NBT_TAGNAME_PLAYERCLASS);
    	EnumBattleClassesPlayerClass playerClass = EnumBattleClassesPlayerClass.values()[classCode];
    	
    	//Loading talent tree states
    	int[] pointsOnTrees = nbttagcompound.getIntArray(NBT_TAGNAME_TALENT_TREE_STATES);
    	
    	if(playerClass != EnumBattleClassesPlayerClass.NONE) {
    		this.applyPlayerClass(playerClass);
    		this.playerClass.getCooldownClock().setEnabled(false);
    		this.playerClass.talentMatrix.applyPointsOnTrees(pointsOnTrees);
    		this.playerClass.getCooldownClock().setEnabled(true);
    	}
    	
    	//Loading main CooldownClock map
    	NBTTagList cooldownClocksNBTTagList = (NBTTagList) nbttagcompound.getTag(NBT_TAGNAME_CD_MAP);
    	if(cooldownClocksNBTTagList != null) {
    		for (int i = 0; i < cooldownClocksNBTTagList.tagCount(); ++i) {
                NBTTagCompound cooldownClockTagCompound = cooldownClocksNBTTagList.getCompoundTagAt(i);
                CooldownClock cooldownClock = this.mainCooldownMap.get(cooldownClockTagCompound.getInteger(NBT_TAGNAME_CD_KEY));
                if(cooldownClock != null) {
                	cooldownClock.setSetTime(cooldownClockTagCompound.getFloat(NBT_TAGNAME_CD_SETTIME));
                	cooldownClock.setLastUsedDuration(cooldownClockTagCompound.getFloat(NBT_TAGNAME_CD_LASTDURATION));
                	EnumBattleClassesCooldownType cooldownType = EnumBattleClassesCooldownType.values()[cooldownClockTagCompound.getInteger(NBT_TAGNAME_CD_LASTTYPE)];
                	cooldownClock.setLastUsedType(cooldownType);
                }
    		}
    	}
    }
	
    public void readFromNBT(NBTTagList nbtTagList) {
    	//Searching for BattleClasses NBTTagCompound
    	NBTTagCompound nbttagcompound_master = null;
    	for (int i = 0; i < nbtTagList.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbtTagList.getCompoundTagAt(i);
            if(nbttagcompound.getString(NBT_TAGNAME_COMPOUNDNAME_KEY).equals(NBT_TAGNAME_COMPOUNDNAME_VALUE)) {
            	nbttagcompound_master = nbttagcompound;
            }
        }
    	if(nbttagcompound_master == null) {
    		return;
    	}
    	this.readTagCompound(nbttagcompound_master);
    }
	
}
