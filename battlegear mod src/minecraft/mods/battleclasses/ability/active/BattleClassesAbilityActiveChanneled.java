package mods.battleclasses.ability.active;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.enums.EnumBattleClassesAbilityCastingType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BattleClassesAbilityActiveChanneled extends BattleClassesAbstractAbilityActive  {
	
	protected int channelTickCount = 1;
	
	public int getChannelTicks() {
		return this.channelTickCount;
	}
	
	public BattleClassesAbilityActiveChanneled(int parAbilityID) {
		super(parAbilityID);
	}

	@Override
	public boolean performEffects(EntityLivingBase targetEntity, int tickCount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public final EnumBattleClassesAbilityCastingType getCastingType() {
		return EnumBattleClassesAbilityCastingType.CastType_CHANNELED;
	}

	@Override
	protected void onUseStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		super.onUseStart(itemStack, world, entityPlayer);
		this.startCasting(entityPlayer, itemStack);
		
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.SERVER) {
			if(!this.ignoresGlobalCooldown) {
				BattleClassesUtils.getPlayerSpellBook(entityPlayer).setGlobalCooldownForCasting(this.castTime);
			}
		}
	}
	
	@Override
	protected void onUseTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		super.onUseTick(itemStack, entityPlayer, tickCount);
		int currentCastTick = tickCount - 72000;
		if(currentCastTick >= 0) {
			int ticksPerProceed = this.getCastTimeInTicks() / this.channelTickCount;
			int currentCastTickInverted = this.getCastTimeInTicks() - currentCastTick;
			if(currentCastTickInverted > 0 && (currentCastTickInverted % ticksPerProceed) == 0) {
				//Set To Cooldown on first channel tick
				if(currentCastTickInverted == ticksPerProceed) {
					BattleClassesUtils.Log("First Channel tick! Set CD here! CD should set: " + this.getCooldownClock().getDefaultDuration(), LogType.ABILITY);
					this.getCooldownClock().setCooldownDefault();
				}
				BattleClassesUtils.Log("Channeling... Current tick: " + currentCastTickInverted + " Cast time in tick " + this.getCastTimeInTicks(), LogType.ABILITY);
				this.requestUseFinishAndTarget(entityPlayer, itemStack, tickCount);
			}
		}
	}
	
	@Override
	protected void onUseRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		this.cancelCasting(entityPlayer);
	}
	
	@Override
	protected void onUseFinished(EntityLivingBase targetEntity, int tickCount) {
		super.onUseFinished(targetEntity, tickCount);
	}
	
}
