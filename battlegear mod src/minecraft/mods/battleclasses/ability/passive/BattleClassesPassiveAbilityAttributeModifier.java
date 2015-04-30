package mods.battleclasses.ability.passive;

import java.util.List;

import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.attributes.ICWAttributeModifier;
import mods.battleclasses.attributes.ICWAttributeModifierOwner;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;

public class BattleClassesPassiveAbilityAttributeModifier extends BattleClassesAbstractAbilityPassive implements ICWAttributeModifierOwner {
	
	public BattleClassesPassiveAbilityAttributeModifier(int parAbilityID) {
		super(parAbilityID);
	}
	
	public BattleClassesPassiveAbilityAttributeModifier(int parAbilityID, List<ICWAttributeModifier> attributeModifiers) {
		super(parAbilityID);
		this.setAttributeModifiers(attributeModifiers);
	}
	
	List<ICWAttributeModifier> attributeModifiers;


	@Override
	public List<ICWAttributeModifier> getAttributeModifiers() {
		return this.attributeModifiers;
	}


	@Override
	public void setAttributeModifiers(List<ICWAttributeModifier> attributeModifiers) {
		this.attributeModifiers = attributeModifiers;
	}
	
}
