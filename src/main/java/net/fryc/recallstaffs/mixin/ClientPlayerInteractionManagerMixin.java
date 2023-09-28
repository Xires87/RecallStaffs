package net.fryc.recallstaffs.mixin;

import net.fryc.recallstaffs.effects.ModEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
abstract class ClientPlayerInteractionManagerMixin {

    @Shadow
    private @Final MinecraftClient client;

    // disables all interactions when player has Call of Being (INVULNERABILITY_EFFECT)

    @Inject(method = "interactItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    private void preventUsingItemsWhenInvulnerable(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ret) {
        if(player.hasStatusEffect(ModEffects.INVULNERABILITY_EFFECT)){
            ret.setReturnValue(ActionResult.FAIL);
        }
    }


    @Inject(method = "attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"), cancellable = true)
    private void preventMiningWhenInvulnerable(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> ret) {
        if(this.client.player != null){
            if(this.client.player.hasStatusEffect(ModEffects.INVULNERABILITY_EFFECT)){
                ret.setReturnValue(false);
            }
        }
    }


    @Inject(method = "interactBlockInternal(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    private void preventUsingBlocksInternalWhenInvulnerable(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> ret) {
        if(player.hasStatusEffect(ModEffects.INVULNERABILITY_EFFECT)){
            ret.setReturnValue(ActionResult.FAIL);
        }
    }


    @Inject(method = "interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    private void preventUsingBlocksWhenInvulnerable(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> ret) {
        if(player.hasStatusEffect(ModEffects.INVULNERABILITY_EFFECT)){
            ret.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(method = "interactEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    private void preventInteractingWithEntitiesWhenInvulnerable(PlayerEntity player, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> ret) {
        if(player.hasStatusEffect(ModEffects.INVULNERABILITY_EFFECT)){
            ret.setReturnValue(ActionResult.FAIL);
        }
    }
}
