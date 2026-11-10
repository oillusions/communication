package cn.oillusions.communication;

import cn.oillusions.communication.content.redstone.block.RedstoneAdapterBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.neoforged.bus.api.IEventBus;

public class AllBlocks {
    private static final CreateRegistrate REGISTRATE = Communication.REGISTRATE;

    static {
        REGISTRATE.setCreativeTab(AllCreativeTabs.BASE_TAB);
    }

    public static final BlockEntry<RedstoneAdapterBlock> REDSTONE_ADAPTER_BLOCK = REGISTRATE
        .block("redstone_adapter", RedstoneAdapterBlock::new)
        .properties(p -> p.strength(2.0f))
        .register();

    public static void register(IEventBus modEventBus) {
    }
}
