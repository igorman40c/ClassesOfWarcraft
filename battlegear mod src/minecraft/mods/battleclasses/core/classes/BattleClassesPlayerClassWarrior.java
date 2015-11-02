package mods.battleclasses.core.classes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbilityActiveDirect;
import mods.battleclasses.ability.active.BattleClassesAbilityShieldBlock;
import mods.battleclasses.ability.active.BattleClassesAbilityTestCasted;
import mods.battleclasses.ability.active.BattleClassesAbilityTestChanneled;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.active.EnumBattleClassesAbilityCastingType;
import mods.battleclasses.ability.effect.BattleClassesAbstractAbilityEffectInstantValue;
import mods.battleclasses.ability.effect.EffectFactory;
import mods.battleclasses.ability.talent.BattleClassesTalentAbilityActiveContainer;
import mods.battleclasses.ability.talent.BattleClassesTalentEffectContainer;
import mods.battleclasses.ability.talent.BattleClassesTalentTest;
import mods.battleclasses.core.BattleClassesPlayerClass;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.BattleClassesTalentTree;
import mods.battleclasses.core.classes.BattleClassesPlayerClassMage.ArcaneBlast;
import mods.battleclasses.core.classes.BattleClassesPlayerClassMage.ArcaneMissilesTestAbility;
import mods.battleclasses.core.classes.BattleClassesPlayerClassMage.DragonsBreathTestAbility;
import mods.battleclasses.core.classes.BattleClassesPlayerClassMage.FrostBolt;
import mods.battleclasses.core.classes.BattleClassesPlayerClassMage.FrostNovaTestAbility;
import mods.battleclasses.core.classes.BattleClassesPlayerClassMage.IceBlockTestAbility;
import mods.battleclasses.core.classes.BattleClassesPlayerClassMage.PolymorphTestAbility;
import mods.battleclasses.core.classes.BattleClassesPlayerClassMage.ScorchTestAbility;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.enums.EnumBattleClassesPlayerEnviroment;
import mods.battleclasses.enums.EnumBattleClassesPlayerRole;
import net.minecraft.item.Item;

public class BattleClassesPlayerClassWarrior extends BattleClassesPlayerClass {
	
	public BattleClassesPlayerClassWarrior(BattleClassesPlayerHooks parPlayerHooks) {
		super(parPlayerHooks, EnumBattleClassesPlayerClass.WARRIOR);
	}
	
	
	@Override
	public List<BattleClassesAbstractAbilityActive> getClassAbilities() {
		List<BattleClassesAbstractAbilityActive> abilities = new ArrayList<BattleClassesAbstractAbilityActive>();
		//Arms
		abilities.add(new MortalStrike());
		abilities.add(new Charge());
		//Fury
		abilities.add(new Hamstring());
		abilities.add(new Whirlwind());
		//Protection
		abilities.add(new BattleClassesAbilityShieldBlock());
		abilities.add(new ChallengingShout());
		return abilities;
	}
	
	@Override
	public List<BattleClassesTalentTree> getClassTalents() {
		ArrayList<BattleClassesTalentTree> talentTrees = new ArrayList<BattleClassesTalentTree>();
		
		BattleClassesTalentTree talentTree1 = new BattleClassesTalentTree();
		talentTree1.setName("warrior.arms").setRole(EnumBattleClassesPlayerRole.DAMAGE_DEALER).setEnviroments(EnumSet.of(EnumBattleClassesPlayerEnviroment.PVP));
		talentTree1.talentList.add(new BattleClassesTalentTest("arms0", 0));
		talentTree1.talentList.add(new BattleClassesTalentTest("arms1", 1));
		talentTree1.talentList.add(new BattleClassesTalentAbilityActiveContainer("arms2", 2, new ShatteringThrow()));
		
		BattleClassesTalentTree talentTree2 = new BattleClassesTalentTree();
		talentTree2.setName("warrior.fury").setRole(EnumBattleClassesPlayerRole.DAMAGE_DEALER).setEnviroments(EnumSet.of(EnumBattleClassesPlayerEnviroment.PVE));
		talentTree2.talentList.add(new BattleClassesTalentTest("fury0", 0));
		talentTree2.talentList.add(new BattleClassesTalentTest("fury1", 1));
		talentTree2.talentList.add(new BattleClassesTalentAbilityActiveContainer("fury2", 2, new Ravage()));
		
		BattleClassesTalentTree talentTree3 = new BattleClassesTalentTree();
		talentTree3.setName("warrior.protection").setRole(EnumBattleClassesPlayerRole.TANK).setEnviroments(EnumSet.of(EnumBattleClassesPlayerEnviroment.PVE));
		talentTree3.talentList.add(new BattleClassesTalentTest("protection0", 0));
		talentTree3.talentList.add(new BattleClassesTalentTest("protection1", 1));
		talentTree3.talentList.add(new BattleClassesTalentAbilityActiveContainer("protection2", 2, new LastStand()));
		
		talentTrees.add(talentTree1);
		talentTrees.add(talentTree2);
		talentTrees.add(talentTree3);
		
		return talentTrees;
	}
	
	public ArrayList<EnumBattleClassesAttributeType> getPrimaryAttributesToDisplay() {
		ArrayList<EnumBattleClassesAttributeType> attributes = new ArrayList<EnumBattleClassesAttributeType>();
		attributes.add(EnumBattleClassesAttributeType.STRENGTH);
		return attributes;
	}
	
	public ArrayList<EnumBattleClassesAttributeType> getSecondaryAttributesToDisplay() {
		ArrayList<EnumBattleClassesAttributeType> attributes = new ArrayList<EnumBattleClassesAttributeType>();
		attributes.add(EnumBattleClassesAttributeType.MELEE_ATTACK_DAMAGE);
		attributes.add(EnumBattleClassesAttributeType.CRITICAL_RATING);
		attributes.add(EnumBattleClassesAttributeType.HASTE_RATING);
		attributes.add(EnumBattleClassesAttributeType.ARMOR_PENETRATION);
		return attributes;
	}
	
	//--------------------------------------------------------------------------------------------------------
	//										ACTIVE CLASS ABILITIES
	//--------------------------------------------------------------------------------------------------------
	
	//----------------------------------------
	//SUB-SECTION - Arms abilities
	//----------------------------------------
	
	public static class MortalStrike extends BattleClassesAbilityActiveDirect {
		
		public static final MortalStrike INSTANCE = new MortalStrike();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public MortalStrike() {
			super();
			this.setUnlocalizedName("warrior.mortal_strike");
		}
	}
	
	public static class Charge extends BattleClassesAbilityActiveDirect {
		
		public static final Charge INSTANCE = new Charge();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public Charge() {
			super();
			this.setUnlocalizedName("warrior.charge");
		}
	}
	
	public static class ShatteringThrow extends BattleClassesAbilityActiveDirect {
		
		public static final ShatteringThrow INSTANCE = new ShatteringThrow();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public ShatteringThrow() {
			super();
			this.setUnlocalizedName("warrior.shattering_throw");
		}
	}
	
	
	//----------------------------------------
	//SUB-SECTION - Fury abilities
	//----------------------------------------
	
	public static class Hamstring extends BattleClassesAbilityActiveDirect {
		
		public static final Hamstring INSTANCE = new Hamstring();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public Hamstring() {
			super();
			this.setUnlocalizedName("warrior.hamstring");
		}
	}
	
	public static class Whirlwind extends BattleClassesAbilityActiveDirect {
		
		public static final Whirlwind INSTANCE = new Whirlwind();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public Whirlwind() {
			super();
			this.setUnlocalizedName("warrior.whirlwind");
		}
	}
	
	public static class Ravage extends BattleClassesAbilityActiveDirect {
		
		public static final Ravage INSTANCE = new Ravage();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public Ravage() {
			super();
			this.setUnlocalizedName("warrior.ravage");
		}
	}
	
	//----------------------------------------
	//SUB-SECTION - Protection abilities
	//----------------------------------------
	
	public static class ChallengingShout extends BattleClassesAbilityActiveDirect {
		
		public static final ChallengingShout INSTANCE = new ChallengingShout();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public ChallengingShout() {
			super();
			this.setUnlocalizedName("warrior.challenging_shout");
		}
	}
	
	public static class LastStand extends BattleClassesAbilityActiveDirect {
		
		public static final LastStand INSTANCE = new LastStand();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public LastStand() {
			super();
			this.setUnlocalizedName("warrior.last_stand");
		}
	}
	
	
	
	//--------------------------------------------------------------------------------------------------------
	//										PASSIVE CLASS ABILITIES
	//--------------------------------------------------------------------------------------------------------

	
}
