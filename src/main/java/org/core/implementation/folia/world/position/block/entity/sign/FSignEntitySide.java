package org.core.implementation.folia.world.position.block.entity.sign;

import net.kyori.adventure.text.Component;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.core.implementation.folia.world.position.block.entity.CommonTileEntity;
import org.core.world.position.block.entity.sign.SignSide;
import org.core.world.position.block.entity.sign.SignTileEntity;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class FSignEntitySide implements SignSide {

    private final SignTileEntity from;
    private final boolean isFront;

    public FSignEntitySide(SignTileEntity signTileEntity, boolean isFront) {
        this.isFront = isFront;
        this.from = signTileEntity;
    }

    public org.bukkit.block.sign.SignSide side() {
        Sign common = (Sign) ((CommonTileEntity) this.from).bukkitState();
        return common.getSide(this.isFront ? Side.FRONT : Side.BACK);
    }

    @Override
    public SignTileEntity getSign() {
        return this.from;
    }

    @Override
    public List<Component> getLines() {
        return this.side().lines();
    }

    @Override
    public SignSide setLines(Collection<Component> componentCollection) {
        int index = 0;
        Iterator<Component> iterator = componentCollection.iterator();
        while (iterator.hasNext()) {
            this.side().line(index, iterator.next());
            index++;
        }
        return this;
    }

    @Override
    public boolean isFront() {
        return this.isFront;
    }

    @Override
    public boolean isGlowing() {
        return this.side().isGlowingText();
    }

    @Override
    public void setGlowing(boolean glowing) {
        this.side().setGlowingText(glowing);
    }
}
