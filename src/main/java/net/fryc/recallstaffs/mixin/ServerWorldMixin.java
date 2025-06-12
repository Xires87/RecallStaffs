package net.fryc.recallstaffs.mixin;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.network.payloads.AnswerConfigPayload;
import net.fryc.recallstaffs.network.payloads.SecondAnswerConfigPayload;
import net.fryc.recallstaffs.network.payloads.ThirdAnswerConfigPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
abstract class ServerWorldMixin {

    //informs client about server's config to avoid visual bugs
    @Inject(method = "onPlayerConnected(Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("TAIL"))
    private void sendConfigToClient(ServerPlayerEntity player, CallbackInfo info) {
        ServerPlayNetworking.send(player, new AnswerConfigPayload(
                RecallStaffs.config.woodenRecallStaffCooldown,
                RecallStaffs.config.copperRecallStaffCooldown,
                RecallStaffs.config.ironRecallStaffCooldown,
                RecallStaffs.config.goldenRecallStaffCooldown,
                RecallStaffs.config.diamondRecallStaffCooldown,
                RecallStaffs.config.netheriteRecallStaffCooldown
        ));
        ServerPlayNetworking.send(player, new SecondAnswerConfigPayload(
                RecallStaffs.config.woodenRecallCost,
                RecallStaffs.config.copperRecallCost,
                RecallStaffs.config.ironRecallCost,
                RecallStaffs.config.goldenRecallCost,
                RecallStaffs.config.diamondRecallCost,
                RecallStaffs.config.netheriteRecallCost
        ));
        ServerPlayNetworking.send(player, new ThirdAnswerConfigPayload(
                RecallStaffs.config.recallStaffCraftCost,
                RecallStaffs.config.calibratedStaffAdditionalCost
        ));
    }
}
