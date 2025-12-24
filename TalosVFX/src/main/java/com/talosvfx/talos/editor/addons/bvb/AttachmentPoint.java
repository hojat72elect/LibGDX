package com.talosvfx.talos.editor.addons.bvb;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.runtime.vfx.values.NumericalValue;

public class AttachmentPoint implements Json.Serializable {

    private Type type;
    private AttachmentType attachmentType = AttachmentType.POSITION;
    private int attachedToSlot = 0;

    /**
     * Attached to bone
     */
    private String boneName;
    private Vector2 offset = new Vector2();
    private float boneScale = 1.0f;


    /**
     * attached to static numeric value
     */
    private final NumericalValue numericalValue = new NumericalValue();

    public AttachmentPoint() {
        setTypeStatic(numericalValue, 0);
    }

    public boolean isStatic() {
        return type == Type.STATIC;
    }

    public boolean seAttached() {
        return type == Type.ATTACHED;
    }

    public AttachmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(AttachmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    public NumericalValue getStaticValue() {
        return numericalValue;
    }

    public void setBoneScale(float scale) {
        this.boneScale = scale;
    }

    public void setTypeAttached(String bone, int toSlot) {
        type = Type.ATTACHED;
        this.attachedToSlot = toSlot;
        attachmentType = AttachmentType.POSITION;
        boneName = bone;
    }

    public void setTypeStatic(NumericalValue value, int toSlot) {
        type = Type.STATIC;
        this.attachedToSlot = toSlot;
        numericalValue.set(value);
    }

    public void setTypeAttached() {
        type = Type.ATTACHED;
        attachmentType = AttachmentType.POSITION;
        if (boneName == null || boneName.isEmpty()) boneName = "root";
    }

    public void setTypeStatic() {
        type = Type.STATIC;
    }

    public void setTypeAttached(AttachmentType attachmentType) {
        if (type == Type.ATTACHED) {
            this.attachmentType = attachmentType;
        }
    }

    public void setBone(String name) {
        boneName = name;
    }

    @Override
    public int hashCode() {
        return attachedToSlot;
    }

    public void setOffset(float offsetX, float offsetY) {
        offset.set(offsetX, offsetY);
    }

    public float getWorldOffsetX() {
        return offset.x * boneScale;
    }

    public float getWorldOffsetY() {
        return offset.y * boneScale;
    }

    public String getBoneName() {
        return boneName;
    }

    public int getSlotId() {
        return attachedToSlot;
    }

    public void setSlotId(int id) {
        attachedToSlot = id;
    }

    public float getOffsetX() {
        return offset.x;
    }

    public void setOffsetX(float offsetX) {
        offset.x = offsetX;
    }

    public float getOffsetY() {
        return offset.y;
    }

    public void setOffsetY(float offsetY) {
        offset.y = offsetY;
    }

    @Override
    public void write(Json json) {
        if (attachedToSlot >= 0) {
            json.writeValue("slotId", attachedToSlot);
        }
        json.writeValue("type", type.name());
        if (type == Type.ATTACHED) {
            json.writeValue("attachmentType", attachmentType.name());
            json.writeValue("boneName", boneName);
            json.writeValue("offset", offset);
        } else {
            json.writeArrayStart("value");
            json.writeValue(numericalValue.get(0));
            json.writeValue(numericalValue.get(1));
            json.writeValue(numericalValue.get(2));
            json.writeArrayEnd();
        }
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        attachedToSlot = jsonData.getInt("slotId", -1);
        type = Type.valueOf(jsonData.getString("type", Type.STATIC.name()));

        if (type == Type.ATTACHED) {
            attachmentType = AttachmentType.valueOf(jsonData.getString("attachmentType", AttachmentType.POSITION.name()));
            boneName = jsonData.getString("boneName");
            offset = json.readValue(Vector2.class, jsonData.get("offset"));
        } else {
            JsonValue arr = jsonData.get("value");
            numericalValue.set(arr.get(0).asFloat(), arr.get(1).asFloat(), arr.get(2).asFloat());
        }
    }

    public float getOffsetLength() {
        return offset.angle();
    }

    public float getOffsetAngle() {
        return offset.angle();
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        STATIC,
        ATTACHED
    }

    public enum AttachmentType {
        POSITION,
        ROTATION,
        TRANSPARENCY,
        COLOR;

        private static final AttachmentType[] vals = values();

        public AttachmentType next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }
    }
}
