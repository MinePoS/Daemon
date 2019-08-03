package net.minepos.daemon.file.implementations;

import net.minepos.daemon.file.framework.AbstractFileConfiguration;
import net.minepos.daemon.file.framework.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class BlankFileConfiguration extends AbstractFileConfiguration {
    public BlankFileConfiguration() {
        super("null");
    }

    @Override
    public Object get(String path) {
        return null;
    }

    @Override
    public FileConfiguration getConfigSection(String path) {
        return this;
    }

    @Override
    public String getString(String path) {
        return NULL_STRING;
    }

    @Override
    public Integer getInt(String path) {
        return NULL_NUM;
    }

    @Override
    public Long getLong(String path) {
        return (long) NULL_NUM;
    }

    @Override
    public Double getDouble(String path) {
        return (double) NULL_NUM;
    }

    @Override
    public Boolean getBoolean(String path) {
        return NULL_BOOL;
    }

    @Override
    public List<String> getStringList(String path) {
        return new ArrayList<>();
    }

    @Override
    public List<FileConfiguration> getConfigList(String path) {
        return new ArrayList<>();
    }

    @Override
    public List<?> getList(String path) {
        return new ArrayList<>();
    }
}