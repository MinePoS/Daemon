package net.minepos.daemon.registerables.implementations;

import com.google.inject.Inject;
import net.minepos.daemon.file.framework.FileConfiguration;
import net.minepos.daemon.guice.annotations.Config;
import net.minepos.daemon.registerables.Registerable;
import org.slf4j.LoggerFactory;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class TestRegisterable extends Registerable {
    @Inject @Config private FileConfiguration config;

    @Override
    protected void execute() {
        LoggerFactory.getLogger("Test").info(config.getString("test", "default"));
    }
}
