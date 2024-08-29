package net.fryc.recallstaffs.network.s2c;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fryc.recallstaffs.network.payloads.ThirdAnswerConfigPayload;
import net.fryc.recallstaffs.util.ConfigHelper;

public class ThirdConfigAnswerS2CPacket {

    public static void receive(ThirdAnswerConfigPayload payload, ClientPlayNetworking.Context context){
        ConfigHelper.recallStaffCraftCost = payload.recallStaffCraftCost();
    }
}
