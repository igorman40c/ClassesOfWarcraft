package mods.battleclasses.gui.tab;

import java.util.Collection;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import mods.battleclasses.client.BattleClassesClientEvents;
import mods.battlegear2.client.ClientProxy;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BattleClassesGuiInventory extends GuiInventory {

	public BattleClassesGuiInventory(EntityPlayer par1EntityPlayer) {
		super(par1EntityPlayer);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public void initGui() {
        super.initGui();
        BattleClassesClientEvents.onOpenGui(this.buttonList, guiLeft-28, guiTop);
        /*
        if(ClientProxy.tconstructEnabled){
            this.buttonList.clear();
            try{
                if(equipTab==null){
                    equipTab = Class.forName("mods.battlegear2.client.gui.controls.EquipGearTab");
                }
                ClientProxy.updateTab.invoke(null, guiLeft, guiTop, equipTab);
                ClientProxy.addTabs.invoke(null, this.buttonList);
            }catch(Exception e){
                ClientProxy.tconstructEnabled = false;
            }
        }
        */
		//this.setTitlePosition(this.guiLeft + DEFAULT_GUI_WIDTH/2, this.guiTop + 8);
    }
    
    private void func_147044_g()
    {
    	System.out.println("asd");
        int i = this.guiLeft - 140;
        int j = this.guiTop;
        boolean flag = true;
        Collection collection = this.mc.thePlayer.getActivePotionEffects();

        if (!collection.isEmpty())
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            int k = 33;

            if (collection.size() > 5)
            {
                k = 132 / (collection.size() - 1);
            }

            for (Iterator iterator = this.mc.thePlayer.getActivePotionEffects().iterator(); iterator.hasNext(); j += k)
            {
                PotionEffect potioneffect = (PotionEffect)iterator.next();
                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.mc.getTextureManager().bindTexture(field_147001_a);
                this.drawTexturedModalRect(i, j, 0, 166, 140, 32);

                if (potion.hasStatusIcon())
                {
                    int l = potion.getStatusIconIndex();
                    this.drawTexturedModalRect(i + 6, j + 7, 0 + l % 8 * 18, 198 + l / 8 * 18, 18, 18);
                }

                String s1 = I18n.format(potion.getName(), new Object[0]);

                if (potioneffect.getAmplifier() == 1)
                {
                    s1 = s1 + " II";
                }
                else if (potioneffect.getAmplifier() == 2)
                {
                    s1 = s1 + " III";
                }
                else if (potioneffect.getAmplifier() == 3)
                {
                    s1 = s1 + " IV";
                }

                this.fontRendererObj.drawStringWithShadow(s1, i + 10 + 18, j + 6, 16777215);
                String s = Potion.getDurationString(potioneffect);
                this.fontRendererObj.drawStringWithShadow(s, i + 10 + 18, j + 6 + 10, 8355711);
            }
        }

    }


}