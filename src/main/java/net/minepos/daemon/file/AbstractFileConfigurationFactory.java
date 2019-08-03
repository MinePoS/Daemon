package net.minepos.daemon.file;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import net.minepos.daemon.file.exceptions.UnknownConfigTypeException;
import net.minepos.daemon.file.framework.AbstractFileConfiguration;
import net.minepos.daemon.file.framework.FileConfiguration;
import net.minepos.daemon.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class AbstractFileConfigurationFactory {
    @Inject private Injector injector;

    private final List<Class<? extends AbstractFileConfiguration>> fileTypes = new ArrayList<>();

    public FileConfiguration get(File file) throws Exception {
        String fileContent = FileUtils.readFileToString(file);
        return getAFC(file.getPath()).load(file, fileContent);
    }

    public FileConfiguration get(String path, String fileContent) throws Exception {
        return getAFC(path).load(null, fileContent);
    }

    @SuppressWarnings("unchecked")
    private AbstractFileConfiguration getAFC(String path) throws Exception {
        String[] pathBits = path.toLowerCase().split("\\.");

        Optional<AbstractFileConfiguration> abstractFileConfiguration = (Optional<AbstractFileConfiguration>) fileTypes.stream()
                .map(injector::getInstance)
                .filter(f -> f.getExtension().equalsIgnoreCase(pathBits[pathBits.length - 1]))
                .findFirst();

        return abstractFileConfiguration.orElseThrow(() -> new UnknownConfigTypeException("Unknown config type: " + pathBits[pathBits.length - 1]));
    }

    public List<Class<? extends AbstractFileConfiguration>> getFileTypes() {
        return fileTypes;
    }
}