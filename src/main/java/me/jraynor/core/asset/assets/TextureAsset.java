package me.jraynor.core.asset.assets;

import me.jraynor.core.asset.AssetMapper;
import me.jraynor.core.asset.assets.data.TextureData;
import me.jraynor.core.asset.urn.Urn;
import me.jraynor.core.registry.CoreRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.MemoryStack;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.stb.STBImage.*;

public class TextureAsset extends Asset<TextureData> {
    private int textureID = 0;
    private Logger logger = LogManager.getLogger();
    public TextureAsset(Urn urn) {
        super(urn);
    }

    @Override
    protected void doReload(Optional<TextureData> data) {
        if (textureID != 0)
            glDeleteTextures(textureID);
        textureID = glGenTextures();

        AssetMapper mapper = CoreRegistry.get(AssetMapper.class);
        Path path = Objects.requireNonNull(mapper).getAssetPath(AssetMapper.AssetType.IMAGE, urn);
        ByteBuffer imageBuffer = createBuffer(path.normalize().toString());
        if (imageBuffer == null) {
            logger.error("{} texture could not be loaded.", urn.toString());
            dispose();
            return;
        }
        updateData(imageBuffer);
        logger.debug("{} texture successfully loaded.", urn.toString());
    }

    /**
     * Load the texture from the texture data
     *
     * @param data The data to load.
     */
    protected void doReload(TextureData data) {

    }

    /**
     * Puts the data into the opengl buffer
     *
     * @param image
     */
    private void updateData(ByteBuffer image) {
        bind();
        glTexParameteri(data.getType().value, GL_TEXTURE_WRAP_S, data.getWrapMode().value);
        glTexParameteri(data.getType().value, GL_TEXTURE_WRAP_T, data.getWrapMode().value);
        glTexParameteri(data.getType().value, GL_TEXTURE_WRAP_T, data.getWrapMode().value);
        glTexParameteri(data.getType().value, GL_TEXTURE_MIN_FILTER, data.getFilterMode().value);
        glTexParameteri(data.getType().value, GL_TEXTURE_MAG_FILTER, data.getFilterMode().value);
        glTexImage2D(data.getType().value, 0, GL_RGBA8, data.getWidth(), data.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        stbi_image_free(image);
    }

    /**
     * Load texture from file.
     *
     * @param path File path of the texture
     * @return TextureAsset from specified file
     */
    private ByteBuffer createBuffer(String path) {
        ByteBuffer image;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            /* Prepare image buffers */
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            /* Load image */

            stbi_set_flip_vertically_on_load(data.isFlipVertically());
            image = stbi_load(path, w, h, comp, 4);
            if (image == null) {
                return null;
            }
            data.setWidth(w.get());
            data.setWidth(h.get());
        }
        return image;
    }

    /**
     * Binds the texture to the specified texture unit
     */
    public void bind(int textureUnit) {
        glActiveTexture(GL_TEXTURE0 + textureUnit);
        bind();
    }

    public void bind() {
        glBindTexture(data.getType().value, textureID);
    }


    public enum WrapMode {
        CLAMP_BORER(GL_CLAMP_TO_BORDER),
        CLAMP_EDGE(GL_CLAMP_TO_EDGE),
        REPEAT(GL_REPEAT);
        int value;

        WrapMode(int value) {
            this.value = value;
        }
    }

    public enum FilterMode {
        NEAREST(GL_NEAREST),
        LINEAR(GL_LINEAR);
        int value;

        FilterMode(int value) {
            this.value = value;
        }
    }

    public enum Type {
        TEXTURE1D(GL_TEXTURE_1D),
        TEXTURE2D(GL_TEXTURE_2D),
        TEXTURE3D(GL_TEXTURE_3D);
        int value;

        Type(int value) {
            this.value = value;
        }
    }


}
