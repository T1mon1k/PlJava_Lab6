import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Translator {
    private final Map<String, String> dict = new HashMap<>();
    private final Path dictFile = Paths.get("dictionary.json");
    public Translator() {
        try {
            loadDictionary();
        } catch (IOException e) {
            System.out.println("Зі словником виникла проблема! Створюється новий.");
        }
    }

    public void addWord(String eng, String ukr) {
        dict.put(eng.toLowerCase(), ukr.toLowerCase());
        try {
            saveDictionary();
        } catch (IOException e) {
            System.out.println("Помилка збереження словника: " + e.getMessage());
        }
    }

    public boolean hasWord(String word) {
        return dict.containsKey(word.toLowerCase());
    }

    public String translatePhrase(String phrase) {
        StringBuilder result = new StringBuilder();
        String[] words = phrase.split("(?<=\\W)|(?=\\W)");
        boolean capitalizeNext = true;
        for (String word : words) {
            String w = word.toLowerCase();
            String toAppend;
            if (dict.containsKey(w)) {
                toAppend = dict.get(w);
            } else if (w.matches("\\W")) {
                toAppend = word;
                if (word.matches("[.!?]")) capitalizeNext = true;
            } else {
                toAppend = "[" + word + "]";
            }
            if (capitalizeNext && !toAppend.isEmpty() && toAppend.matches("\\p{L}.*")) {
                toAppend = toAppend.substring(0, 1).toUpperCase() + toAppend.substring(1);
                capitalizeNext = false;
            }
            result.append(toAppend);
        }
        return result.toString();
    }

    private void saveDictionary() throws IOException {
        Path parent = dictFile.getParent();
        if (parent != null) Files.createDirectories(parent);
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        int i = 0;
        for (Map.Entry<String, String> entry : dict.entrySet()) {
            sb.append("  \"").append(escape(entry.getKey())).append("\": \"").append(escape(entry.getValue()))
                    .append("\"");
            if (i < dict.size() - 1) sb.append(",");
            sb.append("\n");
            i++;
        }
        sb.append("}\n");
        Files.writeString(dictFile, sb.toString(), StandardCharsets.UTF_8);
    }

    private void loadDictionary() throws IOException {
        if (!Files.exists(dictFile)) return;
        String json = Files.readString(dictFile, StandardCharsets.UTF_8);
        dict.clear();
        String[] lines = json.replaceAll("[{}]", "").split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.endsWith(",")) line = line.substring(0, line.length() - 1);
            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                String key = unescape(parts[0].trim().replaceAll("^\"|\"$", ""));
                String value = unescape(parts[1].trim().replaceAll("^\"|\"$", ""));
                dict.put(key, value);
            }
        }
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String unescape(String s) {
        return s.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
