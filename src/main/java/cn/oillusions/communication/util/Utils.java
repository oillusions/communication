package cn.oillusions.communication.util;

import cn.oillusions.communication.AllTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Utils {
    public static Component name(String prefix, ResourceLocation id) {
        return Component.translatable(prefix + "." + id.getNamespace() + "." + id.getPath());
    }

    public static Component PortName(ResourceLocation id) {
        return name(AllTypes.SLOT_KEY, id);
    }

    public static Component ConnectorName(ResourceLocation id) {
        return name(AllTypes.CONNECTOR_KEY, id);
    }
}
