package mods.battleclasses;

import java.util.Collection;
import java.util.EnumSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbilityShieldBlock;
import mods.battleclasses.core.BattleClassesPlayerAttributes;
import mods.battleclasses.core.BattleClassesPlayerClass;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.BattleClassesSpellBook;
import mods.battleclasses.core.BattleClassesTalentMatrix;
import mods.battleclasses.core.BattleClassesWeaponHitHandler;
import mods.battleclasses.core.CooldownClock;
import mods.battleclasses.core.ICooldownOwner;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battlegear2.api.core.BattlegearUtils;
import mods.battlegear2.api.core.IBattlePlayer;
import mods.battlegear2.api.core.InventoryPlayerBattle;
import mods.battlegear2.api.shield.IShield;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
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
    
    public static void ErrorLog(String message) {
    	battleClassesLogger.error(message);
    }
    
    public static boolean testMode() {
    	return false;
    }
	
	public static double getCurrentTimeInSeconds() {
		return ((double) Minecraft.getSystemTime()) / 1000L;
	}
	
	/*
	private long getTime() {
        return (System.nanoTime() / 1000000);
        //return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    */
	
	public static float getCooldownPercentage(ICooldownOwner coolDownHolder) {
		return getCooldownPercentage(coolDownHolder.getCooldownClock());
	}
	
	public static float getCooldownPercentage(CooldownClock coolClock) {
		return (float) (coolClock.getRemainingDuration() / coolClock.getLastUsedDuration());
	}
	
	public static void setEntityPlayerItemInUseInSeconds(EntityPlayer entityPlayer, ItemStack itemStack, float time) {
		int countDownTickStart = (int) (72000 + time*20);
		entityPlayer.setItemInUse(itemStack, countDownTickStart);
	}
	
	public static boolean isPlayerInBattlemode(EntityPlayer entityplayer) {
		return entityplayer instanceof IBattlePlayer && ((IBattlePlayer)entityplayer).isBattlemode();
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
	
	public static BattleClassesPlayerClass getPlayerClassObj(EntityPlayer entityPlayer) {
		BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(entityPlayer);
		if(playerHooks != null) {
			return playerHooks.playerClass;
		}
		return null;
	}
	
	public static EnumBattleClassesPlayerClass getPlayerClassEnum(EntityPlayer entityPlayer) {
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
	
	public static BattleClassesPlayerAttributes getPlayerAttributes(EntityPlayer entityPlayer) {
		BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(entityPlayer);
		if(playerHooks.playerClass != null) {
			return playerHooks.playerAttributes;
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
	
	public static BattleClassesWeaponHitHandler getPlayerWeaponHandler(EntityPlayer entityPlayer) {
		BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(entityPlayer);
		if(playerHooks.weaponHitHandler != null) {
			return playerHooks.weaponHitHandler;
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
		if(target instanceof EntityPlayer) {
			EntityPlayer targetPlayer = (EntityPlayer) target;
			MinecraftServer mcs = MinecraftServer.getServer();
			if(entityPlayerTargeting.getTeam() != null && target.getTeam() != null) {
				return entityPlayerTargeting.getTeam() == target.getTeam();
			}
			if(mcs.isPVPEnabled()) {
				return false;
			}
			else {
				return true;
			}
		}
		if(target instanceof IEntityOwnable) {
			IEntityOwnable targetOwnable = (IEntityOwnable) target;
			if(targetOwnable.getOwner() != null && (targetOwnable.getOwner() instanceof EntityPlayer)) {
				return isTargetFriendly(entityPlayerTargeting, (EntityPlayer)targetOwnable);
			}
			return true;
		}
		if(target instanceof EntityHorse) {
			EntityHorse targetHorse = (EntityHorse) target;
			if(targetHorse.isTame()) {
				return true;
			}
			return false;
		}
		
		return false;
	}
	
	public static boolean isReadyToUseShield(EntityPlayer entityPlayer) {
		BattleClassesSpellBook spellBook = getPlayerSpellBook(entityPlayer);
		if(spellBook != null) {
			BattleClassesAbstractAbility chosenAbility = spellBook.getChosenAbility();
			if(chosenAbility != null) {
				if(chosenAbility.getAbilityID().equals(BattleClassesAbilityShieldBlock.INSTANCE.getAbilityID())) {
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
		//return BattleClassesUtils.getBattleInventory(entityPlayer).getStackInSlot(0 + InventoryPlayerBattle.OFFSET+(InventoryPlayerBattle.WEAPON_SETS));
		return BattleClassesUtils.getBattleInventory(entityPlayer).extraItems[1];
	}
	
	public static ItemStack getMainhandItemStack(EntityPlayer entityPlayer) {
		//return BattleClassesUtils.getBattleInventory(entityPlayer).getStackInSlot(0 + InventoryPlayerBattle.OFFSET);
		return BattleClassesUtils.getBattleInventory(entityPlayer).extraItems[0];
	}
	
	public static ItemStack getMainhandItemHeld(EntityPlayer entityPlayer) {
		return entityPlayer.getHeldItem();
	}
	
	public static ItemStack getOffhandItemHeld(EntityPlayer entityPlayer) {
		if(isPlayerInBattlemode(entityPlayer)) {
			return getOffhandItemStack(entityPlayer);
		}
		return null;
	}
	
	public static Potion getPotionByID(int potionID) {
		return Potion.potionTypes[potionID];
	}
	
	public static Collection<PotionEffect> getActivePotionEffectsFromEntity(EntityLivingBase entity) {
		return entity.getActivePotionEffects();
	}
	
	public static boolean playerHasConsumableItems(EntityPlayer entityPlayer, Item item, int count) {
		int itemsFound = 0;
		for (int j = 0; j < entityPlayer.inventory.mainInventory.length; ++j) {
            if (entityPlayer.inventory.mainInventory[j] != null && entityPlayer.inventory.mainInventory[j].getItem() == item) {
            	itemsFound += entityPlayer.inventory.mainInventory[j].stackSize;
            	if(itemsFound >= count) {
            		return true;
            	}
            }
        }
		return false;
	}
	
}
