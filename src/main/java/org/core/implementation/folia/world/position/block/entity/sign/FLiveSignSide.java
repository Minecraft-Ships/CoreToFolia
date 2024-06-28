package org.core.implementation.folia.world.position.block.entity.sign;

import net.kyori.adventure.text.Component;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.core.world.position.block.entity.sign.SignSide;
import org.core.world.position.block.entity.sign.SignTileEntity;

import java.util.Collection;
import java.util.List;

public class FLiveSignSide implements SignSide {

    private final Sign sign;
    private final Side side;

    public FLiveSignSide(Sign sign, Side side) {
        this.sign = sign;
        this.side = side;
    }

    @Override
    public SignTileEntity getSign() {
        return new BSignEntity(this.sign);
    }

    @Override
    public List<Component> getLines() {
        return this.sign.getSide(this.side).lines();
    }

    @Override
    public SignSide setLines(Collection<Component> componentCollection) {
        int i = 0;
        for (Component component : componentCollection) {
            this.sign.getSide(this.side).line(i, component);
            i++;
        }
        this.sign.update();
        return this;
    }

    @Override
    public boolean isFront() {
        return this.side == Side.FRONT;
    }

    @Override
    public boolean isGlowing() {
        return this.sign.getSide(this.side).isGlowingText();
    }

    @Override
    public void setGlowing(boolean glowing) {
        this.sign.getSide(this.side).setGlowingText(glowing);
        this.sign.update();
    }
}
