package mods.battleclasses.packet;

import java.util.HashMap;
import java.util.Iterator;

import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.talent.BattleClassesAbstractTalent;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;

public class BattleClassesPacketTalentSync extends AbstractMBPacket {

	public static final String packetName = "BC|TalentSync";

	private String username;
	private EntityPlayer savedEntityPlayer;
	private int talentPointsLeft = -1;
	//Key - talentID, Value - talentState
	private HashMap<String, Integer> talentStateMap;
	
	public BattleClassesPacketTalentSync(EntityPlayer user) {
    	this.username = user.getCommandSenderName();
    	savedEntityPlayer = user;
    	talentPointsLeft = BattleClassesUtils.getPlayerTalentMatrix(user).getUnspentTalentPoints();
    }

    public BattleClassesPacketTalentSync() {
    	
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		ByteBufUtils.writeUTF8String(out, username);
		out.writeInt(talentPointsLeft);
		if(savedEntityPlayer != null) {
			BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(savedEntityPlayer);
			out.writeInt(playerHooks.playerClass.talentMatrix.talentHashMap.size());
			for (String keyTalentID : playerHooks.playerClass.talentMatrix.talentHashMap.keySet()) {
        		BattleClassesAbstractTalent talentAbility = playerHooks.playerClass.talentMatrix.talentHashMap.get(keyTalentID);
        		ByteBufUtils.writeUTF8String(out, keyTalentID);
        		out.writeInt(talentAbility.getCurrentState());
        	}
		}

	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);

		username = ByteBufUtils.readUTF8String(in);
		talentPointsLeft = in.readInt();
		int mapSize = in.readInt();
		talentStateMap = new HashMap<String, Integer>();
		for(int i = 0; i < mapSize; ++i) {
			String key = ByteBufUtils.readUTF8String(in);
			int value = in.readInt();
			talentStateMap.put(key, value);
		}
        
        if (username != null && talentStateMap != null) {
            EntityPlayer entityPlayer = player.worldObj.getPlayerEntityByName(username);
            if(entityPlayer!=null){
            	BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(entityPlayer);
            	playerHooks.playerClass.talentMatrix.setUnspentTalentPoints(talentPointsLeft);
            	for (String keyTalentID : talentStateMap.keySet()) {
            		BattleClassesAbstractTalent talentAbility = playerHooks.playerClass.talentMatrix.talentHashMap.get(keyTalentID);
            		if(talentAbility != null) {
            			talentAbility.setCurrentState(talentStateMap.get(keyTalentID));
            		}
            		else {
            			BattleClassesUtils.Log("FATAL ERROR while trying to apply synchronized talentStateMap on client! TalentAbility was null for key:" + keyTalentID, LogType.PACKET);
            		}
            	}
            }
        }
	}

}