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

public class BattleClassesAbilityActiveCasted extends BattleClassesAbstractAbilityActive {

	public BattleClassesAbilityActiveCasted(int parAbilityID) {
		super(parAbilityID);
	}

	@Override
	public boolean performEffects(EntityLivingBase targetEntity, int tickCount) {
		//TODO
		return false;
	}
	
	@Override
	public final EnumBattleClassesAbilityCastingType getCastingType() {
		return EnumBattleClassesAbilityCastingType.CastType_CASTED;
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
	protected void onUseRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		super.onUseTick(itemStack, entityPlayer, tickCount);
		int remainingCastTick = tickCount - 72000;
		if(remainingCastTick <= 0) {
			this.requestUseFinishAndTarget(entityPlayer, itemStack, tickCount);
		}
		else {
			this.cancelCasting(entityPlayer);
		}	
	}
	
	@Override
	protected void onUseFinished(EntityLivingBase targetEntity, int tickCount) {
		super.onUseFinished(targetEntity, tickCount);
		this.getCooldownClock().setCooldownDefault();
		Side side = FMLCommonHandler.instance().getEffectiveSide();
	}
}
