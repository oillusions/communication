package cn.oillusions.communication.foundation.core.type;

import cn.oillusions.communication.AllRegistries;
import cn.oillusions.communication.AllTypes;
import cn.oillusions.communication.foundation.core.graph.Medium;
import cn.oillusions.communication.util.Utils;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record MediumType<M extends Medium>(
    @NotNull MapCodec<M> codec,
    @NotNull Supplier<M> mediumSupplier
    ) {
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
