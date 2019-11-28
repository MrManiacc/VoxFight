package me.jraynor.core.chunk;

public class ChunkCull {
    private Chunk localChunk;
    private Chunk[] neighbors;
    private boolean missingNeighbors = false;

    public ChunkCull(Chunk localChunk) {
        this.localChunk = localChunk;
    }

    public void setNeighbors(Chunk[] neighbors) {
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
    public boolean[] cullFaces(int x, int y, int z) {
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

    public void setNeighborsDirty() {
        for (Chunk chunk : neighbors)
            chunk.setDirty();
    }

    public boolean isVisible(boolean[] array) {
        for (boolean b : array) if (b) return true;
        return false;
    }

    public void updateLighting() {
        for (int x = 0; x < 16; x++)
            for (int y = 0; y < 256; y++)
                for (int z = 0; z < 16; z++) {
                    if (localChunk.blocks[x][y][z] == 9) {
                        localChunk.blockLights[x][y][z] = 10;
                        updateNeighbors(x, y, z, 10, localChunk);
                    }
                }
    }

    private void updateNeighbors(int x, int y, int z, int level, Chunk chunk) {
        if (level >= 3) {
            if (z + 1 <= 15 && chunk.blockLights[x][y][z + 1] < level) {
                chunk.blockLights[x][y][z + 1] = level;
                updateNeighbors(x, y, z + 1, level - 1, chunk);
            } else if (z + 1 == 16) {
                //EAST Chunk
                if (neighbors[2] != null)
                    if (neighbors[2].blockLights[x][y][0] < level) {
                        neighbors[2].blockLights[x][y][0] = level;
                        updateNeighbors(x, y, 0, level - 1, neighbors[2]);
                    } else
                        missingNeighbors = true;
            }
            if (z - 1 >= 0 && chunk.blockLights[x][y][z - 1] < level) {
                chunk.blockLights[x][y][z - 1] = level;
                updateNeighbors(x, y, z - 1, level - 1, chunk);
            } else if (z - 1 == -1) {
                //SOUTH Chunk
                if (neighbors[3] != null)
                    if (neighbors[3].getOrigin().y < 0) {
                        if (neighbors[3].blockLights[x][y][0] < level) {
                            neighbors[3].blockLights[x][y][0] = level;
                            updateNeighbors(x, y, 0, level - 1, neighbors[3]);
                        }
                    } else {
                        if (neighbors[3].blockLights[15][y][z] < level) {
                            neighbors[3].blockLights[15][y][z] = level;
                            updateNeighbors(x, y, 15, level - 1, neighbors[3]);
                        }
                    }
                else
                    missingNeighbors = true;
            }


            if (x + 1 <= 15 && chunk.blockLights[x + 1][y][z] < level) {
                chunk.blockLights[x + 1][y][z] = level;
                updateNeighbors(x + 1, y, z, level - 1, chunk);
            } else if (x + 1 == 16) {
                //EAST Chunk
                if (neighbors[1] != null) {
                    if (neighbors[1].blockLights[0][y][z] < level) {
                        neighbors[1].blockLights[0][y][z] = level;
                        updateNeighbors(0, y, z, level - 1, neighbors[1]);
                    }
                } else
                    missingNeighbors = true;
            }
            if (x - 1 >= 0 && chunk.blockLights[x - 1][y][z] < level) {
                chunk.blockLights[x - 1][y][z] = level;
                updateNeighbors(x - 1, y, z, level - 1, chunk);
            } else if (x - 1 == -1) {
                //WEST Chunk
                if (neighbors[0] != null) {
                    if (neighbors[0].getOrigin().x < 0) {
                        if (neighbors[0].blockLights[0][y][z] < level) {
                            neighbors[0].blockLights[0][y][z] = level;
                            updateNeighbors(0, y, z, level - 1, neighbors[0]);
                        }
                    } else {
                        if (neighbors[0].blockLights[15][y][z] < level) {
                            neighbors[0].blockLights[15][y][z] = level;
                            updateNeighbors(15, y, z, level - 1, neighbors[0]);
                        }
                    }

                } else
                    missingNeighbors = true;
            }


            if (y - 1 >= 0 && chunk.blockLights[x][y - 1][z] < level) {
                chunk.blockLights[x][y - 1][z] = level;
                updateNeighbors(x, y - 1, z, level - 1, chunk);

            }

            if (y + 1 <= 127 && chunk.blockLights[x][y + 1][z] < level) {
                chunk.blockLights[x][y + 1][z] = level;
                updateNeighbors(x, y + 1, z, level - 1, chunk);
            }
        }
    }

    public boolean isMissingNeighbors() {
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

    /**
     * This method will do face culling relative to blocks inside the chunk
     *
     * @param faces the outputted faces
     * @param x     the block x
     * @param y     the block y
     * @param z     the block z
     */
    private void cullInChunk(boolean[] faces, int x, int y, int z) {
        int airBlock = 0;
        if (x != 0)
            if (localChunk.blocks[x - 1][y][z] == airBlock)
                faces[3] = true;
        if (x != 15)
            if (localChunk.blocks[x + 1][y][z] == airBlock)
                faces[2] = true;


        if (y != 0) {
            if (localChunk.blocks[x][y - 1][z] == airBlock)
                faces[5] = true;
        } else
            faces[5] = true;

        if (y != 127) {
            if (localChunk.blocks[x][y + 1][z] == airBlock)
                faces[4] = true;
        } else
            faces[4] = true;

        if (z != 0)
            if (localChunk.blocks[x][y][z - 1] == airBlock)
                faces[0] = true;

        if (z != 15)
            if (localChunk.blocks[x][y][z + 1] == airBlock)
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
                int value = west.blocks[15][y][z];
                if (value == 0)
                    faces[3] = true;
            }
        }

        if (east == null) {
            if (x == 15)
                faces[2] = true;
        } else {
            if (x == 15) {
                int value = east.blocks[0][y][z];
                if (value == 0)
                    faces[2] = true;
            }
        }

        if (south == null) {
            if (z == 0)
                faces[0] = true;
        } else {
            if (z == 0) {
                int value = south.blocks[x][y][15];
                if (value == 0)
                    faces[0] = true;
            }
        }

        if (north == null) {
            if (z == 15)
                faces[1] = true;
        } else {
            if (z == 15) {
                int value = north.blocks[x][y][0];
                if (value == 0)
                    faces[1] = true;
            }
        }
    }


}
