import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Map;

public class WordStatWordsSuffix {
    private static boolean function(String s, int i) {
        return !Character.isLetter(s.charAt(i)) && (Character.getType(s.charAt(i)) != Character.DASH_PUNCTUATION) && (s.charAt(i) != '\'');
    }

    public static void main(String[] args) {
        String inputFileName = args[0];
        String outputFileName = args[1];
        TreeMap<String, Integer> ans = new TreeMap<>(Collections.reverseOrder());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileName), StandardCharsets.UTF_8))) {
            int start;
            while (true) {
                int i = 0;
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String s = line.toLowerCase();
                while (i < s.length()) {
                    if (function(s, i)) {
                        i++;
                        continue;
                    }
                    start = i;
                    while (i < s.length() && !function(s, i)) {
                        i++;
                    }
                    String k = s.substring(start, i);
                    if (k.length() >= 3) {
                        k = k.substring(k.length() - 3);
                    }
                    if (ans.containsKey(k)) {
                        ans.put(k, ans.get(k) + 1);
                    } else {
                        ans.put(k, 1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read input file: " + e.getMessage());
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), StandardCharsets.UTF_8))) {
            for (Map.Entry<String, Integer> entry : ans.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to write output file: " + e.getMessage());
        }
    }
}
