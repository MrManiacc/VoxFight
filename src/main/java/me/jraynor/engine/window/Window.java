package me.jraynor.engine.window;

import lombok.Getter;

/**
 * Represents a window, abstract so it can be used for things other
 * than opengl
 */
public abstract class Window {
    @Getter
    protected String title;
    @Getter
    protected int width, height;
    @Getter
    protected boolean fullscreen, vSync, resized, borderless, resizeable;

    public Window(String title, int width, int height) {
        this(title, width, height, false, false);
    }

    public Window(String title, int width, int height, boolean fullscreen) {
        this(title, width, height, fullscreen, !fullscreen); //resizeable by default if not fullscreen
    }


    public Window(String title, int width, int height, boolean fullscreen, boolean resizeable) {
        this(title, width, height, fullscreen, resizeable, false);
    }

    public Window(String title, int width, int height, boolean fullscreen, boolean resizeable, boolean vSync) {
        this(title, width, height, fullscreen, resizeable, vSync, false);
    }

    public Window(String title, int width, int height, boolean fullscreen, boolean resizeable, boolean vSync, boolean borderless) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;
        this.resizeable = resizeable;
        this.vSync = vSync;
        this.borderless = borderless;
        this.resized = false;
    }

    /**
     * Hide the window
     */
    public abstract void hide();

    /**
     * Show the window
     */
    public abstract void show();

    /**
     * Check if a window close event has been requested (when the X button is pressed most time)
     *
     * @return window close state
     */
    public abstract boolean shouldClose();

    /**
     * Create the window
     */
    public abstract void createWindow();

    /**
     * Update the window
     */
    public abstract void update();

    /**
     * Close the window
     */
    public abstract void disposeWindow();
}
