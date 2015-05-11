package mods.battleclasses.ability.active;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.enums.EnumBattleClassesAbilityCastingType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

class AbilityUseProcessor {
	
	public static AbilityUseProcessor INSTANCE = new AbilityUseProcessor();
	
	public AbilityUseProcessor createUseProcessor(EnumBattleClassesAbilityCastingType castingType,
			BattleClassesAbstractAbilityActive ability) {
		AbilityUseProcessor useProcessor;
		switch(castingType) {
		case CASTED:
			useProcessor = new AbilityUseProcessorCasted(ability);
			break;
		case CHANNELED:
			useProcessor = new AbilityUseProcessorChanneled(ability);
			break;
		case INSTANT:
			useProcessor = new AbilityUseProcessorInstant(ability);
			break;
		default:
			useProcessor = new AbilityUseProcessor(ability);
			break;
		}
		return useProcessor;
	}
	
	protected BattleClassesAbstractAbilityActive ability;
	
	protected AbilityUseProcessor() {
		
	}
	
	protected AbilityUseProcessor(BattleClassesAbstractAbilityActive ability) {
		this.ability = ability;
	}
	
	//----------------------------------------------------------------------------------
	//					SECTION - Blank use process implementation
	//----------------------------------------------------------------------------------	
	public void onUseStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		
	}
	
	public void onUseRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		
	}
	
	public void onUseFinished(EntityLivingBase targetEntity, int tickCount) {
		
	}
	
	public void onUseTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		
	}
	

	//----------------------------------------------------------------------------------
	//					SECTION - Instant use process implementation
	//----------------------------------------------------------------------------------	
	class AbilityUseProcessorInstant extends AbilityUseProcessor {

		protected AbilityUseProcessorInstant(BattleClassesAbstractAbilityActive ability) {
			super(ability);
		}

		@Override
		public void onUseStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
			
		}
		
		@Override
		public void onUseTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
			
		}

		@Override
		public void onUseRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
			this.ability.requestUseFinishAndTarget(entityPlayer, itemStack, tickCount);
			
		}

		@Override
		public void onUseFinished(EntityLivingBase targetEntity, int tickCount) {
			this.ability.getCooldownClock().setCooldownDefault();
		}

	}
	
	//----------------------------------------------------------------------------------
	//					SECTION - Casted use process implementation
	//----------------------------------------------------------------------------------
	class AbilityUseProcessorCasted extends AbilityUseProcessor {

		protected AbilityUseProcessorCasted(BattleClassesAbstractAbilityActive ability) {
			super(ability);
		}

		@Override
		public void onUseStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
			this.ability.startCasting(entityPlayer, itemStack);
			
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			if(side == Side.SERVER) {
				if(!this.ability.ignoresGlobalCooldown) {
					BattleClassesUtils.getPlayerSpellBook(entityPlayer).setGlobalCooldownForCasting(this.ability.castTime);
				}
			}
		}
		
		@Override
		public void onUseTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
			
		}

		@Override
		public void onUseRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
			int remainingCastTick = tickCount - 72000;
			if(remainingCastTick <= 0) {
				this.ability.requestUseFinishAndTarget(entityPlayer, itemStack, tickCount);
			}
			else {
				this.ability.cancelCasting(entityPlayer);
			}
		}

		@Override
		public void onUseFinished(EntityLivingBase targetEntity, int tickCount) {
			this.ability.getCooldownClock().setCooldownDefault();
		}

	}
	
	//----------------------------------------------------------------------------------
	//					SECTION - Channeled use process implementation
	//----------------------------------------------------------------------------------
	class AbilityUseProcessorChanneled extends AbilityUseProcessor {

		protected AbilityUseProcessorChanneled(BattleClassesAbstractAbilityActive ability) {
			super(ability);
		}

		@Override
		public void onUseStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
			this.ability.startCasting(entityPlayer, itemStack);
			
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			if(side == Side.SERVER) {
				if(!this.ability.ignoresGlobalCooldown) {
					BattleClassesUtils.getPlayerSpellBook(entityPlayer).setGlobalCooldownForCasting(this.ability.castTime);
				}
			}
		}
		
		@Override
		public void onUseTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
			int currentCastTick = tickCount - 72000;
			if(currentCastTick >= 0) {
				int ticksPerProceed = this.ability.getCastTimeInTicks() / this.ability.channelTickCount;
				int currentCastTickInverted = this.ability.getCastTimeInTicks() - currentCastTick;
				if(currentCastTickInverted > 0 && (currentCastTickInverted % ticksPerProceed) == 0) {
					//Set To Cooldown on first channel tick
					if(currentCastTickInverted == ticksPerProceed) {
						BattleClassesUtils.Log("First Channel tick! Set CD here! CD should set: " + this.ability.getCooldownClock().getDefaultDuration(), LogType.ABILITY);
						this.ability.getCooldownClock().setCooldownDefault();
					}
					BattleClassesUtils.Log("Channeling... Current tick: " + currentCastTickInverted + " Cast time in tick " + this.ability.getCastTimeInTicks(), LogType.ABILITY);
					this.ability.requestUseFinishAndTarget(entityPlayer, itemStack, tickCount);
				}
			}
		}

		@Override
		public void onUseRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
			this.ability.cancelCasting(entityPlayer);
		}

		@Override
		public void onUseFinished(EntityLivingBase targetEntity, int tickCount) {
			
		}
		
	}
}
