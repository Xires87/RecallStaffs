package net.fryc.recallstaffs.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.network.payloads.AnswerConfigPayload;
import net.fryc.recallstaffs.network.payloads.SecondAnswerConfigPayload;
import net.fryc.recallstaffs.network.payloads.ThirdAnswerConfigPayload;
import net.fryc.recallstaffs.network.s2c.ConfigAnswerS2CPacket;
import net.fryc.recallstaffs.network.s2c.SecondConfigAnswerS2CPacket;
import net.fryc.recallstaffs.network.s2c.ThirdConfigAnswerS2CPacket;
import net.minecraft.util.Identifier;

public class ModPackets {

    public static final Identifier ANSWER_CONFIG_ID = Identifier.of(RecallStaffs.MOD_ID, "answer_config_id");
    public static final Identifier SECOND_ANSWER_CONFIG_ID = Identifier.of(RecallStaffs.MOD_ID, "second_answer_config_id");
    public static final Identifier THIRD_ANSWER_CONFIG_ID = Identifier.of(RecallStaffs.MOD_ID, "third_answer_config_id");

    public static void registerPayloads(){
        PayloadTypeRegistry.playS2C().register(AnswerConfigPayload.ID, AnswerConfigPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SecondAnswerConfigPayload.ID, SecondAnswerConfigPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ThirdAnswerConfigPayload.ID, ThirdAnswerConfigPayload.CODEC);
    }
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(AnswerConfigPayload.ID, ConfigAnswerS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(SecondAnswerConfigPayload.ID, SecondConfigAnswerS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ThirdAnswerConfigPayload.ID, ThirdConfigAnswerS2CPacket::receive);
    }
}
