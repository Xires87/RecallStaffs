package net.fryc.recallstaffs.network.payloads;

import net.fryc.recallstaffs.network.ModPackets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record AnswerConfigPayload(int woodCooldown, int copperCooldown, int ironCooldown, int goldCooldown, int diamondCooldown, int netheriteCooldown) implements CustomPayload {

    public static final Id<AnswerConfigPayload> ID = new Id<>(ModPackets.ANSWER_CONFIG_ID);
    public static final PacketCodec<RegistryByteBuf, AnswerConfigPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, AnswerConfigPayload::woodCooldown,
            PacketCodecs.INTEGER, AnswerConfigPayload::copperCooldown,
            PacketCodecs.INTEGER, AnswerConfigPayload::ironCooldown,
            PacketCodecs.INTEGER, AnswerConfigPayload::goldCooldown,
            PacketCodecs.INTEGER, AnswerConfigPayload::diamondCooldown,
            PacketCodecs.INTEGER, AnswerConfigPayload::netheriteCooldown,
            AnswerConfigPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}