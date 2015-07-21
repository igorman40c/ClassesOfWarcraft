package mods.battleclasses.ability.active;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.effect.BattleClassesAbilityEffectInstantDamage;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.items.IBattleClassesWeapon;
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
	
	public void setWeaponDamageOnAttributes(BattleClassesAttributes attributes) {
		EntityPlayer ownerPlayer = this.getOwnerPlayer();
		ItemStack mainHandItemHeld = BattleClassesUtils.getMainhandItemHeld(ownerPlayer);
		if(mainHandItemHeld != null && mainHandItemHeld.getItem() instanceof IBattleClassesWeapon) {
			attributes.addValueByType(EnumBattleClassesAttributeType.MELEE_ATTACK_DAMAGE, (-1F)*((IBattleClassesWeapon)mainHandItemHeld.getItem()).getWeaponDamage());
		}
	}
	
	public ItemStack getCorrespondingHeldItemstack() {
		return BattleClassesUtils.getOffhandItemHeld(this.getOwnerPlayer());
	}
	
}
