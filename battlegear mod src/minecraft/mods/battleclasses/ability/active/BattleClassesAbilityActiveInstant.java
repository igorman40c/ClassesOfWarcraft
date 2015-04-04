package mods.battleclasses.ability.active;

import mods.battleclasses.enums.EnumBattleClassesAbilityCastingType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BattleClassesAbilityActiveInstant extends BattleClassesAbstractAbilityActive  {

	public BattleClassesAbilityActiveInstant(int parAbilityID) {
		super(parAbilityID);
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}


	@Override
	public boolean performEffects(EntityLivingBase targetEntity, int tickCount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public final EnumBattleClassesAbilityCastingType getCastingType() {
		return EnumBattleClassesAbilityCastingType.CastType_INSTANT;
	}
	
	@Override
	protected void onUseStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		super.onUseStart(itemStack, world, entityPlayer);
	}
	
	@Override
	protected void onUseRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		this.requestUseFinish(entityPlayer, itemStack, tickCount);	
	}

	@Override
	protected void onUseFinished(EntityLivingBase targetEntity, int tickCount) {
		super.onUseFinished(targetEntity, tickCount);
		this.getCooldownClock().setCooldownDefault();
	}
}
