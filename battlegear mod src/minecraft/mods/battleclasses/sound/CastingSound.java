package mods.battleclasses.sound;

import java.util.HashMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.BattleClassesMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class CastingSound extends PositionedSound {

	public static HashMap<String, CastingSound> castingEffectMap = new HashMap<String, CastingSound>();
	
	public static void startCastingSound(String playerName, String resource) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc!= null && mc.thePlayer != null) {
			//Stopping & removing casting sound if already registered
			stopCastingSound(playerName);
			EntityPlayer entityPlayerEmitting = mc.thePlayer.worldObj.getPlayerEntityByName(playerName);
			if(entityPlayerEmitting != null) {
				CastingSound castingSound = new CastingSound(new ResourceLocation(BattleClassesMod.MODID, resource), entityPlayerEmitting);
				mc.getSoundHandler().playSound(castingSound);
				castingEffectMap.put(playerName, castingSound);
			}
		}
	}
	
	public static void stopCastingSound(String playerName) {
		Minecraft mc = Minecraft.getMinecraft();
		if(castingEffectMap.containsKey(playerName)) {
			mc.getSoundHandler().stopSound(castingEffectMap.get(playerName));
			castingEffectMap.remove(playerName);
		}
	}
	
	protected EntityPlayer playerEmitting;
	protected CastingSound(ResourceLocation soundResourceLocation, EntityPlayer player) {
		super(soundResourceLocation);
		playerEmitting = player;
		this.repeat = true;
	}
	
    public float getXPosF()
    {
        return (float) playerEmitting.posX;
    }

    public float getYPosF()
    {
        return (float) playerEmitting.posY;
    }

    public float getZPosF()
    {
        return (float) playerEmitting.posZ;
    }
	
}
