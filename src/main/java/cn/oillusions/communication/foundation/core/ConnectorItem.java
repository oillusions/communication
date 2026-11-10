package cn.oillusions.communication.foundation.core;

import cn.oillusions.communication.foundation.core.graph.EdgeID;
import cn.oillusions.communication.foundation.core.type.ConnectorType;

public interface ConnectorItem<C extends Connector> {
    C newConnector(EdgeID edgeID);
    ConnectorType<C> type();
}
