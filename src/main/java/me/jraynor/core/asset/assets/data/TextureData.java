package me.jraynor.core.asset.assets.data;

import lombok.Getter;
import lombok.Setter;
import me.jraynor.core.asset.assets.TextureAsset;

public class TextureData implements AssetData {
    @Getter
    @Setter
    private int width;
    @Getter
    @Setter
    private int height;
    @Getter
    @Setter
    private TextureAsset.WrapMode wrapMode;
    @Getter
    @Setter
    private TextureAsset.FilterMode filterMode;
    @Getter
    @Setter
    private TextureAsset.Type type;
    @Getter
    @Setter
    private boolean flipVertically = true;


    public TextureData() {
        this.wrapMode = TextureAsset.WrapMode.CLAMP_EDGE;
        this.filterMode = TextureAsset.FilterMode.NEAREST;
        this.type = TextureAsset.Type.TEXTURE2D;
    }

    public TextureData(TextureAsset.WrapMode wrapMode, TextureAsset.FilterMode filterMode) {
        this.wrapMode = wrapMode;
        this.filterMode = filterMode;
        this.type = TextureAsset.Type.TEXTURE2D;
    }

    public TextureData(TextureAsset.WrapMode wrapMode, TextureAsset.FilterMode filterMode, TextureAsset.Type type, boolean flipVertically) {
        this.wrapMode = wrapMode;
        this.filterMode = filterMode;
        this.type = type;
        this.flipVertically = flipVertically;
    }
}
