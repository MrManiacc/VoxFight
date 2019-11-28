package me.jraynor.core.entity;

import org.joml.Vector3f;

public class BaseEntity {
    Vector3f position;
    Vector3f rotation;
    Vector3f scale;

    public BaseEntity(Vector3f position) {
        this.position = position;
        this.rotation = new Vector3f();
        this.scale = new Vector3f(1);
    }

    public void update(float deltaTime){
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
