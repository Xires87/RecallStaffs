package net.fryc.recallstaffs.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fryc.recallstaffs.items.custom.StaffItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin implements AutoCloseable {

    @Shadow
    @Final MinecraftClient client;


    @ModifyExpressionValue(
            method = "render(FJZ)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z")
    )
    private boolean renderNauseaEffectWhenUsingStaff(boolean original) {
        // the place I'm injecting into is under 'this.client.player != null' check
        return original || shouldRenderNauseaEffect(this.client.player);
    }

    @ModifyExpressionValue(
            method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z")
    )
    private boolean setNauseaStrengthWhenUsingStaff(boolean original) {
        if(this.client.player == null){
            return original;
        }
        return original || shouldRenderNauseaEffect(this.client.player);
    }

    private static boolean shouldRenderNauseaEffect(PlayerEntity player){
        return player.isUsingItem() && player.getActiveItem().getItem() instanceof StaffItem;
    }

}
