package me.jraynor.core.chunk;

import me.jraynor.core.block.Blocks;
import me.jraynor.core.block.blocks.Block;

 class ChunkCull {
    private Chunk localChunk;
    private Chunk[] neighbors;

     ChunkCull(Chunk localChunk) {
        this.localChunk = localChunk;
    }

     void setNeighbors(Chunk[] neighbors) {
        this.neighbors = neighbors;
    }

    /**
     * This method is responsible for checking if there's a solid block at the offset positions in order to cull faces
     *
     * @param x the x block position
     * @param y the z block position
     * @param z the z block position
     * @return the array of faces to draw for the given position
     */
     boolean[] cullFaces(int x, int y, int z) {
        boolean[] faces = new boolean[]{
                false, false, false, false, false, false
        };
        cullInChunk(faces, x, y, z);
        if (neighbors != null)
            cullWithNeighbors(faces, x, y, z);
        else
            cullSides(faces, x, y, z);
        return faces;
    }

     void setNeighborsDirty() {
        for (Chunk chunk : neighbors)
            chunk.setDirty();
    }

     boolean isVisible(boolean[] array) {
        for (boolean b : array) if (b) return true;
        return false;
    }

     boolean isMissingNeighbors() {
         boolean missingNeighbors = false;
         return missingNeighbors;
    }

    private void cullSides(boolean[] faces, int x, int y, int z) {
        if (x == 0)
            faces[3] = true;
        if (x == 15)
            faces[2] = true;
        if (z == 0)
            faces[0] = true;
        if (z == 15)
            faces[1] = true;
        if (y == 0)
            faces[5] = true;
        if (y == 127)
            faces[4] = true;
    }

    private boolean shouldCullBlock(Chunk chunk, int x, int y, int z) {
        return Blocks.getBlock(chunk.getBlock(x, y, z)).isSpecialRender() || chunk.getBlock(x, y, z) == 0;
    }

    /**
     * This method will do face culling relative to blocks inside the chunk
     *
     * @param faces the outputted faces
     * @param x     the block x
     * @param y     the block y
     * @param z     the block z
     */
    private void cullInChunk(boolean[] faces, int x, int y, int z) {
        if (x != 0)
            if (shouldCullBlock(localChunk, x - 1, y, z))
                faces[3] = true;
        if (x != 15)
            if (shouldCullBlock(localChunk, x + 1, y, z))
                faces[2] = true;


        if (y != 0) {
            if (shouldCullBlock(localChunk, x, y - 1, z))
                faces[5] = true;
        } else
            faces[5] = true;

        if (y != 127) {
            if (shouldCullBlock(localChunk, x, y + 1, z))
                faces[4] = true;
        } else
            faces[4] = true;

        if (z != 0)
            if (shouldCullBlock(localChunk, x, y, z - 1))
                faces[0] = true;

        if (z != 15)
            if (shouldCullBlock(localChunk, x, y, z + 1))
                faces[1] = true;

    }

    /**
     * This method will cull the faces with respect to the surrounding chunks
     *
     * @param faces the outputted faces
     * @param x     the block x
     * @param y     the block y
     * @param z     the block z
     */
    private void cullWithNeighbors(boolean[] faces, int x, int y, int z) {
        Chunk west = neighbors[0];
        Chunk east = neighbors[1];
        Chunk north = neighbors[2];
        Chunk south = neighbors[3];

        if (west == null) {
            if (x == 0)
                faces[3] = true;
        } else {
            if (x == 0) {
                if (shouldCullBlock(west, 15, y, z))
                    faces[3] = true;
            }
        }

        if (east == null) {
            if (x == 15)
                faces[2] = true;
        } else {
            if (x == 15) {
                if (shouldCullBlock(east, 0, y, z))
                    faces[2] = true;
            }
        }

        if (south == null) {
            if (z == 0)
                faces[0] = true;
        } else {
            if (z == 0) {
                if (shouldCullBlock(south, x, y, 15))
                    faces[0] = true;
            }
        }

        if (north == null) {
            if (z == 15)
                faces[1] = true;
        } else {
            if (z == 15) {
                if (shouldCullBlock(north, x, y, 0))
                    faces[1] = true;
            }
        }
    }


}
