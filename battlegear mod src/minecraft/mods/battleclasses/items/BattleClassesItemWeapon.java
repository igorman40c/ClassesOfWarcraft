package mods.battleclasses.items;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesMetaConfig.AttributeConfig;
import mods.battleclasses.attributes.AttributesFactory;
import mods.battleclasses.attributes.BattleClassesAttributeModifierBonus;
import mods.battleclasses.attributes.AttributesFactory.WeaponDamageCreationMode;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesItemRarity;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.enums.EnumBattleClassesHandHeldType;
import mods.battleclasses.enums.EnumBattleClassesWieldAccess;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import mods.battlegear2.api.PlayerEventChild.OffhandAttackEvent;
import mods.battlegear2.api.core.IBattlePlayer;
import mods.battlegear2.api.weapons.WeaponRegistry;
import mods.battlegear2.utils.BattlegearConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.StatCollector;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class BattleClassesItemWeapon extends BattleClassesItemHandheld implements IBattleClassesWeapon {
	
//	protected final ToolMaterial material;
	protected String name;
	protected float weaponSpeed = 0;
	
    public BattleClassesItemWeapon() {
    	super();
	}
    
    public String getTextureFolderName() {
		return "weapons";
	}
    
    protected void setName(String parName) {
    	this.name = parName;
    	this.setUnlocalizedName("battleclasses:"+"weapons/" + parName);
    	this.setTextureName("battleclasses:"+"weapons/" + parName);
    }
    
    public BattleClassesItemWeapon setItemLevelAndHeldType(int itemLevel, EnumBattleClassesHandHeldType handHeldType, float weaponSpeed) {
    	super.setItemLevelAndHeldType(itemLevel, handHeldType);
		this.weaponSpeed = weaponSpeed;
		return this;
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
    	BattleClassesUtils.getPlayerSpellBook(entityPlayer).UseStartOnChosenAbility(itemStack, world, entityPlayer);
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
	public boolean isUsedOverAttack(ItemStack itemStack) {
		return true;
	}
    
	@Override
	public float getWeaponDamage() {
		return this.storedAttributes.getValueByType(EnumBattleClassesAttributeType.MELEE_ATTACK_DAMAGE);
	}

	@Override
	public float getBonusReach() {
		return (this.handHeldType == EnumBattleClassesHandHeldType.TWO_HANDED) ? 0F : 0;
	}
	
	@Override
	public float getSpeedInSeconds() {
		return this.weaponSpeed;
	}
	
	public void setWeaponSpeed(float weaponSpeed) {
		this.weaponSpeed = weaponSpeed;
	}
	
	@Override
	public List<String> getTooltipText() {
		List<String> text = super.getTooltipText();
		text.add(StatCollector.translateToLocal("bcattribute.weaponSpeed") + ": " + 
				BattleClassesGuiHelper.formatFloatToNice(this.getSpeedInSeconds()) + " " + 
				StatCollector.translateToLocal("bcattribute.weaponSpeed.s"));
		
		float weaponDamage = this.getWeaponDamage();
		if(weaponDamage > 0) {
			text.add(BattleClassesGuiHelper.getTranslatedBonusLine(weaponDamage, EnumBattleClassesAttributeType.MELEE_ATTACK_DAMAGE));
		}
		float bonusReach = this.getBonusReach();
		if(bonusReach > 0) {
			text.add(BattleClassesGuiHelper.getTranslatedBonusLine(bonusReach, StatCollector.translateToLocal("bcattribute.weaponReach"), false));
		}
				
		return text;
	}
	
	@Override
	public Set<EnumBattleClassesAttributeType> getActiveTypeExceptions() {
		Set<EnumBattleClassesAttributeType> exceptions = new HashSet<EnumBattleClassesAttributeType>();
		exceptions.add(EnumBattleClassesAttributeType.MELEE_ATTACK_DAMAGE);
		return exceptions;
	}
	
	
	/**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
	@Override
    public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = HashMultimap.create();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", this.getWeaponDamage(), 0));
        return multimap;
    }
	
	/*
	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack itemStack)
    {
		if(entityLiving instanceof EntityPlayer && entityLiving instanceof IBattlePlayer) {
			EntityPlayer entityPlayer = (EntityPlayer)entityLiving;
			if(itemStack != null && itemStack.getItem() instanceof IControlledSpeedWeapon) {
				return BattleClassesUtils.getPlayerWeaponHandler(entityPlayer).processAttack(itemStack);
			}
		}
		
    	return false;
    }
    */
}
