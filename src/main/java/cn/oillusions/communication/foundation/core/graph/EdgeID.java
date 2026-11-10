package cn.oillusions.communication.foundation.core.graph;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public record EdgeID(long value) {
    public static final Codec<EdgeID> CODEC =
        Codec.LONG.xmap(EdgeID::new, EdgeID::value);

    public static final Codec<EdgeID> KEY_CODEC =
        Codec.STRING.comapFlatMap(
            str -> {
                try {
                    return DataResult.success(new EdgeID(Long.parseLong(str)));
                } catch (NumberFormatException e) {
                    return DataResult.error(() -> "Invalid EdgeID string: " + str);
                }
            },
            id -> Long.toString(id.value()));
}
