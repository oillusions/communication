package cn.oillusions.communication.foundation.core;

import javax.annotation.Nullable;

public record Envelope(
    Payload payload,
    @Nullable Object metadata
) {}
