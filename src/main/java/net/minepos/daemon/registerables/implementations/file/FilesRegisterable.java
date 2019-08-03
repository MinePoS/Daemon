package net.minepos.daemon.registerables.implementations.file;

import com.google.inject.Inject;
import net.minepos.daemon.file.FileManager;
import net.minepos.daemon.file.framework.FileConfiguration;
import net.minepos.daemon.guice.annotations.Config;
import net.minepos.daemon.registerables.Registerable;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class FilesRegisterable extends Registerable {
    @Inject private FileManager fileManager;

    @Override
    protected void execute() {
        try {
            addAnnotatedBinding(FileConfiguration.class, Config.class, fileManager.loadConfig("config", "/config.json", "./config.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
