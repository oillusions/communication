package cn.oillusions.communication;

import cn.oillusions.communication.foundation.core.graph.Medium;
import cn.oillusions.communication.foundation.core.type.ConnectorType;
import cn.oillusions.communication.foundation.core.type.PayloadType;
import cn.oillusions.communication.foundation.core.type.SlotType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class AllTypes {
    public static final ResourceKey<Registry<SlotType<?>>> SLOT_TYPE_KEY =
        ResourceKey.createRegistryKey(Communication.modLoc("slot_type"));

    public static final ResourceKey<Registry<ConnectorType<?>>> CONNECTOR_TYPE_KEY =
        ResourceKey.createRegistryKey(Communication.modLoc("connector_type"));

    public static final ResourceKey<Registry<PayloadType<?>>> PAYLOAD_TYPE_KEY =
        ResourceKey.createRegistryKey(Communication.modLoc("payload_type"));

    public static final ResourceKey<Registry<Medium>> MEDIUM_TYPE_KEY =
        ResourceKey.createRegistryKey(Communication.modLoc("medium_type"));


    public static final String MEDIUM_KEY = Communication.ID + ".medium";
    public static final String PAYLOAD_KEY = Communication.ID + ".payload";
    public static final String CONNECTOR_KEY = Communication.ID + ".connector";
    public static final String SLOT_KEY = Communication.ID + ".slot";
    public static final String ADAPTER_KEY = Communication.ID + ".adapter";


    private AllTypes() {};
}
