//package slimeknights.mantle.client.model.util;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.VertexFormatElement;
//import com.mojang.math.Matrix3f;
//import com.mojang.math.Matrix4f;
//import com.mojang.math.Transformation;
//import com.mojang.math.Vector3f;
//import com.mojang.math.Vector4f;
//import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
//import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.client.renderer.block.model.BakedQuad;
//import net.minecraft.client.renderer.block.model.ItemOverrides;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.resources.model.BakedModel;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.BlockAndTintGetter;
//import net.minecraft.world.level.block.state.BlockState;
//import io.github.fabricators_of_create.porting_lib.util.TransformationHelper;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//import java.util.List;
//import java.util.Random;
//import java.util.function.Supplier;
//
///**
// * Model that Translates, Rotates, Scales, then Rotates a child model
// * TODO: is this still needed?
// */
//public class TRSRBakedModel extends DynamicBakedWrapper<BakedModel> implements FabricBakedModel {
//
//  private final Transformation transformation;
//  private final TRSROverride override;
//  private final int faceOffset;
//
//  public TRSRBakedModel(BakedModel original, float x, float y, float z, float scale) {
//    this(original, x, y, z, 0, 0, 0, scale, scale, scale);
//  }
//
//  public TRSRBakedModel(BakedModel original, float x, float y, float z, float rotX, float rotY, float rotZ, float scale) {
//    this(original, x, y, z, rotX, rotY, rotZ, scale, scale, scale);
//  }
//
//  public TRSRBakedModel(BakedModel original, float x, float y, float z, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
//    this(original, new Transformation(new Vector3f(x, y, z),
//            null,
//            new Vector3f(scaleX, scaleY, scaleZ),
//            TransformationHelper.quatFromXYZ(new float[] { rotX, rotY, rotZ }, false)));
//  }
//
//  public TRSRBakedModel(BakedModel original, Transformation transform) {
//    super(original);
//    this.transformation = transform.blockCenterToCorner();
//    this.override = new TRSROverride(this);
//    this.faceOffset = 0;
//  }
//
//  /** Rotates around the Y axis and adjusts culling appropriately. South is default. */
//  public TRSRBakedModel(BakedModel original, Direction facing) {
//    super(original);
//    this.override = new TRSROverride(this);
//
//    this.faceOffset = 4 + Direction.NORTH.get2DDataValue() - facing.get2DDataValue();
//
//    double r = Math.PI * (360 - facing.getOpposite().get2DDataValue() * 90) / 180d;
//    this.transformation = new Transformation(null, null, null, TransformationHelper.quatFromXYZ(new float[] { 0, (float) r, 0 }, false)).blockCenterToCorner();
//  }
//
//  @Override
//  public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
//    context.getEmitter()
//    // transform quads obtained from parent
//
//    ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
//    if (!this.wrapped.isCustomRenderer()) {
//      try {
//        // adjust side to facing-rotation
//        if (side != null && side.get2DDataValue() > -1) {
//          side = Direction.from2DDataValue((side.get2DDataValue() + this.faceOffset) % 4);
//        }
//        for (BakedQuad quad : this.originalModel.getQuads(state, side, rand, data)) {
//          Transformer transformer = new Transformer(this.transformation, quad.getSprite());
//          quad.pipe(transformer);
//          builder.add(transformer.build());
//        }
//      }
//      catch (Exception e) {
//        // do nothing. Seriously, why are you using immutable lists?!
//      }
//    }
//
//    return builder.build();
//  }
//
//  @Nonnull
//  @Override
//  public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand, IModelData data) {
//    // transform quads obtained from parent
//
//    ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
//    if (!this.wrapped.isCustomRenderer()) {
//      try {
//        // adjust side to facing-rotation
//        if (side != null && side.get2DDataValue() > -1) {
//          side = Direction.from2DDataValue((side.get2DDataValue() + this.faceOffset) % 4);
//        }
//        for (BakedQuad quad : this.originalModel.getQuads(state, side, rand, data)) {
//          Transformer transformer = new Transformer(this.transformation, quad.getSprite());
//          quad.pipe(transformer);
//          builder.add(transformer.build());
//        }
//      }
//      catch (Exception e) {
//        // do nothing. Seriously, why are you using immutable lists?!
//      }
//    }
//
//    return builder.build();
//  }
//
//  @Override
//  public ItemOverrides getOverrides() {
//    return this.override;
//  }
//
//  private static class TRSROverride extends ItemOverrides {
//
//    private final TRSRBakedModel model;
//
//    TRSROverride(TRSRBakedModel model) {
//      this.model = model;
//    }
//
//    @Nullable
//    @Override
//    public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed) {
//      BakedModel baked = this.model.originalModel.getOverrides().resolve(originalModel, stack, world, entity, seed);
//      if (baked == null) {
//        return null;
//      }
//      return new TRSRBakedModel(baked, this.model.transformation);
//    }
//  }
//
//  private static class Transformer extends VertexTransformer {
//
//    protected Matrix4f transformation;
//    protected Matrix3f normalTransformation;
//
//    public Transformer(Transformation transformation, TextureAtlasSprite textureAtlasSprite) {
//      super(new BakedQuadBuilder(textureAtlasSprite));
//      // position transform
//      this.transformation = transformation.getMatrix();
//      // normal transform
//      this.normalTransformation = new Matrix3f(this.transformation);
//      this.normalTransformation.invert();
//      this.normalTransformation.transpose();
//    }
//
//    @Override
//    public void put(int element, float... data) {
//      VertexFormatElement.Usage usage = this.parent.getVertexFormat().getElements().get(element).getUsage();
//
//      // transform normals and position
//      if (usage == VertexFormatElement.Usage.POSITION && data.length >= 3) {
//        Vector4f vec = new Vector4f(data[0], data[1], data[2], 1f);
//        vec.transform(this.transformation);
//        data = new float[4];
//        data[0] = vec.x();
//        data[1] = vec.y();
//        data[2] = vec.z();
//        data[3] = vec.w();
//      }
//      else if (usage == VertexFormatElement.Usage.NORMAL && data.length >= 3) {
//        Vector3f vec = new Vector3f(data);
//        vec.transform(this.normalTransformation);
//        vec.normalize();
//        data = new float[4];
//        data[0] = vec.x();
//        data[1] = vec.y();
//        data[2] = vec.z();
//      }
//      super.put(element, data);
//    }
//
//    public BakedQuad build() {
//      return ((BakedQuadBuilder) this.parent).build();
//    }
//  }
//}
