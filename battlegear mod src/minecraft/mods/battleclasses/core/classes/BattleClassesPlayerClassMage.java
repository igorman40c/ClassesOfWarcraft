package mods.battleclasses.core.classes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import net.minecraft.item.Item;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbilityActiveDirect;
import mods.battleclasses.ability.active.BattleClassesAbilityShieldBlock;
import mods.battleclasses.ability.active.BattleClassesAbilityTestCasted;
import mods.battleclasses.ability.active.BattleClassesAbilityTestChanneled;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.active.EnumBattleClassesAbilityCastingType;
import mods.battleclasses.ability.effect.BattleClassesAbstractAbilityEffectInstantValue;
import mods.battleclasses.ability.effect.EffectFactory;
import mods.battleclasses.ability.passive.BattleClassesPassiveAbilityAttributeModifier;
import mods.battleclasses.ability.talent.BattleClassesTalentAbilityActiveContainer;
import mods.battleclasses.ability.talent.BattleClassesTalentEffectContainer;
import mods.battleclasses.ability.talent.BattleClassesTalentTest;
import mods.battleclasses.core.BattleClassesPlayerClass;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.BattleClassesTalentTree;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.enums.EnumBattleClassesPlayerEnviroment;
import mods.battleclasses.enums.EnumBattleClassesPlayerRole;

public class BattleClassesPlayerClassMage extends BattleClassesPlayerClass {
		
	public BattleClassesPlayerClassMage(BattleClassesPlayerHooks parPlayerHooks) {
		super(parPlayerHooks, EnumBattleClassesPlayerClass.MAGE);
	}
	
	static {
		BattleClassesAbstractAbility.registerAbility(new BattleClassesAbilityTestCasted("mage.blink"));
		BattleClassesAbstractAbility.registerAbility(new BattleClassesAbilityTestCasted("mage.fireball"));
		BattleClassesAbstractAbility.registerAbility(new BattleClassesAbilityTestCasted("mage.frostnova"));
	}

	@Override
	public List<BattleClassesAbstractAbilityActive> getClassAbilities() {
		List<BattleClassesAbstractAbilityActive> abilities = new ArrayList<BattleClassesAbstractAbilityActive>();
		abilities.add(new ArcaneMissilesTestAbility());
        abilities.add(new BattleClassesAbilityTestCasted("mage.blink"));
		abilities.add(new BattleClassesAbilityTestCasted("mage.fireball"));
		abilities.add(new ScorchTestAbility());
		abilities.add(new FrostBolt());
		abilities.add(new BattleClassesAbilityTestCasted("mage.frostnova"));
		abilities.add(new BattleClassesAbilityShieldBlock());
		//abilities.put(ABILITY_ID_FROSTNOVA, new FrostNovaTestAbility());
		return abilities;
	}
	
	@Override
	public List<BattleClassesTalentTree> getClassTalents() {
		ArrayList<BattleClassesTalentTree> talentTrees = new ArrayList<BattleClassesTalentTree>();
		BattleClassesTalentTree talentTree1 = new BattleClassesTalentTree();
		talentTree1.setName("mage.arcane").setRole(EnumBattleClassesPlayerRole.RANGED_DAMAGE_DEALER).setEnviroments(EnumSet.of(EnumBattleClassesPlayerEnviroment.PVP));
//		talentTree1.talentList.add(new BattleClassesTalentAbilityContainer(1100, 0, new BattleClassesPassiveAbilityAmplifier(1100, 0)));
		talentTree1.talentList.add(new BattleClassesTalentTest("arcane0", 0));
		talentTree1.talentList.add(new BattleClassesTalentTest("arcane1", 1));
		talentTree1.talentList.add(new BattleClassesTalentAbilityActiveContainer("arcane2", 2, new PolymorphTestAbility()));
		BattleClassesTalentTree talentTree2 = new BattleClassesTalentTree();
		talentTree2.setName("mage.fire").setRole(EnumBattleClassesPlayerRole.RANGED_DAMAGE_DEALER).setEnviroments(EnumSet.of(EnumBattleClassesPlayerEnviroment.PVE));
		talentTree2.talentList.add(new BattleClassesTalentTest("fire0", 0));
		talentTree2.talentList.add(new BattleClassesTalentTest("fire1", 1));
		talentTree2.talentList.add(new BattleClassesTalentAbilityActiveContainer("fire2", 2, new DragonsBreathTestAbility()));
		BattleClassesTalentTree talentTree3 = new BattleClassesTalentTree();
		talentTree3.setName("mage.frost").setRole(EnumBattleClassesPlayerRole.RANGED_DAMAGE_DEALER).setEnviroments(EnumSet.of(EnumBattleClassesPlayerEnviroment.PVP));
		talentTree3.talentList.add(new BattleClassesTalentTest("frost0", 0));
		talentTree3.talentList.add(new BattleClassesTalentTest("frost1", 1));
		talentTree3.talentList.add(new BattleClassesTalentAbilityActiveContainer("frost2", 2, new IceBlockTestAbility()));
		
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
		attributes.add(EnumBattleClassesAttributeType.MELEE_ATTACK_DAMAGE);
		attributes.add(EnumBattleClassesAttributeType.CRITICAL_RATING);
		attributes.add(EnumBattleClassesAttributeType.HASTE_RATING);
		return attributes;
	}
	
	//--------------------------------------------------------------------------------------------------------
	//										ACTIVE CLASS ABILITIES
	//--------------------------------------------------------------------------------------------------------
	public static class ArcaneMissilesTestAbility extends BattleClassesAbilityTestChanneled {
		
		public static final ArcaneMissilesTestAbility INSTANCE = new ArcaneMissilesTestAbility();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public ArcaneMissilesTestAbility() {
			super();
			this.setUnlocalizedName("mage.arcanemissiles");
			this.setAmmoRequirement(Item.getItemById(263), 1);
			
		}
	}
	
	public static class PolymorphTestAbility extends BattleClassesAbilityTestCasted {
		
		public static final PolymorphTestAbility INSTANCE = new PolymorphTestAbility();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public PolymorphTestAbility() {
			super();
			this.setUnlocalizedName("mage.polymorph");
		}
	}
	
	public static class ScorchTestAbility extends BattleClassesAbilityActiveDirect {
		
		public static final ScorchTestAbility INSTANCE = new ScorchTestAbility();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public ScorchTestAbility() {
			super();
			this.setUnlocalizedName("mage.scorch");
			this.setCastingType(EnumBattleClassesAbilityCastingType.CASTED);
			this.castTime = 0.5F;
			this.school = EnumBattleClassesAbilitySchool.SPELL_FIRE;
			//this.cooldownClock.setDefaultDuration(5F);
			this.range = 10F;
			BattleClassesAbstractAbilityEffectInstantValue effect = EffectFactory.createInstantAbilityEffect(10, 0, EnumBattleClassesAbilitySchool.SPELL_FIRE); 
			this.addEffect(effect);
			this.setAmmoRequirement(Item.getItemById(263), 1);
		}
	}
	
	public static class DragonsBreathTestAbility extends BattleClassesAbilityTestCasted {
		
		public static final DragonsBreathTestAbility INSTANCE = new DragonsBreathTestAbility();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public DragonsBreathTestAbility() {
			super();
			this.setUnlocalizedName("mage.dragonsbreath");
		}
	}
	
	public static class FrostBolt extends BattleClassesAbilityTestCasted {
		
		public static final FrostBolt INSTANCE = new FrostBolt();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public FrostBolt() {
			super();
			this.setUnlocalizedName("mage.frostbolt");
			this.castTime = 1F;
			this.school = EnumBattleClassesAbilitySchool.SPELL_FROST;
			this.range = 20F;
			BattleClassesAbstractAbilityEffectInstantValue effect = EffectFactory.createInstantAbilityEffect(15, 0, EnumBattleClassesAbilitySchool.SPELL_FROST);
			this.addEffect(effect);
		}
	}
	
	public static class FrostNovaTestAbility extends BattleClassesAbilityTestCasted {
		
		public static final FrostNovaTestAbility INSTANCE = new FrostNovaTestAbility();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public FrostNovaTestAbility() {
			super();
			this.setUnlocalizedName("mage.frostnova");
		}
	}
	
	public static class IceBlockTestAbility extends BattleClassesAbilityTestCasted {
		
		public static final IceBlockTestAbility INSTANCE = new IceBlockTestAbility();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public IceBlockTestAbility() {
			super();
			this.setUnlocalizedName("mage.iceblock");
		}
	}
	
	
	
	//--------------------------------------------------------------------------------------------------------
	//										PASSIVE CLASS ABILITIES
	//--------------------------------------------------------------------------------------------------------
	class FrostTalentWintersChill extends BattleClassesTalentEffectContainer {

		protected FrostTalentWintersChill() {
			super(0, "mage.frostbolt");
			// TODO Auto-generated constructor stub
		}
		
	}
	
}
