package mods.battleclasses.enums;

import java.util.EnumSet;

import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import net.minecraft.util.EnumChatFormatting;
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
	MELEE_ATTACK_DAMAGE {
		public int getDisplayIconV()  {
			return 144;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.melee_attack_damage;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.melee_attack_damage = value;
		}
	},
	RANGED_ATTACK_DAMAGE {
		public int getDisplayIconV()  {
			return 208;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return attributes.ranged_attack_damage;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			attributes.ranged_attack_damage = value;
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
	
	//Vanilla attributes
	VANILLA_ARMOR {
		public int getDisplayIconV()  {
			return 16;
		}

		@Override
		public float getValueFromAttributes(BattleClassesAttributes attributes) {
			return 0;
		}

		@Override
		public void setValueForAttributes(BattleClassesAttributes attributes, float value) {
			//stub
		}
	};
	
	public static final EnumSet<EnumBattleClassesAttributeType> ALL_PRIMARY_TYPES = EnumSet.of(/*HEALTH,*/ STRENGTH, AGILITY, SPELLPOWER_ARCANE, SPELLPOWER_FIRE, SPELLPOWER_FROST, SPELLPOWER_HOLY, SPELLPOWER_SHADOW);
	public static final EnumSet<EnumBattleClassesAttributeType> ALL_SECONDARY_TYPES = EnumSet.of(HASTE_RATING, CRITICAL_RATING, ARMOR_PENETRATION);
	public static final EnumSet<EnumBattleClassesAttributeType> ALL_SPELLPOWER_TYPES = EnumSet.of(SPELLPOWER_ARCANE, SPELLPOWER_FIRE, SPELLPOWER_FROST, SPELLPOWER_HOLY, SPELLPOWER_SHADOW);
	
	public static final EnumSet<EnumBattleClassesAttributeType> getPrimaryTypesOfSet(EnumSet<EnumBattleClassesAttributeType> types) {
		System.out.println("getPrimaryTypesOfSet tpyes:" + types);
		EnumSet<EnumBattleClassesAttributeType> primarySet = types.clone();
		primarySet.retainAll(ALL_PRIMARY_TYPES);
		System.out.println("getPrimaryTypesOfSet primarySet:" + primarySet);
		return primarySet;
	}
	
	public static final EnumSet<EnumBattleClassesAttributeType> getSecondaryTypesOfSet(EnumSet<EnumBattleClassesAttributeType> types) {
		System.out.println("getSecondaryTypesOfSet tpyes:" + types);
		EnumSet<EnumBattleClassesAttributeType> secondarySet = types.clone();
		secondarySet.retainAll(ALL_SECONDARY_TYPES);
		System.out.println("getSecondaryTypesOfSet secondarySet:" + secondarySet);
		return secondarySet;
	}
	
	public abstract float getValueFromAttributes(BattleClassesAttributes attributes);
	public abstract void setValueForAttributes(BattleClassesAttributes attributes, float value);
	
	public boolean isPrimary() {
		return true;
	}
	
	public EnumChatFormatting getBonusLineColor() {
		if(this.isPrimary()){
			return EnumChatFormatting.BLUE;
		}
		else {
			return EnumChatFormatting.DARK_AQUA;
		}
	}
	
	public int getDisplayIconSquareSize() {
		return 9;
	}
			
	public boolean isDisplayedInPercentage() {
		return !isPrimary();
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
