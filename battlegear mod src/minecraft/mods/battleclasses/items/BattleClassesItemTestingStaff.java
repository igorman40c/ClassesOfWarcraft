package mods.battleclasses.items;

import java.util.EnumSet;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battlegear2.utils.BattlegearConfig;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

public class BattleClassesItemTestingStaff extends BattleClassesItemWeapon {
	
	public BattleClassesItemTestingStaff(ToolMaterial toolMaterial) {
		super(toolMaterial);
		// TODO Auto-generated constructor stub
		
        this.setUnlocalizedName("battleclasses:"+"weapons/WoodenStaff");
		this.setTextureName("battleclasses:"+"weapons/WoodenStaff");
		classAccessSet = EnumSet.of(
				EnumBattleClassesPlayerClass.MAGE,
				EnumBattleClassesPlayerClass.PRIEST,
				EnumBattleClassesPlayerClass.WARLOCK,
				EnumBattleClassesPlayerClass.ROGUE,
				EnumBattleClassesPlayerClass.HUNTER,
				EnumBattleClassesPlayerClass.PALADIN,
				EnumBattleClassesPlayerClass.WARRIOR);
	}
	
	
}
