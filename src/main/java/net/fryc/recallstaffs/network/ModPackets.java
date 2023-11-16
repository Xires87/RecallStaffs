package net.fryc.recallstaffs.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.network.s2c.ConfigAnswerS2CPacket;
import net.minecraft.util.Identifier;

public class ModPackets {

    public static final Identifier ANSWER_CONFIG_ID = new Identifier(RecallStaffs.MOD_ID, "answer_config_id");

    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(ANSWER_CONFIG_ID, ConfigAnswerS2CPacket::receive);
    }
}
