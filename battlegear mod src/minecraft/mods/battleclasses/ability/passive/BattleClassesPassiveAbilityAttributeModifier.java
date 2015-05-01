package mods.battleclasses.ability.passive;

import java.util.ArrayList;
import java.util.List;

import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.attributes.ICWAttributeModifier;
import mods.battleclasses.attributes.ICWAttributeModifierOwner;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;

public class BattleClassesPassiveAbilityAttributeModifier extends BattleClassesAbstractAbilityPassive implements ICWAttributeModifierOwner {
	
	/*
	public BattleClassesPassiveAbilityAttributeModifier(int parAbilityID) {
		super(parAbilityID);
	}
	*/
	
	public BattleClassesPassiveAbilityAttributeModifier(int parAbilityID, List<ICWAttributeModifier> attributeModifiers) {
		super(parAbilityID);
		this.setAttributeModifiers(attributeModifiers);
	}
	
	public BattleClassesPassiveAbilityAttributeModifier(int parAbilityID, ICWAttributeModifier attributeModifier) {
		super(parAbilityID);
		this.setSingleAttributeModifier(attributeModifier);
	}
	
	List<ICWAttributeModifier> attributeModifiers;

	@Override
	public List<ICWAttributeModifier> getAttributeModifiers() {
		return attributeModifiers;
	}

	@Override
	public void setAttributeModifiers(List<ICWAttributeModifier> attributeModifiers) {
		this.attributeModifiers = attributeModifiers; 
	}
	
	public void setSingleAttributeModifier(ICWAttributeModifier attributeModifier) {
		this.attributeModifiers = new ArrayList<ICWAttributeModifier>();
		attributeModifiers.add(attributeModifier);
	}
	
}
