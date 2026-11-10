package cn.oillusions.communication.foundation.core.type;

import cn.oillusions.communication.AllRegistries;
import cn.oillusions.communication.AllTypes;
import cn.oillusions.communication.foundation.core.Payload;
import cn.oillusions.communication.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public record PayloadType<P extends Payload>() {
    @Nullable
    public ResourceLocation id() {
        return AllRegistries.PAYLOADS.getKey(this);
    }

    public Component getName() {
        ResourceLocation ID = id();
        if (ID == null) return Component.literal("Not registered");
        return Utils.name(AllTypes.PAYLOAD_KEY, ID);
    }
}
