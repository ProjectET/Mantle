package slimeknights.mantle.client.model.fluid;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import slimeknights.mantle.client.model.util.SimpleBlockModel;
import io.github.fabricators_of_create.porting_lib.model.IModelConfiguration;
import io.github.fabricators_of_create.porting_lib.model.IModelGeometry;
import io.github.fabricators_of_create.porting_lib.model.IModelLoader;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * This model contains a list of fluid cuboids for the sake of rendering multiple fluid regions in world. It is used by the faucet at this time
 */
@AllArgsConstructor
public class FluidsModel implements IModelGeometry<FluidsModel> {
  private final SimpleBlockModel model;
  private final List<FluidCuboid> fluids;

  @Override
  public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation,UnbakedModel> modelGetter, Set<Pair<String,String>> missingTextureErrors) {
    return model.getTextures(owner, modelGetter, missingTextureErrors);
  }

  @Override
  public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material,TextureAtlasSprite> spriteGetter, ModelState transform, ItemOverrides overrides, ResourceLocation location) {
    BakedModel baked = model.bakeModel(owner, transform, overrides, spriteGetter, location);
    return new Baked(baked, fluids);
  }

  /** Baked model, mostly a data wrapper around a normal model */
  @SuppressWarnings("WeakerAccess")
  public static class Baked extends ForwardingBakedModel {
    @Getter
    private final List<FluidCuboid> fluids;
    public Baked(BakedModel originalModel, List<FluidCuboid> fluids) {
      wrapped = originalModel;
      this.fluids = fluids;
    }
  }

  /** Loader for this model */
  public static class Loader implements IModelLoader<FluidsModel> {
    /**
     * Shared loader instance
     */
    public static final Loader INSTANCE = new Loader();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {}

    @Override
    public FluidsModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
      SimpleBlockModel model = SimpleBlockModel.deserialize(deserializationContext, modelContents);
      List<FluidCuboid> fluid = FluidCuboid.listFromJson(modelContents, "fluids");
      return new FluidsModel(model, fluid);
    }
  }
}
