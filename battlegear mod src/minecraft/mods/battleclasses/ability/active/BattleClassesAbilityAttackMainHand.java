package mods.battleclasses.ability.active;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.effect.BattleClassesAbilityEffectInstantDamage;
import mods.battleclasses.ability.effect.BattleClassesAbstractAbilityEffect;
import mods.battleclasses.ability.effect.BattleClassesAbstractAbilityEffectInstantValue;
import mods.battleclasses.ability.effect.EffectFactory;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.core.BattleClassesPlayerAttributes;
import mods.battleclasses.enums.EnumBattleClassesAbilityDirectTargetRequirement;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.items.IBattleClassesWeapon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BattleClassesAbilityAttackMainHand extends BattleClassesAbilityActiveDirect{

	public static final BattleClassesAbilityAttackMainHand INSTANCE = new BattleClassesAbilityAttackMainHand();
	static {
		BattleClassesAbstractAbility.registerAbility(INSTANCE);
	}
	
	public BattleClassesAbilityAttackMainHand() {
		super();
		this.setUnlocalizedName("universal.attack.mainhand");
		this.setCastingType(EnumBattleClassesAbilityCastingType.UNKNOWN);
		this.ignoresGlobalCooldown = true;
		
		this.school=EnumBattleClassesAbilitySchool.PHYSICAL_MELEE;
		this.addEffect(BattleClassesAbilityEffectInstantDamage.createWeaponAttackEffect());
	}
		
	@Override
	public void performEffects(EntityLivingBase targetEntity, float partialMultiplier) {
		BattleClassesAttributes attributesForParentAbility = this.getPlayerAttributes().getTotalAttributesForAbility(this);
		float critChance = attributesForParentAbility.crit;
		
		System.out.println(this.getClass() + " PERFORM");
		
		this.setWeaponDamageOnAttributes(attributesForParentAbility);
		
		BattleClassesAbstractAbilityEffect.performListOfEffects(this.effects, attributesForParentAbility, critChance, partialMultiplier, this.getOwnerPlayer(), targetEntity);
	}
	
	public void setWeaponDamageOnAttributes(BattleClassesAttributes attributes) {
		ItemStack counterHeldItemStack = this.getCounterHeldItemstack();
		if(counterHeldItemStack != null && counterHeldItemStack.getItem() instanceof IBattleClassesWeapon && 
				BattleClassesUtils.getPlayerClassEnum(this.getOwnerPlayer()).isEligibleForClassAccessSet(((IBattleClassesWeapon)counterHeldItemStack.getItem()).getClassAccessSet())) {
			attributes.addValueByType(EnumBattleClassesAttributeType.MELEE_ATTACK_DAMAGE, (-1F)*((IBattleClassesWeapon)counterHeldItemStack.getItem()).getWeaponDamage());
		}
	}
	
	@Override
	public void startCooldown() {
		this.startCooldown(1);
	}
	
	public ItemStack getCorrespondingHeldItemstack() {
		return BattleClassesUtils.getMainhandBattleSlot(this.getOwnerPlayer());
	}
	
	public ItemStack getCounterHeldItemstack() {
		return BattleClassesUtils.getOffhandBattleSlot(this.getOwnerPlayer());
	}
	
	public static final float DEFAULT_WEAPON_COOLDOWN_DURATION = 1F;
	public void startCooldown(float contextMultiplier) {
		EntityPlayer ownerPlayer = this.getOwnerPlayer();
		ItemStack handHeldItemStack = this.getCorrespondingHeldItemstack();
		float duration;
		if(handHeldItemStack != null && handHeldItemStack.getItem() instanceof IBattleClassesWeapon) {
			duration = ((IBattleClassesWeapon)handHeldItemStack.getItem()).getSpeedInSeconds();
		}
		else {
			duration = DEFAULT_WEAPON_COOLDOWN_DURATION;
		}
		 
		duration *= contextMultiplier;
		duration *= this.getHasteMultiplier();
		this.cooldownClock.startCooldown(duration, false, this.cooldownClock.getDefaultType());
	}

}
