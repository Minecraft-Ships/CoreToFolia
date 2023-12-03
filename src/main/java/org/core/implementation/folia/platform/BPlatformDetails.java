package org.core.implementation.folia.platform;

import org.bukkit.Bukkit;
import org.core.platform.PlatformDetails;
import org.core.platform.plugin.details.CorePluginVersion;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class BPlatformDetails implements PlatformDetails {

    private final String type;
    private Integer version;

    public BPlatformDetails() {
        this.type = Bukkit.getName();
        try {
            String versionString = Bukkit.getMinecraftVersion();
            String[] versionSplit = versionString.split(Pattern.quote("."));
            this.version = Integer.parseInt(versionSplit[1]);
        } catch (Throwable e) {
            System.err.println(
                    "Unknown version number for implementation: Report this on dev.bukkit asking for support for "
                            + Bukkit.getVersion());
        }
    }

    @Override
    public @NotNull String getName() {
        return this.type;
    }

    @Override
    public @NotNull String getIdName() {
        return this.type.toLowerCase();
    }

    @Override
    public @NotNull CorePluginVersion getVersion() {
        String versionString = Bukkit.getBukkitVersion();
        String version = versionString.substring(0, versionString.indexOf("-"));
        String[] splitVersion = version.split(Pattern.quote("."));
        int major = 0;
        int minor = 0;
        int patch = 0;
        if (splitVersion.length >= 1) {
            major = Integer.parseInt(splitVersion[0]);
        }
        if (splitVersion.length >= 2) {
            minor = Integer.parseInt(splitVersion[1]);
        }
        if (splitVersion.length >= 3) {
            patch = Integer.parseInt(splitVersion[2]);
        }
        return new CorePluginVersion(major, minor, patch, this.type, this.version);
    }
}
