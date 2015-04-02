package mods.battleclasses.core.classes;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import mods.battleclasses.ability.active.BattleClassesAbilityActiveChanneled;
import mods.battleclasses.ability.active.BattleClassesAbilityShieldBlock;
import mods.battleclasses.ability.active.BattleClassesAbilityTestCasted;
import mods.battleclasses.ability.active.BattleClassesAbilityTestChanneled;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.passive.BattleClassesPassiveAbilityAmplifier;
import mods.battleclasses.ability.passive.BattleClassesTalentAbilityAmplifier;
import mods.battleclasses.ability.passive.BattleClassesTalentAbilityContainer;
import mods.battleclasses.ability.passive.BattleClassesTalentTest;
import mods.battleclasses.core.BattleClassesPlayerClass;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.BattleClassesTalentTree;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;

public class BattleClassesPlayerClassMage extends BattleClassesPlayerClass {

	public BattleClassesPlayerClassMage(BattleClassesPlayerHooks parPlayerHooks) {
		super(parPlayerHooks, EnumBattleClassesPlayerClass.MAGE);
	}

	public LinkedHashMap<Integer, BattleClassesAbstractAbilityActive> getClassAbilities() {
		LinkedHashMap<Integer, BattleClassesAbstractAbilityActive> abilities = new LinkedHashMap<Integer, BattleClassesAbstractAbilityActive>();
		abilities.put(100, new ArcaneMissilesTestAbility().setName("mage.arcanemissiles"));
		abilities.put(101, new BattleClassesAbilityTestCasted(101).setName("mage.blink"));
		abilities.put(102, new BattleClassesAbilityTestCasted(110).setName("mage.fireball"));
		abilities.put(110, new BattleClassesAbilityTestCasted(111).setName("mage.scorch"));
		abilities.put(111, new BattleClassesAbilityTestCasted(120).setName("mage.frostbolt"));
		abilities.put(112, new BattleClassesAbilityTestCasted(121).setName("mage.frostnova"));
		//abilities.put(BattleClassesAbilityShieldBlock.SHIELD_BLOCK_ABILITY_ID, new BattleClassesAbilityShieldBlock());
		//abilities.put(ABILITY_ID_FROSTNOVA, new FrostNovaTestAbility());
		return abilities;
	}
	
	public ArrayList<BattleClassesTalentTree> getClassTalents() {
		ArrayList<BattleClassesTalentTree> talentTrees = new ArrayList<BattleClassesTalentTree>();
		BattleClassesTalentTree talentTree1 = new BattleClassesTalentTree();
		talentTree1.setName("Arcane");
//		talentTree1.talentList.add(new BattleClassesTalentAbilityContainer(1100, 0, new BattleClassesPassiveAbilityAmplifier(1100, 0)));
		talentTree1.talentList.add(new BattleClassesTalentAbilityAmplifier(1100, 0));
		talentTree1.talentList.add(new BattleClassesTalentTest(1101, 1));
		talentTree1.talentList.add(new BattleClassesTalentAbilityContainer(1102, 2, new PolymorphTestAbility()));
		BattleClassesTalentTree talentTree2 = new BattleClassesTalentTree();
		talentTree2.setName("Fire");
		talentTree2.talentList.add(new BattleClassesTalentTest(1110, 0));
		talentTree2.talentList.add(new BattleClassesTalentTest(1111, 1));
		talentTree2.talentList.add(new BattleClassesTalentAbilityContainer(1112, 2, new DragonsBreathTestAbility()));
		BattleClassesTalentTree talentTree3 = new BattleClassesTalentTree();
		talentTree3.setName("Frost");
		talentTree3.talentList.add(new BattleClassesTalentTest(1120, 0));
		talentTree3.talentList.add(new BattleClassesTalentTest(1121, 1));
		talentTree3.talentList.add(new BattleClassesTalentAbilityContainer(1122, 2, new IceBlockTestAbility()));
		
		//TODO : Add talents to the talent trees
		
		
		talentTrees.add(talentTree1);
		talentTrees.add(talentTree2);
		talentTrees.add(talentTree3);
		
		
		
		return talentTrees;
	}
	
	public ArrayList<EnumBattleClassesAttributeType> getPrimaryAttributesToDisplay() {
		ArrayList<EnumBattleClassesAttributeType> attributes = new ArrayList<EnumBattleClassesAttributeType>();
		attributes.add(EnumBattleClassesAttributeType.SPELLPOWER_ARCANE);
		attributes.add(EnumBattleClassesAttributeType.SPELLPOWER_FIRE);
		attributes.add(EnumBattleClassesAttributeType.SPELLPOWER_FROST);
		return attributes;
	}
	
	public ArrayList<EnumBattleClassesAttributeType> getSecondaryAttributesToDisplay() {
		ArrayList<EnumBattleClassesAttributeType> attributes = new ArrayList<EnumBattleClassesAttributeType>();
		attributes.add(EnumBattleClassesAttributeType.WEAPON_DAMAGE);
		attributes.add(EnumBattleClassesAttributeType.CRITICAL_RATING);
		attributes.add(EnumBattleClassesAttributeType.HASTE_RATING);
		return attributes;
	}

	
	//--------------------------------------------------------------------------------------------------------
	//										CLASS SPECIFIC ABILITIES
	//--------------------------------------------------------------------------------------------------------
	public static final int ABILITY_ID_ARCANE_MISSILES = 100;
	class ArcaneMissilesTestAbility extends BattleClassesAbilityTestChanneled {
		public ArcaneMissilesTestAbility() {
			super(ABILITY_ID_ARCANE_MISSILES);
		}
	}
	
	public static final int ABILITY_ID_FROSTNOVA = 121;
	class FrostNovaTestAbility extends BattleClassesAbilityTestCasted {
		public FrostNovaTestAbility() {
			super(ABILITY_ID_FROSTNOVA);
		}
	}
	
	public static final int ABILITY_ID_POLYMORPH = 102;
	class PolymorphTestAbility extends BattleClassesAbilityTestCasted {
		public PolymorphTestAbility() {
			super(ABILITY_ID_POLYMORPH);
			this.setName("mage.polymorph");
		}
	}
	
	public static final int ABILITY_ID_DRAGONSBREATH = 112;
	class DragonsBreathTestAbility extends BattleClassesAbilityTestCasted {
		public DragonsBreathTestAbility() {
			super(ABILITY_ID_DRAGONSBREATH);
			this.setName("mage.dragonsbreath");
		}
	}
	
	public static final int ABILITY_ID_ICEBLOCK = 122;
	class IceBlockTestAbility extends BattleClassesAbilityTestCasted {
		public IceBlockTestAbility() {
			super(ABILITY_ID_ICEBLOCK);
			this.setName("mage.iceblock");
		}
	}
	
	
}
