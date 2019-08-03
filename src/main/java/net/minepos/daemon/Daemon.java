package net.minepos.daemon;

import com.google.inject.Injector;
import net.minepos.daemon.guice.module.RegisterableChildModule;
import net.minepos.daemon.registerables.Registerable;
import net.minepos.daemon.registerables.implementations.TestRegisterable;
import net.minepos.daemon.registerables.implementations.file.FileTypesRegisterable;
import net.minepos.daemon.registerables.implementations.file.FilesRegisterable;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
final class Daemon {
    void start(Injector parentInjector) {
        AtomicReference<Injector> injectorRef = new AtomicReference<>(parentInjector);

        Stream.of(
                FileTypesRegisterable.class,
                FilesRegisterable.class,
                TestRegisterable.class
        ).forEach(r -> {
            Injector injector = injectorRef.get();
            Registerable registerable = injector.getInstance(r);
            registerable.run(injector);

            if (registerable.getBindings().size() > 0 || registerable.getAnnotatedBindings().size() > 0 || registerable.getStaticInjections().size() > 0) {
                injectorRef.set(injector.createChildInjector(new RegisterableChildModule(
                        registerable.getBindings(),
                        registerable.getAnnotatedBindings(),
                        registerable.getStaticInjections().toArray(new Class[]{})
                )));
            }
        });

        LoggerFactory.getLogger("Daemon").info("Startup completed.");
    }
}
