package mods.battleclasses.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.passive.BattleClassesAbstractAbilityPassive;
import mods.battleclasses.core.classes.BattleClassesPlayerClassMage;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.gui.tab.BattleClassesTabOverlayAttributes;
import mods.battleclasses.items.IAttributeProviderItem;
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


public class BattleClassesPlayerHooks implements IMainCooldownMap {
	
	protected EntityPlayer ownerPlayer;
	
	public BattleClassesPlayerClass playerClass;
	public BattleClassesPlayerAttributes playerAttributes;
	public BattleClassesWeaponHitHandler weaponHitHandler;
	public HashMap<String, CooldownClock> mainCooldownMap;
	
	public BattleClassesPlayerHooks(EntityPlayer parOwnerPlayer) {
		this.ownerPlayer = parOwnerPlayer;
		mainCooldownMap = new HashMap<String, CooldownClock>();
		
		playerAttributes = new BattleClassesPlayerAttributes(this);
		playerClass = new BattleClassesPlayerClass(this, EnumBattleClassesPlayerClass.NONE);
		weaponHitHandler = new BattleClassesWeaponHitHandler(this);
		System.out.println("PlayerHooks constructed!");
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
			playerClass.getCooldownClock().startDefaultCooldownForced();
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
		onAttributeSourcesChanged();
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.CLIENT) {
			BattleClassesTabOverlayAttributes.INSTANCE.reInit();
		}
		BattleClassesUtils.Log(this.ownerPlayer.getDisplayName() + " switched to class: " + parPlayerClass.toString(), LogType.CORE);
	}
	
	public EntityPlayer getOwnerPlayer() {
		return this.ownerPlayer;
	}
	
	
	public void onAttributeSourcesChanged() {
		this.playerAttributes.onAttributeSourcesChanged();
	}

	@Override
	public HashMap<String, CooldownClock> getCooldownMap() {
		return this.mainCooldownMap;
	}

	@Override
	public EntityPlayer getCooldownCenterOwner() {
		return this.getOwnerPlayer();
	}
	
	@Override
	public float getCooldownMultiplierForAbility(BattleClassesAbstractAbility ability) {
		float cooldownMultiplier = 1F;
		for(BattleClassesAbstractAbilityPassive passiveAbility : this.playerClass.spellBook.getPassiveAbilitiesInArray()) {
			if(passiveAbility instanceof ICooldownModifier) {
				cooldownMultiplier *= ((ICooldownModifier) passiveAbility).getMultiplierForAbility(ability);
			}
		}
		return cooldownMultiplier;
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
    	ArrayList<String> cooldownClockKeys = new ArrayList<String>(mainCooldownMap.keySet());
    	for(int i = 0; i < cooldownClockKeys.size(); ++i) {
    		String cooldownClockKey = cooldownClockKeys.get(i);
    		CooldownClock cooldownClock = mainCooldownMap.get(cooldownClockKey);
    		
    		NBTTagCompound cooldownClockTagCompound = new NBTTagCompound();
    		cooldownClockTagCompound.setDouble(NBT_TAGNAME_CD_SETTIME, cooldownClock.getSetTime());
    		cooldownClockTagCompound.setFloat(NBT_TAGNAME_CD_LASTDURATION, cooldownClock.getLastUsedDuration());
    		cooldownClockTagCompound.setInteger(NBT_TAGNAME_CD_LASTTYPE, cooldownClock.getLastUsedType().ordinal());
    		cooldownClockTagCompound.setString(NBT_TAGNAME_CD_KEY, cooldownClockKey);
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
                CooldownClock cooldownClock = this.mainCooldownMap.get(cooldownClockTagCompound.getString(NBT_TAGNAME_CD_KEY));
                if(cooldownClock != null) {
                	cooldownClock.setSetTime(cooldownClockTagCompound.getDouble(NBT_TAGNAME_CD_SETTIME));
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
