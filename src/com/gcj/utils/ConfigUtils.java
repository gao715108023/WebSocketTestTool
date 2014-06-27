package com.gcj.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-9
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
public class ConfigUtils {

    private Properties propertie;
    private FileInputStream inputFile;

    public ConfigUtils() {
    }

    public ConfigUtils(String filePath) {
        propertie = new Properties();
        try {
            inputFile = new FileInputStream(filePath);
            propertie.load(inputFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputFile != null)
                    inputFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getInt(String key) {
        return Integer.parseInt(getValue(key));
    }

    public String getString(String key) {
        return getValue(key);
    }

    public Double getDouble(String key) {
        return Double.parseDouble(getValue(key));
    }

    public int getInt(String fileName, String key) {
        return Integer.parseInt(getValue(fileName, key));
    }

    public String getString(String fileName, String key) {
        return getValue(fileName, key);
    }

    public Double getDouble(String fileName, String key) {
        return Double.parseDouble(getValue(fileName, key));
    }

    private String getValue(String key) {
        if (propertie.containsKey(key)) {
            String value = propertie.getProperty(key);
            return value;
        } else
            return null;
    }

    private String getValue(String fileName, String key) {
        try {
            String value = "";
            inputFile = new FileInputStream(fileName);
            propertie.load(inputFile);
            if (propertie.contains(key)) {
                value = propertie.getProperty(key);
                return value;
            } else
                return value;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (inputFile != null)
                    inputFile.close();
            } catch (IOException e) {
                e.printStackTrace(); // To change body of catch statement use
                // File | Settings | File Templates.
            }
        }
    }
}