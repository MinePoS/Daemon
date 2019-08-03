package net.minepos.daemon.guice.module;

import com.google.inject.*;
import net.minepos.daemon.file.AbstractFileConfigurationFactory;
import org.reflections.Reflections;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class InitialModule extends AbstractModule {
    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    public void configure() {
        requestStaticInjection(AbstractFileConfigurationFactory.class);
    }

    @Provides
    @Singleton
    public Reflections providesReflections() {
        return new Reflections("net.minepos.daemon");
    }
}
