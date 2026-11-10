package cn.oillusions.communication;

import cn.oillusions.communication.content.redstone.block.entity.RedstoneAdapterEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.neoforged.bus.api.IEventBus;

public class AllBlockEntities {
    private static final CreateRegistrate REGISTRATE = Communication.REGISTRATE;

    static {
        REGISTRATE.setCreativeTab(AllCreativeTabs.BASE_TAB);
    }

    public static final BlockEntityEntry<RedstoneAdapterEntity> COMMUNICATION_ADAPTER_BLOCK_ENTITY = REGISTRATE
        .blockEntity("communication_adapter", RedstoneAdapterEntity::new)
        .validBlock(AllBlocks.REDSTONE_ADAPTER_BLOCK)
        .register();

    public static void register(IEventBus modEventBus) {
    }
}
