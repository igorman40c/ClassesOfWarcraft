package mods.battleclasses.items;

import com.google.common.collect.Multimap;

import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.enums.EnumBattleClassesArmorSlot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class BattleClassesItemArmor extends ItemArmor {

	/** The EnumArmorMaterial used for this ItemArmor */
    private final ItemArmor.ArmorMaterial material;
	protected int itemLevel;
	protected String armorTexture;
	public BattleClassesItemArmor(ArmorMaterial material, int armorType, int itemLevel, String name) {
		super(material, 1, armorType);
		//super(material, renderIndex, armorType);
		this.material = material;
		this.setName(name);
		this.setCreativeTab(BattleClassesItems.TabArmors);
	}
	
	public BattleClassesItemArmor setName(String parName) {
		String slotName = (EnumBattleClassesArmorSlot.values()[this.armorType]).toString().toLowerCase();
		this.setUnlocalizedName("battleclasses:"+"armors/" + parName + "." + slotName);
    	this.setTextureName("battleclasses:"+"armors/" + parName + "." + slotName);
    	this.armorTexture = parName;
		return this;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
	    return BattleClassesMod.MODID + ":textures/armor/" + this.armorTexture + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
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
	
}
