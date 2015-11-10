package mods.battleclasses.blocks;

import java.util.ArrayList;
import java.util.Random;

import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.items.BattleClassesItem;
import mods.battleclasses.items.misc.BattleClassesItemGem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BattleClassesBlockOreGem extends Block {

    public BattleClassesBlockOreGem()
    {
        super(Material.rock);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
        this.setStepSound(soundTypePiston);
        this.setBlockName("gemsOre");
        this.setCreativeTab(CreativeTabs.tabBlock);
        
        this.setBlockTextureName("battleclasses:" + "gemsOre");
        
        //this.setBlockTextureName("emerald_ore"));
        //this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return BattleClassesItem.itemGem;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random p_149745_1_)
    {
        return 1;
    }

    /**
     * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
     */
    public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_)
    {
        if (p_149679_1_ > 0 && Item.getItemFromBlock(this) != this.getItemDropped(0, p_149679_2_, p_149679_1_))
        {
            int j = p_149679_2_.nextInt(p_149679_1_ + 2) - 1;

            if (j < 0)
            {
                j = 0;
            }

            return this.quantityDropped(p_149679_2_) * (j + 1);
        }
        else
        {
            return this.quantityDropped(p_149679_2_);
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int p_149692_1_)
    {
    	int randomGemMeta = MathHelper.getRandomIntegerInRange(rand, 1, BattleClassesItemGem.ITEM_GEM_TYPES_COUNT-1);
        return randomGemMeta;
    }

    private Random rand = new Random();
    
    public int getExpDrop(IBlockAccess p_149690_1_, int p_149690_5_, int p_149690_7_)
    {
        return MathHelper.getRandomIntegerInRange(rand, 3, 7);
    }
    
}
