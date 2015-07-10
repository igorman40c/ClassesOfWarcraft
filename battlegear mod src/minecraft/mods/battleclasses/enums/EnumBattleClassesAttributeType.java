package mods.battleclasses.enums;

import mods.battleclasses.attributes.BattleClassesAttributes;
import net.minecraft.util.StatCollector;

public enum EnumBattleClassesAttributeType {
	//Primary
	HEALTH {
		public int getDisplayIconV()  {
			return 0;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.health;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.health = value;
		}
	},
	STRENGTH {
		public int getDisplayIconV()  {
			return 32;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.strength;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.strength = value;
		}
	},
	AGILITY {
		public int getDisplayIconV()  {
			return 48;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.agility;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.agility = value;
		}
	},
	SPELLPOWER_ARCANE {
		public int getDisplayIconV()  {
			return 96;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.spellpower_arcane;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.spellpower_arcane = value;
		}
	},
	SPELLPOWER_FIRE {
		public int getDisplayIconV()  {
			return 64;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.spellpower_fire;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.spellpower_fire = value;
		}
	},
	SPELLPOWER_FROST {
		public int getDisplayIconV()  {
			return 80;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.spellpower_frost;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.spellpower_frost = value;
		}
	},
	SPELLPOWER_HOLY {
		public int getDisplayIconV()  {
			return 112;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.spellpower_holy;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.spellpower_holy = value;
		}
	},
	SPELLPOWER_SHADOW {
		public int getDisplayIconV()  {
			return 128;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.spellpower_shadow;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.spellpower_shadow = value;
		}
	},
	//Secondary
	HASTE_RATING {
		public int getDisplayIconV()  {
			return 192;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.haste;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.haste = value;
		}
		
		@Override
		public boolean isDisplayedInPercentage() {
			return true;
		}
		
		@Override
		public boolean isPrimary() {
			return false;
		}
	},
	CRITICAL_RATING {
		public int getDisplayIconV()  {
			return 176;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.crit;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.crit = value;
		}
		
		@Override
		public boolean isDisplayedInPercentage() {
			return true;
		}
		
		@Override
		public boolean isPrimary() {
			return false;
		}
	},
	ARMOR_PENETRATION {
		public int getDisplayIconV()  {
			return 160;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.armor_pen;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.armor_pen = value;
		}
		
		@Override
		public boolean isDisplayedInPercentage() {
			return true;
		}
		
		@Override
		public boolean isPrimary() {
			return false;
		}
	},
	WEAPON_DAMAGE {
		public int getDisplayIconV()  {
			return 144;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.weapon_damage;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.weapon_damage = value;
		}
	},
	
	//Vanilla attributes
	ARMOR {
		public int getDisplayIconV()  {
			return 16;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.armor_pen;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.armor_pen = value;
		}
	};
	
	public abstract float getValueFromAttributes(BattleClassesAttributes attributes);
	public abstract void setValueForAttributes(BattleClassesAttributes attributes, float value);
	
	public boolean isPrimary() {
		return true;
	}
		
	public int getDisplayIconSquareSize() {
		return 9;
	}
			
	public boolean isDisplayedInPercentage() {
		return false;
	}
	
	/**
	 * Determines the attribute type icon texture draw V parameter
	 * @return
	 */
	public abstract int getDisplayIconV();
	
	/**
	 * Determines the attribute type icon texture draw U parameter
	 * @return
	 */
	public int getDisplayIconU() {
		return 208; 
	}
		
	protected static final String unlocalizedPrefix = "bcattribute.";
	
	public String getUnlocalizedName() {
		return unlocalizedPrefix + this.toString().toLowerCase() + ".name";
	}
	
	public String getUnlocalizedDescription() {
		return unlocalizedPrefix + this.toString().toLowerCase() + ".description";
	}
	
	public String getTranslatedName() {
		return StatCollector.translateToLocal(this.getUnlocalizedName());
	}
	
	public String getTranslatedDescription() {
		return StatCollector.translateToLocal(this.getUnlocalizedDescription());
	}
}
