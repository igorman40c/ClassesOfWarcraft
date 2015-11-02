package mods.battleclasses.core.classes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbilityActiveDirect;
import mods.battleclasses.ability.active.BattleClassesAbilityShieldBlock;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.talent.BattleClassesTalentAbilityActiveContainer;
import mods.battleclasses.ability.talent.BattleClassesTalentTest;
import mods.battleclasses.core.BattleClassesPlayerClass;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.BattleClassesTalentTree;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.enums.EnumBattleClassesPlayerEnviroment;
import mods.battleclasses.enums.EnumBattleClassesPlayerRole;

public class BattleClassesPlayerClassHunter  extends BattleClassesPlayerClass {
	
	public BattleClassesPlayerClassHunter(BattleClassesPlayerHooks parPlayerHooks) {
		super(parPlayerHooks, EnumBattleClassesPlayerClass.HUNTER);
	}
	
	
	@Override
	public List<BattleClassesAbstractAbilityActive> getClassAbilities() {
		List<BattleClassesAbstractAbilityActive> abilities = new ArrayList<BattleClassesAbstractAbilityActive>();
		//Beastmastery
		abilities.add(new CallPet());
		abilities.add(new MendPet());
		//Marksmanship
		abilities.add(new Shoot());
		abilities.add(new SerpentString());
		//Survival
		abilities.add(new ConcussiveShot());
		abilities.add(new Disengage());
		return abilities;
	}
	
	@Override
	public List<BattleClassesTalentTree> getClassTalents() {
		ArrayList<BattleClassesTalentTree> talentTrees = new ArrayList<BattleClassesTalentTree>();
		
		BattleClassesTalentTree talentTree1 = new BattleClassesTalentTree();
		talentTree1.setName("hunter.beastmastery").setRole(EnumBattleClassesPlayerRole.RANGED_DAMAGE_DEALER).setEnviroments(EnumSet.of(EnumBattleClassesPlayerEnviroment.UNIVERSAL));
		talentTree1.talentList.add(new BattleClassesTalentTest("beastmastery0", 0));
		talentTree1.talentList.add(new BattleClassesTalentAbilityActiveContainer("beastmastery1", 1, new Dash()));
		talentTree1.talentList.add(new BattleClassesTalentAbilityActiveContainer("beastmastery2", 2, new Intimidation()));
		
		BattleClassesTalentTree talentTree2 = new BattleClassesTalentTree();
		talentTree2.setName("hunter.marksmanship").setRole(EnumBattleClassesPlayerRole.RANGED_DAMAGE_DEALER).setEnviroments(EnumSet.of(EnumBattleClassesPlayerEnviroment.PVE));
		talentTree2.talentList.add(new BattleClassesTalentTest("marksmanship0", 0));
		talentTree2.talentList.add(new BattleClassesTalentTest("marksmanship1", 1));
		talentTree2.talentList.add(new BattleClassesTalentAbilityActiveContainer("marksmanship2", 2, new MortalShot()));
		
		BattleClassesTalentTree talentTree3 = new BattleClassesTalentTree();
		talentTree3.setName("hunter.survival").setRole(EnumBattleClassesPlayerRole.RANGED_DAMAGE_DEALER).setEnviroments(EnumSet.of(EnumBattleClassesPlayerEnviroment.PVP));
		talentTree3.talentList.add(new BattleClassesTalentTest("survival0", 0));
		talentTree3.talentList.add(new BattleClassesTalentTest("survival1", 1));
		talentTree3.talentList.add(new BattleClassesTalentAbilityActiveContainer("survival2", 2, new ArcaneShot()));
		
		talentTrees.add(talentTree1);
		talentTrees.add(talentTree2);
		talentTrees.add(talentTree3);
		
		return talentTrees;
	}
	
	public ArrayList<EnumBattleClassesAttributeType> getPrimaryAttributesToDisplay() {
		ArrayList<EnumBattleClassesAttributeType> attributes = new ArrayList<EnumBattleClassesAttributeType>();
		attributes.add(EnumBattleClassesAttributeType.AGILITY);
		return attributes;
	}
	
	public ArrayList<EnumBattleClassesAttributeType> getSecondaryAttributesToDisplay() {
		ArrayList<EnumBattleClassesAttributeType> attributes = new ArrayList<EnumBattleClassesAttributeType>();
		attributes.add(EnumBattleClassesAttributeType.RANGED_ATTACK_DAMAGE);
		attributes.add(EnumBattleClassesAttributeType.CRITICAL_RATING);
		attributes.add(EnumBattleClassesAttributeType.HASTE_RATING);
		attributes.add(EnumBattleClassesAttributeType.ARMOR_PENETRATION);
		return attributes;
	}
	
	//--------------------------------------------------------------------------------------------------------
	//										ACTIVE CLASS ABILITIES
	//--------------------------------------------------------------------------------------------------------
	
	//----------------------------------------
	//SUB-SECTION - Beastmastery abities
	//----------------------------------------
	
	public static class CallPet extends BattleClassesAbilityActiveDirect {
		
		public static final CallPet INSTANCE = new CallPet();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public CallPet() {
			super();
			this.setUnlocalizedName("hunter.call_pet");
		}
	}
	
	public static class MendPet extends BattleClassesAbilityActiveDirect {
		
		public static final MendPet INSTANCE = new MendPet();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public MendPet() {
			super();
			this.setUnlocalizedName("hunter.mend_pet");
		}
	}
	
	public static class Dash extends BattleClassesAbilityActiveDirect {
		
		public static final Dash INSTANCE = new Dash();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public Dash() {
			super();
			this.setUnlocalizedName("hunter.dash");
		}
	}
	
	public static class Intimidation extends BattleClassesAbilityActiveDirect {
		
		public static final Intimidation INSTANCE = new Intimidation();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public Intimidation() {
			super();
			this.setUnlocalizedName("hunter.intimidation");
		}
	}
	
	
	//----------------------------------------
	//SUB-SECTION - Marksmanship abities
	//----------------------------------------
	
	public static class Shoot extends BattleClassesAbilityActiveDirect {
		
		public static final Shoot INSTANCE = new Shoot();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public Shoot() {
			super();
			this.setUnlocalizedName("hunter.shoot");
		}
	}
	
	public static class SerpentString extends BattleClassesAbilityActiveDirect {
		
		public static final SerpentString INSTANCE = new SerpentString();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public SerpentString() {
			super();
			this.setUnlocalizedName("hunter.serpent_string");
		}
	}
	
	public static class MortalShot extends BattleClassesAbilityActiveDirect {
		
		public static final MortalShot INSTANCE = new MortalShot();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public MortalShot() {
			super();
			this.setUnlocalizedName("hunter.mortal_shot");
		}
	}
		
	//----------------------------------------
	//SUB-SECTION - Survival abities
	//----------------------------------------
	
	public static class ConcussiveShot extends BattleClassesAbilityActiveDirect {
		
		public static final ConcussiveShot INSTANCE = new ConcussiveShot();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public ConcussiveShot() {
			super();
			this.setUnlocalizedName("hunter.concussive_shot");
		}
	}
	
	public static class Disengage extends BattleClassesAbilityActiveDirect {
		
		public static final Disengage INSTANCE = new Disengage();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public Disengage() {
			super();
			this.setUnlocalizedName("hunter.disengage");
		}
	}
	
	public static class ArcaneShot extends BattleClassesAbilityActiveDirect {
		
		public static final ArcaneShot INSTANCE = new ArcaneShot();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public ArcaneShot() {
			super();
			this.setUnlocalizedName("hunter.arcane_shot");
		}
	}
	
	
	
	//--------------------------------------------------------------------------------------------------------
	//										PASSIVE CLASS ABILITIES
	//--------------------------------------------------------------------------------------------------------

	
}
