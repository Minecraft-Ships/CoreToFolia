package org.core.implementation.folia.world.position.block.entity.banner.pattern;

import org.bukkit.block.banner.Pattern;
import org.core.world.position.block.entity.banner.pattern.PatternLayer;
import org.core.world.position.block.entity.banner.pattern.PatternLayers;
import org.core.world.position.block.entity.banner.pattern.PatternLayersSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BPatternLayers implements PatternLayers {

    protected final org.bukkit.block.Banner banner;

    public BPatternLayers(org.bukkit.block.Banner banner) {
        this.banner = banner;
    }

    @Override
    public List<PatternLayer> getLayers() {
        List<PatternLayer> set = new ArrayList<>();
        this.banner.getPatterns().forEach(p -> set.add(new BPatternLayer(p)));
        return set;
    }

    @Override
    public PatternLayers addLayers(Collection<? extends PatternLayer> layers) {
        layers.forEach(l -> this.banner.addPattern(((BPatternLayer) l).getBukkitValue()));
        this.banner.update();
        return this;
    }

    @Override
    public PatternLayers addLayer(int layerIndex, PatternLayer layer) {
        List<Pattern> list = this.banner.getPatterns();
        list.add(layerIndex, ((BPatternLayer) layer).getBukkitValue());
        this.banner.setPatterns(list);
        this.banner.update();
        return this;
    }

    @Override
    public PatternLayers removeLayer(int layer) {
        this.banner.removePattern(layer);
        this.banner.update();
        return this;
    }

    @Override
    public PatternLayers removeLayers() {
        for (int index = this.banner.numberOfPatterns(); index > 0; index--) {
            this.banner.removePattern(index);
        }
        this.banner.update();
        return this;
    }

    @Override
    public PatternLayersSnapshot createSnapshot() {
        return new BPatternLayersSnapshot(this.getLayers());
    }
}
