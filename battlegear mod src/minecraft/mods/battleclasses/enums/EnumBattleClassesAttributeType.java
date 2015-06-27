package mods.battleclasses.enums;

import net.minecraft.util.StatCollector;

public enum EnumBattleClassesAttributeType {
	//Primary
	HEALTH {
		public int getDisplayIconV()  {
			return 0;
		}
	},
	STRENGTH {
		public int getDisplayIconV()  {
			return 32;
		}
	},
	AGILITY {
		public int getDisplayIconV()  {
			return 48;
		}
	},
	SPELLPOWER_ARCANE {
		public int getDisplayIconV()  {
			return 96;
		}
	},
	SPELLPOWER_FIRE {
		public int getDisplayIconV()  {
			return 64;
		}
	},
	SPELLPOWER_FROST {
		public int getDisplayIconV()  {
			return 80;
		}
	},
	SPELLPOWER_HOLY {
		public int getDisplayIconV()  {
			return 112;
		}
	},
	SPELLPOWER_SHADOW {
		public int getDisplayIconV()  {
			return 128;
		}
	},
	//Secondary
	HASTE_RATING {
		public int getDisplayIconV()  {
			return 192;
		}
	},
	CRITICAL_RATING {
		public int getDisplayIconV()  {
			return 176;
		}
	},
	ARMOR_PENETRATION {
		public int getDisplayIconV()  {
			return 160;
		}
	},
	WEAPON_DAMAGE {
		public int getDisplayIconV()  {
			return 144;
		}
	},
	
	//Vanilla attributes
	ARMOR {
		public int getDisplayIconV()  {
			return 16;
		}
	};
	
	public float getRelativeCreateCost() {
		switch(this) {
			default: {
				return 1F;
			}
		}
	}
	
	public float getConstantCreateBonus() {
		switch(this) {
			case HEALTH:  {
				return -1F;
			}
			default: {
				return 0F;
			}
		}
	}
		
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
	
	/**
	 * Determines the attribute type icon texture draw V parameter
	 * @return
	 */
	public abstract int getDisplayIconV();
		
	public static final String attributeUnlocalizedPre = "bcattribute.";
	
	public String getUnlocalizedName() {
		return attributeUnlocalizedPre + this.toString().toLowerCase() + ".name";
	}
	
	public String getUnlocalizedDescription() {
		return attributeUnlocalizedPre + this.toString().toLowerCase() + ".description";
	}
	
	public String getTranslatedName() {
		return StatCollector.translateToLocal(this.getUnlocalizedDescription());
	}
}
