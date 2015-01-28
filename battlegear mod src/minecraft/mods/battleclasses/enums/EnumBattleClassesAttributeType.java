package mods.battleclasses.enums;

public enum EnumBattleClassesAttributeType {
	//Primary
	HEALTH,
	STRENGTH,
	AGILITY,
	SPELLPOWER_ARCANE,
	SPELLPOWER_FIRE,
	SPELLPOWER_FROST,
	SPELLPOWER_HOLY,
	SPELLPOWER_SHADOW,
	//Secondary
	HASTE_RATING,
	CRITICAL_RATING,
	ARMOR_PENETRATION,
	WEAPON_DAMAGE,
	
	//Vanilla attributes
	ARMOR
	;
		
	public int getDisplayIconSquareSize() {
		return 9;
	}
		
	public int getDisplayIconU() {
		return 208; 
	}
	
	public boolean isDisplayedInPercentage() {
		switch(this) {
			case ARMOR_PENETRATION:  {
				return true;
			}
			case CRITICAL_RATING: {
				return true;
			}
			case HASTE_RATING: {
				return true;
			}
		
		default:
			break;
		
		}
		return false;
	}
	
	public int getDisplayIconV() {
		switch(this) {
		case HEALTH:{
			return 0;
		}
		case ARMOR: {
			return 16;
		}
		
		case STRENGTH: {
			return 32;
		}
		case AGILITY: {
			return 48;
		}
		
		case SPELLPOWER_FIRE: {
			return 64;
		}
		case SPELLPOWER_FROST: {
			return 80;
		}
		case SPELLPOWER_ARCANE: {
			return 96;
		}
		case SPELLPOWER_HOLY: {
			return 112;
		}
		case SPELLPOWER_SHADOW: {
			return 128;
		}
		
		case WEAPON_DAMAGE: {
			return 144;
		}
		case ARMOR_PENETRATION:  {
			return 160;
		}
		case CRITICAL_RATING: {
			return 176;
		}
		case HASTE_RATING: {
			return 192;
		}
		
		default:
			break;
		
		}
		return 0;
	}
	
	public static final String attributeUnlocalizedPre = "bcattribute.";
	
	public String getUnlocalizedName() {
		return attributeUnlocalizedPre + this.toString().toLowerCase() + ".name";
	}
	
	public String getUnlocalizedDescription() {
		return attributeUnlocalizedPre + this.toString().toLowerCase() + ".description";
	}
}
