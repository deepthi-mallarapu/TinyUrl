import java.util.*;

public class TinyURLDSA {
    private static final String BASE_URL = "short.ly/";
    private static final String CHAR_SET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    private static Map<String, String> codeToUrl = new HashMap<>();
    private static Map<String, String> urlToCode = new HashMap<>();
    private static Map<String, Integer> usageCount = new HashMap<>();
    private static LinkedList<String> recentUrls = new LinkedList<>();
    private static final int MAX_RECENT = 5;

    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    public static void main(String[] args) {
        while (true) {
            System.out.println("TinyURL DSA App");
            System.out.println("1. Shorten a URL");
            System.out.println("2. Retrieve original URL");
            System.out.println("3. Show top 3 most used URLs");
            System.out.println("4. Show last 5 shortened URLs");
            System.out.println("5. Show all mappings");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1 -> shortenUrl();
                case 2 -> retrieveUrl();
                case 3 -> showTopUsed();
                case 4 -> showRecentUrls();
                case 5 -> showAllMappings();
                case 6 -> {
                    System.out.println("Thank you!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void shortenUrl() {
        System.out.print("Enter the original URL: ");
        String longUrl = scanner.nextLine();

        if (urlToCode.containsKey(longUrl)) {
            String existingCode = urlToCode.get(longUrl);
            System.out.println("Short URL: " + BASE_URL + existingCode);
            updateUsage(existingCode);
            return;
        }

        String shortCode = generateShortCode();
        urlToCode.put(longUrl, shortCode);
        codeToUrl.put(shortCode, longUrl);
        updateUsage(shortCode);
        updateRecent(shortCode);

        System.out.println(" Short URL: " + BASE_URL + shortCode);
    }

    private static void retrieveUrl() {
        System.out.print("Enter short code (without domain): ");
        String code = scanner.nextLine();

        if (codeToUrl.containsKey(code)) {
            System.out.println(" Original URL: " + codeToUrl.get(code));
            updateUsage(code);
            updateRecent(code);
        } else {
            System.out.println(" Code not found.");
        }
    }

    private static void updateUsage(String code) {
        usageCount.put(code, usageCount.getOrDefault(code, 0) + 1);
    }

    private static void updateRecent(String code) {
        recentUrls.remove(code);
        recentUrls.addFirst(code);
        if (recentUrls.size() > MAX_RECENT) {
            recentUrls.removeLast();
        }
    }

    private static void showTopUsed() {
        System.out.println("🏆 Top 3 Most Used Short URLs:");
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
            (a, b) -> b.getValue() - a.getValue()
        );
        pq.addAll(usageCount.entrySet());

        int count = 0;
        while (!pq.isEmpty() && count < 3) {
            Map.Entry<String, Integer> entry = pq.poll();
            System.out.println(BASE_URL + entry.getKey() + " -> used " + entry.getValue() + " times");
            count++;
        }
    }

    private static void showRecentUrls() {
        System.out.println("Recent Shortened URLs:");
        for (String code : recentUrls) {
            System.out.println(BASE_URL + code + " -> " + codeToUrl.get(code));
        }
    }

    private static void showAllMappings() {
        System.out.println("All URL Mappings:");
        for (String code : codeToUrl.keySet()) {
            System.out.println(BASE_URL + code + " -> " + codeToUrl.get(code));
        }
    }

    private static String generateShortCode() {
        StringBuilder sb = new StringBuilder();
        while (true) {
            for (int i = 0; i < CODE_LENGTH; i++) {
                int index = random.nextInt(CHAR_SET.length());
                sb.append(CHAR_SET.charAt(index));
            }
            String code = sb.toString();
            if (!codeToUrl.containsKey(code)) {
                return code;
            }
            sb.setLength(0); 
        }
    }
}
