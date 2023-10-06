package org.core.implementation.folia.world.position.block.entity.sign;

import net.kyori.adventure.text.Component;
import org.bukkit.block.Sign;
import org.core.world.position.block.entity.sign.SignSide;
import org.core.world.position.block.entity.sign.SignTileEntity;

import java.util.Collection;
import java.util.List;

public class FLiveLegacySignSide implements SignSide {

    private final Sign sign;

    public FLiveLegacySignSide(Sign sign) {
        this.sign = sign;
    }

    @Override
    public SignTileEntity getSign() {
        return new BSignEntity(this.sign);
    }

    @Override
    public List<Component> getLines() {
        return this.sign.lines();
    }

    @Override
    public SignSide setLines(Collection<Component> componentCollection) {
        int i = 0;
        for (Component component : componentCollection) {
            this.sign.line(i, component);
            i++;
        }
        this.sign.update();
        return this;
    }

    @Override
    public boolean isFront() {
        return true;
    }

    @Override
    public boolean isGlowing() {
        return this.isGlowing();
    }

    @Override
    public void setGlowing(boolean glowing) {
        this.setGlowing(glowing);
    }
}
