package net.minepos.daemon.guice.objects;

import java.lang.annotation.Annotation;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class AnnotatedBinding {
    private final Class clazz;
    private final Class<? extends Annotation> annotation;
    private final Object instance;

    public AnnotatedBinding(Class clazz, Class<? extends Annotation> annotation, Object instance) {
        this.clazz = clazz;
        this.annotation = annotation;
        this.instance = instance;
    }


    public Class getClazz() {
        return clazz;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    public Object getInstance() {
        return instance;
    }
}
