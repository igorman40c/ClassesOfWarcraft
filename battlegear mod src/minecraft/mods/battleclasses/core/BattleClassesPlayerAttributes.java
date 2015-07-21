package mods.battleclasses.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.passive.BattleClassesAbstractAbilityPassive;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.attributes.ICWAttributeModifier;
import mods.battleclasses.attributes.ICWAttributeModifierOwner;
import mods.battleclasses.attributes.ICWClassAccessAttributeModifier;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BattleClassesPlayerAttributes {
	protected BattleClassesPlayerHooks parentPlayerHooks;
	BattleClassesPlayerAttributes(BattleClassesPlayerHooks playerHooks) {
		this.parentPlayerHooks = playerHooks;
	}
	protected EntityPlayer getOwnerPlayer() {
		return this.parentPlayerHooks.getOwnerPlayer();
	}
	
	protected BattleClassesAttributes baseAttributes;
	protected BattleClassesAttributes savedTotalAttributes;
	
	/**
	 * Updates the stored attribute modifiers, calculates total attributes.
	 * Than updates the displayed attributes and the player's max health.
	 * 
	 * HAS TO BE CALLED ON Change in: Equipment OR Weapon OR Passive abilties OR player spawn
	 */
	public void onAttributeSourcesChanged() {
		this.refreshAttributeModifiers();
		BattleClassesAttributes totalAttributes = this.getTotalAttributesForAbility(null);
		savedTotalAttributes = totalAttributes;
		//par1EntityLivingBase.setAbsorptionAmount(par1EntityLivingBase.getAbsorptionAmount() - (float)(4 * (par3 + 1)));
		
		//Refreshing health based on totalAttributes
		this.updatePlayerMaxHealth(totalAttributes.health);
	}
	
	/**
	 * Returns the accumulated common attributes of the player.
	 * @return
	 */
	public BattleClassesAttributes getTotalAttributes() {
		if(this.savedTotalAttributes == null) {
			this.onAttributeSourcesChanged();
		}
		return this.savedTotalAttributes.copy();
	}
	
	public BattleClassesAttributes getDisplayedAttributes() {
		return this.getTotalAttributes();
	}
		
	/**
	 * Calculates the total attributes for an ability, using the stored attribute modifier arrays. 
	 * Can be used with null parameter to get the common attributes (for example for displaying attributes data).
	 * @param modifiedAbility reference to the ability, can be @null
	 * @return
	 */
	public BattleClassesAttributes getTotalAttributesForAbility(BattleClassesAbstractAbilityActive modifiedAbility) {
		BattleClassesAttributes totalAttributes = new BattleClassesAttributes();
		totalAttributes.add(this.getDefaultAttributes());
		for(ICWAttributeModifier attributeModifier : AttributesArray_BASE_ATTRIBUTE_BONUS) {
			attributeModifier.applyAttributeModifier(modifiedAbility, totalAttributes);
		}
		for(ICWAttributeModifier attributeModifier : AttributesArray_BASE_ATTRIBUTE_AMPLIFIER) {
			attributeModifier.applyAttributeModifier(modifiedAbility, totalAttributes);
		}
		for(ICWAttributeModifier attributeModifier : AttributesArray_TOTAL_BASED_ATTRIBUTE_BONUS) {
			attributeModifier.applyAttributeModifier(modifiedAbility, totalAttributes);
		}
		return totalAttributes;
	}
	
	public float getHasteMultiplierFromAttributes(BattleClassesAttributes attributes) {
		return 1F / (1F + attributes.haste);
	}
	
	public float getHasteMultiplierFromTotalAttributes() {
		return getHasteMultiplierFromAttributes(this.getTotalAttributes());
	}
	
	//Stored attribte modifiers, separated by apply type
	protected ArrayList<ICWAttributeModifier> AttributesArray_BASE_ATTRIBUTE_BONUS = new ArrayList<ICWAttributeModifier>();
	protected ArrayList<ICWAttributeModifier> AttributesArray_BASE_ATTRIBUTE_AMPLIFIER = new ArrayList<ICWAttributeModifier>();
	protected ArrayList<ICWAttributeModifier> AttributesArray_TOTAL_BASED_ATTRIBUTE_BONUS = new ArrayList<ICWAttributeModifier>();
	
	/**
	 * Collects all attribute modifers from the player and updates the content stored arrays.
	 */
	protected void refreshAttributeModifiers() {
		List<ICWAttributeModifier> allAttributeModifiers = this.getAttributeModifiersFromPlayer(this.getOwnerPlayer(), this.parentPlayerHooks.playerClass.spellBook);
		AttributesArray_BASE_ATTRIBUTE_BONUS = new ArrayList<ICWAttributeModifier>();
		AttributesArray_BASE_ATTRIBUTE_AMPLIFIER = new ArrayList<ICWAttributeModifier>();
		AttributesArray_TOTAL_BASED_ATTRIBUTE_BONUS = new ArrayList<ICWAttributeModifier>();
		for(ICWAttributeModifier attributeModifier : allAttributeModifiers) {
			switch(attributeModifier.getApplyType()) {
			case BASE_ATTRIBUTE_AMPLIFIER:
				AttributesArray_BASE_ATTRIBUTE_BONUS.add(attributeModifier);
				break;
			case BASE_ATTRIBUTE_BONUS:
				AttributesArray_BASE_ATTRIBUTE_AMPLIFIER.add(attributeModifier);
				break;
			case TOTAL_BASED_ATTRIBUTE_BONUS:
				AttributesArray_TOTAL_BASED_ATTRIBUTE_BONUS.add(attributeModifier);
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Collects all the attribute modifiers from a player
	 * @param entityPlayer referene to the player
	 * @param spellBook	spellBook of the player
	 * @return
	 */
	protected List<ICWAttributeModifier> getAttributeModifiersFromPlayer(EntityPlayer entityPlayer, BattleClassesSpellBook spellBook) {
		List<ICWAttributeModifier> attributeModifierList = new ArrayList<ICWAttributeModifier>();
		attributeModifierList.addAll(this.getAttributeModifiersFromFullEquipment(entityPlayer));
		attributeModifierList.addAll(this.getAttributeModifiersFromPassiveAbiltiies(spellBook));
		attributeModifierList.addAll(this.getAttributeModifiersFromActivePotionEffectsOnPlayer(entityPlayer));
		return attributeModifierList;
	}
	
	private void addItemToAttributeModifierList(Item item, List<ICWAttributeModifier> attributeModifierList) {
		if(item instanceof ICWClassAccessAttributeModifier) {
			EnumBattleClassesPlayerClass playerClassEnum = this.parentPlayerHooks.playerClass.getPlayerClass();
			ICWClassAccessAttributeModifier classRestrictedAttributeOwnerItem = (ICWClassAccessAttributeModifier)item;
			if(playerClassEnum.isEligibleForClassAccessSet(classRestrictedAttributeOwnerItem.getClassAccessSet())) {
				attributeModifierList.addAll(classRestrictedAttributeOwnerItem.getAttributeModifiers());
			}
		}
		else if (item instanceof ICWAttributeModifierOwner) {
			attributeModifierList.addAll(((ICWAttributeModifierOwner)item).getAttributeModifiers());
		}
	}
	
	/**
	 * Collects the attribute modifiers from the item(s) held by a player
	 * @param entityPlayer the player to check his/her equipment
	 * @return
	 */
	protected List<ICWAttributeModifier> getAttributeModifiersFromItemsHeld(EntityPlayer entityPlayer) {
		List<ICWAttributeModifier> attributeModifierList = new ArrayList<ICWAttributeModifier>();
		/*
		if(BattleClassesUtils.isPlayerInBattlemode(entityPlayer)) {
			ItemStack mainHandItemStack = BattleClassesUtils.getMainhandItemStack(getOwnerPlayer());
			if(mainHandItemStack != null) {
				this.addItemToAttributeModifierList(mainHandItemStack.getItem(), attributeModifierList);
			}
			ItemStack offHandItemStack = BattleClassesUtils.getOffhandItemStack(getOwnerPlayer());
			if(offHandItemStack != null) {
				this.addItemToAttributeModifierList(offHandItemStack.getItem(), attributeModifierList);
			}
		}
		else {
			if(entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() != null) {
				this.addItemToAttributeModifierList(entityPlayer.getHeldItem().getItem(), attributeModifierList);
			}
		}
		*/
		
		ItemStack mainHandItemStackHeld = BattleClassesUtils.getMainhandItemHeld(getOwnerPlayer());
		if(mainHandItemStackHeld != null) {
			this.addItemToAttributeModifierList(mainHandItemStackHeld.getItem(), attributeModifierList);
		}
		ItemStack offHandItemStackHeld = BattleClassesUtils.getOffhandItemHeld(getOwnerPlayer());
		if(offHandItemStackHeld != null) {
			this.addItemToAttributeModifierList(offHandItemStackHeld.getItem(), attributeModifierList);
		}
		
		return attributeModifierList;
	}
	
	/**
	 * Collects the attribute modifiers from the armor pieces worn by a player
	 * @param entityPlayer the player to check his/her equipment
	 * @return
	 */
	protected List<ICWAttributeModifier> getAttributeModifiersFromArmorWorn(EntityPlayer entityPlayer) {
		List<ICWAttributeModifier> attributeModifierList = new ArrayList<ICWAttributeModifier>();
		for(ItemStack armorItemStack : entityPlayer.inventory.armorInventory) {
			if(armorItemStack != null) {
				this.addItemToAttributeModifierList(armorItemStack.getItem(), attributeModifierList);
			}
		}
		return attributeModifierList;
	}
	
	/**
	 * Collects the attribute modifiers from the full equipment of a player
	 * @param entityPlayer the player to check his/her equipment
	 * @return
	 */
	protected List<ICWAttributeModifier> getAttributeModifiersFromFullEquipment(EntityPlayer entityPlayer) {
		List<ICWAttributeModifier> attributeModifierList = new ArrayList<ICWAttributeModifier>();
		if(entityPlayer.inventory != null) {
			//Collecting from armor pieces worn
			attributeModifierList.addAll(getAttributeModifiersFromArmorWorn(entityPlayer));
			//Collecting from item(s) held
			attributeModifierList.addAll(getAttributeModifiersFromItemsHeld(entityPlayer));
		}
		return attributeModifierList;
	}
	
	/**
	 * Collects the attribute modifiers from the passive abilities stored by the given spellbook. 
	 * @param spellBook
	 * @return
	 */
	protected List<ICWAttributeModifier> getAttributeModifiersFromPassiveAbiltiies(BattleClassesSpellBook spellBook) {
		List<ICWAttributeModifier> attributeModifierList = new ArrayList<ICWAttributeModifier>();
		for(BattleClassesAbstractAbilityPassive passiveAbility : spellBook.getPassiveAbilitiesInArray()) {
			if(passiveAbility instanceof ICWAttributeModifierOwner) {
				attributeModifierList.addAll(((ICWAttributeModifierOwner) passiveAbility).getAttributeModifiers());
			}
		}
		return attributeModifierList;
	}
	
	/**
	 * Collects the attribute modifiers from the active potion effects of a player
	 * @param entityPlayer
	 * @return
	 */
	protected List<ICWAttributeModifier> getAttributeModifiersFromActivePotionEffectsOnPlayer(EntityPlayer entityPlayer) {
		List<ICWAttributeModifier> attributeModifierList = new ArrayList<ICWAttributeModifier>();
		for(PotionEffect potionEffect : BattleClassesUtils.getActivePotionEffectsFromEntity(entityPlayer)) {
			Potion potion = BattleClassesUtils.getPotionByID(potionEffect.getPotionID());
			if(potion instanceof ICWAttributeModifierOwner) {
				attributeModifierList.addAll(((ICWAttributeModifierOwner) potion).getAttributeModifiers());
			}
		}
		return attributeModifierList;
	}
	
	/**
	 * Default ammount of maximal health points a player has
	 */
	protected static float DEFAULT_PLAYER_HP = 20;
	/**
	 * Returns the default attributes of a player
	 * @return
	 */
	protected BattleClassesAttributes getDefaultAttributes() {
		BattleClassesAttributes baseAttributes = new BattleClassesAttributes();
		//baseAttributes.stamina = this.getOwnerPlayer().getMaxHealth();
		//baseAttributes.health = DEFAULT_PLAYER_HP;
		//baseAttributes.armor = this.getOwnerPlayer().getArm
		return baseAttributes;
	}
	
	/**
	 * Updates the maximal health by the given ammount
	 * @param healthBonus the float value to add to the player's max health
	 */
	protected void updatePlayerMaxHealth(float healthBonus) {
		float relativeHealth = this.getOwnerPlayer().getHealth() / this.getOwnerPlayer().getMaxHealth();
		AttributeModifier newHealthModifier = new AttributeModifier(UUID.fromString("5D6F0BA2-1186-46AC-B896-BCA001EE0001"), "bcattribute.healthBoost", healthBonus, 0);
		IAttributeInstance iattributeinstance = this.getOwnerPlayer().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
		AttributeModifier oldHealthModifier = iattributeinstance.getModifier(UUID.fromString("5D6F0BA2-1186-46AC-B896-BCA001EE0001"));
		if(oldHealthModifier != null) {
			iattributeinstance.removeModifier(oldHealthModifier);
		}
		iattributeinstance.applyModifier(newHealthModifier);
		
		this.getOwnerPlayer().setHealth(relativeHealth * this.getOwnerPlayer().getMaxHealth());
	}
}
