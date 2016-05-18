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
import mods.battleclasses.items.BattleClassesItem;

public class BattleClassesPlayerClassMage extends BattleClassesPlayerClass {
		
	public BattleClassesPlayerClassMage(BattleClassesPlayerHooks parPlayerHooks) {
		super(parPlayerHooks, EnumBattleClassesPlayerClass.MAGE);
	}

	//STORED ACTIVE ABILITY REFERENCES
	//ARCANE
	protected Blink spell_Blink;
	protected Polymorph spell_Polymorph;
	protected ArcaneBlast spell_ArcaneBlast;
	protected ArcaneMissiles spell_ArcaneMissiles;
	//FIRE
	protected FireBall spell_FireBall;
	protected Scorch spell_Scorch;
	protected DragonsBreath spell_DragonsBreath;
	//FROST
	protected FrostBolt spell_FrostBolt;
	protected FrostNova spell_FrostNova;
	protected IceBlock spell_IceBlock;
	
	@Override
	protected void initClassContent() {
		this.spell_Blink = new Blink();
		this.spell_Polymorph = new Polymorph();
		this.spell_ArcaneBlast = new ArcaneBlast();
		this.spell_ArcaneMissiles = new ArcaneMissiles();
		this.spell_FireBall = new FireBall();
		this.spell_Scorch = new Scorch();
		this.spell_DragonsBreath = new DragonsBreath();
		this.spell_FrostBolt = new FrostBolt();
		this.spell_FrostNova = new FrostNova();
		this.spell_IceBlock = new IceBlock();
		super.initClassContent();
	}
	
	@Override
	public List<BattleClassesAbstractAbilityActive> getAllActiveAbilties() {
		List<BattleClassesAbstractAbilityActive> activeAbilities = new ArrayList<BattleClassesAbstractAbilityActive>();
		activeAbilities.add(this.spell_Blink);
		activeAbilities.add(this.spell_Polymorph);
		activeAbilities.add(this.spell_ArcaneBlast);
		activeAbilities.add(this.spell_ArcaneMissiles);
		
		activeAbilities.add(this.spell_FireBall);
		activeAbilities.add(this.spell_Scorch);
		activeAbilities.add(this.spell_DragonsBreath);
		
		activeAbilities.add(this.spell_FrostBolt);
		activeAbilities.add(this.spell_FrostNova);
		activeAbilities.add(this.spell_IceBlock);
		return activeAbilities;
	}

	@Override
	public List<BattleClassesAbstractAbilityActive> getInitialAbilities() {
		List<BattleClassesAbstractAbilityActive> abilities = new ArrayList<BattleClassesAbstractAbilityActive>();
		abilities.add(this.spell_Blink);
        abilities.add(this.spell_Polymorph);
		abilities.add(this.spell_FireBall);
		abilities.add(this.spell_Scorch);
		abilities.add(this.spell_FrostBolt);
		abilities.add(this.spell_FrostNova);
		return abilities;
	}
	
	@Override
	public List<BattleClassesTalentTree> getClassTalents() {
		ArrayList<BattleClassesTalentTree> talentTrees = new ArrayList<BattleClassesTalentTree>();
		BattleClassesTalentTree talentTree1 = new BattleClassesTalentTree();
		talentTree1.setName("mage.arcane").setRole(EnumBattleClassesPlayerRole.RANGED_DAMAGE_DEALER).setEnviroments(EnumSet.of(EnumBattleClassesPlayerEnviroment.PVP));
//		talentTree1.talentList.add(new BattleClassesTalentAbilityContainer(1100, 0, new BattleClassesPassiveAbilityAmplifier(1100, 0)));
		talentTree1.talentList.add(new BattleClassesTalentTest("arcane0", 0));
		talentTree1.talentList.add(new BattleClassesTalentAbilityActiveContainer("arcane1", 1, this.spell_ArcaneBlast));
		talentTree1.talentList.add(new BattleClassesTalentAbilityActiveContainer("arcane2", 2, this.spell_ArcaneMissiles));
		BattleClassesTalentTree talentTree2 = new BattleClassesTalentTree();
		talentTree2.setName("mage.fire").setRole(EnumBattleClassesPlayerRole.RANGED_DAMAGE_DEALER).setEnviroments(EnumSet.of(EnumBattleClassesPlayerEnviroment.PVE));
		talentTree2.talentList.add(new BattleClassesTalentTest("fire0", 0));
		talentTree2.talentList.add(new BattleClassesTalentTest("fire1", 1));
		talentTree2.talentList.add(new BattleClassesTalentAbilityActiveContainer("fire2", 2, this.spell_DragonsBreath));
		BattleClassesTalentTree talentTree3 = new BattleClassesTalentTree();
		talentTree3.setName("mage.frost").setRole(EnumBattleClassesPlayerRole.RANGED_DAMAGE_DEALER).setEnviroments(EnumSet.of(EnumBattleClassesPlayerEnviroment.PVP));
		talentTree3.talentList.add(new BattleClassesTalentTest("frost0", 0));
		talentTree3.talentList.add(new BattleClassesTalentTest("frost1", 1));
		talentTree3.talentList.add(new BattleClassesTalentAbilityActiveContainer("frost2", 2, this.spell_IceBlock));
		
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
	
	public static class Blink extends BattleClassesAbilityActiveDirect {
		
		public static final Blink INSTANCE = new Blink();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public Blink() {
			super();
			this.setUnlocalizedName("mage.blink");
			this.setAmmoRequirement(BattleClassesItem.manaGem, 1);
			this.school = EnumBattleClassesAbilitySchool.SPELL_ARCANE;
		}
	}
	
	public static class Polymorph extends BattleClassesAbilityActiveDirect {
		
		public static final Polymorph INSTANCE = new Polymorph();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public Polymorph() {
			super();
			this.setUnlocalizedName("mage.polymorph");
			this.setAmmoRequirement(BattleClassesItem.manaGem, 1);
			this.school = EnumBattleClassesAbilitySchool.SPELL_ARCANE;
			this.setInitialRank(0);
		}
	}

	public static class ArcaneMissiles extends BattleClassesAbilityTestChanneled {
		
		public static final ArcaneMissiles INSTANCE = new ArcaneMissiles();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public ArcaneMissiles() {
			super();
			this.setUnlocalizedName("mage.arcane_missiles");
			this.setAmmoRequirement(BattleClassesItem.manaGem, 1);
			this.school = EnumBattleClassesAbilitySchool.SPELL_ARCANE;
			
		}
	}
	
	public static class ArcaneBlast extends BattleClassesAbilityActiveDirect {
		
		public static final ArcaneBlast INSTANCE = new ArcaneBlast();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public ArcaneBlast() {
			super();
			this.setUnlocalizedName("mage.arcane_blast");
			this.setAmmoRequirement(BattleClassesItem.manaGem, 1);
			this.school = EnumBattleClassesAbilitySchool.SPELL_ARCANE;
		}
	}
	
	
	
	public static class FireBall extends BattleClassesAbilityTestCasted {
		
		public static final FireBall INSTANCE = new FireBall();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public FireBall() {
			super();
			this.setUnlocalizedName("mage.fireball");
			this.baseCastTime = 1F;
			this.school = EnumBattleClassesAbilitySchool.SPELL_FIRE;
			this.range = 20F;
			BattleClassesAbstractAbilityEffectInstantValue effect = EffectFactory.createInstantAbilityEffect(15, 0, EnumBattleClassesAbilitySchool.SPELL_FIRE);
			//this.addEffect(effect);
		}
	}
	
	public static class Scorch extends BattleClassesAbilityActiveDirect {
		
		public static final Scorch INSTANCE = new Scorch();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public Scorch() {
			super();
			this.setUnlocalizedName("mage.scorch");
			this.school = EnumBattleClassesAbilitySchool.SPELL_FIRE;
			this.setCastingType(EnumBattleClassesAbilityCastingType.CASTED);
			this.setInitialRank(0);
			
			this.baseCastTime = 0.5F;
			//this.cooldownClock.setDefaultDuration(5F);
			this.range = 10F;
			BattleClassesAbstractAbilityEffectInstantValue effect = EffectFactory.createInstantAbilityEffect(10, 0, EnumBattleClassesAbilitySchool.SPELL_FIRE); 
			this.addEffect(effect);
			this.setAmmoRequirement(BattleClassesItem.manaGem, 1);
		}
	}
	
	public static class DragonsBreath extends BattleClassesAbilityActiveDirect {
		
		public static final DragonsBreath INSTANCE = new DragonsBreath();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public DragonsBreath() {
			super();
			this.setUnlocalizedName("mage.dragonsbreath");
			this.school = EnumBattleClassesAbilitySchool.SPELL_FIRE;
		}
	}
	
	public static class FrostBolt extends BattleClassesAbilityActiveDirect {
		
		public static final FrostBolt INSTANCE = new FrostBolt();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public FrostBolt() {
			super();
			this.setUnlocalizedName("mage.frostbolt");
			this.baseCastTime = 1F;
			this.school = EnumBattleClassesAbilitySchool.SPELL_FROST;
			this.setCastingType(EnumBattleClassesAbilityCastingType.CASTED);
			this.range = 20F;
			BattleClassesAbstractAbilityEffectInstantValue effect = EffectFactory.createInstantAbilityEffect(15, 0, EnumBattleClassesAbilitySchool.SPELL_FROST);
			this.addEffect(effect);
			this.setAmmoRequirement(BattleClassesItem.manaGem, 1);
		}
	}
	
	public static class FrostNova extends BattleClassesAbilityTestCasted {
		
		public static final FrostNova INSTANCE = new FrostNova();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public FrostNova() {
			super();
			this.setUnlocalizedName("mage.frostnova");
			this.school = EnumBattleClassesAbilitySchool.SPELL_FROST;
			this.setInitialRank(0);
		}
	}
	
	public static class IceBlock extends BattleClassesAbilityActiveDirect {
		
		public static final IceBlock INSTANCE = new IceBlock();
		static {
			BattleClassesAbstractAbility.registerAbility(INSTANCE);
		}
		
		public IceBlock() {
			super();
			this.setUnlocalizedName("mage.iceblock");
			this.school = EnumBattleClassesAbilitySchool.SPELL_FROST;
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
