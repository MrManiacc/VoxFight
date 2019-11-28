package me.jraynor.core.world;

import me.jraynor.core.chunk.Chunk;
import me.jraynor.gl.*;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.glEnable;

class WorldRenderer {
    private Shader shader;
    private Texture texture;

    void init() {
        texture = Texture.loadTexture("atlas.png");
        shader = new Shader("chunks") {
            @Override
            protected void doBinds() {
                binds(new ShaderBind("vertex", 0), new ShaderBind("uv", 1), new ShaderBind("light", 2));
            }

            @Override
            protected void doUniformBinds() {
                bindUniforms("proMatrix", "viewMatrix", "transMatrix");
            }
        };
    }


    void start(Camera camera) {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        GLUtils.antialias(true);
        GLUtils.enableAlphaBlending();
        shader.start();
        shader.loadMat4("proMatrix", camera.getProjectionMatrix());
        shader.loadMat4("viewMatrix", camera.getViewMatrix());
        texture.bind();
    }

    void render(Chunk chunk) {
        if (chunk.isReady() && !chunk.isCulled()) {
            shader.loadMat4("transMatrix", chunk.getTransform());
            chunk.getModel().bind(0, 1, 2);
            GL11.glDrawElements(GL11.GL_TRIANGLES, chunk.getModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
            chunk.getModel().unbind(0, 1, 2);
        }
    }

    void stop() {
        shader.stop();
    }

}


