package mods.battleclasses.items;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import mods.battleclasses.attributes.AttributesFactory;
import mods.battleclasses.attributes.BattleClassesAttributeModifierBonus;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.attributes.ICWAttributeModifier;
import mods.battleclasses.attributes.AttributesFactory.WeaponDamageCreationMode;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesItemRarity;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.enums.EnumBattleClassesHandHeldType;
import mods.battlegear2.api.PlayerEventChild.OffhandAttackEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class BattleClassesItemHandheld extends ItemSword implements IBattleClassesHandHeld, IAttributeProviderItem {

	public BattleClassesItemHandheld() {
		super(ToolMaterial.IRON);
		this.setCreativeTab(BattleClassesItems.TabWeapons);
	}

	protected EnumBattleClassesHandHeldType handHeldType = EnumBattleClassesHandHeldType.ONE_HANDED;
	@Override
	public EnumBattleClassesHandHeldType getHeldType() {
		return handHeldType;
	}

	/*
	@Override
	public EnumBattleClassesItemRarity getRarity() {
		// TODO Auto-generated method stub
		return null;
	}
	*/
	
	public BattleClassesItemHandheld setItemLevelAndAttributeTypes(int itemLevel, EnumSet<EnumBattleClassesAttributeType> types, float weaponSpeed, WeaponDamageCreationMode weaponDamageMode) {
		this.itemLevel = itemLevel;
		this.setMaxDamage(this.getDurability());
		this.storedAttributes = AttributesFactory.createForHandheld(itemLevel, this.handHeldType, types, weaponSpeed, weaponDamageMode);
		this.setSingleAttributeModifier(new BattleClassesAttributeModifierBonus(storedAttributes));
		return this;
	}
	
    @Override
    public int getItemEnchantability() {
    	int bonusPerItemLevel = 1;
    	int enchantabilityBaseItemLevel = 3;
    	int enchantabilityBaseValue = ToolMaterial.IRON.getEnchantability();
    	int enchantability = enchantabilityBaseValue + bonusPerItemLevel*(this.getItemLevel() - enchantabilityBaseItemLevel);
    	return enchantability;
    }
    
    /**
     * Returns the durability of the handheld
     */
    public int getDurability() {
    	int durability;
    	int bonusPerItemLevel = 500;
    	switch(this.getItemLevel()) {
    		case 0: {
    			durability = ToolMaterial.WOOD.getMaxUses();
    			break;
    		}
    		case 1: {
    			durability = ToolMaterial.STONE.getMaxUses();
    			break;
    		}
    		case 2: {
    			durability = ToolMaterial.IRON.getMaxUses();
    			break;
    		}
    		case 3: {
    			durability = ToolMaterial.EMERALD.getMaxUses();
    			break;
    		}
    		default: {
    			durability = ToolMaterial.EMERALD.getMaxUses() + this.getItemLevel() * bonusPerItemLevel;
    			break;
    		}
    	}

    	return durability;
    }
    
    //----------------------------------------------------------------------------------
  	//						 SECTION - IBattleClassesHandHeld
  	//----------------------------------------------------------------------------------

	@Override
	public boolean sheatheOnBack(ItemStack item) {
		return true;
	}


	@Override
	public boolean isOffhandHandDual(ItemStack off) {
		return this.getHeldType().isOffhandEligible();
	}
	
	@Override
	public boolean allowOffhand(ItemStack mainhand, ItemStack offhand) {
		switch(this.getHeldType()) {
		case MAIN_HANDED: {
			return true;
		}
		case OFF_HANDED: {
			if(mainhand.getItem() instanceof IBattleClassesHandHeld) {
				if(((IBattleClassesHandHeld) mainhand.getItem()).getHeldType() == EnumBattleClassesHandHeldType.OFF_HANDED) {
					return false;
				}
			}
			return true;
		}
		case ONE_HANDED: {
			return true;
		}
		case TWO_HANDED: {
			return offhand == null;
		}
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean invertOnBack(ItemStack itemStack) {
		return false;
	}

	
	@Override
	public boolean offhandAttackEntity(OffhandAttackEvent event, ItemStack mainhandItem, ItemStack offhandItem) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean offhandClickAir(PlayerInteractEvent event, ItemStack mainhandItem, ItemStack offhandItem) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean offhandClickBlock(PlayerInteractEvent event, ItemStack mainhandItem, ItemStack offhandItem) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void performPassiveEffects(Side effectiveSide, ItemStack mainhandItem, ItemStack offhandItem) {
		// TODO Auto-generated method stub
	}
	
	
    //----------------------------------------------------------------------------------
  	//						 SECTION - IAttributeProviderItem
  	//----------------------------------------------------------------------------------
    
	protected int itemLevel = 0;
	protected EnumSet<EnumBattleClassesPlayerClass> classAccessSet = EnumSet.of(EnumBattleClassesPlayerClass.NONE);//EnumSet.noneOf(EnumBattleClassesPlayerClass.class);
	protected BattleClassesAttributes storedAttributes = new BattleClassesAttributes();
	protected List<ICWAttributeModifier> attributeModifiers;

	@Override
	public List<ICWAttributeModifier> getAttributeModifiers() {
		return attributeModifiers;
	}

	@Override
	public void setAttributeModifiers(List<ICWAttributeModifier> attributeModifiers) {
		this.attributeModifiers = attributeModifiers; 
	}
	
	public void setSingleAttributeModifier(ICWAttributeModifier attributeModifier) {
		this.attributeModifiers = new ArrayList<ICWAttributeModifier>();
		attributeModifiers.add(attributeModifier);
	}

	@Override
	public BattleClassesAttributes getAttributes() {
		return this.storedAttributes;
	}
	
	@Override
	public EnumSet<EnumBattleClassesPlayerClass> getClassAccessSet() {
    	return this.classAccessSet;
    }

	@Override
	public int getItemLevel() {
		return this.itemLevel;
	}

	@Override
	public List<String> getTooltipText() {
		List<String> text = new ArrayList<String>();
		text.add(this.handHeldType.getTranslatedName());
		return text;
	}

}