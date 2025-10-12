package com.devanshvyas.BackblazeStorage.model.metadata;

public enum ContentType {
    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT,
    OTHER;

    public static ContentType fromString(String type) {
        try {
            return ContentType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            return OTHER;
        }
    }
}
