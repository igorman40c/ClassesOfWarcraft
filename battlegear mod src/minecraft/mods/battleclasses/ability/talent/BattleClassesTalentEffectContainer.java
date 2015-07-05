package mods.battleclasses.ability.talent;

import java.util.ArrayList;
import java.util.List;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.effect.BattleClassesAbstractAbilityEffect;
import mods.battleclasses.ability.passive.BattleClassesAbstractAbilityPassive;

public class BattleClassesTalentEffectContainer extends BattleClassesAbstractTalent  {

	protected BattleClassesTalentEffectContainer(int parTalentLevel, String targetAbilityID) {
		super(parTalentLevel);
		this.targetAbilityID = targetAbilityID;
	}

	public BattleClassesTalentEffectContainer(int parAbilityID, int parTalentLevel, String targetAbilityID, List<BattleClassesAbstractAbilityEffect> containedEffects) {
		this(parTalentLevel, targetAbilityID);
		this.setContainedEffects(containedEffects);
	}
	
	public BattleClassesTalentEffectContainer(int parAbilityID, int parTalentLevel, String targetAbilityID, BattleClassesAbstractAbilityEffect effect) {
		this( parTalentLevel, targetAbilityID);
		this.setSingleContainedEffect(effect);
	}

	protected List<BattleClassesAbstractAbilityEffect> containedEffects;
	protected String targetAbilityID;
	
	public List<BattleClassesAbstractAbilityEffect> getContainedEffects() {
		return this.containedEffects;
	}
	
	public void setContainedEffects(List<BattleClassesAbstractAbilityEffect> containedEffects) {
		this.containedEffects = containedEffects;
	}
	
	public void setSingleContainedEffect(BattleClassesAbstractAbilityEffect effect) {
		this.containedEffects = new ArrayList<BattleClassesAbstractAbilityEffect>();
		this.containedEffects.add(effect);
	}
	
	public void onStateChanged() {
		super.onStateChanged();
		BattleClassesAbstractAbilityActive targetAbility = BattleClassesUtils.getPlayerSpellBook(this.getOwnerPlayer()).getActiveAbilityByID(targetAbilityID);
		if(targetAbility != null && this.containedEffects != null) {
			for(BattleClassesAbstractAbilityEffect effect : this.containedEffects) {
				if(this.getCurrentState() == 0) {
					targetAbility.addEffect(effect);
				}
				else {
					targetAbility.removeEffect(effect);
				}
			}
		}
	}
	
}
