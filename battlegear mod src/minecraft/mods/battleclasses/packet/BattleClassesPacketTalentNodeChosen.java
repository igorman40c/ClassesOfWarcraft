package mods.battleclasses.packet;

import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.talent.BattleClassesAbstractTalent;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class BattleClassesPacketTalentNodeChosen extends AbstractMBPacket {

	public static final String packetName = "BC|TalentNodeChosen";
	public static final String talentNullID = "talentNullID";
	
	private String talentID = talentNullID;
	private String username;
	
	public static final String TALENT_TREE_BUTTON_ID_OFFSET = "learnTree:";
	public static final String RESET_TALENTS_ID = "resetTalents";
	
	public BattleClassesPacketTalentNodeChosen(EntityPlayer user, String parTalentID) {
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
		ByteBufUtils.writeUTF8String(out, talentID);
        ByteBufUtils.writeUTF8String(out, username);
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		talentID = ByteBufUtils.readUTF8String(in);
        username = ByteBufUtils.readUTF8String(in);
        if (username != null && talentID != null && !talentID.equals(talentNullID))  {
            EntityPlayer entityPlayer = player.worldObj.getPlayerEntityByName(username);
            if(entityPlayer!=null){
            	BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(entityPlayer);
            	if(talentID.equals(RESET_TALENTS_ID)) {
            		playerHooks.playerClass.talentMatrix.resetTalentPoints();
            	}
            	else if(TALENT_TREE_BUTTON_ID_OFFSET.equals(talentID.substring(0,talentID.length()-1))) {
            		int treeIndex = Integer.parseInt(talentID.substring(talentID.length()-1));
            		//playerHooks.playerClass.talentMatrix.learnFullTreeAtIndex(treeIndex);
            		playerHooks.playerClass.talentMatrix.talentTrees.get(treeIndex).spendTalentPoints(playerHooks.playerClass.talentMatrix.getUnspentTalentPoints());
            	}
            	else {
            		BattleClassesAbstractTalent talentAbility = playerHooks.playerClass.talentMatrix.talentHashMap.get(talentID);
            		playerHooks.playerClass.talentMatrix.learnTalent(talentAbility);
            	}
            	
            	//Talent sync to player
            	FMLProxyPacket p = new BattleClassesPacketTalentSync(entityPlayer).generatePacket();
            	if(entityPlayer instanceof EntityPlayerMP) {
            		EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entityPlayer;
            		BattleClassesMod.packetHandler.sendPacketToPlayer(p, entityPlayerMP);
            	}
            }
        }
	}

}