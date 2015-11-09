package mods.battleclasses.items;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Multimap;

import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.attributes.BattleClassesAttributeModifierBonus;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.attributes.AttributesFactory;
import mods.battleclasses.attributes.ICWAttributeModifier;
import mods.battleclasses.attributes.ICWAttributeModifierOwner;
import mods.battleclasses.client.ITooltipProvider;
import mods.battleclasses.enums.EnumBattleClassesArmorSlot;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class BattleClassesItemArmor extends ItemArmor implements IAttributeProviderItem {

	/** Copy reference of superclass field, only used to calculate armorValue */
    private final ItemArmor.ArmorMaterial material;    
	protected String armorTexture;

	protected String armorSetName = "";
	protected String armorSetMODID = "";
	
	public BattleClassesItemArmor(ArmorMaterial material, int armorType, String MODID, String name) {
		//super(material, renderIndex, armorType);
		super(material, 1, armorType);
		this.material = material;
		this.setName(MODID, name);
		this.setCreativeTab(BattleClassesItem.TabArmors);
		
		//TEST DATA
		/*
		classAccessSet = EnumSet.of(
				EnumBattleClassesPlayerClass.MAGE,
				EnumBattleClassesPlayerClass.PRIEST,
				EnumBattleClassesPlayerClass.WARLOCK,
				EnumBattleClassesPlayerClass.ROGUE,
				EnumBattleClassesPlayerClass.HUNTER,
				EnumBattleClassesPlayerClass.PALADIN,
				EnumBattleClassesPlayerClass.WARRIOR);
		storedAttributes = AttributesFactory.createForArmor(itemLevel, EnumSet.of(EnumBattleClassesAttributeType.HEALTH, EnumBattleClassesAttributeType.STRENGTH, EnumBattleClassesAttributeType.SPELLPOWER_FIRE));
		this.setSingleAttributeModifier(new BattleClassesAttributeModifierBonus(storedAttributes));
		*/
	}
	
	public BattleClassesItemArmor(EnumSet<EnumBattleClassesPlayerClass> classAccessSet, ArmorMaterial material, int armorType, int itemLevel, String MODID, String name) {
		this(material, armorType, MODID, name);
		this.classAccessSet = classAccessSet;
		this.setItemLevel(itemLevel);
	}
		
	public BattleClassesItemArmor setItemLevel(int itemLevel) {
		this.itemLevel = itemLevel;
		this.setMaxDamage(this.armorType);
		return this;
	}
	
	public void setAttributes(BattleClassesAttributes attributes) {
		this.storedAttributes = attributes;
		this.setSingleAttributeModifier(new BattleClassesAttributeModifierBonus(attributes));
	}
	
	public BattleClassesItemArmor setName(String MODID, String parName) {
		String slotName = (EnumBattleClassesArmorSlot.values()[this.armorType]).toString().toLowerCase();
		this.setUnlocalizedName(MODID +":armors/" + parName + "." + slotName);
    	this.setTextureName(MODID +":armors/" + parName + "." + slotName);
    	this.armorTexture = parName;
    	this.armorSetName= parName;
    	this.armorSetMODID = MODID;
		return this;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
	    return armorSetMODID + ":textures/armor/" + this.armorTexture + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
	}
	
    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
    public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        //multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.field_150934_a, 0));
        return multimap;
    }
    
    public int getArmorValue() { 
    	return this.damageReduceAmount;
    }
    
    @Override
    public int getItemEnchantability() {
    	int bonusPerItemLevel = 2;
    	int enchantabilityBaseItemLevel = 3;
    	int enchantabilityBaseValue = ArmorMaterial.IRON.getEnchantability();
    	int enchantability = enchantabilityBaseValue + bonusPerItemLevel*(this.getItemLevel() - enchantabilityBaseItemLevel);
    	return enchantability;
    }
    
    /** Holds the 'base' maxDamage that each armorType have. */
    private static final int[] maxDamageArray = new int[] {11, 16, 15, 13};
    /**
     * Returns the durability for a armor slot of for this type.
     */
    public int getDurability(int slotType) {
    	int bonusPerItemLevel = 5;
    	int maxDamageFactorBaseValue = 5;
    	int maxDamageFactor = maxDamageFactorBaseValue + this.getItemLevel() * bonusPerItemLevel;
        return maxDamageArray[slotType] * maxDamageFactor;
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
		text.add(StatCollector.translateToLocal(this.getArmorMaterial().toString().toLowerCase())); 
		text.add(BattleClassesGuiHelper.getTranslatedBonusLine(this.getArmorValue(), EnumBattleClassesAttributeType.VANILLA_ARMOR));
		return text;
	}

	@Override
	public Set<EnumBattleClassesAttributeType> getActiveTypeExceptions() {
		// TODO Auto-generated method stub
		return null;
	}


}
