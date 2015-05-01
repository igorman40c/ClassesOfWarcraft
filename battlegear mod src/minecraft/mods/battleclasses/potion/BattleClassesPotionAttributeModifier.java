package mods.battleclasses.potion;

import java.util.ArrayList;
import java.util.List;

import mods.battleclasses.attributes.ICWAttributeModifier;
import mods.battleclasses.attributes.ICWAttributeModifierOwner;

public class BattleClassesPotionAttributeModifier extends BattleClassesPotionAbilityEffect implements ICWAttributeModifierOwner {

	BattleClassesPotionAttributeModifier(int id, boolean badEffect, int liquidColorCode) {
		super(id, badEffect, liquidColorCode);
		// TODO Auto-generated constructor stub
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
