package mods.battleclasses.items.weapons;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IBattleClassesBow {
	@SideOnly(Side.CLIENT)
    public IIcon getItemIconForUse(EntityLivingBase entityLivingBase, ItemStack itemStack);
	@SideOnly(Side.CLIENT)
	public float getInUseRatio(EntityLivingBase entityLivingBase, ItemStack itemStack);
}
