package net.fryc.recallstaffs.network.s2c;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fryc.recallstaffs.network.payloads.AnswerConfigPayload;
import net.fryc.recallstaffs.util.ConfigHelper;

public class ConfigAnswerS2CPacket {

    public static void receive(AnswerConfigPayload payload, ClientPlayNetworking.Context context){
        ConfigHelper.woodenRecallStaffCooldown = payload.woodCooldown();
        ConfigHelper.copperRecallStaffCooldown = payload.copperCooldown();
        ConfigHelper.ironRecallStaffCooldown = payload.ironCooldown();
        ConfigHelper.goldenRecallStaffCooldown = payload.goldCooldown();
        ConfigHelper.diamondRecallStaffCooldown = payload.diamondCooldown();
        ConfigHelper.netheriteRecallStaffCooldown = payload.netheriteCooldown();
    }
}
