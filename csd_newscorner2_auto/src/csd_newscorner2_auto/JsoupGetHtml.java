package csd_newscorner2_auto;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.net.Proxy;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class JsoupGetHtml {
    private Elements links;
    private Proxy proxy;
    private Document doc;
    private ArrayList<String> titleArray;
    private ArrayList<String> hrefArray;
    private ArrayList<String> matchedKeywordArray;
    private ArrayList<String> dateArray;
    private ArrayList<String> contentArray;
    private ArrayList<String> sourceArray;

    public JsoupGetHtml(final String proxyHost, final String proxyPort, final String url, final String[] keywords, final String DEBUG, final String chromeDriverPath, final String Binary) throws IOException {
        this.titleArray = new ArrayList<>();
        this.hrefArray = new ArrayList<>();
        this.matchedKeywordArray = new ArrayList<>();
        this.dateArray = new ArrayList<>();
        this.contentArray = new ArrayList<>();
        this.sourceArray = new ArrayList<>();

        // Disable Selenium logs
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);

        // Updated path to ChromeDriver
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        final ChromeOptions options = new ChromeOptions();
        options.setBinary(Binary);
        if (DEBUG.equals("0")) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--remote-allow-origins=*");

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver(options);
        driver.get(url);

        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("isd-closePopup")));
            cancelButton.click();
        } catch (Exception e) {
            if (DEBUG.equals("1")) {
                System.out.println("Cancel button not found or already closed.");
            }
        }

        // Load the HTML document using Jsoup
        if (!proxyHost.isEmpty() && !proxyPort.isBlank()) {
            this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort)));
            this.doc = Jsoup.parse(Jsoup.connect(url).proxy(this.proxy).get().outerHtml(), "UTF-8");
        } else {
            this.doc = Jsoup.parse(Jsoup.connect(url).get().outerHtml(), "UTF-8");
        }

        this.links = this.doc.select("a.NEW");

        if (this.links.isEmpty()) {
            Element emptyTextDiv = this.doc.selectFirst("div.emptyText.fontSize1");
            if (emptyTextDiv != null && emptyTextDiv.text().contains("本網頁暫時未有今日稿件，待新聞稿發放傳媒後，稿件隨即上網。")) {
                if (DEBUG.equals("1")) {
                    System.out.println("No posts available: " + emptyTextDiv.text());
                }
                driver.quit();
                return; // Exit without storing any data
            }
        }


        if (DEBUG.equals("0")) {
            System.out.println("--DEBUG=0, not print out ctoday.html reading data--");
        }

        int i = 0;
        for (final Element link : this.links) {
            final String href = "https://www.info.gov.hk" + link.attr("href");
            if (DEBUG.equals("1")) {
                System.out.println("link: " + href);
            }

            final Document tmp_html = Jsoup.parse(Jsoup.connect(href).proxy(this.proxy).get().outerHtml(), "UTF-8");
            final String title = removeEmojis(tmp_html.select("span#PRHeadlineSpan").text());
            final String content = removeEmojis(tmp_html.select("span#pressrelease.fontSize1").text());
            if (DEBUG.equals("1")) {
                System.out.println("Link: " + href);
                System.out.println("Title: " + title);
                System.out.println("Content: " + content);
                System.out.println("-----------------------------");
            }

            for (final String keyword : keywords) {
                if (tmp_html.outerHtml().contains(keyword)) {
                    final Elements tmp_date = tmp_html.select(".mB15");
                    int j = 0;
                    for (final Element tmp : tmp_date) {
                        if (j == 1) {
                            final Pattern pattern = Pattern.compile("(\\d{4})\u5e74(\\d{1,2})\u6708(\\d{1,2})\u65e5.*?(\\d{1,2})\u6642(\\d{1,2})\u5206");
                            final Matcher matcher = pattern.matcher(tmp.text());
                            if (matcher.find()) {
                                final String year = matcher.group(1);
                                String month = matcher.group(2);
                                String day = matcher.group(3);
                                String hour = matcher.group(4);
                                String minute = matcher.group(5);
                                if (month.length() < 2) {
                                    month = "0" + month;
                                }
                                if (day.length() < 2) {
                                    day = "0" + day;
                                }
                                if (hour.length() < 2) {
                                    hour = "0" + hour;
                                }
                                if (minute.length() < 2) {
                                    minute = "0" + minute;
                                }
                                final String formattedDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00";
                                this.dateArray.add(formattedDate);
                            }
                        }
                        ++j;
                    }
                    this.titleArray.add(title);
                    this.hrefArray.add(href);
                    this.matchedKeywordArray.add(keyword);
                    this.contentArray.add(content);
                    this.sourceArray.add("1"); // Assuming 1 represents the source
                    ++i;
                }
            }
        }

        driver.quit();
    }
    private String removeEmojis(String text) {
        return text.replaceAll("[\\p{So}\\p{Cn}]", ""); // Remove emojis and other non-standard characters
    }
    public int sizeOfArray() {
        return this.titleArray.size();
    }

    public String getTitleArray(final int i) {
        return this.titleArray.get(i);
    }

    public String getHrefArray(final int i) {
        return this.hrefArray.get(i);
    }

    public String getMatchedKeywordArray(final int i) {
        return this.matchedKeywordArray.get(i);
    }

    public String getDateArray(final int i) {
        return this.dateArray.get(i);
    }

    public String getContentArray(final int i) {
        return this.contentArray.get(i);
    }

    public String getSourceArray(final int i) {
        return this.sourceArray.get(i);
    }
}
