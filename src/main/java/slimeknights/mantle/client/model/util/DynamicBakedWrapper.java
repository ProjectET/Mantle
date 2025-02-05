package slimeknights.mantle.client.model.util;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Cross between {@link ForwardingBakedModel} and {@link net.minecraftforge.client.model.data.IDynamicBakedModel}.
 * Used to create a baked model wrapper that has a dynamic {@link #getQuads(BlockState, Direction, Random, IModelData)} without worrying about overriding the deprecated variant.
 * @param <T>  Baked model parent
 */
@SuppressWarnings("WeakerAccess")
public abstract class DynamicBakedWrapper<T extends BakedModel> extends ForwardingBakedModel {

  protected DynamicBakedWrapper(T originalModel) {
    wrapped = originalModel;
  }
}
