package com.gcj.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by gaochuanjun on 14-1-13.
 */
public class FileUtils {

    public static List<String> getUrlListFromFile(String filePath, String contentEncoding) {
        List<String> urlList = new ArrayList<String>();
        File file = new File(filePath);
        BufferedReader br = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fis, contentEncoding));
            String tempString;
            while ((tempString = br.readLine()) != null) {
                if (tempString.startsWith("/json")) {
                    urlList.add(tempString);
                } else {
                    tempString = "/json/subscribe?" + tempString;
                    urlList.add(tempString);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return urlList;
    }

    public static Vector<String> getUrlVectorFromFile(String filePath, String contentEncoding, boolean flag) {
        Vector<String> urlList = new Vector<String>();
        File file = new File(filePath);
        BufferedReader br = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fis, contentEncoding));
            String tempString;
            while ((tempString = br.readLine()) != null) {
                if (tempString.startsWith("/json")) {
                    urlList.add(tempString);
                } else {
                    tempString = "/json/subscribe?" + tempString;
                    urlList.add(tempString);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return urlList;
    }

    public static Vector<String> getUrlVectorFromFile(String filePath, String contentEncoding) {
        Vector<String> urlList = new Vector<String>();
        File file = new File(filePath);
        BufferedReader br = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fis, contentEncoding));
            String tempString;
            while ((tempString = br.readLine()) != null) {
                urlList.add(tempString);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return urlList;
    }

    public static void viewFileContent(String filePath, String contentEncoding) {
        File file = new File(filePath);
        BufferedReader br = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fis, contentEncoding));
            String tempString;
            while ((tempString = br.readLine()) != null) {
                if (tempString.contains("32128119871109843X")) {
                    System.out.println(tempString);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeLog(String filePath, String content) {
        FileWriter writer = null;
        try {
            File f = new File(filePath);
            if (f.exists()) {
                writer = new FileWriter(filePath, true);
            } else {
                writer = new FileWriter(filePath);
            }
            writer.write(content + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeFile(String filePath, String content) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath)), true);
            pw.println(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (pw != null)
                pw.close();
        }
    }


    public static void main(String[] args) {
        String rootDir = "/Users/gaochuanjun/Downloads/2K万(csv格式）/2000W/";
        String[] filePaths = {"1-200W.csv", "1000W-1200W.csv", "1200W-1400W.csv", "1400W-1600W.csv", "1600w-1800w.csv", "1800w-2000w.csv", "200W-400W.csv", "400W-600W.csv", "600W-800W.csv", "800W-1000W.csv", "最后5000.csv"};
        for (int i = 0; i < filePaths.length; i++) {
            String filePath = rootDir + filePaths[i];
            FileUtils.viewFileContent(filePath, "utf-8");
        }
    }
}
