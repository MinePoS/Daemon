package net.minepos.daemon.guice.module;

import com.google.inject.AbstractModule;
import net.minepos.daemon.guice.objects.AnnotatedBinding;

import java.util.List;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class RegisterableChildModule extends AbstractModule {
    private final Map<Class, Object> bindings;
    private final List<AnnotatedBinding> annotatedBindings;
    private final Class[] staticInjections;

    public RegisterableChildModule(Map<Class, Object> bindings, List<AnnotatedBinding> annotatedBindings, Class[] staticInjections) {
        this.bindings = bindings;
        this.annotatedBindings = annotatedBindings;
        this.staticInjections = staticInjections;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure() {
        bindings.forEach((c, o) -> bind(c).toInstance(o));
        annotatedBindings.forEach(b -> bind(b.getClazz()).annotatedWith(b.getAnnotation()).toInstance(b.getInstance()));
        requestStaticInjection(staticInjections);
    }
}
