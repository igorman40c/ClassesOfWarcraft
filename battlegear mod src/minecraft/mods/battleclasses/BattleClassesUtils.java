package mods.battleclasses;

import java.util.EnumSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import mods.battleclasses.ability.BattleClassesAbilityShieldBlock;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.BattleClassesSpellBook;
import mods.battleclasses.core.BattleClassesTalentMatrix;
import mods.battleclasses.core.ICooldownOwner;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battlegear2.api.core.InventoryPlayerBattle;
import mods.battlegear2.api.shield.IShield;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BattleClassesUtils {
	
public static Logger battleClassesLogger = LogManager.getLogger("Battle Classes");
	
	public enum LogType {
		INIT,
    	GUI,
    	CORE,
    	PACKET,
    	ABILITY,
    	ATTRIBUTE,
    	AMPLIFIER,
    	COMMAND
    }
    
    public static void Log(String message, LogType logType) {
    	EnumSet<LogType> ignoredTypes = EnumSet.of(LogType.PACKET, LogType.GUI);
    	if(ignoredTypes.contains(logType)) {
    		return;
    	}
    	String logString = new String("[" + /*side.toString() + "/" + */  logType.toString() + "] " + ": " + message);
    	battleClassesLogger.info(logString);
    }
	
	public static float getCurrentTimeInSeconds() {
		return ((float) Minecraft.getSystemTime()) / 1000;
	}
	
	public static float getCooldownPercentage(ICooldownOwner coolDownHolder) {
		return coolDownHolder.getCooldownClock().getCooldownRemaining() / coolDownHolder.getCooldownClock().getLastUsedDuration();
	}
	
	public static void setEntityPlayerItemInUseInSeconds(EntityPlayer entityPlayer, ItemStack itemStack, float time) {
		int countDownTickStart = (int) (72000 + time*20);
		entityPlayer.setItemInUse(itemStack, countDownTickStart);
	}
	
	public static InventoryPlayerBattle getBattleInventory(EntityPlayer entityplayer) throws NullPointerException {
		if(entityplayer.inventory instanceof InventoryPlayerBattle) {
			return (InventoryPlayerBattle)entityplayer.inventory;
		}
		else {
			battleClassesLogger.error("Couldn't find InventoryPlayerBattle of player:" + entityplayer.getDisplayName());
			throw(new NullPointerException("Error when looking for InventoryPlayerBattle!"));
			//return null;
		}
	}
	
	public static BattleClassesPlayerHooks getPlayerHooks(EntityPlayer entityplayer) throws NullPointerException {
		if(entityplayer.inventory instanceof InventoryPlayerBattle) {
			InventoryPlayerBattle battleInventory = (InventoryPlayerBattle)entityplayer.inventory;
			return battleInventory.battleClassesPlayerHooks;
		}
		else {
			battleClassesLogger.error("Couldn't find InventoryPlayerBattle of player:" + entityplayer.getDisplayName());
			throw(new NullPointerException("Error when looking for InventoryPlayerBattle!"));
			//return null;
		}
	}
	
	public static float getCastPercentage(EntityPlayer entityplayer) {
		BattleClassesSpellBook spellBook = getPlayerSpellBook(entityplayer);
		if(spellBook != null) {
			if(spellBook.getChosenAbility() != null) {
				return spellBook.getChosenAbility().getCastPercentage(entityplayer);
			}
		}
		return 0;
	}
	
	public static EnumBattleClassesPlayerClass getPlayerClass(EntityPlayer entityPlayer) {
		BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(entityPlayer);
		if(playerHooks.playerClass != null) {
			return playerHooks.playerClass.getPlayerClass();
		}
		return null;
	}
	
	public static BattleClassesSpellBook getPlayerSpellBook(EntityPlayer entityPlayer) {
		BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(entityPlayer);
		if(playerHooks.playerClass != null) {
			return playerHooks.playerClass.spellBook;
		}
		return null;
	}
	
	public static BattleClassesTalentMatrix getPlayerTalentMatrix(EntityPlayer entityPlayer) {
		BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(entityPlayer);
		if(playerHooks.playerClass != null) {
			return playerHooks.playerClass.talentMatrix;
		}
		return null;
	}
	
	public static Entity getEntityByID(int entityID, World world)        
	{         
	    for(Object o: world.getLoadedEntityList())                
	    {                        
	        if(((Entity)o).getEntityId() == entityID)                        
	        {                                
	            //System.out.println("Found the entity");                                
	            return ((Entity)o);                        
	        }                
	    }                
	    return null;        
	} 
		
	public static boolean isTargetFriendly(EntityPlayer entityPlayerTargeting, EntityLivingBase target) {
		//TODO : !!!
		if(target instanceof EntityPlayer) {
			return true;
		}
		if(target instanceof IEntityOwnable) {
			return true;
		}
		/*
		if(target instanceof EntityHorse) {
			return true;
		}
		*/
		
		return false;
	}
	
	public static boolean isReadyToUseShield(EntityPlayer entityPlayer) {
		BattleClassesSpellBook spellBook = getPlayerSpellBook(entityPlayer);
		if(spellBook != null) {
			BattleClassesAbstractAbility chosenAbility = spellBook.getChosenAbility();
			if(chosenAbility != null) {
				if(chosenAbility.getAbilityID() == BattleClassesAbilityShieldBlock.SHIELD_BLOCK_ABILITY_ID) {
					ItemStack offhand = ((InventoryPlayerBattle)(entityPlayer).inventory).getCurrentOffhandWeapon();
					if(offhand != null) {
						return offhand.getItem() instanceof IShield;
					}
				}
				else {
					return false;
				}
			}
		}
		return true;
	}
	
	public static ItemStack getOffhandItemStack(EntityPlayer entityPlayer) {
		return BattleClassesUtils.getBattleInventory(entityPlayer).getStackInSlot(0 + InventoryPlayerBattle.OFFSET+(InventoryPlayerBattle.WEAPON_SETS));
	}
}
