package cn.oillusions.communication.content.cct;

import cn.oillusions.communication.foundation.core.Payload;
import cn.oillusions.communication.foundation.core.type.PayloadType;
import org.jetbrains.annotations.NotNull;

public record CCTweakedPayload(
    byte[] bytes
) implements Payload {
    public static final PayloadType<CCTweakedPayload> TYPE = new PayloadType<>();

    @Override
    public @NotNull PayloadType<?> getType() {
        return TYPE;
    }
}
