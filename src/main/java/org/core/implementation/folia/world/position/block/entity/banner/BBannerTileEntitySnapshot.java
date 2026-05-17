package org.core.implementation.folia.world.position.block.entity.banner;

import org.core.implementation.folia.world.position.block.entity.AbstractTileEntitySnapshot;
import org.core.implementation.folia.world.position.block.entity.CommonTileEntity;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.blocktypes.post.BlockTypes1V13;
import org.core.world.position.block.entity.banner.BannerTileEntity;
import org.core.world.position.block.entity.banner.BannerTileEntitySnapshot;
import org.core.world.position.block.entity.banner.LiveBannerTileEntity;
import org.core.world.position.block.entity.banner.pattern.PatternLayersSnapshot;

import java.util.Collection;

public class BBannerTileEntitySnapshot extends AbstractTileEntitySnapshot<LiveBannerTileEntity>
        implements BannerTileEntitySnapshot {

    protected final PatternLayersSnapshot layers;

    public BBannerTileEntitySnapshot(BannerTileEntity bannerTileEntity) {
        super(((CommonTileEntity) bannerTileEntity).bukkitState());
        this.layers = bannerTileEntity.getLayers().createSnapshot();
    }

    @Override
    public LiveBannerTileEntity apply(LiveBannerTileEntity lbte) {
        super.apply(lbte);
        lbte.getLayers().removeLayers().addLayers(this.layers.getLayers());
        return lbte;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return BlockTypes1V13.BLACK_BANNER.getLike();
    }

    @Override
    public PatternLayersSnapshot getLayers() {
        return this.layers;
    }

    @Override
    public BannerTileEntitySnapshot getSnapshot() {
        return new BBannerTileEntitySnapshot(this);
    }
}
