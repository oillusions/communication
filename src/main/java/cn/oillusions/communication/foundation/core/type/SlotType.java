package cn.oillusions.communication.foundation.core.type;

import cn.oillusions.communication.AllRegistries;
import cn.oillusions.communication.AllTypes;
import cn.oillusions.communication.foundation.core.Slot;
import cn.oillusions.communication.util.Utils;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public record SlotType<T extends Slot>(
    @NotNull MapCodec<T> codec
) {
    @Nullable
    public ResourceLocation id() {
        return AllRegistries.SLOTS.getKey(this);
    }

    public Component getName() {
        ResourceLocation ID = id();
        if (ID == null) return Component.literal("Not registered");
        return Utils.name(AllTypes.SLOT_KEY, ID);
    }
}
