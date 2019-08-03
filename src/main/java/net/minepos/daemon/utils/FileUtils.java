package net.minepos.daemon.utils;

import net.minepos.daemon.DaemonBootstrap;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class FileUtils {
    public static boolean exportResource(InputStream in, String destination) {
        boolean success = true;

        try {
            Files.copy(in, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }

        return success;
    }

    public static String readFileToString(File file) {
        try {
            return org.apache.commons.io.FileUtils.readFileToString(file, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String readEmbedToString(String path) {
        try {
            return IOUtils.toString(DaemonBootstrap.class.getResourceAsStream(path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
