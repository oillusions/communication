package cn.oillusions.communication.content.redstone;

import cn.oillusions.communication.foundation.core.Payload;
import cn.oillusions.communication.foundation.core.type.PayloadType;
import org.jetbrains.annotations.NotNull;

public record RedstoneSignalStrengthPayload(long id, byte level) implements Payload {

    public static final PayloadType<RedstoneSignalStrengthPayload> TYPE = new PayloadType<>();

    @Override
    public @NotNull PayloadType<?> getType() {
        return TYPE;
    }
}
