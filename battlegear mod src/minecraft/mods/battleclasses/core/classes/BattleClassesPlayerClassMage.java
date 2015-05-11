package mods.battleclasses.core.classes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbilityActiveDirect;
import mods.battleclasses.ability.active.BattleClassesAbilityShieldBlock;
import mods.battleclasses.ability.active.BattleClassesAbilityTestCasted;
import mods.battleclasses.ability.active.BattleClassesAbilityTestChanneled;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.effect.BattleClassesAbstractAbilityEffectInstantValue;
import mods.battleclasses.ability.effect.EffectFactory;
import mods.battleclasses.ability.passive.BattleClassesPassiveAbilityAttributeModifier;
import mods.battleclasses.ability.talent.BattleClassesTalentAbilityActiveContainer;
import mods.battleclasses.ability.talent.BattleClassesTalentEffectContainer;
import mods.battleclasses.ability.talent.BattleClassesTalentTest;
import mods.battleclasses.core.BattleClassesPlayerClass;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.BattleClassesTalentTree;
import mods.battleclasses.enums.EnumBattleClassesAbilityCastingType;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;

public class BattleClassesPlayerClassMage extends BattleClassesPlayerClass {
	
	/*
	static BattleClassesPlayerClassMage INSTANCE = new BattleClassesPlayerClassMage();
	static{
		INSTANCE.registerAllAbilities();
	}
	BattleClassesPlayerClassMage() {
		super(null, EnumBattleClassesPlayerClass.MAGE);
	}
	*/
	
	public BattleClassesPlayerClassMage(BattleClassesPlayerHooks parPlayerHooks) {
		super(parPlayerHooks, EnumBattleClassesPlayerClass.MAGE);
	}

	public LinkedHashMap<Integer, BattleClassesAbstractAbilityActive> getClassAbilities() {
		LinkedHashMap<Integer, BattleClassesAbstractAbilityActive> abilities = new LinkedHashMap<Integer, BattleClassesAbstractAbilityActive>();
		abilities.put(100, new ArcaneMissilesTestAbility().setUnlocalizedName("mage.arcanemissiles"));
        abilities.put(101, new BattleClassesAbilityTestCasted(101).setUnlocalizedName("mage.blink"));
		abilities.put(110, new BattleClassesAbilityTestCasted(110).setUnlocalizedName("mage.fireball"));
		abilities.put(ABILITY_ID_SCORCH, new ScorchTestAbility());
		abilities.put(ABILITY_ID_FROSTBOLT, new FrostBolt());
		abilities.put(122, new BattleClassesAbilityTestCasted(121).setUnlocalizedName("mage.frostnova"));
		abilities.put(BattleClassesAbilityShieldBlock.SHIELD_BLOCK_ABILITY_ID, new BattleClassesAbilityShieldBlock());
		//abilities.put(ABILITY_ID_FROSTNOVA, new FrostNovaTestAbility());
		return abilities;
	}
	
	public ArrayList<BattleClassesTalentTree> getClassTalents() {
		ArrayList<BattleClassesTalentTree> talentTrees = new ArrayList<BattleClassesTalentTree>();
		BattleClassesTalentTree talentTree1 = new BattleClassesTalentTree();
		talentTree1.setName("Arcane");
//		talentTree1.talentList.add(new BattleClassesTalentAbilityContainer(1100, 0, new BattleClassesPassiveAbilityAmplifier(1100, 0)));
		talentTree1.talentList.add(new BattleClassesTalentTest(1100, 0));
		talentTree1.talentList.add(new BattleClassesTalentTest(1101, 1));
		talentTree1.talentList.add(new BattleClassesTalentAbilityActiveContainer(1102, 2, new PolymorphTestAbility()));
		BattleClassesTalentTree talentTree2 = new BattleClassesTalentTree();
		talentTree2.setName("Fire");
		talentTree2.talentList.add(new BattleClassesTalentTest(1110, 0));
		talentTree2.talentList.add(new BattleClassesTalentTest(1111, 1));
		talentTree2.talentList.add(new BattleClassesTalentAbilityActiveContainer(1112, 2, new DragonsBreathTestAbility()));
		BattleClassesTalentTree talentTree3 = new BattleClassesTalentTree();
		talentTree3.setName("Frost");
		talentTree3.talentList.add(new BattleClassesTalentTest(1120, 0));
		talentTree3.talentList.add(new BattleClassesTalentTest(1121, 1));
		talentTree3.talentList.add(new BattleClassesTalentAbilityActiveContainer(1122, 2, new IceBlockTestAbility()));
		
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

	public void registerAllAbilities() {
		ArrayList<BattleClassesAbstractAbility> allAbilitiesToRegister = new ArrayList<BattleClassesAbstractAbility>();
		allAbilitiesToRegister.add(new ArcaneMissilesTestAbility());
		allAbilitiesToRegister.add(new PolymorphTestAbility());
		//TODO
		//...

		super.registerAbilties(allAbilitiesToRegister);
	}
	
	//--------------------------------------------------------------------------------------------------------
	//										ACTIVE CLASS ABILITIES
	//--------------------------------------------------------------------------------------------------------
	public static final int ABILITY_ID_ARCANE_MISSILES = 100;
	class ArcaneMissilesTestAbility extends BattleClassesAbilityTestChanneled {		
		public ArcaneMissilesTestAbility() {
			super(ABILITY_ID_ARCANE_MISSILES);
		}
	}
	
	public static final int ABILITY_ID_POLYMORPH = 102;
	class PolymorphTestAbility extends BattleClassesAbilityTestCasted {
		public PolymorphTestAbility() {
			super(ABILITY_ID_POLYMORPH);
			this.setUnlocalizedName("mage.polymorph");
		}
	}
	
	public static final int ABILITY_ID_SCORCH = 111;
	class ScorchTestAbility extends BattleClassesAbilityActiveDirect {
		public ScorchTestAbility() {
			super(ABILITY_ID_SCORCH);
			this.setUnlocalizedName("mage.scorch");
			this.setCastingType(EnumBattleClassesAbilityCastingType.CastType_CASTED);
			this.castTime = 0.5F;
			this.school = EnumBattleClassesAbilitySchool.SPELL_FIRE;
			//this.cooldownClock.setDefaultDuration(5F);
			this.range = 10F;
			BattleClassesAbstractAbilityEffectInstantValue effect = EffectFactory.createInstantAbilityEffect(10, 0, EnumBattleClassesAbilitySchool.SPELL_FIRE); 
			this.addEffect(effect);
		}
	}
	
	public static final int ABILITY_ID_DRAGONSBREATH = 112;
	class DragonsBreathTestAbility extends BattleClassesAbilityTestCasted {
		public DragonsBreathTestAbility() {
			super(ABILITY_ID_DRAGONSBREATH);
			this.setUnlocalizedName("mage.dragonsbreath");
		}
	}
	
	public static final int ABILITY_ID_FROSTBOLT= 120;
	class FrostBolt extends BattleClassesAbilityTestCasted {
		public FrostBolt() {
			super(ABILITY_ID_FROSTBOLT);
			this.setUnlocalizedName("mage.frostbolt");
			this.castTime = 1F;
			this.school = EnumBattleClassesAbilitySchool.SPELL_FROST;
			this.range = 20F;
			BattleClassesAbstractAbilityEffectInstantValue effect = EffectFactory.createInstantAbilityEffect(15, 0, EnumBattleClassesAbilitySchool.SPELL_FROST);
			this.addEffect(effect);
		}
	}
	
	public static final int ABILITY_ID_FROSTNOVA = 121;
	class FrostNovaTestAbility extends BattleClassesAbilityTestCasted {
		public FrostNovaTestAbility() {
			super(ABILITY_ID_FROSTNOVA);
		}
	}
	
	public static final int ABILITY_ID_ICEBLOCK = 122;
	class IceBlockTestAbility extends BattleClassesAbilityTestCasted {
		public IceBlockTestAbility() {
			super(ABILITY_ID_ICEBLOCK);
			this.setUnlocalizedName("mage.iceblock");
		}
	}
	
	
	
	//--------------------------------------------------------------------------------------------------------
	//										PASSIVE CLASS ABILITIES
	//--------------------------------------------------------------------------------------------------------
	public static final int TALENT_FROST_1 = 1120;
	class FrostTalentWintersChill extends BattleClassesTalentEffectContainer {

		protected FrostTalentWintersChill() {
			super(TALENT_FROST_1, 0, ABILITY_ID_FROSTBOLT);
			// TODO Auto-generated constructor stub
		}
		
	}
	
}
