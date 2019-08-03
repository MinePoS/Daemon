package net.minepos.daemon.file;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minepos.daemon.DaemonBootstrap;
import net.minepos.daemon.file.framework.AbstractFileConfiguration;
import net.minepos.daemon.file.framework.FileConfiguration;
import net.minepos.daemon.file.objects.FileWrapper;
import net.minepos.daemon.utils.FileUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
@SuppressWarnings("ResultOfMethodCallIgnored")
@Singleton
public final class FileManager {
    @Inject private AbstractFileConfigurationFactory fileConfigurationFactory;

    private final Map<String, Object> files = new HashMap<>();

    public FileWrapper loadFile(String name, String internalPath, String externalPath) throws Exception {
        LoggerFactory.getLogger("FileManager").info("Loading {}.", name);

        if (externalPath == null) {
            return putAndGet(name, new FileWrapper(null, FileUtils.readEmbedToString(internalPath)));
        }

        File file = createFile(externalPath, internalPath);

        return putAndGet(name, new FileWrapper(file, FileUtils.readFileToString(file)));
    }

    public FileConfiguration loadConfig(String name, String internalPath, String externalPath) throws Exception {
        LoggerFactory.getLogger("FileManager").info("Loading {}.", name);

        if (externalPath == null) {
            return putAndGet(name, fileConfigurationFactory.get(internalPath, FileUtils.readEmbedToString(internalPath)));
        }

        File file = createFile(externalPath, internalPath);

        return putAndGet(name, fileConfigurationFactory.get(file));
    }

    public FileConfiguration getConfig(String name) {
        return (FileConfiguration) files.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) files.get(name);
    }

    // only works with abstract file configuration extensions
    public void update(String name) {
        FileConfiguration config = getConfig(name);

        if (config instanceof AbstractFileConfiguration) {
            AbstractFileConfiguration ac = (AbstractFileConfiguration) config;
            ac.load(ac.getFile(), FileUtils.readFileToString(ac.getFile()));
        }
    }

    private <T> T putAndGet(String name, T file) {
        files.put(name, file);
        return file;
    }

    private File createFile(String externalPath, String internalPath) throws Exception {
        File file = new File(externalPath);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileUtils.exportResource(DaemonBootstrap.class.getResourceAsStream(internalPath), externalPath);
        }

        return file;
    }

}