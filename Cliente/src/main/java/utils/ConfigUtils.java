package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Barth
 */
public class ConfigUtils {

    private static String config;

    public static void setConfig(String config) throws IOException {
        File configFile = FileUtils.openConfigFile();
        FileWriter fileWriter = new FileWriter(configFile);
        try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(config);
        }
        ConfigUtils.config = config;
    }
    
    static public String getConfig() throws IOException {
        if (config == null) {
            carregaConfiguracoes();
        }
        return config;
    }
    
    static public void carregaConfiguracoes() throws IOException {
        File config = FileUtils.openConfigFile();
        FileInputStream fileInputStream = new FileInputStream(config);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            ConfigUtils.config = bufferedReader.readLine();
        }
    }
    
}