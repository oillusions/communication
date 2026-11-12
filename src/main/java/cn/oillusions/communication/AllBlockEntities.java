package cn.oillusions.communication;

import cn.oillusions.communication.content.cct.CCTweakedAdapterBlockEntity;
import cn.oillusions.communication.content.redstone.RedstoneAdapterBlockEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.neoforged.bus.api.IEventBus;

public class AllBlockEntities {
    private static final CreateRegistrate REGISTRATE = Communication.REGISTRATE;

    static {
        REGISTRATE.setCreativeTab(AllCreativeTabs.BASE_TAB);
    }

    public static final BlockEntityEntry<RedstoneAdapterBlockEntity> COMMUNICATION_ADAPTER_BLOCK_ENTITY = REGISTRATE
        .blockEntity("communication_adapter", RedstoneAdapterBlockEntity::new)
        .validBlock(AllBlocks.REDSTONE_ADAPTER_BLOCK)
        .register();

    public static final BlockEntityEntry<CCTweakedAdapterBlockEntity> CC_TWEAKED_ADAPTER_BLOCK_ENTITY = REGISTRATE
        .blockEntity("cc_tweaked_adapter", CCTweakedAdapterBlockEntity::new)
        .validBlock(AllBlocks.CC_TWEAKED_ADAPTER_BLOCK)
        .register();

    public static void register(IEventBus modEventBus) {
    }
}
