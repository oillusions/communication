package cn.oillusions.communication.foundation.core.type;

import cn.oillusions.communication.AllRegistries;
import cn.oillusions.communication.AllTypes;
import cn.oillusions.communication.foundation.core.Connector;
import cn.oillusions.communication.foundation.core.Slot;
import cn.oillusions.communication.util.Utils;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Set;

public record ConnectorType<T extends Connector>(
    @NotNull MapCodec<T> codec,
    @Nullable ResourceLocation supportMediumID,
    @NotNull Set<ResourceLocation> supportSlotTypeIDs
    ) {
    @Nullable
    public ResourceLocation id() {
        return AllRegistries.CONNECTORS.getKey(this);
    }

    public Component getName() {
        ResourceLocation ID = id();
        if (ID == null) return Component.literal("Not registered");
        return Utils.name(AllTypes.CONNECTOR_KEY, ID);
    }

    public boolean supports(Slot slot) {
        return supportSlotTypeIDs.contains(slot.getType().id());
    }
}
