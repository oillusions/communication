package cn.oillusions.communication.foundation.core;

import cn.oillusions.communication.foundation.core.type.PayloadType;
import org.jetbrains.annotations.NotNull;

public interface Payload {

    @NotNull
    PayloadType<?> getType();
}
