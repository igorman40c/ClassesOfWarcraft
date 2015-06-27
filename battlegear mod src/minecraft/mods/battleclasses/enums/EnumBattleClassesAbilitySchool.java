package mods.battleclasses.enums;

import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public enum EnumBattleClassesAbilitySchool {
	PHYSICAL_MELEE_RAGE {
		public float getCriticalStrikeChanceBase() {
			return 0.10F;
		}
		
		public float getCriticalStrikeBonus() {
			return 1.5F;
		}
				
		@SideOnly(Side.CLIENT)
		public int getCastBarColoringV() {
			return 64;
		}
	},
	PHYSICAL_MELEE_ENERGY {
		public float getCriticalStrikeChanceBase() {
			return 0.10F;
		}
		
		public float getCriticalStrikeBonus() {
			return 1.5F;
		}
				
		@SideOnly(Side.CLIENT)
		public int getCastBarColoringV() {
			return 80;
		}
	},
	PHYSICAL_RANGED {
		public float getCriticalStrikeChanceBase() {
			return 0.10F;
		}
		
		public float getCriticalStrikeBonus() {
			return 2F;
		}
		
		public boolean hasCastingSound() {
			return true;
		}
		
		@SideOnly(Side.CLIENT)
		public int getCastBarColoringV() {
			return 96;
		}
	},
	
	SPELL_ARCANE {
		public float getCriticalStrikeChanceBase() {
			return 0.10F;
		}
		
		public float getCriticalStrikeBonus() {
			return 1.5F;
		}
		
		public boolean isMagical() {
			return true;
		}
		
		public boolean hasCastingSound() {
			return true;
		}
		
		@SideOnly(Side.CLIENT)
		public int getCastBarColoringV() {
			return 144;
		}
	},
	SPELL_FIRE {
		public float getCriticalStrikeChanceBase() {
			return 0.10F;
		}
		
		public float getCriticalStrikeBonus() {
			return 2F;
		}
		
		public boolean isMagical() {
			return true;
		}
		
		public boolean hasCastingSound() {
			return true;
		}
		
		@SideOnly(Side.CLIENT)
		public int getCastBarColoringV() {
			return 112;
		}
	},
	SPELL_FROST {
		public float getCriticalStrikeChanceBase() {
			return 0.10F;
		}
		
		public float getCriticalStrikeBonus() {
			return 25F;
		}
		
		public boolean isMagical() {
			return true;
		}
		
		public boolean hasCastingSound() {
			return true;
		}
		
		@SideOnly(Side.CLIENT)
		public int getCastBarColoringV() {
			return 128;
		}
	},
	SPELL_HOLY {
		public float getCriticalStrikeChanceBase() {
			return 0.10F;
		}
		
		public float getCriticalStrikeBonus() {
			return 1.5F;
		}
		
		public boolean isMagical() {
			return true;
		}
		
		public boolean hasCastingSound() {
			return true;
		}
		
		@SideOnly(Side.CLIENT)
		public int getCastBarColoringV() {
			return 160;
		}
	},
	SPELL_SHADOW {
		public float getCriticalStrikeChanceBase() {
			return 0.10F;
		}
		
		public float getCriticalStrikeBonus() {
			return 1.5F;
		}
		
		public boolean isMagical() {
			return true;
		}
		
		public boolean hasCastingSound() {
			return true;
		}
		
		@SideOnly(Side.CLIENT)
		public int getCastBarColoringV() {
			return 176;
		}
	},
	
	UNKNOWN;
	
	public float getCriticalStrikeChanceBase() {
		return 0.10F;
	}
	
	public float getCriticalStrikeBonus() {
		return 1.5F;
	}
	
	public boolean isMagical() {
    	return false;
    }
	
	public boolean hasCastingSound() {
		return false;
	}
	
    @SideOnly(Side.CLIENT)
	public int getCastBarColoringV() {
    	return 48;
    }
        
    public String getTranslatedDisplayedName() {
    	String unlocaizedName = new String("abilityschool." + this.toString() + ".name");
    	String displayedName = StatCollector.translateToLocal(unlocaizedName);
    	return displayedName;
    }

}



