package mods.battleclasses.ability.active;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public enum EnumBattleClassesAbilityCastingType {
	CASTED {
		@Override
		public void onUseStart(BattleClassesAbstractAbilityActive ability, ItemStack itemStack, World world, EntityPlayer entityPlayer) {
			ability.startCasting(entityPlayer, itemStack);
			
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			if(side == Side.SERVER) {
				if(!ability.ignoresGlobalCooldown) {
					BattleClassesUtils.getPlayerSpellBook(entityPlayer).setGlobalCooldownForCasting(ability.baseCastTime);
				}
			}
		}
		
		@Override
		public void onUseTick(BattleClassesAbstractAbilityActive ability, ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
			
		}

		@Override
		public void onUseRelease(BattleClassesAbstractAbilityActive ability, ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
			int remainingCastTick = tickCount - 72000;
			if(remainingCastTick <= 0) {
				ability.requestUseFinishAndTargetSearch(entityPlayer, itemStack, tickCount);
			}
			else {
				ability.cancelCasting(entityPlayer);
			}
		}

		@Override
		public void onUseFinished(BattleClassesAbstractAbilityActive ability, EntityLivingBase targetEntity, int tickCount) {
			ability.consumeResources();
		}
	},
	CHANNELED {
		@Override
		public void onUseStart(BattleClassesAbstractAbilityActive ability, ItemStack itemStack, World world, EntityPlayer entityPlayer) {
			ability.startCasting(entityPlayer, itemStack);
			
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			if(side == Side.SERVER) {
				if(!ability.ignoresGlobalCooldown) {
					BattleClassesUtils.getPlayerSpellBook(entityPlayer).setGlobalCooldownForCasting(ability.baseCastTime);
				}
			}
		}
		
		@Override
		public void onUseTick(BattleClassesAbstractAbilityActive ability, ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
			int currentCastTick = tickCount - 72000;
			if(currentCastTick >= 0) {
				int ticksPerProceed = ability.getCastTimeInTicks() / ability.channelTickCount;
				int currentCastTickInverted = ability.getCastTimeInTicks() - currentCastTick;
				if(currentCastTickInverted > 0 && (currentCastTickInverted % ticksPerProceed) == 0) {
					//Set To Cooldown on first channel tick
					if(currentCastTickInverted == ticksPerProceed) {
						BattleClassesUtils.Log("First Channel tick! Set CD here! CD should set: " + ability.getCooldownClock().getDefaultDuration(), LogType.ABILITY);
						ability.startCooldown();
					}
					BattleClassesUtils.Log("Channeling... Current tick: " + currentCastTickInverted + " Cast time in tick " + ability.getCastTimeInTicks(), LogType.ABILITY);
					ability.consumeAmmo();
					ability.requestUseFinishAndTargetSearch(entityPlayer, itemStack, tickCount);
				}
			}
		}

		@Override
		public void onUseRelease(BattleClassesAbstractAbilityActive ability, ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
			ability.cancelCasting(entityPlayer);
		}

		@Override
		public void onUseFinished(BattleClassesAbstractAbilityActive ability, EntityLivingBase targetEntity, int tickCount) {
			
		}
	},
	INSTANT {
		@Override
		public void onUseStart(BattleClassesAbstractAbilityActive ability, ItemStack itemStack, World world, EntityPlayer entityPlayer) {
			
		}
		
		@Override
		public void onUseTick(BattleClassesAbstractAbilityActive ability, ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
			
		}

		@Override
		public void onUseRelease(BattleClassesAbstractAbilityActive ability, ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
			ability.requestUseFinishAndTargetSearch(entityPlayer, itemStack, tickCount);
			
		}

		@Override
		public void onUseFinished(BattleClassesAbstractAbilityActive ability, EntityLivingBase targetEntity, int tickCount) {
			ability.consumeResources();
		}
	},
	
	UNKNOWN;
	
	
	public void onUseStart(BattleClassesAbstractAbilityActive ability, ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		
	}
	
	public void onUseRelease(BattleClassesAbstractAbilityActive ability, ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		
	}
	
	public void onUseFinished(BattleClassesAbstractAbilityActive ability, EntityLivingBase targetEntity, int tickCount) {
		
	}
	
	public void onUseTick(BattleClassesAbstractAbilityActive ability, ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		
	}
	
	public String getTranslatedAbilityInfo() {
		return StatCollector.translateToLocal("bcability.info.casttype." + this.toString().toLowerCase());
	}
}
