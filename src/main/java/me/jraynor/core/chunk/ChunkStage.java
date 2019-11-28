package me.jraynor.core.chunk;

public enum ChunkStage {
    STAGE_ZERO,//NOTHING GENERATED, CHUNK JUST CREATED
    STAGE_ONE, //THE BLOCKS ARE GENERATED, NEIGHBORS NOT YET LOADED
    STAGE_TWO, //NEIGHBORS ARE GENERATED AND WE HAVE A REFERENCE TO THEM
    STAGE_THREE, //BLOCK FACES HAVE BEEN CULLED, AND MODEL IS READY TO BE RENDERED
    STAGE_FOUR, //CHUNK IS DIRTY, NEEDS TO GO BACK TO STAGE_TWO
}
