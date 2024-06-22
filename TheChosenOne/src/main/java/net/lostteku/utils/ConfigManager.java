package net.lostteku.utils;

import net.lostteku.TheChosenOne;
import net.lostteku.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {

    private static List<File> fileList = new ArrayList<>();

    public void loadFiles() {
        if (!TheChosenOne.getChosenOne().getDataFolder().exists()) {
            TheChosenOne.getChosenOne().getDataFolder().mkdir();
        }

        setDefaultsToConfig(createNewFile("config"), "general.blockedcommands", Arrays.asList(new String[]{"/pl", "/plugins", "/version", "/ver"}));

        for(Messages msgs : Messages.values()){
            setDefaultsToConfig(createNewFile("messages"), msgs.getPath(), msgs.getStandardMessage());
        }

        for (File files : TheChosenOne.getChosenOne().getDataFolder().listFiles()) {
            if (files.getName().endsWith(".yml")) {
                fileList.add(files);
            }
        }
    }

    private File createNewFile(String name) {
        File newfile = new File(TheChosenOne.getChosenOne().getDataFolder() + "\\" + name + ".yml");
        if(newfile.exists()) return newfile;
        try {
            newfile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return newfile;
    }


    public YamlConfiguration getConfigFile(String name) {
        for (File files : fileList) {
            if (files.getName().equals(name + ".yml")) {
                return YamlConfiguration.loadConfiguration(files);
            }
        }
        return null;
    }

    private void setDefaultsToConfig(File newfile, String path, Object value) {

        YamlConfiguration conf = YamlConfiguration.loadConfiguration(newfile);
        if(conf.get(path) != null) return;

        conf.set(path, value);
        try {
            conf.save(newfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setConfigurationToFile(String name, String configpath, Object value) {
        for (File files : fileList) {
            if (files.getName().equals(name + ".yml")) {
                YamlConfiguration cnf = YamlConfiguration.loadConfiguration(files);
                if (cnf.get(configpath) == null) {
                    Bukkit.getConsoleSender().sendMessage("§cBitte starte den Server nochmal neu, damit ich die fehlenden Configdateien erstellen kann!");
                    return;
                }
                cnf.set(configpath, value);
                try {
                    cnf.save(files);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
