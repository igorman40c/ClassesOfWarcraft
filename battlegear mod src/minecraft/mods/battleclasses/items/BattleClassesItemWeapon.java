package mods.battleclasses.items;

import java.util.EnumSet;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battleclasses.enumhelper.EnumBattleClassesWieldAccess;
import mods.battlegear2.utils.BattlegearConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.world.World;

public class BattleClassesItemWeapon extends ItemSword {
	
	protected final ToolMaterial material;
	protected int itemAbilityLevel = 0;
	protected EnumSet<EnumBattleClassesPlayerClass> classAccessSet;
	
    public BattleClassesItemWeapon(ToolMaterial toolMaterial) {
		super(toolMaterial);
		this.material = toolMaterial;
		this.setCreativeTab(BattlegearConfig.customTab);
	}
    
    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
    	BattleClassesUtils.getPlayerSpellBook(entityPlayer).CastStart(itemStack, world, entityPlayer);
    	return itemStack;
    }
	
    public void onUsingTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
    	BattleClassesUtils.getPlayerSpellBook(entityPlayer).CastTick(itemStack, entityPlayer, tickCount);
    }

    /**
     * called when the player releases the use item button. Args: itemstack, world, entityplayer, itemInUseCount
     */
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer entityPlayer, int tickCount) {
    	BattleClassesUtils.getPlayerSpellBook(entityPlayer).CastRelease(itemStack, entityPlayer, tickCount);
    }
    
    public int getItemAbilityLevel() {
    	return this.itemAbilityLevel;
    }
    
    public EnumSet<EnumBattleClassesPlayerClass> getClassAccessSet() {
    	return this.classAccessSet;
    }

}
