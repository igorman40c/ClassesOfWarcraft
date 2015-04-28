package mods.battleclasses.core;

import java.util.ArrayList;
import java.util.UUID;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.passive.BattleClassesAbstractAbilityPassive;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;
import mods.battleclasses.items.IAttributeProvider;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BattleClassesPlayerAttributes {
	protected BattleClassesPlayerHooks parentPlayerHooks;
	BattleClassesPlayerAttributes(BattleClassesPlayerHooks playerHooks) {
		this.parentPlayerHooks = playerHooks;
	}
	public EntityPlayer getOwnerPlayer() {
		return this.parentPlayerHooks.getOwnerPlayer();
	}
	
	protected BattleClassesAttributes baseAttributes;
	protected BattleClassesAttributes displayedAttributes;
	//AMPLIFIERS
	/** Should be called every time when there is a change in: Weapon equiped OR Armor worn OR Talent points OR Potion effects OR onPlayerSpawn  */
	public void onAttributeSourcesChanged() {
		refreshBaseAttributes();
		BattleClassesAttributes totalAttributes = this.getTotalAttributesForAbility(0);
		
		//par1EntityLivingBase.setAbsorptionAmount(par1EntityLivingBase.getAbsorptionAmount() - (float)(4 * (par3 + 1)));
		//SharedMonsterAttributes.maxHealth
		
		//Refreshing health based on totalAttributes
		float relativeHealth = this.getOwnerPlayer().getHealth() / this.getOwnerPlayer().getMaxHealth();
		AttributeModifier newHealthModifier = new AttributeModifier(UUID.fromString("5D6F0BA2-1186-46AC-B896-BCA001EE0001"), "bcattribute.healthBoost", totalAttributes.health, 0);
		IAttributeInstance iattributeinstance = this.getOwnerPlayer().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
		AttributeModifier oldHealthModifier = iattributeinstance.getModifier(UUID.fromString("5D6F0BA2-1186-46AC-B896-BCA001EE0001"));
		if(oldHealthModifier != null) {
			iattributeinstance.removeModifier(oldHealthModifier);
		}
		iattributeinstance.applyModifier(newHealthModifier);
		
		this.getOwnerPlayer().setHealth(relativeHealth * this.getOwnerPlayer().getMaxHealth());
	}
	
	private ArrayList<IAmplifyProvider> getAmplifiersFromPotionEffects(EnumBattleClassesAmplifierApplyType applyType) {
		ArrayList<IAmplifyProvider> amplifiers = new ArrayList<IAmplifyProvider>();
		//TODO
		return amplifiers;
	}
	
	private ArrayList<IAmplifyProvider> getAmplifiersFromPassiveAbilities(EnumBattleClassesAmplifierApplyType applyType) {
		ArrayList<IAmplifyProvider> amplifiers = new ArrayList<IAmplifyProvider>();
		for(BattleClassesAbstractAbilityPassive passiveAbility : this.parentPlayerHooks.playerClass.spellBook.getPassiveAbilitiesInArray()) {
			if(passiveAbility instanceof IAmplifyProvider) {
				IAmplifyProvider amplifier = (IAmplifyProvider)passiveAbility;
				if(amplifier.getApplyType() == applyType) {
					amplifiers.add(amplifier);
				}
			}
		}
		return amplifiers;
	}
	
	public static float DEFAULT_PLAYER_HP = 20;
	
	public BattleClassesAttributes getDefaultAttributes() {
		BattleClassesAttributes baseAttributes = new BattleClassesAttributes();
		//baseAttributes.stamina = this.getOwnerPlayer().getMaxHealth();
		//baseAttributes.health = DEFAULT_PLAYER_HP;
		//baseAttributes.armor = this.getOwnerPlayer().getArm
		return baseAttributes;
	}
	
	public BattleClassesAttributes getItemAttributes() {
		BattleClassesAttributes itemAttributes = new BattleClassesAttributes();
		if(this.getOwnerPlayer().inventory != null) {
			for(ItemStack armorItemStack : this.getOwnerPlayer().inventory.armorInventory) {
				addAttributesFromItemStack(itemAttributes, armorItemStack, this.getOwnerPlayer());
			}
			ItemStack mainHandItemStack = BattleClassesUtils.getMainhandItemStack(getOwnerPlayer());
			addAttributesFromItemStack(itemAttributes, mainHandItemStack, this.getOwnerPlayer());
			ItemStack offHandItemStack = BattleClassesUtils.getOffhandItemStack(getOwnerPlayer());
			addAttributesFromItemStack(itemAttributes, offHandItemStack, this.getOwnerPlayer());
		}
		
		return itemAttributes;
	}
	
	public static void addAttributesFromItemStack(BattleClassesAttributes attributes, ItemStack itemStack, EntityPlayer entityPlayer) {
		if(itemStack != null && itemStack.getItem() instanceof IAttributeProvider) {
			if(((IAttributeProvider)itemStack.getItem()).getClassAccessSet() != null && 
				((IAttributeProvider)itemStack.getItem()).getClassAccessSet().contains(BattleClassesUtils.getPlayerClassEnum(entityPlayer))) {
				attributes.add(((IAttributeProvider)itemStack.getItem()).getAttributes());
			}
		}
	}
	
	private BattleClassesAttributes getBaseAttributeBonuses(int targetAbilityID) {
		BattleClassesAttributes baseAttributeBonuses = new BattleClassesAttributes();
		ArrayList<IAmplifyProvider> bonuses = this.getAmplifiersFromPassiveAbilities(EnumBattleClassesAmplifierApplyType.BASE_ATTRIBUTE_BONUS);
		for(IAmplifyProvider bonus : bonuses) {
			baseAttributeBonuses.add(bonus.getAttributes(targetAbilityID));
		}
		return baseAttributeBonuses;
	}
	
	private BattleClassesAttributes getBaseAttributeAmplifiers(int targetAbilityID) {
		BattleClassesAttributes baseAttributeAmplifiers = new BattleClassesAttributes(1);
		ArrayList<IAmplifyProvider> amplifiers = this.getAmplifiersFromPassiveAbilities(EnumBattleClassesAmplifierApplyType.BASE_ATTRIBUTE_AMPLIFIER);
		for(IAmplifyProvider amplifier : amplifiers) {
			baseAttributeAmplifiers.multiply(amplifier.getAttributes(targetAbilityID));
		}
		return baseAttributeAmplifiers;
	}
	
	private BattleClassesAttributes getTotalBasedAttributeBonuses(int targetAbilityID, BattleClassesAttributes totalAttributes) {
		BattleClassesAttributes totalAttributeBonuses = new BattleClassesAttributes();
		ArrayList<IAmplifyProvider> bonuses = this.getAmplifiersFromPassiveAbilities(EnumBattleClassesAmplifierApplyType.TOTAL_BASED_ATTRIBUTE_BONUS);
		for(IAmplifyProvider bonus : bonuses) {
			totalAttributeBonuses.add(bonus.getAttributes(targetAbilityID, totalAttributes));
		}
		return totalAttributeBonuses;
	}
	
	protected void refreshBaseAttributes() {
		BattleClassesAttributes attributes = new BattleClassesAttributes();
		attributes.add(this.getDefaultAttributes());
		attributes.add(this.getItemAttributes());
		this.baseAttributes = attributes;
		this.displayedAttributes = new BattleClassesAttributes();
		this.displayedAttributes.add(baseAttributes);
		this.displayedAttributes.add(this.getTotalBasedAttributeBonuses(0, this.displayedAttributes));
	}
	
	public BattleClassesAttributes getDisplayedAttributes() {
		if(this.displayedAttributes == null) {
			this.refreshBaseAttributes();
		}
		return this.displayedAttributes;
	}
	
	public BattleClassesAttributes getTotalAttributesForAbility(int targetAbilityID) {
		BattleClassesAttributes totalAttributes = new BattleClassesAttributes();
		totalAttributes.add(this.baseAttributes);
		totalAttributes.add(this.getBaseAttributeBonuses(targetAbilityID));
		totalAttributes.multiply(this.getBaseAttributeAmplifiers(targetAbilityID));
		totalAttributes.add(this.getTotalBasedAttributeBonuses(targetAbilityID, totalAttributes));
		
		return totalAttributes;
	}
}
