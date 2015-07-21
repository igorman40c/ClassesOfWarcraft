package mods.battleclasses.attributes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.util.StatCollector;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesHandHeldType;
import mods.battleclasses.gui.BattleClassesGuiHelper;

public class BattleClassesAttributes {

	//Primary attributes (in points)
	public float health = 0;
	public float strength = 0;
	public float agility = 0;
	public float spellpower_arcane = 0;
	public float spellpower_fire = 0;
	public float spellpower_frost = 0;
	public float spellpower_holy = 0;
	public float spellpower_shadow = 0;
	
	//Secondary attributes (in percentage)
	public float haste = 0;
	public float crit = 0;
	public float armor_pen = 0;
	public float melee_attack_damage = 0;
	public float ranged_attack_damage = 0;
		
	public BattleClassesAttributes() {
		
	}
	
	public BattleClassesAttributes(float value) {
		//Primary attributes (in points)
		this.health = value;
		this.strength = value;
		this.agility = value;
		this.spellpower_arcane = value;
		this.spellpower_fire = value;
		this.spellpower_frost = value;
		this.spellpower_holy = value;
		this.spellpower_shadow = value;
		//Secondary attributes (in percentage)
		this.haste = value;
		this.crit = value;
		this.armor_pen = value;
		this.melee_attack_damage = value;
		this.ranged_attack_damage = value;
	}
	
	public BattleClassesAttributes add(BattleClassesAttributes attributes) {
		//Primary attributes (in points)
		this.health += attributes.health;
		this.strength += attributes.strength;
		this.agility += attributes.agility;
		this.spellpower_arcane += attributes.spellpower_arcane;
		this.spellpower_fire += attributes.spellpower_fire;
		this.spellpower_frost += attributes.spellpower_frost;
		this.spellpower_holy += attributes.spellpower_holy;
		this.spellpower_shadow += attributes.spellpower_shadow;
		//Secondary attributes (in percentage)
		this.haste += attributes.haste;
		this.crit += attributes.crit;
		this.armor_pen += attributes.armor_pen;
		this.melee_attack_damage += attributes.melee_attack_damage;
		this.ranged_attack_damage += attributes.ranged_attack_damage;
		return this;
	}
	
	public BattleClassesAttributes multiply(BattleClassesAttributes attributes) {
		//Primary attributes (in points)
		this.health *= attributes.health;
		this.strength *= attributes.strength;
		this.agility *= attributes.agility;
		this.spellpower_arcane *= attributes.spellpower_arcane;
		this.spellpower_fire *= attributes.spellpower_fire;
		this.spellpower_frost *= attributes.spellpower_frost;
		this.spellpower_holy *= attributes.spellpower_holy;
		this.spellpower_shadow *= attributes.spellpower_shadow;
		//Secondary attributes (in percentage)
		this.haste *= attributes.haste;
		this.crit *= attributes.crit;
		this.armor_pen *= attributes.armor_pen;
		this.melee_attack_damage *= attributes.melee_attack_damage;
		this.ranged_attack_damage *= attributes.ranged_attack_damage;
		return this;
	}
	
	public List<EnumBattleClassesAttributeType> getActiveTypes() {
		ArrayList<EnumBattleClassesAttributeType> activeTypes = new ArrayList<EnumBattleClassesAttributeType>();
		for(EnumBattleClassesAttributeType type : EnumBattleClassesAttributeType.values()) {
			if(this.getValueByType(type) != 0) {
				activeTypes.add(type);
			}
		}
		return activeTypes;
	}
	
	public List<EnumBattleClassesAttributeType> getActiveMultiplierTypes() {
		ArrayList<EnumBattleClassesAttributeType> activeTypes = new ArrayList<EnumBattleClassesAttributeType>();
		for(EnumBattleClassesAttributeType type : EnumBattleClassesAttributeType.values()) {
			if(this.getValueByType(type) != 1) {
				activeTypes.add(type);
			}
		}
		return activeTypes;
	}
	
	public float getValueByType(EnumBattleClassesAttributeType attributeType) {
		return attributeType.getValueFromAttributes(this);
	}
	
	
	public void setValueByType(EnumBattleClassesAttributeType attributeType, float value) {
		attributeType.setValueForAttributes(this, value);
	}
	
	public void addValueByType(EnumBattleClassesAttributeType attributeType, float value) {
		attributeType.setValueForAttributes(this, getValueByType(attributeType) + value);
	}
	
	//Helper
	public String getTranslatedBonusStringByType(EnumBattleClassesAttributeType attributeType) {
		float value = this.getValueByType(attributeType);
		String valueString = "+" + BattleClassesGuiHelper.formatFloatToNice(value);
		return valueString + " " + StatCollector.translateToLocal(attributeType.getUnlocalizedName());
	}

	public BattleClassesAttributes copy() {
		BattleClassesAttributes attributesCopy = new BattleClassesAttributes();
		attributesCopy.add(this);
		return attributesCopy;
	}
}
