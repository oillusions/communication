package cn.oillusions.communication.foundation.communication.network.payload;

import cn.oillusions.communication.Communication;
import cn.oillusions.communication.foundation.communication.AttachConnectorHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ReturnChannelSelectResultPayload(long taskID, long edgeID) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, ReturnChannelSelectResultPayload> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, ReturnChannelSelectResultPayload::taskID,
            ByteBufCodecs.VAR_LONG, ReturnChannelSelectResultPayload::edgeID,
            ReturnChannelSelectResultPayload::new
        );

    public static final Type<ReturnChannelSelectResultPayload> TYPE =
        new Type<>(Communication.modLoc("return_channel_select_result"));

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ReturnChannelSelectResultPayload payload, IPayloadContext context) {
        AttachConnectorHandler.returnChannelSelectResult(payload.taskID(), payload.edgeID);
    }
}
