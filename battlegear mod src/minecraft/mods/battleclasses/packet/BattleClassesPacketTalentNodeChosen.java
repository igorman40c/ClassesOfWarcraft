package mods.battleclasses.packet;

import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractTalent;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class BattleClassesPacketTalentNodeChosen  extends AbstractMBPacket {

	public static final String packetName = "BC|TalentNodeChosen";
	private int talentID = -1 ;
	private String username;
	
	public static final int TALENT_TREE_BUTTON_ID_OFFSET = 10000;
	public static final int RESET_TALENTS_ID = 0;
	
	public BattleClassesPacketTalentNodeChosen(EntityPlayer user, int parTalentID) {
    	this.talentID = parTalentID;
    	this.username = user.getCommandSenderName();
    }

    public BattleClassesPacketTalentNodeChosen() {
    	
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		out.writeInt(talentID);
        ByteBufUtils.writeUTF8String(out, username);
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		talentID = in.readInt();
        username = ByteBufUtils.readUTF8String(in);
        if (username != null && talentID != -1) {
            EntityPlayer entityPlayer = player.worldObj.getPlayerEntityByName(username);
            if(entityPlayer!=null){
            	BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(entityPlayer);
            	if(talentID == RESET_TALENTS_ID) {
            		playerHooks.playerClass.talentMatrix.resetTalentPoints();
            	}
            	else if(talentID >= TALENT_TREE_BUTTON_ID_OFFSET) {
            		int treeIndex = talentID - TALENT_TREE_BUTTON_ID_OFFSET;
            		playerHooks.playerClass.talentMatrix.learnFullTreeAtIndex(treeIndex);
            	}
            	else {
            		BattleClassesAbstractTalent talentAbility = playerHooks.playerClass.talentMatrix.talentHashMap.get(talentID);
            		playerHooks.playerClass.talentMatrix.learnTalent(talentAbility);
            	}
            	
            	//Talent sync to player
            	FMLProxyPacket p = new BattleClassesPacketTalentSync(entityPlayer).generatePacket();
            	if(entityPlayer instanceof EntityPlayerMP) {
            		EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entityPlayer;
            		Battlegear.packetHandler.sendPacketToPlayer(p, entityPlayerMP);
            	}
            }
        }
	}

}