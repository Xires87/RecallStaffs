package net.fryc.recallstaffs.network.s2c;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fryc.recallstaffs.network.payloads.SecondAnswerConfigPayload;
import net.fryc.recallstaffs.util.ConfigHelper;

public class SecondConfigAnswerS2CPacket {

    public static void receive(SecondAnswerConfigPayload payload, ClientPlayNetworking.Context context){
        ConfigHelper.woodenRecallCost = payload.woodCost();
        ConfigHelper.copperRecallCost = payload.copperCost();
        ConfigHelper.ironRecallCost = payload.ironCost();
        ConfigHelper.goldenRecallCost = payload.goldCost();
        ConfigHelper.diamondRecallCost = payload.diamondCost();
        ConfigHelper.netheriteRecallCost = payload.netheriteCost();
    }
}
