package mods.battleclasses.ability.active;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.effect.BattleClassesAbilityEffectInstantDamage;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.items.weapons.IBattleClassesWeapon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BattleClassesAbilityAttackOffHand extends BattleClassesAbilityAttackMainHand {
	
	public static final BattleClassesAbilityAttackOffHand INSTANCE = new BattleClassesAbilityAttackOffHand();
	static {
		BattleClassesAbstractAbility.registerAbility(INSTANCE);
	}
	
	public BattleClassesAbilityAttackOffHand() {
		super();
		this.setUnlocalizedName("universal.attack.offhand");
	}
		
	public ItemStack getCorrespondingHeldItemstack() {
		return BattleClassesUtils.getOffhandBattleSlot(this.getOwnerPlayer());
	}
	
	public ItemStack getCounterHeldItemstack() {
		return BattleClassesUtils.getMainhandBattleSlot(this.getOwnerPlayer());
	}
	
}
