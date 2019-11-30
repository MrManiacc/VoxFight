package me.jraynor.core.entity;

import lombok.Getter;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class BaseEntity {
    Vector3f position;
    Vector3f rotation;
    Vector3f scale;
    @Getter
    Vector3i block;
    @Getter
    boolean moved;

    public BaseEntity(Vector3f position) {
        this.position = position;
        this.rotation = new Vector3f();
        this.scale = new Vector3f(1);
        this.block = new Vector3i();
    }

    public void update(float deltaTime) {
        int x = Math.round(position.x);
        int y = Math.round(position.y);
        int z = Math.round(position.z);
        if (block.x != x || block.y != y || block.z != z) {
            this.block.x = x;
            this.block.y = y;
            this.block.z = z;
            moved = true;
        } else
            moved = false;
    }


    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setScale(float x, float y, float z) {
        this.scale.x = x;
        this.scale.y = y;
        this.scale.z = z;
    }

    public Vector3f getScale() {
        return scale;
    }


}
