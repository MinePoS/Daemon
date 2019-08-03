package net.minepos.daemon;

import com.google.inject.Injector;
import net.minepos.daemon.guice.module.InitialModule;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class DaemonBootstrap {
    private DaemonBootstrap() {
        final Injector injector = new InitialModule().createInjector();
        injector.getInstance(Daemon.class).start(injector);
    }

    public static void main(String[] args) {
        new DaemonBootstrap();
    }
}
