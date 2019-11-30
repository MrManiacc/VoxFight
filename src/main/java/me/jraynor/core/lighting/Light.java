package me.jraynor.core.lighting;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

public class Light {
    @Getter
    @Setter
    private Vector3f position, color;
    @Getter
    @Setter
    private float linear, quadratic;
    @Getter
    @Setter
    private float radius;

    public Light(Vector3f position, Vector3f color, float linear, float quadratic, float radius) {
        this.position = position;
        this.color = color;
        this.linear = linear;
        this.quadratic = quadratic;
        this.radius = radius;
    }

    public Light(Vector3f position, Vector3f color, float linear, float quadratic) {
        this.position = position;
        this.color = color;
        this.linear = linear;
        this.quadratic = quadratic;

    }
}
