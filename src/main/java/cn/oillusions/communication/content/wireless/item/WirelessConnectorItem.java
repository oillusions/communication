package cn.oillusions.communication.content.wireless.item;

import cn.oillusions.communication.content.wireless.WirelessConnector;
import cn.oillusions.communication.foundation.core.ConnectorItem;
import cn.oillusions.communication.foundation.core.graph.EdgeID;
import cn.oillusions.communication.foundation.core.type.ConnectorType;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class WirelessConnectorItem extends Item implements ConnectorItem<WirelessConnector> {
    public WirelessConnectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public WirelessConnector newConnector(@NotNull EdgeID edgeID) {
        return new WirelessConnector(edgeID);
    }

    @Override
    public ConnectorType<WirelessConnector> type() {
        return WirelessConnector.TYPE;
    }
}
