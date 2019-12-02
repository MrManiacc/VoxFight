package me.jraynor.engine.asset.assets;

import lombok.Getter;
import me.jraynor.engine.asset.assets.data.AssetData;
import me.jraynor.engine.asset.disposal.DisposalHook;
import me.jraynor.engine.asset.urn.Urn;

import java.util.Optional;

public abstract class Asset<T extends AssetData> {
    @Getter
    protected Urn urn;
    private volatile boolean disposed;
    @Getter
    private boolean loaded = false;
    @Getter
    private final DisposalHook disposalHook = new DisposalHook();
    @Getter
    protected T data;

    public Asset(Urn urn) {
        this.urn = urn;
    }

    /**
     * Reloads this assets using the new data.
     *
     * @param data The data to reload the asset with.
     */
    public final synchronized void reload(T data) {
        if (!disposed) {
            this.data = data;
            doReload(Optional.of(data));
        } else {
            throw new IllegalStateException("Cannot reload disposed asset '" + urn + "'");
        }
    }

    /**
     * reloads a asset without any new data, used mainly for
     * rebuilding the data from files for blocks
     */
    public final synchronized void reload() {
        if (!disposed) {
            doReload(Optional.ofNullable(data));
        } else {
            throw new IllegalStateException("Cannot reload disposed asset '" + urn + "'");
        }
    }

    /**
     * Disposes this asset, freeing resources and making it unusable
     */
    public final synchronized void dispose() {
        if (!disposed) {
            disposed = true;
            disposalHook.dispose();
        }
    }

    /**
     * Called to reload an asset with the given data.
     *
     * @param data The data to load.
     */
    protected abstract void doReload(Optional<T> data);


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Asset) {
            Asset other = (Asset) obj;
            return other.urn.equals(urn);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return urn.hashCode();
    }

    @Override
    public String toString() {
        return urn.toString();
    }

}
