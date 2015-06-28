package mods.battleclasses.potion;

import java.util.ArrayList;
import java.util.List;

import mods.battleclasses.ability.effect.modifier.ICWEffectModifier;
import mods.battleclasses.ability.effect.modifier.ICWEffectModifierOwner;

public class BattleClassesPotionEffectModifier extends BattleClassesPotionAbilityEffect implements ICWEffectModifierOwner {

	BattleClassesPotionEffectModifier(int id, boolean badEffect, int liquidColorCode) {
		super(id, badEffect, liquidColorCode);
		// TODO Auto-generated constructor stub
	}
	
	List<ICWEffectModifier> effectModifiers;

	@Override
	public List<ICWEffectModifier> getEffectModifiers() {
		return effectModifiers;
	}

	@Override
	public void setEffectModifiers(List<ICWEffectModifier> effectModifiers) {
		this.effectModifiers = effectModifiers;
	}
	
	public void setSingleEffectModifier(ICWEffectModifier effectModifier) {
		this.effectModifiers = new ArrayList<ICWEffectModifier>();
		this.effectModifiers.add(effectModifier);
	}


}
