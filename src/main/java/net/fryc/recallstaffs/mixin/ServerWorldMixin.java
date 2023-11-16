package net.fryc.recallstaffs.mixin;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.network.ModPackets;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
abstract class ServerWorldMixin {

    @Inject(method = "onPlayerConnected(Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("TAIL"))
    private void sendConfigToClient(ServerPlayerEntity player, CallbackInfo info) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(RecallStaffs.config.woodenRecallStaffCooldown);
        buf.writeInt(RecallStaffs.config.copperRecallStaffCooldown);
        buf.writeInt(RecallStaffs.config.ironRecallStaffCooldown);
        buf.writeInt(RecallStaffs.config.goldenRecallStaffCooldown);
        buf.writeInt(RecallStaffs.config.diamondRecallStaffCooldown);
        buf.writeInt(RecallStaffs.config.netheriteRecallStaffCooldown);

        buf.writeInt(RecallStaffs.config.woodenRecallCost);
        buf.writeInt(RecallStaffs.config.copperRecallCost);
        buf.writeInt(RecallStaffs.config.ironRecallCost);
        buf.writeInt(RecallStaffs.config.goldenRecallCost);
        buf.writeInt(RecallStaffs.config.diamondRecallCost);
        buf.writeInt(RecallStaffs.config.netheriteRecallCost);

        ServerPlayNetworking.send(player, ModPackets.ANSWER_CONFIG_ID, buf); // <--- informs client about server's config to avoid visual bugs
    }
}
