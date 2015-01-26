package mods.battleclasses.client;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.common.registry.GameData;
import mods.battleclasses.BattleClassesCommonProxy;
import mods.battleclasses.client.renderer.BattleClassesHDWeaponRenderer;
import mods.battleclasses.gui.BattleClassesGuiKeyHandler;
import mods.battleclasses.items.BattleClassesItems;
import mods.battleclasses.items.IHighDetailWeapon;
import mods.battlegear2.Battlegear;
import mods.battlegear2.api.core.InventoryPlayerBattle;
import mods.battlegear2.api.heraldry.IHeraldryItem;
import mods.battlegear2.api.shield.IShield;
import mods.battlegear2.client.BattlegearClientEvents;
import mods.battlegear2.client.BattlegearClientTickHandeler;
import mods.battlegear2.client.ClientProxy;
import mods.battlegear2.client.gui.BattlegearGuiKeyHandler;
import mods.battlegear2.client.renderer.BowRenderer;
import mods.battlegear2.client.renderer.FlagPoleItemRenderer;
import mods.battlegear2.client.renderer.FlagPoleTileRenderer;
import mods.battlegear2.client.renderer.HeraldryCrestItemRenderer;
import mods.battlegear2.client.renderer.HeraldryItemRenderer;
import mods.battlegear2.client.renderer.QuiverItremRenderer;
import mods.battlegear2.client.renderer.ShieldRenderer;
import mods.battlegear2.heraldry.TileEntityFlagPole;
import mods.battlegear2.packet.BattlegearAnimationPacket;
import mods.battlegear2.packet.SpecialActionPacket;
import mods.battlegear2.utils.BattlegearConfig;
import mods.battlegear2.utils.EnumBGAnimations;

public class BattleClassesClientProxy extends BattleClassesCommonProxy {

    @Override
    public void registerKeyHandelers() {
    	super.registerKeyHandelers();
    	FMLCommonHandler.instance().bus().register(new BattleClassesGuiKeyHandler());
    }

    @Override
    public void registerTickHandelers() {
    	super.registerTickHandelers();
        MinecraftForge.EVENT_BUS.register(new BattleClassesClientEvents());
    	/*
        super.registerTickHandelers();
        MinecraftForge.EVENT_BUS.register(new BattleClassesClientEvents());
        FMLCommonHandler.instance().bus().register(new BattlegearClientTickHandeler());
        */
    }
    
    @Override
    public void registerItemRenderers() {
    	super.registerItemRenderers();
    	//Registering HD (32x32) sized weapon renderers
    	BattleClassesHDWeaponRenderer HD_WeaponRenderer =  new BattleClassesHDWeaponRenderer();
        for(IHighDetailWeapon HD_Weapon : BattleClassesItems.TwoHandedWeaponList){
        	if(HD_Weapon!=null) {
        		MinecraftForgeClient.registerItemRenderer((Item)HD_Weapon, HD_WeaponRenderer);
        	}
        }
    }

}
