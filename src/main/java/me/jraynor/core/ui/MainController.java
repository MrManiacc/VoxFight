package me.jraynor.core.ui;

import me.jraynor.bootstrap.Window;
import me.jraynor.engine.block.Blocks;
import me.jraynor.engine.block.blocks.Block;
import me.jraynor.core.world.World;
import me.jraynor.uison.controller.Component;
import me.jraynor.uison.controller.DefaultController;
import me.jraynor.uison.elements.UILabel;

import java.text.DecimalFormat;

public class MainController extends DefaultController {
    @Component(id = "chunk_origin")
    public UILabel chunkOrigin;
    @Component(id = "player_pos")
    public UILabel playerPosition;
    @Component(id = "chunk_status")
    public UILabel chunkStatus;
    @Component(id = "chunk_culled")
    public UILabel chunkCulled;
    @Component(id = "active_block")
    public UILabel activeBlock;
    @Component(id = "looking_at")
    public UILabel lookingAt;
    private World world;

    public MainController(Window window, World world) {
        super(window, false);
        this.world = world;
    }

    @Override
    public void update() {
        super.update();
        if (world.getPlayerEntity().isBlockSelected()) {
            lookingAt.setActive(true);
            Block activeBlock = Blocks.getBlock(world.getBlock(world.getPlayerEntity().getActiveBlock()));
            lookingAt.setText(activeBlock.getDisplayName());
        } else {
            lookingAt.setActive(false);
        }

        chunkOrigin.setText(world.getPlayerEntity().getChunk().toString(DecimalFormat.getInstance()));
        playerPosition.setText(world.getPlayerEntity().getPosition().toString(DecimalFormat.getIntegerInstance()));

        if (world.getChunk(world.getPlayerEntity().getPosition()) == null) {
            chunkStatus.setText("unloaded");
        } else {
            chunkStatus.setText("loaded");
        }
        chunkCulled.setText(world.getCulledChunks() + "");

        activeBlock.setText(world.getPlayerEntity().getActiveBlock().toString(DecimalFormat.getNumberInstance()));
    }

}
