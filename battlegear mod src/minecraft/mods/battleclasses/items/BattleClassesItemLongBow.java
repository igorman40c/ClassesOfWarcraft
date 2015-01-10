package mods.battleclasses.items;

import java.util.ArrayList;

import mods.battleclasses.BattleClassesUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BattleClassesItemLongBow extends BattleClassesItemWeaponTwoHanded implements IBattleClassesBow {

	public int pullFramesToRegister = 0; 
	public ArrayList<IIcon> pullIcons = new ArrayList<IIcon>();
	public static final String PULLSTRING = "_pull_";
	
	public BattleClassesItemLongBow(ToolMaterial toolMaterial, String textureName, int pullFramesCount) {
		super(toolMaterial, textureName);
		this.pullFramesToRegister = pullFramesCount;
		
		this.inverSheat = false;
	}
	
	public float anchorX = 0.44F;
	public float anchorY = 1F - anchorX;

	
	@Override
	public float getRelativeAnchorPointX() {
		// TODO Auto-generated method stub
		return anchorX;
	}

	@Override
	public float getRelativeAnchorPointY() {
		// TODO Auto-generated method stub
		return anchorY;
	}
	
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
    	super.registerIcons(iconRegister);
    	pullIcons.clear();
        for (int i = 0; i < pullFramesToRegister; ++i)
        {
        	pullIcons.add( iconRegister.registerIcon(this.getIconString() + PULLSTRING + i) );
        }
    }

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getItemIconForUse(EntityLivingBase entityLivingBase, ItemStack itemStack) {
		float usePercentage = this.getInUseRatio(entityLivingBase, itemStack);
		if(usePercentage == 0) {
			return this.itemIcon;
		}
		int iconIndex = (int) ((pullIcons.size())*usePercentage);
		iconIndex = (iconIndex <= pullIcons.size()-1) ? iconIndex : pullIcons.size()-1;
		//System.out.println("F: " + usePercentage + " | Index: " + iconIndex);
		return this.pullIcons.get(iconIndex);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getInUseRatio(EntityLivingBase entityLivingBase, ItemStack itemStack) {
		Minecraft mc = Minecraft.getMinecraft();
		if(entityLivingBase instanceof EntityPlayer) {
			EntityPlayer entityPlayer = (EntityPlayer)entityLivingBase;
			if(entityPlayer.getItemInUse() == null) {
				return 0;
			}
			if(entityPlayer == mc.thePlayer) {
				return BattleClassesUtils.getCastPercentage(mc.thePlayer);
			}
			else {
				//Trying to guess useRatio using magic numbers
				float useRatio = 20F /(float)(entityPlayer.getItemInUseCount()-72000);
				useRatio = (useRatio > 0) ? useRatio : 0; 
				return useRatio;
			}
		}
		else {
			return 0;
		}
	}

}
