package me.jraynor.core.lighting;

import me.jraynor.bootstrap.Window;
import me.jraynor.core.chunk.Chunk;
import me.jraynor.core.gl.Camera;
import me.jraynor.core.gl.Shader;
import me.jraynor.core.gl.ShaderBind;
import me.jraynor.core.gl.Vao;
import me.jraynor.core.world.World;
import me.jraynor.core.world.WorldRenderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class Pipeline {
    private final Window window;
    private final GBuffer buffer;
    private final World world;
    private final WorldRenderer worldRenderer;
    private final Camera camera;
    private Shader geometryPassShader, lightingPassShader, debugShader, sphereShader;
    private Vao quadVao, cubeVao;
    private Matrix4f sphereMatrix;

    public Pipeline(Window window, World world) {
        this.window = window;
        this.world = world;
        this.buffer = new GBuffer(window.getWidth(), window.getHeight());
        this.worldRenderer = world.getWorldRenderer();
        this.camera = world.getPlayerEntity().getCamera();
        this.sphereMatrix = new Matrix4f().identity();
    }

    public void init() {


        buffer.init();
        geometryPassShader = new Shader("geo") {
            @Override
            protected void doBinds() {
                super.binds(new ShaderBind("aPos", 0), new ShaderBind("aNormal", 1), new ShaderBind("aTexCoords", 2));
            }

            @Override
            protected void doUniformBinds() {
                super.start();
                super.bindUniforms("model", "view", "projection", "diffuse");
                super.loadSampler("diffuse", 3);
                super.stop();
            }
        };

        lightingPassShader = new Shader("lighting") {
            @Override
            protected void doBinds() {
                super.binds(new ShaderBind("aPos", 0), new ShaderBind("aNormal", 1), new ShaderBind("aTexCoords", 2));
            }

            @Override
            protected void doUniformBinds() {
                super.start();
                for (int i = 0; i < world.getLights().length; i++)
                    super.bindUniforms("lights[" + i + "].Position", "lights[" + i + "].Color", "lights[" + i + "].Linear", "lights[" + i + "].Quadratic");

                super.bindUniforms("model", "view", "projection", "gPosition", "gNormal", "gDiffuse", "viewPos");
                super.loadSampler("gPosition", 0);
                super.loadSampler("gNormal", 1);
                super.loadSampler("gDiffuse", 2);
                super.stop();
            }
        };

        sphereShader = new Shader("sphere") {
            @Override
            protected void doBinds() {
                super.binds(new ShaderBind("aPos", 0));
            }

            @Override
            protected void doUniformBinds() {
                super.bindUniforms("model", "view", "projection", "lightColor");
            }
        };

        debugShader = new Shader("debug") {
            @Override
            protected void doBinds() {
                super.binds(new ShaderBind("aPos", 0), new ShaderBind("aTexCoords", 1));
            }

            @Override
            protected void doUniformBinds() {
                super.start();
                super.bindUniforms("gPosition", "gNormal", "gDiffuse");
                super.loadSampler("gPosition", 0);
                super.loadSampler("gNormal", 1);
                super.loadSampler("gDiffuse", 2);
                super.stop();
            }
        };

        createModels();
        glEnable(GL_DEPTH_TEST);
    }

    private void createModels() {
        quadVao = Vao.create();
        quadVao.bind(0, 1);
        quadVao.createAttribute(0, new float[]{
                // positions        // texture Coords
                -1, 1,
                -1, -1,
                1, 1,
                1, -1
        }, 2);
        quadVao.createAttribute(1, new float[]{
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,}, 2);
        quadVao.unbind(0, 1);


        cubeVao = Vao.create();
        cubeVao.bind(0);
        cubeVao.createAttribute(0, new float[]{
                -1.0f, -1.0f, -1.0f, // triangle 1 : begin
                -1.0f, -1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f, // triangle 1 : end
                1.0f, 1.0f, -1.0f, // triangle 2 : begin
                -1.0f, -1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f, // triangle 2 : end
                1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, -1.0f,
                1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, -1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, 1.0f
        }, 3);
        cubeVao.unbind(0);
    }

    public void render() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        geometryPass();
        lightingPass();
    }

    private void geometryPass() {
        buffer.bind();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Chunk.atlas.bindToUnit(3);
        geometryPassShader.start();
        geometryPassShader.loadMat4("view", camera.getViewMatrix());
        geometryPassShader.loadMat4("projection", camera.getProjectionMatrix());
        for (Chunk chunk : world.getChunks()) {
            if (chunk.getModel() == null) {
                continue;
            }
            geometryPassShader.loadMat4("model", chunk.getTransform());
            chunk.getModel().bind(0, 1, 2);
            glDrawElements(GL11.GL_TRIANGLES, chunk.getModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
            chunk.getModel().unbind(0, 1, 2);
        }
        geometryPassShader.stop();
        buffer.unbind();
    }

    private void lightingPass() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        lightingPassShader.start();
        buffer.activateTextures();
        loadLights();
        lightingPassShader.loadVec3("viewPos", world.getPlayerEntity().getPosition());
        quadVao.bind(0, 1);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        quadVao.unbind(0, 1);
        lightingPassShader.stop();
        buffer.copyDepth();
    }

    private void loadLights() {
        for (int i = 0; i < 32; i++) {
            if (world.getLights()[i] != null) {
                lightingPassShader.loadVec3("lights[" + i + "].Position", world.getLights()[i].getPosition());
                lightingPassShader.loadVec3("lights[" + i + "].Color", world.getLights()[i].getColor());
                lightingPassShader.loadFloat("lights[" + i + "].Linear", world.getLights()[i].getLinear());
                lightingPassShader.loadFloat("lights[" + i + "].Quadratic", world.getLights()[i].getQuadratic());
            }
        }
    }

    private void debugShaderPass() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        debugShader.start();
        buffer.activateTextures();
        quadVao.bind(0, 1);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        quadVao.unbind(0, 1);
        debugShader.stop();
        buffer.copyDepth();
    }


}
