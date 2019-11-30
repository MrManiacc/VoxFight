package me.jraynor.core.asset.assets;

import me.jraynor.core.asset.AssetMapper;
import me.jraynor.core.asset.assets.data.BlockData;
import me.jraynor.core.asset.urn.Urn;
import me.jraynor.core.registry.CoreRegistry;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static me.jraynor.core.asset.AssetMapper.AssetType.BLOCK;

public class BlockAsset extends Asset<BlockData> {
    public BlockAsset(Urn urn) {
        super(urn);
    }

    @Override
    protected void doReload(Optional<BlockData> data) {
        AssetMapper mapper = CoreRegistry.get(AssetMapper.class);
        Path path = Objects.requireNonNull(mapper).getAssetPath(BLOCK, urn);

    }
}
