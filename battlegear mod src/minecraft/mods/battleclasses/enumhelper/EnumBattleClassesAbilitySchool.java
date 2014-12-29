package mods.battleclasses.enumhelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public enum EnumBattleClassesAbilitySchool {
	PHYSICAL_MELEE_RAGE,
	PHYSICAL_MELEE_ENERGY,
	PHYSICAL_RANGED,
	
	SPELL_ARCANE,
	SPELL_FIRE,
	SPELL_FROST,
	SPELL_HOLY,
	SPELL_SHADOW,
	
	UNKNOWN;
	
	public float getCriticalStrikeChanceBase() {
		switch (this) {
			case PHYSICAL_MELEE_RAGE:
				return 0.10F;
			case PHYSICAL_MELEE_ENERGY:
				return 0.10F;
			case PHYSICAL_RANGED:
				return 0.10F;
			case SPELL_ARCANE:
				return 0.10F;
			case SPELL_FIRE:
				return 0.10F;
			case SPELL_FROST:
				return 0.10F;
			case SPELL_HOLY:
				return 0.10F;
			case SPELL_SHADOW:
				return 0.10F;
			default:
				break;
		}
		return 0;
	}
	
	public float getCriticalStrikeBonusBase() {
		switch (this) {
			case PHYSICAL_MELEE_RAGE:
				return 1.0F;
			case PHYSICAL_MELEE_ENERGY:
				return 1.0F;
			case PHYSICAL_RANGED:
				return 1.0F;
			case SPELL_ARCANE:
				return 1.0F;
			case SPELL_FIRE:
				return 1.0F;
			case SPELL_FROST:
				return 1.0F;
			case SPELL_HOLY:
				return 1.0F;
			case SPELL_SHADOW:
				return 1.0F;
			default:
				break;
		}
		return 0;
	}
	
    @SideOnly(Side.CLIENT)
	public int getCastBarColoringV() {
		switch (this) {
			case PHYSICAL_MELEE_RAGE:
				return 64;
			case PHYSICAL_MELEE_ENERGY:
				return 80;
			case PHYSICAL_RANGED:
				return 96;
			case SPELL_ARCANE:
				return 144;
			case SPELL_FIRE:
				return 112;
			case SPELL_FROST:
				return 128;
			case SPELL_HOLY:
				return 160;
			case SPELL_SHADOW:
				return 176;
			default:
				return 48;
		}
    }

}



