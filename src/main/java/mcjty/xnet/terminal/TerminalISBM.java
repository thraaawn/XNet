package mcjty.xnet.terminal;

import com.google.common.primitives.Ints;
import mcjty.xnet.client.XNetClientModelLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TerminalISBM implements IBakedModel {

    private final TextureAtlasSprite spriteFace;

    public TerminalISBM(TextureAtlasSprite spriteFace) {
        this.spriteFace = spriteFace;
    }

    @Override
    public IBakedModel handlePartState(IBlockState state) {
        // Called with the blockstate from our block. Here we get the values of the six properties and pass that to
        // our baked model implementation.
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        EnumFacing side = extendedBlockState.getValue(TerminalPart.SIDE);
        return new BakedModel(side, spriteFace);
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAmbientOcclusion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isGui3d() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBuiltInRenderer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms() {
        throw new UnsupportedOperationException();
    }

    private static class BakedModel implements IBakedModel {

        private final TextureAtlasSprite spriteFace;

        public BakedModel(EnumFacing side, TextureAtlasSprite spriteFace) {
            this.side = side;
            this.spriteFace = spriteFace;
        }

        private final EnumFacing side;

        private int[] vertexToInts(double x, double y, double z, float u, float v, TextureAtlasSprite sprite) {
            return new int[] {
                    Float.floatToRawIntBits((float) x),
                    Float.floatToRawIntBits((float) y),
                    Float.floatToRawIntBits((float) z),
                    -1,
                    Float.floatToRawIntBits(sprite.getInterpolatedU(u)),
                    Float.floatToRawIntBits(sprite.getInterpolatedV(v)),
                    0
            };
        }

        private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
            Vec3d normal = v1.subtract(v2).crossProduct(v3.subtract(v2));
            EnumFacing side = LightUtil.toSide((float) normal.xCoord, (float) normal.yCoord, (float) normal.zCoord);

            return new BakedQuad(Ints.concat(
                    vertexToInts(v1.xCoord, v1.yCoord, v1.zCoord, 0, 0, sprite),
                    vertexToInts(v2.xCoord, v2.yCoord, v2.zCoord, 0, 16, sprite),
                    vertexToInts(v3.xCoord, v3.yCoord, v3.zCoord, 16, 16, sprite),
                    vertexToInts(v4.xCoord, v4.yCoord, v4.zCoord, 16, 0, sprite)
            ), -1, side);
        }

        private BakedQuad createQuadOpp(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
            return createQuad(v4, v3, v2, v1, sprite);
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing side) {
            return Collections.emptyList();
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {
            List<BakedQuad> quads = new ArrayList<>();
            double p = .1;      // Thickness of the connector as it is put on the connecting block
            double q = .2;      // The wideness of the connector

            switch (side) {
                case DOWN:
                    quads.add(createQuad(new Vec3d(1 - q, 0, q),     new Vec3d(1 - q, p, q),     new Vec3d(1 - q, p, 1 - q), new Vec3d(1 - q, 0, 1 - q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(q,     0, 1 - q), new Vec3d(q,     p, 1 - q), new Vec3d(q,     p, q),     new Vec3d(q,     0, q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(q,     p, q),     new Vec3d(1 - q, p, q),     new Vec3d(1 - q, 0, q),     new Vec3d(q,     0, q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(q,     0, 1 - q), new Vec3d(1 - q, 0, 1 - q), new Vec3d(1 - q, p, 1 - q), new Vec3d(q,     p, 1 - q), XNetClientModelLoader.spriteSide));

                    quads.add(createQuad(new Vec3d(q,     p, 1 - q), new Vec3d(1 - q, p, 1 - q), new Vec3d(1 - q, p, q),     new Vec3d(q,     p, q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuadOpp(new Vec3d(q, 0, 1 - q), new Vec3d(1 - q, 0, 1 - q), new Vec3d(1 - q, 0, q), new Vec3d(q, 0, q), spriteFace));
                    break;
                case UP:
                    quads.add(createQuad(new Vec3d(1 - q, 1 - p, q),     new Vec3d(1 - q, 1,     q),     new Vec3d(1 - q, 1,     1 - q), new Vec3d(1 - q, 1 - p, 1 - q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(q,     1 - p, 1 - q), new Vec3d(q,     1,     1 - q), new Vec3d(q,     1,     q),     new Vec3d(q,     1 - p, q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(q,     1,     q),     new Vec3d(1 - q, 1,     q),     new Vec3d(1 - q, 1 - p, q),     new Vec3d(q,     1 - p, q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(q,     1 - p, 1 - q), new Vec3d(1 - q, 1 - p, 1 - q), new Vec3d(1 - q, 1,     1 - q), new Vec3d(q,     1,     1 - q), XNetClientModelLoader.spriteSide));

                    quads.add(createQuad(new Vec3d(q,     1 - p, q),     new Vec3d(1 - q, 1 - p, q),     new Vec3d(1 - q, 1 - p, 1 - q), new Vec3d(q,     1 - p, 1 - q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuadOpp(new Vec3d(q, 1, q), new Vec3d(1 - q, 1, q), new Vec3d(1 - q, 1, 1 - q), new Vec3d(q, 1, 1 - q), spriteFace));
                    break;
                case NORTH:
                    quads.add(createQuad(new Vec3d(q,     1 - q, p), new Vec3d(1 - q, 1 - q, p), new Vec3d(1 - q, 1 - q, 0), new Vec3d(q,     1 - q, 0), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(q,     q,     0), new Vec3d(1 - q, q,     0), new Vec3d(1 - q, q,     p), new Vec3d(q,     q,     p), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(1 - q, q,     0), new Vec3d(1 - q, 1 - q, 0), new Vec3d(1 - q, 1 - q, p), new Vec3d(1 - q, q,     p), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(q,     q,     p), new Vec3d(q,     1 - q, p), new Vec3d(q,     1 - q, 0), new Vec3d(q,     q,     0), XNetClientModelLoader.spriteSide));

                    quads.add(createQuad(new Vec3d(q, q, p), new Vec3d(1 - q, q, p), new Vec3d(1 - q, 1 - q, p), new Vec3d(q, 1 - q, p), XNetClientModelLoader.spriteSide));
                    quads.add(createQuadOpp(new Vec3d(q, q, 0), new Vec3d(1 - q, q, 0), new Vec3d(1 - q, 1 - q, 0), new Vec3d(q, 1 - q, 0), spriteFace));
                    break;
                case SOUTH:
                    quads.add(createQuad(new Vec3d(q,     1 - q, 1),     new Vec3d(1 - q, 1 - q, 1),     new Vec3d(1 - q, 1 - q, 1 - p), new Vec3d(q,     1 - q, 1 - p), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(q,     q,     1 - p), new Vec3d(1 - q, q,     1 - p), new Vec3d(1 - q, q,     1),     new Vec3d(q,     q,     1), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(1 - q, q,     1 - p), new Vec3d(1 - q, 1 - q, 1 - p), new Vec3d(1 - q, 1 - q, 1),     new Vec3d(1 - q, q,     1), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(q,     q,     1),     new Vec3d(q,     1 - q, 1),     new Vec3d(q,     1 - q, 1 - p), new Vec3d(q,     q,     1 - p), XNetClientModelLoader.spriteSide));

                    quads.add(createQuad(new Vec3d(q, 1 - q, 1 - p), new Vec3d(1 - q, 1 - q, 1 - p), new Vec3d(1 - q, q, 1 - p), new Vec3d(q, q, 1 - p), XNetClientModelLoader.spriteSide));
                    quads.add(createQuadOpp(new Vec3d(q, 1 - q, 1), new Vec3d(1 - q, 1 - q, 1), new Vec3d(1 - q, q, 1), new Vec3d(q, q, 1), spriteFace));
                    break;
                case WEST:
                    quads.add(createQuad(new Vec3d(0, 1 - q, 1 - q), new Vec3d(p, 1 - q, 1 - q), new Vec3d(p, 1 - q, q),     new Vec3d(0, 1 - q, q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(0, q,     q),     new Vec3d(p, q,     q),     new Vec3d(p, q,     1 - q), new Vec3d(0, q,     1 - q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(0, 1 - q, q),     new Vec3d(p, 1 - q, q),     new Vec3d(p, q,     q),     new Vec3d(0, q,     q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(0, q,     1 - q), new Vec3d(p, q,     1 - q), new Vec3d(p, 1 - q, 1 - q), new Vec3d(0, 1 - q, 1 - q), XNetClientModelLoader.spriteSide));

                    quads.add(createQuad(new Vec3d(p, q, q), new Vec3d(p, 1 - q, q), new Vec3d(p, 1 - q, 1 - q), new Vec3d(p, q, 1 - q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuadOpp(new Vec3d(0, q, q), new Vec3d(0, 1 - q, q), new Vec3d(0, 1 - q, 1 - q), new Vec3d(0, q, 1 - q), spriteFace));
                    break;
                case EAST:
                    quads.add(createQuad(new Vec3d(1 - p, 1 - q, 1 - q), new Vec3d(1, 1 - q, 1 - q), new Vec3d(1, 1 - q, q),     new Vec3d(1 - p, 1 - q, q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(1 - p, q,     q),     new Vec3d(1, q,     q),     new Vec3d(1, q,     1 - q), new Vec3d(1 - p, q,     1 - q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(1 - p, 1 - q, q),     new Vec3d(1, 1 - q, q),     new Vec3d(1, q,     q),     new Vec3d(1 - p, q,     q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuad(new Vec3d(1 - p, q,     1 - q), new Vec3d(1, q,     1 - q), new Vec3d(1, 1 - q, 1 - q), new Vec3d(1 - p, 1 - q, 1 - q), XNetClientModelLoader.spriteSide));

                    quads.add(createQuad(new Vec3d(1 - p, q, 1 - q), new Vec3d(1 - p, 1 - q, 1 - q), new Vec3d(1 - p, 1 - q, q), new Vec3d(1 - p, q, q), XNetClientModelLoader.spriteSide));
                    quads.add(createQuadOpp(new Vec3d(1, q, 1 - q), new Vec3d(1, 1 - q, 1 - q), new Vec3d(1, 1 - q, q), new Vec3d(1, q, q), spriteFace));
                    break;
            }

            return quads;
        }

        @Override
        public boolean isAmbientOcclusion() {
            return true;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return XNetClientModelLoader.spriteSide;
        }

        @Override
        @SuppressWarnings("deprecation")
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }
    }
}
