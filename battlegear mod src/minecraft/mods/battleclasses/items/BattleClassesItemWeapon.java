package mods.battleclasses.items;

import java.util.EnumSet;

import cpw.mods.fml.relauncher.Side;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.enumhelper.EnumBattleClassesItemHandRequirement;
import mods.battleclasses.enumhelper.EnumBattleClassesItemRarity;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battleclasses.enumhelper.EnumBattleClassesWieldAccess;
import mods.battlegear2.api.PlayerEventChild.OffhandAttackEvent;
import mods.battlegear2.api.weapons.WeaponRegistry;
import mods.battlegear2.utils.BattlegearConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class BattleClassesItemWeapon extends ItemSword implements IBattleClassesWeapon {
	
	protected final ToolMaterial material;
	protected EnumSet<EnumBattleClassesPlayerClass> classAccessSet;
	protected String name;
	protected float baseWeaponDamage;
	
    public BattleClassesItemWeapon(ToolMaterial toolMaterial) {
		super(toolMaterial);
		this.material = toolMaterial;
		this.setCreativeTab(BattlegearConfig.customTab);
	}
    
    protected void setName(String parName) {
    	this.name = parName;
    	this.setUnlocalizedName("battleclasses:"+"weapons/" + parName);
    	this.setTextureName("battleclasses:"+"weapons/" + parName);
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
        
    public EnumSet<EnumBattleClassesPlayerClass> getClassAccessSet() {
    	return this.classAccessSet;
    }

	@Override
	public float getBaseWeaponDamage() {
		// TODO Auto-generated method stub
		return this.baseWeaponDamage;
	}

	@Override
	public float getBonusReach() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public EnumBattleClassesItemHandRequirement getHandRequirement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnumBattleClassesItemRarity getRarity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sheatheOnBack(ItemStack item) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isOffhandHandDual(ItemStack off) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean offhandAttackEntity(OffhandAttackEvent event,
			ItemStack mainhandItem, ItemStack offhandItem) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean offhandClickAir(PlayerInteractEvent event,
			ItemStack mainhandItem, ItemStack offhandItem) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean offhandClickBlock(PlayerInteractEvent event,
			ItemStack mainhandItem, ItemStack offhandItem) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void performPassiveEffects(Side effectiveSide,
			ItemStack mainhandItem, ItemStack offhandItem) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public boolean allowOffhand(ItemStack mainhand, ItemStack offhand) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean invertOnBack(ItemStack itemStack) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
