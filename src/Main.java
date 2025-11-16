import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Translator tr = new Translator();

        System.out.println("Ласкаво просимо до перекладача англійська --→ українська!");
        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1) Перекласти фразу");
            System.out.println("2) Додати пару перекладу");
            System.out.println("0) Вихід");
            System.out.print("Виберіть пункт: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Введіть фразу англійською: ");
                    String phrase = sc.nextLine();
                    String[] words = phrase.split(" ");
                    for (String word : words) {
                        String w = word.replaceAll("\\W", "").toLowerCase();
                        if (!w.isEmpty() && !tr.hasWord(w)) {
                            System.out.print("На жаль, слово \"" + w + "\" не знайдено. Додати переклад? (y/n): ");
                            String ans = sc.nextLine().trim();
                            if (ans.equalsIgnoreCase("y")) {
                                System.out.print("Введіть переклад: ");
                                String translation = sc.nextLine().trim();
                                if (!tr.hasWord(w)) {
                                    tr.addWord(w, translation);
                                    System.out.println("Додано!");
                                } else {
                                    System.out.println("Слово \"" + w + "\" вже існує у словнику!");
                                }
                            }
                        }
                    }
                    String translated = tr.translatePhrase(phrase);
                    System.out.println("\nПереклад: " + translated);
                    break;

                case "2":
                    System.out.print("Введіть пару слів (eng ukr): ");
                    String line = sc.nextLine().trim();
                    String[] parts = line.split(" ");
                    if (parts.length == 2) {
                        String key = parts[0].toLowerCase();
                        if (!tr.hasWord(key)) {
                            tr.addWord(key, parts[1]);
                            System.out.println("Успішно додано!");
                        } else {
                            System.out.println("Слово \"" + key + "\" вже існує у словнику!");
                        }
                    } else {
                        System.out.println("Потрібно в форматі: eng ukr");
                    }
                    break;

                case "0":
                    System.out.println("Дякую за користування перекладачем! Вихід...");
                    return;

                default:
                    System.out.println("Невірний вибір, спробуйте ще раз.");
            }
        }
    }
}
