package mods.battleclasses.ability.effect;

import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class BattleClassesDamageSources {
	//public static DamageSource melee = new DamageSource("bc.damagesource.melee")
	public static DamageSource createEntityDamageSourceForAbilitySchool(EntityLivingBase damageDealer, EnumBattleClassesAbilitySchool abilitySchool) {
		DamageSource damageSource = null;
		if(damageDealer instanceof EntityPlayer) {
			damageSource = DamageSource.causePlayerDamage((EntityPlayer)damageDealer);
		}
		else {
			damageSource = DamageSource.causeMobDamage(damageDealer);
		}
		configureDamageSourceForAbilitySchool(damageSource, abilitySchool);
		return damageSource;
	}
	
	protected static void configureDamageSourceForAbilitySchool(DamageSource damageSource, EnumBattleClassesAbilitySchool abilitySchool) {
		switch(abilitySchool) {
//		case PHYSICAL_MELEE_ENERGY:
//			break;
		case PHYSICAL_MELEE:
			break;
		case PHYSICAL_RANGED:
			damageSource.setProjectile();
			break;
		case SPELL_ARCANE:
			configureDamageSourceToMagic(damageSource);
			break;
		case SPELL_FIRE:
			configureDamageSourceToMagic(damageSource).setFireDamage();
			break;
		case SPELL_FROST:
			configureDamageSourceToMagic(damageSource);
			break;
		case SPELL_HOLY:
			configureDamageSourceToMagic(damageSource);
			break;
		case SPELL_SHADOW:
			configureDamageSourceToMagic(damageSource);
			break;
		case UNKNOWN:
			break;
		default:
			break;
		}
	}
	
	private static DamageSource configureDamageSourceToMagic(DamageSource damageSource) {
		damageSource.setDamageBypassesArmor();
		damageSource.setMagicDamage();
		return damageSource;
	}
}
