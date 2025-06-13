package net.fryc.recallstaffs.items.custom;

import com.mojang.serialization.DataResult;
import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.config.ConfigHelper;
import net.fryc.recallstaffs.items.ModItems;
import net.fryc.recallstaffs.tags.ModBlockTags;
import net.fryc.recallstaffs.util.ServerPlayerGetters;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.Optional;

public class StaffItem extends Item {

    public static final String LODESTONE_POS_KEY = "LodestonePos";
    public static final String LODESTONE_DIMENSION_KEY = "LodestoneDimension";

    public StaffItem(Settings settings) {
        super(settings);
    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(RecallStaffs.config.enableTooltipsForRecallStaffs){
            Pair<Integer, Integer> pair = ConfigHelper.getClientRecallStaffCostAndCooldown(stack);
            tooltip.add(Text.literal(Text.translatable("text.recallstaffs.usage_cost").getString() + ": " + pair.getA() + " " + Text.translatable("text.recallstaffs.levels").getString()).formatted(Formatting.BLUE));
            tooltip.add(Text.literal(Text.translatable("text.recallstaffs.cooldown").getString() + ": " + pair.getB() + Text.translatable("text.recallstaffs.seconds").getString()).formatted(Formatting.GRAY));
        }
        if(isCalibrated(stack)){
            tooltip.add(Text.translatable("text.recallstaffs.revert_calibration"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 140;
    }

    public Text getName(ItemStack stack) {
        String calibrated = isCalibrated(stack) ? "_calibrated" : "";
        return Text.translatable(this.getTranslationKey(stack) + calibrated);
    }

    public ActionResult useOnBlock(ItemUsageContext context){
        if(!context.getWorld().isClient()){
            if(this.canBeCalibrated()){
                if(isCalibrated(context.getStack())){
                    if(context.getWorld().getBlockState(context.getBlockPos()).isIn(ModBlockTags.REVERTS_RECALL_STAFF_CALIBRATION)){
                        context.getStack().removeSubNbt(LODESTONE_POS_KEY);
                        context.getWorld().playSound(null, context.getBlockPos(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS);

                        return ActionResult.success(true);
                    }
                }
                else if(context.getWorld().getBlockState(context.getBlockPos()).isIn(ModBlockTags.CALIBRATES_RECALL_STAFF)){
                    context.getStack().getOrCreateNbt().put(LODESTONE_POS_KEY, NbtHelper.fromBlockPos(context.getBlockPos()));
                    DataResult dataResult = World.CODEC.encodeStart(NbtOps.INSTANCE, context.getWorld().getRegistryKey());
                    dataResult.result().ifPresent((nbtElement) -> context.getStack().getOrCreateNbt().put("LodestoneDimension", (NbtElement) nbtElement));
                    context.getWorld().playSound(null, context.getBlockPos(), SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.BLOCKS);

                    return ActionResult.success(true);
                }
            }
        }

        return ActionResult.PASS;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(user instanceof ServerPlayerEntity splayer){
            int cooldown = ((ServerPlayerGetters) splayer).getRecallStaffCooldown();
            if(cooldown < 1){
                user.setCurrentHand(hand);

                return TypedActionResult.pass(itemStack);
            }
            else{
                cooldown /= 20;
                this.cancelTeleportation(Text.literal(
                        Text.translatable("text.recallstaffs.time_before_next_recall").getString() +
                                ": " +
                                cooldown +
                                Text.translatable("text.recallstaffs.seconds").getString()
                ), splayer, itemStack);

                return TypedActionResult.fail(itemStack);
            }
        }
        return TypedActionResult.fail(itemStack);
    }


    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack itemStack = super.finishUsing(stack, world, user);
        if (!world.isClient()) {
            AbstractHorseEntity horse = null;
            int additionalCost = 0;
            if (user.hasVehicle()) {
                if(user.getVehicle() instanceof AbstractHorseEntity horseEntity){
                    horse = horseEntity;
                    additionalCost = RecallStaffs.config.recallingWithHorseAdditionalCost;
                }
                user.stopRiding();
            }

            if (user instanceof ServerPlayerEntity player) {
                if(player.getServer() != null){
                    Pair<Integer, Integer> pair = ConfigHelper.getRecallStaffCostAndCooldown(stack, world);
                    int recallCost = pair.getA();
                    int recallCooldown = pair.getB();
                    boolean shouldCheckLevel = isCalibrated(stack) ? RecallStaffs.config.checkPlayersLevelBeforeCalibratedRecall : RecallStaffs.config.checkPlayersLevelBeforeRecall;
                    if(!(player.experienceLevel < recallCost && shouldCheckLevel)){
                        BlockPos spawnPos = null;
                        RegistryKey<World> dimension = null;
                        if(isCalibrated(stack)){
                            NbtCompound pos = stack.getNbt().getCompound(LODESTONE_POS_KEY);
                            Optional<RegistryKey<World>> optional = getLodestoneDimension(stack.getNbt());
                            if(pos != null && optional.isPresent()){
                                spawnPos = NbtHelper.toBlockPos(pos);
                                dimension = optional.get();
                            }

                            if(spawnPos == null || !isLodeStone(player.getServer().getWorld(dimension), spawnPos)) {
                                this.cancelTeleportation(Text.translatable("text.recallstaffs.calibrated_teleport_error").formatted(Formatting.RED), player, stack);

                                return itemStack;
                            }
                        }

                        if(spawnPos == null) {
                            spawnPos = ((ServerPlayerGetters) player).getServerPlayerSpawnPosition() == null ?
                                    player.getWorld().getSpawnPos() : ((ServerPlayerGetters) player).getServerPlayerSpawnPosition();
                            dimension = player.getSpawnPointDimension();
                        }

                        this.teleport(
                                world, player, player.getServer().getWorld(dimension),
                                spawnPos, stack, recallCost, recallCooldown, horse, additionalCost
                        );
                    }
                    else {
                        this.cancelTeleportation(Text.translatable("text.recallstaffs.too_low_level").formatted(Formatting.RED), player, stack);
                    }
                }
            }
        }

        return itemStack;
    }

    public boolean canBeCalibrated(){
        return this != ModItems.WOODEN_RECALL_STAFF;
    }

    public static boolean isCalibrated(ItemStack stack){
        return stack.hasNbt() && stack.getNbt().contains(LODESTONE_POS_KEY);
    }

    public boolean hasGlint(ItemStack stack){
        return isCalibrated(stack) || super.hasGlint(stack);
    }

    private void teleport(
            World currentWorld, ServerPlayerEntity player, ServerWorld destinationWorld,
            BlockPos destinationPos, ItemStack usedItem, int recallCost,
            int recallCooldown, @Nullable AbstractHorseEntity horse, int additionalCost
    ){
        player.setInvulnerable(true);

        int prevX = (int) player.getX();
        int prevY = (int) player.getY();
        int prevZ = (int) player.getZ();
        Vec3d previousPos = player.getPos();

        int destY = isCalibrated(usedItem) ? destinationPos.getY() + 1 : destinationPos.getY();

        if(horse != null){
            horse.teleport(destinationWorld, destinationPos.getX(), destY, destinationPos.getZ(), null, horse.getYaw(), horse.getPitch());
        }
        player.teleport(destinationWorld, destinationPos.getX(), destY, destinationPos.getZ(), player.getYaw(), player.getPitch());
        currentWorld.emitGameEvent(GameEvent.TELEPORT, previousPos, GameEvent.Emitter.of(player));

        player.getItemCooldownManager().set(usedItem.getItem(), 100);
        player.setExperienceLevel(changePlayerLevel(player.experienceLevel, recallCost, additionalCost));

        if(RecallStaffs.config.recallingSummonsLightningBolt){
            EntityType.LIGHTNING_BOLT.spawn((ServerWorld) currentWorld, new BlockPos(prevX, prevY, prevZ), SpawnReason.TRIGGERED);
        }
        ((ServerPlayerGetters) player).setRecallStaffCooldown(recallCooldown * 20);

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, setNauseaTime(), 0));

        player.setInvulnerable(false);
    }

    private void cancelTeleportation(Text message, ServerPlayerEntity player, ItemStack stack){
        player.getItemCooldownManager().set(stack.getItem(), 40);
        player.sendMessage(message, true);
    }

    private static boolean isLodeStone(ServerWorld world, BlockPos pos){
        if(world == null){
            return false;
        }

        return world.getBlockState(pos).isIn(ModBlockTags.CALIBRATES_RECALL_STAFF);
    }

    private static int changePlayerLevel(int playerLevel, int cost, int additionalCost){
        playerLevel -= cost + additionalCost;

        if(playerLevel < 0) playerLevel = 0;
        return playerLevel;
    }

    private int setNauseaTime(){
        int time = 500;
        if(this == ModItems.COPPER_RECALL_STAFF) time -= 250;
        else if(this == ModItems.IRON_RECALL_STAFF) time -= 300;
        else if(this == ModItems.GOLDEN_RECALL_STAFF) time -= 350;
        else if(this == ModItems.DIAMOND_RECALL_STAFF) time -= 400;
        else if(this == ModItems.NETHERITE_RECALL_STAFF) time -= 450;
        return time;
    }

    private static Optional<RegistryKey<World>> getLodestoneDimension(NbtCompound nbt) {
        return World.CODEC.parse(NbtOps.INSTANCE, nbt.get(LODESTONE_DIMENSION_KEY)).result();
    }


}
