package net.fryc.recallstaffs.blocks.custom;

import net.fryc.recallstaffs.effects.ModEffects;
import net.fryc.recallstaffs.util.ServerPlayerGetters;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RecoveryAltarBlock extends Block {

    public static final int NO_CHARGES = 0;
    public static final int MAX_CHARGES = 5;
    public static final IntProperty RECOVERY_CHARGES;
    public static final BooleanProperty RECOVERY_READY;

    public RecoveryAltarBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(RECOVERY_CHARGES, 0));
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(RECOVERY_READY, false));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{RECOVERY_CHARGES});
        builder.add(new Property[]{RECOVERY_READY});
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if(state.get(RECOVERY_CHARGES) < MAX_CHARGES){
            if(player.getAbilities().creativeMode){
                charge(player, world, pos, state);
                return ActionResult.success(world.isClient);
            }
            else if(player.experienceLevel > 0){
                player.experienceLevel--;
                charge(player, world, pos, state);
                return ActionResult.success(world.isClient);
            }
            return ActionResult.FAIL;
        }
        else if(!state.get(RECOVERY_READY)){
            if(hand == Hand.MAIN_HAND && !isChargeItem(itemStack) && isChargeItem(player.getStackInHand(Hand.OFF_HAND))){
                return ActionResult.PASS;
            }
            else if(isChargeItem(itemStack)){
                setReady(player, world, pos, state);
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
                return ActionResult.success(world.isClient);
            }

            return ActionResult.FAIL;
        }
        else {
            if(!world.isClient()){
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;

                Optional<GlobalPos> optional = serverPlayerEntity.getLastDeathPos();
                if(optional.isPresent()){
                    if(serverPlayerEntity.getServer() != null){

                        player.addStatusEffect(new StatusEffectInstance(ModEffects.INVULNERABILITY_EFFECT, 600, 0, false, false, false));

                        // saving players current position (before teleporting)
                        ((ServerPlayerGetters) serverPlayerEntity).setPositionBeforeUsingRecoveryAltar(GlobalPos.create(serverPlayerEntity.getWorld().getRegistryKey(),
                                serverPlayerEntity.getBlockPos()));

                        // teleporting player to last death position
                        serverPlayerEntity.teleport(serverPlayerEntity.getServer().getWorld(optional.get().getDimension()),
                                optional.get().getPos().getX(), optional.get().getPos().getY(), optional.get().getPos().getZ(),
                                serverPlayerEntity.getYaw(), serverPlayerEntity.getPitch());

                        // removing all charges from recovery altar
                        removeCharges(serverPlayerEntity, world, pos, state);
                    }

                }
                return ActionResult.success(false);
            }
            return ActionResult.CONSUME;
        }
    }

    public static void charge(@Nullable Entity charger, World world, BlockPos pos, BlockState state) {
        BlockState blockState = (BlockState)state.with(RECOVERY_CHARGES, (Integer)state.get(RECOVERY_CHARGES) + 1);
        world.setBlockState(pos, blockState, 3);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(charger, blockState));
        world.playSound((PlayerEntity)null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public static void setReady(@Nullable Entity charger, World world, BlockPos pos, BlockState state) {
        BlockState blockState = (BlockState)state.with(RECOVERY_READY, true);
        world.setBlockState(pos, blockState, 3);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(charger, blockState));
        world.playSound((PlayerEntity)null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 1.0F, 0.5F);
    }

    public static void removeCharges(@Nullable Entity charger, World world, BlockPos pos, BlockState state) {
        BlockState blockState = (BlockState)state.with(RECOVERY_CHARGES, NO_CHARGES).with(RECOVERY_READY, false);
        world.setBlockState(pos, blockState, 3);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(charger, blockState));
        world.playSound((PlayerEntity)null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    private static boolean isChargeItem(ItemStack stack) {
        return stack.isOf(Items.ECHO_SHARD);
    }

    static {
        RECOVERY_CHARGES = IntProperty.of("recovery_charges", 0, 5);
        RECOVERY_READY = BooleanProperty.of("recovery_ready");
    }
}
