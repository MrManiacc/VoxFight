package me.jraynor.core.asset.internal;

import com.google.common.collect.Sets;
import me.jraynor.core.asset.AssetMapper;
import me.jraynor.core.asset.urn.Name;
import me.jraynor.core.asset.urn.Urn;
import me.jraynor.core.context.Context;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Immutable
public final class AssetMapperImpl extends AssetMapper {
    private Logger logger = LogManager.getLogger(AssetMapper.class);
    @Autowired
    private ResourceLoader resourceLoader;
    private final Set<Name> moduleNames = Sets.newConcurrentHashSet();

    public AssetMapperImpl(Context context) {
        super(context);
        context.put(AssetMapper.class, this);
    }

    /**
     * Finds all of the assets and maps them accordingly
     *
     * @throws IOException
     */
    public void populateMaps() throws IOException {
        assetMapper.values().forEach(Map::clear);
        moduleNames.clear();
        mapAsset(assetMapper.get(AssetType.BLOCK), "classpath*:/modules/**/blocks/*.blk");
        mapAsset(assetMapper.get(AssetType.SHADER), "classpath*:/modules/**/shaders/*.glsl");
        mapAsset(assetMapper.get(AssetType.FONT), "classpath*:/modules/**/fonts/*.ttf");
        mapAsset(assetMapper.get(AssetType.SCRIPT), "classpath*:/modules/**/scripts/*.lua");
        mapAsset(assetMapper.get(AssetType.IMAGE), "classpath*:/modules/**/textures/*.png");
        mapAsset(assetMapper.get(AssetType.IMAGE), "classpath*:/modules/**/textures/*.jpg");
        mapAsset(assetMapper.get(AssetType.IMAGE), "classpath*:/modules/**/textures/*.jpeg");

        logger.info("{} total {} found: {}", this.getModulesCount(), this.getModulesCount() > 1 ? "modules" : "module", this.getModuleNames());
    }

    /**
     * @param pathMap
     * @param pattern
     * @throws IOException
     */
    private void mapAsset(Map<Urn, Path> pathMap, String pattern) throws IOException {
        Resource[] resources = loadResources(pattern);
        for (Resource resource : resources) {
            Name module = extractModule(resource);
            moduleNames.add(module);
            Name name = extractName(resource);
            logger.debug("[{}:{}] found at path '{}'", module.toLowerCase(), name.toLowerCase(), resource.getURI().toString());
            if (!module.isEmpty() && !name.isEmpty())
                pathMap.put(new Urn(module, name), Paths.get(resource.getURI()));
        }
    }

    /**
     * Extracts a name module from a resource
     */
    private Name extractModule(Resource resource) throws IOException {
        if (resource.isFile() && resource.exists()) {
            File file = resource.getFile();
            if (file.getParentFile().getParentFile().exists() && file.getParentFile().getParentFile().isDirectory())
                return new Name(file.getParentFile().getParentFile().getName());
        }
        return Name.EMPTY;
    }

    /**
     * Gets the name of the resource
     *
     * @param resource
     * @return file name
     */
    private Name extractName(Resource resource) {
        if (resource.isFile() && resource.exists())
            return new Name(FilenameUtils.removeExtension(resource.getFilename()));
        return Name.EMPTY;
    }

    /**
     * gets the path for the given urn
     *
     * @return the path or null
     */
    public Path getAssetPath(AssetType assetType, String module, String assetName) {
        return getAssetPath(assetType, new Urn(module, assetName));
    }

    /**
     * gets the path for the given urn
     *
     * @return the path or null
     */
    public Path getAssetPath(AssetType assetType, Name module, Name assetName) {
        return getAssetPath(assetType, module.toString(), assetName.toString());
    }

    /**
     * gets the path for the given urn
     *
     * @param urn
     * @return the path or null
     */
    public Path getAssetPath(AssetType assetType, Urn urn) {
        return assetMapper.get(assetType).get(urn);
    }

    /**
     * Get all of the current moduleNames from the loaded names set
     *
     * @return list of moduleNames
     */
    public Name[] getModuleNames() {
        return moduleNames.toArray(new Name[0]);
    }

    /**
     * Get the iterator for the modules
     *
     * @return
     */
    public Iterator<Name> getModulesIterator() {
        return moduleNames.iterator();
    }

    /**
     * Gets the total number of modules
     *
     * @return module count
     */
    public int getModulesCount() {
        return moduleNames.size();
    }

    /**
     * Helper method to find the locations of the correct assets
     *
     * @param pattern
     * @return
     * @throws IOException
     */
    private Resource[] loadResources(String pattern) throws IOException {
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
    }

    public String getMessage() {
        return "Mapping asset paths...";
    }

    /**
     * Maps the assets to their paths
     *
     * @return
     */
    public boolean step() {
        try {
            populateMaps();
        } catch (IOException e) {
            logger.error("Failed to load asset resources, {}", e.getMessage());
        }
        return true;
    }

    public int getExpectedCost() {
        return 1;
    }
}
