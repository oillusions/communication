package cn.oillusions.communication.foundation.core.graph;

import cn.oillusions.communication.AllRegistries;
import cn.oillusions.communication.AllTypes;
import cn.oillusions.communication.foundation.core.Connector;
import cn.oillusions.communication.foundation.core.Envelope;
import cn.oillusions.communication.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class Medium {
    public void propagate(
        @NotNull Connector source,
        @NotNull EdgeID edgeID,
        @NotNull ArrayList<Connector> connectors,
        @NotNull Envelope envelope
    ) {
        for (Connector connector : connectors) {
            if (connector == source) continue;
            connector.receive(envelope);
        }
    }

    @Nullable
    public ResourceLocation id() {
        return AllRegistries.MEDIUMS.getKey(this);
    }

    public Component getName() {
        ResourceLocation ID = id();
        if (ID == null) return Component.literal("Not registered");
        return Utils.name(AllTypes.MEDIUM_KEY, ID);
    }
}
