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
	public boolean performEffects(EntityLivingBase targetEntity, int tickCount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public final EnumBattleClassesAbilityCastingType getCastingType() {
		return EnumBattleClassesAbilityCastingType.CastType_INSTANT;
	}
	
	@Override
	public void onCastStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		super.onCastStart(itemStack, world, entityPlayer);
	}
	
	@Override
	public void onCastRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		this.requestCastingProcessFinish(entityPlayer, itemStack, tickCount);	
	}
	@Override
	public boolean isInstant() {
		return true;
	}

	@Override
	public void onCastFinished(EntityLivingBase targetEntity, int tickCount) {
		super.onCastFinished(targetEntity, tickCount);
		this.getCooldownClock().setCooldownDefault();
	}
}
