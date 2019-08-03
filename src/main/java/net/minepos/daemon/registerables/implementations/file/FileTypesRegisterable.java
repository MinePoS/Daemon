package net.minepos.daemon.registerables.implementations.file;

import com.google.inject.Inject;
import net.minepos.daemon.file.AbstractFileConfigurationFactory;
import net.minepos.daemon.file.framework.AbstractFileConfiguration;
import net.minepos.daemon.registerables.Registerable;
import org.reflections.Reflections;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class FileTypesRegisterable extends Registerable {
    @Inject private AbstractFileConfigurationFactory fileConfigurationFactory;
    @Inject private Reflections reflections;

    @Override
    protected void execute() {
        fileConfigurationFactory.getFileTypes().addAll(reflections.getSubTypesOf(AbstractFileConfiguration.class));
    }
}
