package me.jraynor.engine.window.internal;

import lombok.Getter;
import me.jraynor.engine.window.Window;
import me.jraynor.uison.misc.Input;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * A simple wrapper to keep track of the opengl window
 */
public class WindowGlfw extends Window {
    private final Logger logger = LogManager.getLogger();
    private long window; //The GLFW window reference

    public WindowGlfw(String title, int width, int height) {
        super(title, width, height);
    }

    public WindowGlfw(String title, int width, int height, boolean fullscreen) {
        super(title, width, height, fullscreen);
    }

    public WindowGlfw(String title, int width, int height, boolean fullscreen, boolean resizeable) {
        super(title, width, height, fullscreen, resizeable);
    }

    public WindowGlfw(String title, int width, int height, boolean fullscreen, boolean resizeable, boolean vSync) {
        super(title, width, height, fullscreen, resizeable, vSync);
    }

    public WindowGlfw(String title, int width, int height, boolean fullscreen, boolean resizeable, boolean vSync, boolean borderless) {
        super(title, width, height, fullscreen, resizeable, vSync, borderless);
    }

    /**
     * Hide the window
     */
    public void hide() {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    }

    /**
     * Show the window
     */
    public void show() {
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
    }


    /**
     * Create the window
     */
    public void createWindow() {
        if (!glfwInit()) {
            logger.error("Couldn't initialize the glfw context!");
            throw new IllegalStateException("Failed to create window!");
        }

        loadHints(); //load the hints

        window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0, 0);
        if (window == 0) throw new IllegalStateException("Failed to create window!");

        if (!fullscreen) {
            GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vid != null;
            glfwSetWindowPos(window, (vid.width() - width) / 2, (vid.height() - height) / 2);
        }

        glfwShowWindow(window);

        glfwMakeContextCurrent(window);

        pushWindow();
        finishWindow();
        createCallbacks();
        Input.init(window);
        logger.info("{} window created width: {}, height: {}, fullscreen: {}, vSync: {}", title, width, height, fullscreen, vSync);

    }

    /**
     * The final step, sets current context and disables/enables vsync, then shows the window
     */
    private void finishWindow() {
        glfwMakeContextCurrent(window);
        glfwSwapInterval(vSync ? 1 : 0);
        glfwShowWindow(window);
        GL.createCapabilities();
    }


    /**
     * If the window close has been requested or not
     *
     * @return window close status
     */
    public boolean shouldClose() {
        if (window == 0)
            return true;
        return glfwWindowShouldClose(window);
    }

    /**
     * Update the window should be called after all game updates have happend
     */
    public void update() {
        resized = false;
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    /**
     * Set the opengl hints
     */
    private void loadHints() {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, resizeable ? GLFW_TRUE : GLFW_FALSE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        glfwWindowHint(GLFW_DECORATED, borderless ? GLFW_FALSE : GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, fullscreen ? GLFW_TRUE : GLFW_FALSE);
    }

    /**
     * Get the thread stack and push a new frame, centers the window as well
     */
    private void pushWindow() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);
            if (!fullscreen) {
                GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
                glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
            }
        }
    }


    /**
     * register a resize callback
     */
    private void createCallbacks() {
        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.resized = true;
            glViewport(0, 0, width, height);
        });
    }

    /**
     * Close the opengl windwo
     */
    public void disposeWindow() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
        logger.info("Window with id '{}' closed gracefully", window);
    }


}
