package cn.oillusions.communication.foundation.core.graph;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class IDAllocator {
    private long curr;

    public static final MapCodec<IDAllocator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        Codec.LONG.fieldOf("currID").forGetter(IDAllocator::current)
    ).apply(i, IDAllocator::new));

    public IDAllocator(long curr) {
        this.curr = curr;
    }

    public long allocId() {
        return curr++;
    }
    public long current() { return curr; }
    public void setCurrent(long value) { curr = value; }
}
