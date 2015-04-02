package mods.battleclasses.ability.active;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.enums.EnumBattleClassesAbilityCastingType;
import mods.battleclasses.enums.EnumBattleClassesAbilityDirectTargetRequirement;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import mods.battlegear2.api.core.InventoryPlayerBattle;
import mods.battlegear2.api.shield.IShield;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class BattleClassesAbilityShieldBlock extends BattleClassesAbstractAbilityActive {
	
	public static final int SHIELD_BLOCK_ABILITY_ID = 93;

	public BattleClassesAbilityShieldBlock() {
		super(SHIELD_BLOCK_ABILITY_ID);
		this.setName("universal.shieldblock");
		this.ignoresGlobalCooldown = true;
		this.castTime = 0;
		this.targetRequirementType = EnumBattleClassesAbilityDirectTargetRequirement.NEEDLESS;
	}
	/*
	@Override
    public float getCastTime() {
		ItemStack offHandItemStack = getIconItemStack();
    	if(offHandItemStack!=null) {
    		if(offHandItemStack.getItem() instanceof IShield) {
    			return 1/((IShield)offHandItemStack.getItem()).getDecayRate(offHandItemStack)*20;
    		}
    	}
    	return this.castTime;
    }
	*/
	@Override
	public boolean performEffects(EntityLivingBase targetEntity, int tickCount) {
		BattleClassesUtils.Log("ShieldBlock Performed effect, doing nothing", LogType.ABILITY);
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		return BattleClassesUtils.getBattleInventory(this.playerHooks.getOwnerPlayer()).getStackInSlot(0 + InventoryPlayerBattle.OFFSET+(InventoryPlayerBattle.WEAPON_SETS));
	}
	
    @SideOnly(Side.CLIENT)
    public boolean hasItemIcon() {
    	ItemStack offHandItemStack = getIconItemStack();
    	if(offHandItemStack!=null) {
    		return (offHandItemStack.getItem() instanceof IShield);
    	}
    	return false;
    }
    
    public String getName() {
		return this.name + " school:" + school + " intent:" + intent + " target:" + targetRequirementType;
	}
	@Override
	public EnumBattleClassesAbilityCastingType getCastingType() {
		return EnumBattleClassesAbilityCastingType.CastType_UNKNOWN;
	}
    
}