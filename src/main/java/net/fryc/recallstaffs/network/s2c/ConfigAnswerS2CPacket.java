package net.fryc.recallstaffs.network.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;

import net.fryc.recallstaffs.config.ConfigHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class ConfigAnswerS2CPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
        ConfigHelper.woodenRecallStaffCooldown = buf.readInt();
        ConfigHelper.copperRecallStaffCooldown = buf.readInt();
        ConfigHelper.ironRecallStaffCooldown = buf.readInt();
        ConfigHelper.goldenRecallStaffCooldown = buf.readInt();
        ConfigHelper.diamondRecallStaffCooldown = buf.readInt();
        ConfigHelper.netheriteRecallStaffCooldown = buf.readInt();

        ConfigHelper.woodenRecallCost = buf.readInt();
        ConfigHelper.copperRecallCost = buf.readInt();
        ConfigHelper.ironRecallCost = buf.readInt();
        ConfigHelper.goldenRecallCost = buf.readInt();
        ConfigHelper.diamondRecallCost = buf.readInt();
        ConfigHelper.netheriteRecallCost = buf.readInt();
    }
}
