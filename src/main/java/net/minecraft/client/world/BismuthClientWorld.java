package net.minecraft.client.world;

import net.minecraft.world.Difficulty;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;

public class BismuthClientWorld extends ClientWorld {
    public BismuthClientWorld() {
        super(null, new LevelInfo(0L, LevelInfo.GameMode.CREATIVE, false, false, LevelGeneratorType.FLAT), 0, Difficulty.PEACEFUL, null);
    }

    public void tick() {
    }
}
