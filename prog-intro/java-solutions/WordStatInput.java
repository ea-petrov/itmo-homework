import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class WordStatInput {
    private static boolean function(char c) {
        return Character.isLetter(c) || c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION;
    }

    public static void main(String[] args) {
        String input = args[0];
        String output = args[1];
        Map<String, Integer> ans = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(input), StandardCharsets.UTF_8))) {
            StringBuilder word = new StringBuilder();
            int current;
            while ((current = reader.read()) != -1) {
                char c = Character.toLowerCase((char) current);
                if (function(c)) {
                    word.append(c);
                } else {
                    if (!word.isEmpty()) {
                        String wordStr = word.toString();
                        ans.put(wordStr, ans.getOrDefault(wordStr, 0) + 1);
                        word.setLength(0);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read input file " + e.getMessage());
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8))) {
            for (Map.Entry<String, Integer> entry : ans.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to write output file: " + e.getMessage());
        }
    }
}
