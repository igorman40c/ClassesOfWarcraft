package mods.battleclasses.enums;

import net.minecraft.util.StatCollector;
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
	
	public float getCriticalStrikeBonus() {
		switch (this) {
			case PHYSICAL_MELEE_RAGE:
				return 1.5F;
			case PHYSICAL_MELEE_ENERGY:
				return 1.5F;
			case PHYSICAL_RANGED:
				return 2F;
			case SPELL_ARCANE:
				return 1.5F;
			case SPELL_FIRE:
				return 2F;
			case SPELL_FROST:
				return 2F;
			case SPELL_HOLY:
				return 1.5F;
			case SPELL_SHADOW:
				return 1.5F;
			default:
				break;
		}
		return 0;
	}
	
	public boolean hasCastingSound() {
		switch (this) {
			case UNKNOWN:
				return false;
			case PHYSICAL_MELEE_RAGE:
				return false;
			case PHYSICAL_MELEE_ENERGY:
				return false;
			default:
				return true;
		}
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
    
    public boolean isMagical() {
    	boolean magical = false;
    	switch(this) {
		case SPELL_ARCANE:
			magical = true;
			break;
		case SPELL_FIRE:
			magical = true;
			break;
		case SPELL_FROST:
			magical = true;
			break;
		case SPELL_HOLY:
			magical = true;
			break;
		case SPELL_SHADOW:
			magical = true;
			break;
		default:
			magical = false;
			break;
    	}
    	return magical;
    }
    
    public String getTranslatedDisplayedName() {
    	String unlocaizedName = new String("abilityschool." + this.toString() + ".name");
    	String displayedName = StatCollector.translateToLocal(unlocaizedName);
    	return displayedName;
    }

}



