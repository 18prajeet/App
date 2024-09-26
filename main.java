import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewsScraper {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // User inputs for date and language
        System.out.println("Enter the year (e.g., 2024): ");
        String year = sc.nextLine();
        
        System.out.println("Enter the month (e.g., january, february): ");
        String month = sc.nextLine().toLowerCase();
        
        System.out.println("Enter the day (1 to 31): ");
        String day = sc.nextLine();
        
        System.out.println("Do you want news in Hindi? (yes/no): ");
        String languageToggle = sc.nextLine();
        String textLanguage = languageToggle.equalsIgnoreCase("yes") ? "2" : "1";

        // Formatting date suffix (st, nd, rd, th)
        String suffix = "th";
        if (day.endsWith("1") && !day.equals("11")) {
            suffix = "st";
        } else if (day.endsWith("2") && !day.equals("12")) {
            suffix = "nd";
        } else if (day.endsWith("3") && !day.equals("13")) {
            suffix = "rd";
        }

        System.out.println("News for the date: " + day + suffix + " " + month + ", " + year);

        // Constructing the URL
        String urlString = "https://sarkaripariksha.com/gk-and-current-affairs/" 
                            + year + "/" + month + "/" + day + "/" + textLanguage + "/";

        try {
            // Make HTTP GET request and fetch the webpage content
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                // Parse the HTML content using Jsoup
                Document doc = Jsoup.connect(urlString).get();
                
                // Select news items based on class name
                Elements newsList = doc.select("div.examlist-details-img-box");

                if (newsList.isEmpty()) {
                    System.out.println("No news available for the selected date.");
                } else {
                    int count = 1;
                    for (Element newsItem : newsList) {
                        Element aTag = newsItem.selectFirst("h2 a");
                        String newsTitle = aTag.text();
                        String hrefLink = aTag.attr("href");

                        // Print the news title and link
                        System.out.println(count + " - " + newsTitle + ": " + hrefLink);
                        count++;
                    }
                }
            } else {
                System.out.println("Error: Unable to retrieve the news. HTTP Response Code: " + responseCode);
            }
        } catch (IOException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}
