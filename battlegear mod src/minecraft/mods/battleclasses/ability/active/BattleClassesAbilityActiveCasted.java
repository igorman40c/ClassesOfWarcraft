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
	public void onCastStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		super.onCastStart(itemStack, world, entityPlayer);
		this.startCastingProcess(entityPlayer, itemStack);
		
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.SERVER) {
			if(!this.ignoresGlobalCooldown) {
				BattleClassesUtils.getPlayerSpellBook(entityPlayer).setGlobalCooldownForCasting(this.castTime);
			}
		}
	}
	
	@Override
	public void onCastRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		super.onCastRelease(itemStack, entityPlayer, tickCount);
		int remainingCastTick = tickCount - 72000;
		if(remainingCastTick <= 0) {
			this.requestCastingProcessFinish(entityPlayer, itemStack, tickCount);
		}
		else {
			this.cancelCasting(entityPlayer);
		}	
	}
	
	@Override
	public void onCastFinished(EntityLivingBase targetEntity, int tickCount) {
		super.onCastFinished(targetEntity, tickCount);
		this.getCooldownClock().setCooldownDefault();
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.SERVER) {
			if(!this.ignoresGlobalCooldown) {
				BattleClassesUtils.getPlayerSpellBook(this.getOwnerPlayer()).setGlobalCooldown();
			}
		}
	}
}
