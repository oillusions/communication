package cn.oillusions.communication.foundation.communication.network.payload;

import cn.oillusions.communication.Communication;
import cn.oillusions.communication.foundation.communication.AttachConnectorHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record CloseChannelSelectPayload(long taskID) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, CloseChannelSelectPayload> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, CloseChannelSelectPayload::taskID,
            CloseChannelSelectPayload::new
        );

    public static final Type<CloseChannelSelectPayload> TYPE =
        new Type<>(Communication.modLoc("close_channel_select"));

    public static void handle(CloseChannelSelectPayload payload, IPayloadContext context) {
        AttachConnectorHandler.closeChannelSelect(payload.taskID());
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
