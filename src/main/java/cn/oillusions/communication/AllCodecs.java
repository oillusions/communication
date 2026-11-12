package cn.oillusions.communication;

import cn.oillusions.communication.foundation.core.Slot;
import cn.oillusions.communication.foundation.core.Connector;
import cn.oillusions.communication.foundation.core.graph.Medium;
import cn.oillusions.communication.foundation.core.type.MediumType;
import cn.oillusions.communication.foundation.core.type.SlotType;
import cn.oillusions.communication.foundation.core.type.ConnectorType;
import com.mojang.serialization.Codec;

public class AllCodecs {
    public static final Codec<Slot> SLOT_CODEC = Codec.lazyInitialized(() ->
        AllRegistries.SLOTS.byNameCodec()
            .dispatch("type", Slot::getType, SlotType::codec));

    public static final Codec<Connector> CONNECTOR_CODEC = Codec.lazyInitialized(() ->
        AllRegistries.CONNECTORS.byNameCodec()
            .dispatch("type", Connector::getType, ConnectorType::codec));

    public static final Codec<Medium> MEDIUM_CODEC = Codec.lazyInitialized(() ->
        AllRegistries.MEDIUMS.byNameCodec()
            .dispatch("type", Medium::getType, MediumType::codec));


    private AllCodecs() {};
}
