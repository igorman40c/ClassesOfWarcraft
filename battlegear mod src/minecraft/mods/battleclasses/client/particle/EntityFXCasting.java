package mods.battleclasses.client.particle;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityFXCasting extends EntityFX {

	public EntityFXCasting(World p_i1219_1_, double p_i1219_2_,
			double p_i1219_4_, double p_i1219_6_, double p_i1219_8_,
			double p_i1219_10_, double p_i1219_12_) {
		super(p_i1219_1_, p_i1219_2_, p_i1219_4_, p_i1219_6_, p_i1219_8_, p_i1219_10_,
				p_i1219_12_);
		// TODO Auto-generated constructor stub
	}

	
	/*
	public EntityFXCasting(World world, EntityPlayer entityPlayer) {
		super();
		// TODO Auto-generated constructor stub
	}
	*/
	
	private static Random rand = new Random();
		
	public static void spawnParticleFXCasting(EntityPlayer entityPlayer) {
		
		Vec3 lookVector = entityPlayer.getLookVec();
		double posX = entityPlayer.posX + lookVector.xCoord;
		double posY = entityPlayer.posY + lookVector.yCoord;
		double posZ = entityPlayer.posZ + lookVector.zCoord;
		
		double motionX = rand.nextGaussian() * 0.02D;
	    double motionY = rand.nextGaussian() * 0.02D;
	    double motionZ = rand.nextGaussian() * 0.02D;
	    entityPlayer.worldObj.spawnParticle(
	          "smoke", 
	          posX + rand.nextFloat() * entityPlayer.width * 2.0F - entityPlayer.width, 
	          posY + 0.5D + rand.nextFloat() * entityPlayer.height, 
	          posZ + rand.nextFloat() * entityPlayer.width * 2.0F - entityPlayer.width, 
	          motionX, 
	          motionY, 
	          motionZ);
		
	}
	
	public static void spawnParticleFXCastingArcane(EntityPlayer entityPlayer) {
		spawnParticleFXCasting(entityPlayer);
	}
	
	public static void spawnParticleFXCastingFire(EntityPlayer entityPlayer) {
		spawnParticleFXCasting(entityPlayer);
	}
	
	public static void spawnParticleFXCastingFrost(EntityPlayer entityPlayer) {
		spawnParticleFXCasting(entityPlayer);
	}
	
	public static void spawnParticleFXCastingHoly(EntityPlayer entityPlayer) {
		spawnParticleFXCasting(entityPlayer);
	}
	
	public static void spawnParticleFXCastingShadow(EntityPlayer entityPlayer) {
		spawnParticleFXCasting(entityPlayer);
	}
	
	public static void spawnParticleFXRelease(EntityPlayer entityPlayer) {
		for(int i = 0; i < 10; ++i) {
			Vec3 lookVector = entityPlayer.getLookVec();
			double posX = entityPlayer.posX + lookVector.xCoord;
			double posY = entityPlayer.posY + lookVector.yCoord;
			double posZ = entityPlayer.posZ + lookVector.zCoord;
			
			double motionX = rand.nextGaussian() * 0.02D;
		    double motionY = rand.nextGaussian() * 0.02D;
		    double motionZ = rand.nextGaussian() * 0.02D;
		    entityPlayer.worldObj.spawnParticle(
		          "lava", 
		          posX + rand.nextFloat() * entityPlayer.width * 2.0F - entityPlayer.width, 
		          posY + 0.5D + rand.nextFloat() * entityPlayer.height, 
		          posZ + rand.nextFloat() * entityPlayer.width * 2.0F - entityPlayer.width, 
		          motionX, 
		          motionY, 
		          motionZ);
		}
	}
	
	public static void spawnParticleFXReleaseArcane(EntityPlayer entityPlayer) {
		spawnParticleFXRelease(entityPlayer);
	}
	
	public static void spawnParticleFXReleaseFire(EntityPlayer entityPlayer) {
		spawnParticleFXRelease(entityPlayer);
	}
	
	public static void spawnParticleFXReleaseFrost(EntityPlayer entityPlayer) {
		spawnParticleFXRelease(entityPlayer);
	}
	
	public static void spawnParticleFXReleaseHoly(EntityPlayer entityPlayer) {
		spawnParticleFXRelease(entityPlayer);
	}
	
	public static void spawnParticleFXReleaseShadow(EntityPlayer entityPlayer) {
		spawnParticleFXRelease(entityPlayer);
	}
}
