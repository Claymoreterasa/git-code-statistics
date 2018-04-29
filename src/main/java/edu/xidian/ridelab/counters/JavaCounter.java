package edu.xidian.ridelab.counters;

import java.io.*;

/**
 * Author: cwz
 * Time: 2018/4/29
 * Description:
 */
public class JavaCounter implements Counter {
    public boolean filter(String fileName) {
        return fileName.matches(".*\\.java$");
    }

    public Statistics count(String fileName, InputStream fileStream) {
        long validLines = 0;
        long commentLines = 0;
        long emptyLines = 0;

        BufferedReader br = null;
        boolean comment = false;
        try {
            br = new BufferedReader(new InputStreamReader(fileStream));
            String line = "";
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.matches("//.*")) {
                    ++commentLines;
                } else if (line.matches("^/\\*.*\\*/$")) {
                    ++commentLines;
                } else if (comment) {
                    ++commentLines;
                    if (line.matches(".*\\*/$")) {
                        comment = false;
                    }
                } else if (line.matches("^/\\*.*[^\\*/$]")) {
                    ++commentLines;
                    comment = true;
                } else if (!comment &&line.matches("[\\s&&[^\\n]]*")) {
                    ++emptyLines;
                } else {
                    ++validLines;
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Statistics statistics = new Statistics(validLines, commentLines, emptyLines);
        LOGGER.info("{} -> {}", fileName, statistics);
        return statistics;
    }
}
