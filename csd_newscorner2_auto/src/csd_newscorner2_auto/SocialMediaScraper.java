package csd_newscorner2_auto;

import java.util.regex.Matcher;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.text.ParseException;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Locale;
import java.util.List;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import java.util.Set;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.Dimension;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import java.util.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.DateFormat;



public class SocialMediaScraper {
    private WebDriver driver;
    private int waitSec_;
    private String DEBUG_;
    private String FacebookBinary_;
    private String UserDataDir_;
    private String Profile_;
    private String facebookUserDataDir_;
    private String facebookProfile_;
    private String deviceName_;
    private String fbigUsername;
    private String fbigPassword;
    private String formattedDate;
    private int youtubeCount_;
    private int facebookCount_;
    private int instaCount_;
    private int RssCount_;
    private String rssUrl_;
    private String[] keywords_;
    private ArrayList<String> titleArray;
    private ArrayList<String> hrefArray;
    private ArrayList<String> dateArray;
    private ArrayList<String> matchedKeywordArray;
    private ArrayList<String> contentArray;
    private ArrayList<String> sourceArray;

    public SocialMediaScraper(final String xmlEnable, final String youtubeEnable, final String youtubeShortsEnable, final String fbEnable,  final String fbAltScraperAbitEnable,final String instaEnable, final String instaReelsEnable, final String RssEnable, final String xmllink, final String youtubeUrl, final String youtubeShortsUrl, final String fbUrl, final String fbAltScraperAbitUrl, final String instaUrl, final String instaReelsUrl, final String RssUrl, final int youtubeCount, final int facebookCount, final int instaCount, final int RssCount,  final String proxyHost, final String proxyPort, final String DEBUG, final int waitSec, final String fbigUsername, final String fbigPassword, final String chromeDriverPath, final String Binary, final String FacebookBinary, final String UserDataDir, final String Profile,  final String facebookUserDataDir, final String facebookProfile, final String deviceName, final String[] keywords) throws InterruptedException {
        this.titleArray = new ArrayList<>();
        this.hrefArray = new ArrayList<>();
        this.dateArray = new ArrayList<>();
        this.matchedKeywordArray = new ArrayList<>();
        this.contentArray = new ArrayList<>();
        this.sourceArray = new ArrayList<>();
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);

        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        final ChromeOptions options = new ChromeOptions();
        options.setBinary(Binary);


        options.addArguments("--remote-allow-origins=*");
        if (!proxyHost.isEmpty() && !proxyPort.isBlank()) {
            options.addArguments("--proxy-server=" + proxyHost + ":" + proxyPort);
        }
        this.waitSec_ = waitSec;
        this.DEBUG_ = DEBUG;
        this.FacebookBinary_ = FacebookBinary;
        this.UserDataDir_ = UserDataDir;
        this.Profile_ = Profile;
        this.facebookUserDataDir_ = facebookUserDataDir;
        this.facebookProfile_ = facebookProfile;
        this.deviceName_ = deviceName;
        this.fbigUsername = fbigUsername;
        this.fbigPassword = fbigPassword;
        this.facebookCount_ = facebookCount;
        this.youtubeCount_ = youtubeCount;
        this.instaCount_ = instaCount; // Initialize Instagram count
        this.RssCount_ = RssCount;
        this.rssUrl_ = RssUrl;
        this.keywords_ = keywords;
        if (this.DEBUG_.equals("0")) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--disable-notifications");
        options.addArguments("--mute-audio");
        this.driver = new ChromeDriver(options);
        final Date currentDate = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        this.formattedDate = formatter.format(currentDate);
        if (youtubeEnable.equals("1")) {
            this.youtubeScraper(youtubeUrl);
        }
         if (youtubeShortsEnable.equals("1")) {
            this.youtubeShortsScraper(youtubeShortsUrl);
         }
        if (instaEnable.equals("1")) {
            this.instagramScraper(instaUrl); // Updated Instagram scraping
        }
        if (instaReelsEnable.equals("1")) {
            this.InstagramReelsScraper(instaReelsUrl);
        }
        if (fbEnable.equals("1")) {
            this.facebookScraper(fbUrl);
        }
        if (fbAltScraperAbitEnable.equals("1")) {
            this.fbAltScraperAbitScraper(fbAltScraperAbitUrl);
        }
        if (RssEnable.equals("1")) {
            this.RssScraper(RssUrl,RssCount_);
        }
        if (xmlEnable.equals("1")) {
            this.xmllinkScraper(xmllink);
        }
        this.driver.quit();
    }

    private void youtubeShortsScraper(final String youtubeShortsUrl) throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        if (this.DEBUG_.equals("0")) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--incognito");
        options.addArguments("user-data-dir=" + UserDataDir_);
        options.addArguments("profile-directory=" + Profile_);
        options.addArguments("--disable-dev-shm-usage"); // Overcome resource limitations
        options.addArguments("--no-sandbox");            // Bypass OS security model
        options.addArguments("--remote-debugging-port=9222"); // Enable debugging

        // Set up mobile emulation (example device: Techno Pop 7, or whatever is in deviceName_)
        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", deviceName_);
        options.setExperimentalOption("mobileEmulation", mobileEmulation);

        this.driver = new ChromeDriver(options);
        this.driver.manage().window().setSize(new Dimension(720, 1520));
        if (this.DEBUG_.equals("1")) {
            System.out.println("Browser window resized to 720x1520. Done!");
        }

        driver.get(youtubeShortsUrl);
        if (this.DEBUG_.equals("1")) {
            System.out.println("Navigating to URL: " + youtubeShortsUrl);
        }
        // Short wait for initial page load
        Thread.sleep(5000);

        if (this.youtubeCount_ > 28) {
            // Remove overflow restrictions (optional, but included for consistency)
            ((JavascriptExecutor) this.driver).executeScript("document.body.style.overflow = 'visible';");

            int diff = this.youtubeCount_ - 28;
            int scrollCount = (diff / 28) + (diff % 28 == 0 ? 0 : 1);

            for (int i = 0; i < scrollCount; i++) {
                // Scroll the page
                ((JavascriptExecutor) this.driver).executeScript("window.scrollBy(0, 2800);");
                // Wait 5s for new items to load
                Thread.sleep(5000);
            }
            // Extra wait 10s after final scroll to ensure items are fully rendered
            Thread.sleep(10000);
        }

        Thread.sleep(20000); // As in original code for Shorts

        JavascriptExecutor js = (JavascriptExecutor) this.driver;
        @SuppressWarnings("unchecked")
        List<String> shortsUrls = (List<String>) js.executeScript(
                "return Array.from("
                        + "  document.querySelectorAll("
                        + "    \"ytm-app[id='app'] > div > ytm-browse > "
                        + "     ytm-single-column-browse-results-renderer > div:nth-of-type(2) > "
                        + "     div:nth-of-type(3) > ytm-tab-renderer > ytm-rich-grid-renderer > div > "
                        + "     div:nth-of-type(2) > ytm-rich-item-renderer > ytm-shorts-lockup-view-model > a\""
                        + "  )"
                        + ").map(el => el.href);"
        );

        if (this.DEBUG_.equals("1")) {
            System.out.println("Extracted Shorts URLs: " + shortsUrls);
        }
        if (shortsUrls.isEmpty()) {
            if (this.DEBUG_.equals("1")) {
                System.out.println("No Shorts URLs found.");
                System.out.println("--- youtubeShortsScraper End ---");
            }
            driver.quit();
            return;
        }

        // Remove duplicates while preserving order
        Set<String> uniqueShortsUrlsSet = new LinkedHashSet<>(shortsUrls);
        List<String> uniqueShortsUrls = new ArrayList<>(uniqueShortsUrlsSet);
        if (this.DEBUG_.equals("1")) {
            System.out.println("Found " + uniqueShortsUrls.size() + " Shorts URLs total.");
        }

        // Will store how many Shorts we actually scraped
        int scrapedCount = 0;

        if (this.youtubeCount_ == 0) {
            if (this.DEBUG_.equals("1")) {
                System.out.println("Scraping Youtube Shorts From Today");
            }

            boolean foundTodayShort = false;

            for (String shortUrl : uniqueShortsUrls) {
                // If the URL is relative, fix it
                if (!shortUrl.startsWith("http")) {
                    shortUrl = "https://www.youtube.com" + shortUrl;
                }

                // Navigate to the Shorts URL
                driver.get(shortUrl);
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Navigating to Shorts URL: " + shortUrl);
                }
                Thread.sleep(this.waitSec_);
                Thread.sleep(5000);

                // Click the menu button
                js.executeScript(
                        "document.querySelector("
                                + "  \"div[id='player-shorts-container'] > shorts-page > shorts-carousel > div > "
                                + "   div > div > shorts-video > div > div:nth-of-type(2) > "
                                + "   ytm-reel-player-overlay-renderer > div > div:nth-of-type(2) > "
                                + "   ytm-bottom-sheet-renderer > yt-button-shape > label > button > "
                                + "   yt-touch-feedback-shape > div > div:nth-of-type(2)\""
                                + ")?.click();"
                );
                Thread.sleep(this.waitSec_);

                // Click the description button
                js.executeScript(
                        "document.querySelector("
                                + "  \"div[id='content-wrapper'] > div > div > div > "
                                + "   ytm-menu-service-item-renderer > ytm-menu-item > button > span\""
                                + ")?.click();"
                );
                Thread.sleep(this.waitSec_);

                // Extract the description content
                String descriptionContent = (String) js.executeScript(
                        "return document.querySelector("
                                + "  \"ytm-app[id='app'] > panel-container > ytm-engagement-panel > "
                                + "   ytm-engagement-panel-section-list-renderer > div > div > div:nth-of-type(2) > "
                                + "   ytm-structured-description-content-renderer > ytm-video-description-header-renderer > "
                                + "   div > div > span\""
                                + ")?.textContent;"
                );

                // Parse out title/content
                String title;
                String content;
                try {
                    title = descriptionContent.substring(
                            descriptionContent.indexOf("【"),
                            descriptionContent.indexOf("】") + 1
                    );
                    content = descriptionContent.substring(
                            descriptionContent.indexOf("】") + 1
                    ).trim();
                    if (content.isEmpty() && !title.isEmpty()) {
                        WebElement contentElement = driver.findElement(By.cssSelector(
                                "ytm-app[id='app'] > panel-container > ytm-engagement-panel > "
                                        + " ytm-engagement-panel-section-list-renderer > div > div > div:nth-of-type(2) > "
                                        + " ytm-structured-description-content-renderer > "
                                        + " ytm-expandable-video-description-body-renderer > div > div > span"
                        ));
                        content = contentElement.getText().trim();
                    }
                } catch (IndexOutOfBoundsException e) {
                    // Fallback
                    title = descriptionContent;
                    content = "No Content Found";
                }

                // Extract the date
                String rawDateText = (String) js.executeScript(
                        "return document.querySelector("
                                + "  \"ytm-app[id='app'] > panel-container > ytm-engagement-panel > "
                                + "   ytm-engagement-panel-section-list-renderer > div > div > div:nth-of-type(2) > "
                                + "   ytm-structured-description-content-renderer > ytm-video-description-header-renderer > "
                                + "   div > div:nth-of-type(3) > ytm-factoid-renderer:nth-of-type(2) > div > span > span\""
                                + ")?.textContent;"
                );

                // If rawDateText is just a year, try to fetch the additional date text
                if (rawDateText != null && rawDateText.matches("\\d{4}")) {
                    String additionalDateText = (String) js.executeScript(
                            "const element = document.querySelector("
                                    + "  \"ytm-app[id='app'] > panel-container > ytm-engagement-panel > "
                                    + "   ytm-engagement-panel-section-list-renderer > div > div > div:nth-of-type(2) > "
                                    + "   ytm-structured-description-content-renderer > ytm-video-description-header-renderer > "
                                    + "   div > div:nth-of-type(3) > ytm-factoid-renderer:nth-of-type(2) > div > "
                                    + "   span:nth-of-type(2) > span\""
                                    + ");"
                                    + "if (element) {"
                                    + "    return element.textContent;"
                                    + "} else {"
                                    + "    return null;"
                                    + "}"
                    );
                    if (additionalDateText != null) {
                        rawDateText = rawDateText + " " + additionalDateText;
                    }
                }

                String checkRawDateText = (rawDateText == null) ? "" : rawDateText.toLowerCase();
                boolean isToday = checkRawDateText.contains("hour ago")
                        || checkRawDateText.contains("hours ago")
                        || checkRawDateText.contains("minute ago")
                        || checkRawDateText.contains("minutes ago")
                        || checkRawDateText.contains("second ago")
                        || checkRawDateText.contains("seconds ago");

                // If *any* Short is not from today, stop immediately and say "No Youtube Shorts From Today"
                if (!isToday) {
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("No Youtube Shorts From Today");
                        System.out.println("--- youtubeShortsScraper End ---");
                    }
                    if (driver != null) {
                        driver.quit();
                    }
                    Thread.sleep(this.waitSec_);
                    return;
                }

                // If we reach here, it means it's "today," so let's parse/format the date.
                String formattedDate = formatFacebookDate(rawDateText);

                if (this.DEBUG_.equals("1")) {
                    System.out.println("Formatted Date: " + formattedDate);
                    System.out.println("Gotten title and content");
                }
                Thread.sleep(this.waitSec_);

                // Replace "m." with "www." in the final URL
                String updatedUrl = shortUrl.replace("m.", "www.");

                // Debug print
                if (this.DEBUG_.equals("1")) {
                    System.out.println("---Youtube Shorts Link: " + updatedUrl);
                    System.out.println("---Youtube Shorts Title: " + title);
                    System.out.println("---Youtube Shorts Date: " + formattedDate);
                    System.out.println("---Youtube Shorts Content: " + content);
                }

                // Save data to arrays
                this.hrefArray.add(updatedUrl);
                this.dateArray.add(formattedDate);
                this.titleArray.add(title);
                this.matchedKeywordArray.add("Youtube Shorts Data");
                this.contentArray.add(content);
                this.sourceArray.add("3");  // "3" for YouTube

                foundTodayShort = true;
                scrapedCount++;
            }

            // If we finish the loop without hitting a non-today Short:
            if (!foundTodayShort) {
                if (this.DEBUG_.equals("1")) {
                    System.out.println("No Shorts from today found.");
                    System.out.println("--- youtubeShortsScraper End ---");
                }
            } else {
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Read YouTube Shorts (" + scrapedCount + ") from today.");
                    System.out.println("--- youtubeShortsScraper End ---");
                }
            }

            if (driver != null) {
                driver.quit();
            }
            Thread.sleep(this.waitSec_);
            return;
        }

        else {
            int limit = Math.min(uniqueShortsUrls.size(), this.youtubeCount_);
            if (this.DEBUG_.equals("1")) {
                System.out.println("Will scrape up to " + limit + " Shorts URLs.");
            }

            for (int i = 0; i < limit; i++) {
                String shortUrl = uniqueShortsUrls.get(i);

                // Fix relative URL if needed
                if (!shortUrl.startsWith("http")) {
                    shortUrl = "https://www.youtube.com" + shortUrl;
                }

                // Navigate
                driver.get(shortUrl);
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Navigating to Shorts URL: " + shortUrl);
                }
                Thread.sleep(this.waitSec_);
                Thread.sleep(5000);

                // Click menu button
                js.executeScript(
                        "document.querySelector("
                                + "  \"div[id='player-shorts-container'] > shorts-page > shorts-carousel > div > "
                                + "   div > div > shorts-video > div > div:nth-of-type(2) > "
                                + "   ytm-reel-player-overlay-renderer > div > div:nth-of-type(2) > "
                                + "   ytm-bottom-sheet-renderer > yt-button-shape > label > button > "
                                + "   yt-touch-feedback-shape > div > div:nth-of-type(2)\""
                                + ")?.click();"
                );
                Thread.sleep(this.waitSec_);

                // Click the description button
                js.executeScript(
                        "document.querySelector("
                                + "  \"div[id='content-wrapper'] > div > div > div > "
                                + "   ytm-menu-service-item-renderer > ytm-menu-item > button > span\""
                                + ")?.click();"
                );
                Thread.sleep(this.waitSec_);

                // Extract description
                String descriptionContent = (String) js.executeScript(
                        "return document.querySelector("
                                + "  \"ytm-app[id='app'] > panel-container > ytm-engagement-panel > "
                                + "   ytm-engagement-panel-section-list-renderer > div > div > div:nth-of-type(2) > "
                                + "   ytm-structured-description-content-renderer > ytm-video-description-header-renderer > "
                                + "   div > div > span\""
                                + ")?.textContent;"
                );

                // Title & content
                String title;
                String content;
                try {
                    title = descriptionContent.substring(
                            descriptionContent.indexOf("【"),
                            descriptionContent.indexOf("】") + 1
                    );
                    content = descriptionContent.substring(
                            descriptionContent.indexOf("】") + 1
                    ).trim();
                    if (content.isEmpty() && !title.isEmpty()) {
                        WebElement contentElement = driver.findElement(By.cssSelector(
                                "ytm-app[id='app'] > panel-container > ytm-engagement-panel > "
                                        + " ytm-engagement-panel-section-list-renderer > div > div > div:nth-of-type(2) > "
                                        + " ytm-structured-description-content-renderer > "
                                        + " ytm-expandable-video-description-body-renderer > div > div > span"
                        ));
                        content = contentElement.getText().trim();
                    }
                } catch (IndexOutOfBoundsException e) {
                    title = descriptionContent;
                    content = "No Content Found";
                }

                // Date extraction
                String rawDateText = (String) js.executeScript(
                        "return document.querySelector("
                                + "  \"ytm-app[id='app'] > panel-container > ytm-engagement-panel > "
                                + "   ytm-engagement-panel-section-list-renderer > div > div > div:nth-of-type(2) > "
                                + "   ytm-structured-description-content-renderer > ytm-video-description-header-renderer > "
                                + "   div > div:nth-of-type(3) > ytm-factoid-renderer:nth-of-type(2) > div > span > span\""
                                + ")?.textContent;"
                );

                // If rawDateText is just a year (e.g. "2024"), get extra text
                if (rawDateText != null && rawDateText.matches("\\d{4}")) {
                    String additionalDateText = (String) js.executeScript(
                            "const element = document.querySelector("
                                    + "  \"ytm-app[id='app'] > panel-container > ytm-engagement-panel > "
                                    + "   ytm-engagement-panel-section-list-renderer > div > div > div:nth-of-type(2) > "
                                    + "   ytm-structured-description-content-renderer > ytm-video-description-header-renderer > "
                                    + "   div > div:nth-of-type(3) > ytm-factoid-renderer:nth-of-type(2) > div > "
                                    + "   span:nth-of-type(2) > span\""
                                    + ");"
                                    + "if (element) {"
                                    + "    return element.textContent;"
                                    + "} else {"
                                    + "    return null;"
                                    + "}"
                    );
                    if (additionalDateText != null) {
                        rawDateText = rawDateText + " " + additionalDateText;
                    }
                }

                // Format date via the same helper method
                String formattedDate = formatFacebookDate(rawDateText);
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Formatted Date: " + formattedDate);
                }

                if (this.DEBUG_.equals("1")) {
                    System.out.println("Gotten title and content");
                }
                Thread.sleep(this.waitSec_);

                // Clean up the final URL
                String updatedUrl = shortUrl.replace("m.", "www.");

                // Debug logging
                if (this.DEBUG_.equals("1")) {
                    System.out.println("---Youtube Shorts Link: " + updatedUrl);
                    System.out.println("---Youtube Shorts Title: " + title);
                    System.out.println("---Youtube Shorts Date: " + formattedDate);
                    System.out.println("---Youtube Shorts Content: " + content);
                }

                // Save
                this.hrefArray.add(updatedUrl);
                this.dateArray.add(formattedDate);
                this.titleArray.add(title);
                this.matchedKeywordArray.add("Youtube Shorts Data");
                this.contentArray.add(content);
                this.sourceArray.add("3");  // "3" = YouTube

                scrapedCount++;
            }

            if (this.DEBUG_.equals("1")) {
                System.out.println("Read YouTube Shorts (" + scrapedCount + ") Data(s)");
                System.out.println("--- youtubeShortsScraper End ---");
            }

            if (driver != null) {
                driver.quit();
            }
            Thread.sleep(this.waitSec_);
        }
    }


    private void youtubeScraper(final String youtubeUrl) throws InterruptedException {

        // Navigate to the provided URL
        driver.get(youtubeUrl);
        if (this.DEBUG_.equals("1")) {
            System.out.println("Navigating to URL: " + youtubeUrl);
        }
        Thread.sleep(5000); // Wait for the page to load

        // We'll store the scraped URLs and titles here before processing them
        List<String> videoUrls = new ArrayList<>();
        List<String> videoTitles = new ArrayList<>();

        // Find all video timestamp elements
        List<WebElement> timestampElements = driver.findElements(
                By.cssSelector("div#metadata-line .inline-metadata-item.style-scope.ytd-video-meta-block")
        );


        if (this.youtubeCount_ == 0) {
            // Identify which videos are from today
            List<Integer> todayVideoIndexes = new ArrayList<>();
            for (int i = 0; i < Math.min(timestampElements.size(), 20); i++) {
                String timestampText = timestampElements.get(i).getText().toLowerCase();
                // Check if timestamp indicates "hour(s)/minute(s)/second(s) ago"
                if (timestampText.contains("hour ago") || timestampText.contains("hours ago") ||
                        timestampText.contains("minute ago") || timestampText.contains("minutes ago") ||
                        timestampText.contains("second ago") || timestampText.contains("seconds ago")) {

                    todayVideoIndexes.add(i);
                }
            }

            if (todayVideoIndexes.isEmpty()) {
                if (this.DEBUG_.equals("1")) {
                    System.out.println("No videos from today found.");
                }
                return;
            }

            // Scrape info for each "today" video
            for (int index : todayVideoIndexes) {
                // Get the corresponding video element
                WebElement videoElement = driver.findElements(
                        By.cssSelector("div#contents ytd-rich-item-renderer a#thumbnail.yt-simple-endpoint.inline-block.style-scope.ytd-thumbnail")
                ).get(index);

                String postUrl = videoElement.getAttribute("href");
                System.out.println(postUrl);
                int indexOfAmp = postUrl.indexOf('&');
                if (indexOfAmp != -1) {
                    postUrl = postUrl.substring(0, indexOfAmp);
                }
                // Navigate to the video page
                driver.get(postUrl);
                Thread.sleep(this.waitSec_);

                // Scrape the title
                WebElement titleElement = driver.findElement(
                        By.cssSelector("div#title.ytd-watch-metadata h1.ytd-watch-metadata yt-formatted-string.style-scope.ytd-watch-metadata")
                );
                String titleContent = removeEmojiAndUnicode(titleElement.getText());

                // Click "more" to expand the description (if present/visible)
                WebElement moreButton = driver.findElement(By.cssSelector("tp-yt-paper-button#expand"));
                if (moreButton.isDisplayed()) {
                    moreButton.click();
                    Thread.sleep(this.waitSec_);
                }

                // Scrape the video description
                WebElement descriptionElement = driver.findElement(
                        By.cssSelector("yt-attributed-string.style-scope.ytd-text-inline-expander")
                );
                String descriptionContent = removeEmojiAndUnicode(descriptionElement.getText());
                if (descriptionContent.isEmpty()) {
                    descriptionContent = "No content for the video";
                }

                // Grab the date (after expansion)
                WebElement dateElement = driver.findElement(By.cssSelector("yt-formatted-string#info span:last-child"));
                String dateText = dateElement.getText().trim();

                // Parse the date (handle formats like "MMM d, yyyy" or "yyyy年MM月dd日")
                Locale locale = Locale.ENGLISH;
                DateTimeFormatter inputFormatter;
                if (dateText.contains("年")) {
                    // e.g., "2024年12月6日"
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日", locale);
                } else {
                    // e.g., "Dec 6, 2024"
                    inputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", locale);
                }
                LocalDate date = LocalDate.parse(dateText, inputFormatter);

                // Reformat date to "yyyy-MM-dd 00:00"
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00", locale);
                String formattedDate = date.format(outputFormatter);

                // Debug prints
                if (this.DEBUG_.equals("1")) {
                    System.out.println("---Youtube Link: " + postUrl);
                    System.out.println("---Youtube Title: " + titleContent);
                    System.out.println("---Youtube Date: " + formattedDate);
                    System.out.println("---Youtube Content: " + descriptionContent);
                }

                // Save the data to the respective arrays
                this.hrefArray.add(postUrl);
                this.dateArray.add(formattedDate);
                this.titleArray.add(titleContent);
                this.matchedKeywordArray.add("Youtube Data");
                this.contentArray.add(descriptionContent);
                this.sourceArray.add("3"); // e.g., "3" represents YouTube
            }

            if (this.DEBUG_.equals("1")) {
                System.out.println("Read Youtube (" + todayVideoIndexes.size() + ") Data(s) from today");
                System.out.println("--- youtubeScraper End ---");
            }

            // Cleanup
            if (driver != null) {
                driver.quit();
            }
            Thread.sleep(this.waitSec_);
            return;
        }

        if (this.youtubeCount_ > 28) {
            // Remove overflow restrictions
            ((JavascriptExecutor) driver).executeScript("document.body.style.overflow = 'visible';");

            int diff = this.youtubeCount_ - 28;
            // Number of scrolls needed
            int scrollCount = (diff / 28) + (diff % 28 == 0 ? 0 : 1);

            // Perform scrolls
            for (int i = 0; i < scrollCount; i++) {
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 2800);");
                Thread.sleep(5000);
            }
            // Wait for elements to fully load
            Thread.sleep(10000);
        }

        // Grab all video link elements again (in case new ones loaded after scrolling)
        List<WebElement> linkElements = driver.findElements(
                By.cssSelector("div#contents ytd-rich-item-renderer a#thumbnail.yt-simple-endpoint.inline-block.style-scope.ytd-thumbnail")
        );
        if (this.DEBUG_.equals("1")) {
            System.out.println("Total number of link elements: " + linkElements.size());
        }

        if (this.youtubeCount_ == 0) {
            // Just in case someone calls it with youtubeCount_ = 0 but the code logic didn't exit
            if (!linkElements.isEmpty() && !timestampElements.isEmpty()) {
                String timestampText = timestampElements.get(0).getText();
                Pattern timePattern = Pattern.compile("\\d+[smh]");
                Matcher matcher = timePattern.matcher(timestampText);

                boolean looksLikeToday = matcher.find()
                        || timestampText.contains("second") || timestampText.contains("seconds")
                        || timestampText.contains("minute") || timestampText.contains("minutes")
                        || timestampText.contains("hour")   || timestampText.contains("hours");

                if (!looksLikeToday) {
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("No Youtube content found");
                    }
                    return;
                }
            } else {
                if (this.DEBUG_.equals("1")) {
                    System.out.println("No Youtube content found");
                }
                return;
            }
        }


        for (int i = 0; i < linkElements.size() && i < this.youtubeCount_; i++) {
            WebElement linkElement = linkElements.get(i);
            String postUrl = linkElement.getAttribute("href");
            String postTitle = removeEmojiAndUnicode(
                    EmojiCharacterUtil.filter(linkElement.getText())
            );

            videoUrls.add(postUrl);
            videoTitles.add(postTitle);
        }

        for (int i = 0; i < videoUrls.size(); i++) {
            String postUrl = videoUrls.get(i);
            String postTitle = videoTitles.get(i);

            // Navigate to the video
            int indexOfAmp = postUrl.indexOf('&');
            if (indexOfAmp != -1) {
                postUrl = postUrl.substring(0, indexOfAmp);
            }
            driver.get(postUrl);
            Thread.sleep(this.waitSec_);

            // Title
            WebElement titleElement = driver.findElement(
                    By.cssSelector("div#title.ytd-watch-metadata h1.ytd-watch-metadata yt-formatted-string.style-scope.ytd-watch-metadata")
            );
            String titleContent = removeEmojiAndUnicode(titleElement.getText());

            // Expand description if available
            WebElement moreButton = driver.findElement(By.cssSelector("tp-yt-paper-button#expand"));
            if (moreButton.isDisplayed()) {
                moreButton.click();
                Thread.sleep(this.waitSec_);
            }

            // Video description
            WebElement descriptionElement = driver.findElement(
                    By.cssSelector("yt-attributed-string.style-scope.ytd-text-inline-expander")
            );
            String descriptionContent = removeEmojiAndUnicode(descriptionElement.getText());
            if (descriptionContent.isEmpty()) {
                descriptionContent = "No content for the video";
            }

            // Get the date text (last-child or nth-child(3))
            WebElement dateWebElement = driver.findElement(
                    By.cssSelector("yt-formatted-string#info span:last-child, yt-formatted-string#info span:nth-child(3)")
            );
            String dateText = dateWebElement.getText().trim();

            // Normalize some abbreviations if needed
            dateText = dateText.replace("Sept", "Sep");

            // Attempt to parse various date formats
            Locale locale = Locale.ENGLISH;
            DateTimeFormatter inputFormatter;

            if (dateText.contains("年")) {
                // e.g., 2024年12月6日
                inputFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日", locale);
            } else if (dateText.matches("\\d{1,2} \\w{3} \\d{4}")) {
                // e.g., 6 Dec 2024 or 30 Sep 2024
                inputFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", locale);
            } else if (dateText.matches("\\w{3} \\d{1,2} \\d{4}")) {
                // e.g., Sep 6 2024
                inputFormatter = DateTimeFormatter.ofPattern("MMM d yyyy", locale);
            } else if (dateText.matches("\\w{3} \\d{1,2}, \\d{4}")) {
                // e.g., Dec 6, 2024
                inputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", locale);
            } else if (dateText.matches("(\\d+) (days|day) ago")) {
                // e.g., 4 days ago
                int daysAgo = Integer.parseInt(dateText.split(" ")[0]);
                LocalDate date = LocalDate.now().minusDays(daysAgo);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00", locale);
                String formattedDate = date.format(outputFormatter);

                // Debug prints
                if (this.DEBUG_.equals("1")) {
                    System.out.println("---Youtube Link: " + postUrl);
                    System.out.println("---Youtube Title: " + titleContent);
                    System.out.println("---Youtube Date: " + formattedDate);
                    System.out.println("---Youtube Content: " + descriptionContent);
                }

                // Store results
                this.hrefArray.add(postUrl);
                this.dateArray.add(formattedDate);
                this.titleArray.add(titleContent);
                this.matchedKeywordArray.add("Youtube Data");
                this.contentArray.add(descriptionContent);
                this.sourceArray.add("3");

                continue; // Done for this iteration
            } else if (dateText.matches("(\\d+) (weeks|week) ago")) {
                int weeksAgo = Integer.parseInt(dateText.split(" ")[0]);
                LocalDate date = LocalDate.now().minusWeeks(weeksAgo);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00", locale);
                String formattedDate = date.format(outputFormatter);

                // Debug prints (if needed)
                if (this.DEBUG_.equals("1")) {
                    System.out.println("---Youtube Link: " + postUrl);
                    System.out.println("---Youtube Title: " + titleContent);
                    System.out.println("---Youtube Date: " + formattedDate);
                    System.out.println("---Youtube Content: " + descriptionContent);
                }

                // Store results
                this.hrefArray.add(postUrl);
                this.dateArray.add(formattedDate);
                this.titleArray.add(titleContent);
                this.matchedKeywordArray.add("Youtube Data");
                this.contentArray.add(descriptionContent);
                this.sourceArray.add("3");
                continue;
            } else if (dateText.matches("(\\d+) (months|month) ago")) {
                // e.g., 1 month ago
                int monthsAgo = Integer.parseInt(dateText.split(" ")[0]);
                LocalDate date = LocalDate.now().minusMonths(monthsAgo);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00", locale);
                String formattedDate = date.format(outputFormatter);

                // Debug prints
                if (this.DEBUG_.equals("1")) {
                    System.out.println("---Youtube Link: " + postUrl);
                    System.out.println("---Youtube Title: " + titleContent);
                    System.out.println("---Youtube Date: " + formattedDate);
                    System.out.println("---Youtube Content: " + descriptionContent);
                }

                // Store results
                this.hrefArray.add(postUrl);
                this.dateArray.add(formattedDate);
                this.titleArray.add(titleContent);
                this.matchedKeywordArray.add("Youtube Data");
                this.contentArray.add(descriptionContent);
                this.sourceArray.add("3");

                continue;
            } else if (dateText.matches("(\\d+) (years|year) ago")) {
                // e.g., 3 years ago
                int yearsAgo = Integer.parseInt(dateText.split(" ")[0]);
                LocalDate date = LocalDate.now().minusYears(yearsAgo);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00", locale);
                String formattedDate = date.format(outputFormatter);

                // Debug prints
                if (this.DEBUG_.equals("1")) {
                    System.out.println("---Youtube Link: " + postUrl);
                    System.out.println("---Youtube Title: " + titleContent);
                    System.out.println("---Youtube Date: " + formattedDate);
                    System.out.println("---Youtube Content: " + descriptionContent);
                }

                // Store results
                this.hrefArray.add(postUrl);
                this.dateArray.add(formattedDate);
                this.titleArray.add(titleContent);
                this.matchedKeywordArray.add("Youtube Data");
                this.contentArray.add(descriptionContent);
                this.sourceArray.add("3");

                continue;
            } else {
                throw new IllegalArgumentException("Unsupported date format: " + dateText);
            }

            // If not a relative date, parse as a standard date
            LocalDate parsedDate = LocalDate.parse(dateText, inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00", locale);
            String finalDateStr = parsedDate.format(outputFormatter);

            // Debug prints
            if (this.DEBUG_.equals("1")) {
                System.out.println("---Youtube Link: " + postUrl);
                System.out.println("---Youtube Title: " + titleContent);
                System.out.println("---Youtube Date: " + finalDateStr);
                System.out.println("---Youtube Content: " + descriptionContent);
            }

            // Store results
            this.hrefArray.add(postUrl);
            this.dateArray.add(finalDateStr);
            this.titleArray.add(titleContent);
            this.matchedKeywordArray.add("Youtube Data");
            this.contentArray.add(descriptionContent);
            this.sourceArray.add("3");
        }

        if (this.DEBUG_.equals("1")) {
            System.out.println("Read Youtube (" + videoUrls.size() + ") Data(s)");
            System.out.println("--- youtubeScraper End ---");
        }

        // Clean up driver if needed
        if (driver != null) {
            driver.quit();
        }
        Thread.sleep(this.waitSec_);
    }

    public static String removeEmojiAndUnicode(final String input) {
        // Define the regex pattern to match emojis
        final String emojiPattern = "[\\p{So}]";
        final Pattern emojiPatternCompiled = Pattern.compile(emojiPattern);
        final Matcher emojiMatcher = emojiPatternCompiled.matcher(input);

        // Remove all emojis from the input
        String cleanedInput = emojiMatcher.replaceAll("");

        // Define a regex to match trailing English text, numbers, and special characters
        final String trailingPattern = "(?<=\\p{IsHan})[a-zA-Z0-9\\s]+$|^[a-zA-Z0-9\\s]+(?=\\p{IsHan})";

        // Replace trailing or leading English text, numbers, and special characters if not surrounded by Chinese characters
        cleanedInput = cleanedInput.replaceAll(trailingPattern, "");

        // Define a regex to match lines that are only English text or numbers at the end
        final String linePattern = "(?m)^[a-zA-Z0-9\\s]+$";

        // Replace such lines with an empty string
        cleanedInput = cleanedInput.replaceAll(linePattern, "").trim();

        return cleanedInput;
    }

    public void facebookScraper(final String fbUrl) {
        try {
            ChromeOptions options = new ChromeOptions();
            options.setBinary(FacebookBinary_);
            options.addArguments("user-data-dir=" + facebookUserDataDir_);
            if (this.DEBUG_.equals("0")) {
                options.addArguments("--headless=new");
            }
            options.addArguments("profile-directory=" + facebookProfile_);
            options.addArguments("--disable-dev-shm-usage"); // Overcome resource limitations
            options.addArguments("--no-sandbox"); // Bypass OS security model
            options.addArguments("--remote-debugging-port=9222"); // Enable debugging

            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

            // Set up mobile emulation to Techno Pop 7 (720x1520 resolution)
            Map<String, Object> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", deviceName_); // Use an existing mobile device emulation
            options.setExperimentalOption("mobileEmulation", mobileEmulation);

            // Initialize the ChromeDriver with options
            this.driver = new ChromeDriver(options);

            // Resize the browser window to the mobile screen size of Techno Pop 7 (720x1520)
            this.driver.manage().window().setSize(new Dimension(400, 498));
            if (this.DEBUG_.equals("1")) {
                System.out.println("Browser window resized to 720x1520. Done!");
            }

            // Navigate to the URL
            if (this.DEBUG_.equals("1")) {
                System.out.println("Navigating to URL: " + fbUrl);
            }
            this.driver.get(fbUrl);

            Thread.sleep(10000);

            // Wait for the page to load fully
            new WebDriverWait(this.driver, 10)
                    .until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

            if (this.DEBUG_.equals("1")) {
                System.out.println("Page loaded successfully.");
            }

            Thread.sleep(2000);
            fbScraper();
        } catch (WebDriverException wde) {
            if (this.DEBUG_.equals("1")) {
                System.err.println("WebDriver error: " + wde.getMessage());
                wde.printStackTrace();
            }
        } catch (Exception e) {
            if (this.DEBUG_.equals("1")) {
                System.err.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            // Close the driver in the finally block
            if (this.driver != null) {
                try {
                    this.driver.quit();
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("Browser closed.");
                    }
                } catch (Exception e) {
                    if (this.DEBUG_.equals("1")) {
                        System.err.println("Error while closing the browser: " + e.getMessage());
                    }
                }
            }
        }
    }



    private void fbAltScraperAbitScraper(final String fbAltScraperAbitUrl) throws InterruptedException {
        try {
            ChromeOptions options = new ChromeOptions();
            options.setBinary(FacebookBinary_);
            if (this.DEBUG_.equals("0")) {
                options.addArguments("--headless=new");
            }
            options.addArguments("user-data-dir=" + facebookUserDataDir_);
            options.addArguments("profile-directory=" + facebookProfile_);
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--remote-debugging-port=9222");

            Map<String, Object> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", deviceName_);
            options.setExperimentalOption("mobileEmulation", mobileEmulation);

            this.driver = new ChromeDriver(options);
            this.driver.manage().window().setSize(new Dimension(400, 498));

            if ("1".equals(this.DEBUG_)) {
                System.out.println("Browser window resized to 720x1520. Done!");
                System.out.println("Navigating to URL: " + fbAltScraperAbitUrl);
            }

            this.driver.get(fbAltScraperAbitUrl);
            Thread.sleep(10000);

            new WebDriverWait(this.driver, 10)
                    .until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

            if ("1".equals(this.DEBUG_)) {
                System.out.println("Page loaded successfully.");
            }

            Thread.sleep(2000);

            try {
                Thread.sleep(3000);

                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

                int requestedCount = this.facebookCount_;
                List<WebElement> posts = driver.findElements(By.cssSelector("div.post"));
                int availableCount = posts.size();

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String todayStr = dateOnlyFormat.format(new Date());

                int fbCount = 0, ytCount = 0, igCount = 0;
                int fbLimit = RssCount_, ytLimit = RssCount_, igLimit = RssCount_;

                int postCount = (requestedCount == 0) ? availableCount : Math.min(requestedCount, availableCount);

                for (int i = 0; i < postCount; i++) {
                    if ("1".equals(this.DEBUG_)) {
                        System.out.println("Processing post #" + (i + 1));
                    }

                    jsExecutor.executeScript(
                            "document.querySelectorAll('div.post')[" + i + "].scrollIntoView({behavior: 'smooth', block: 'center'});");
                    Thread.sleep(1000);

                    String rawDate = (String) jsExecutor.executeScript(
                            "let element = document.querySelectorAll('div.post')[" + i + "]?.querySelector('span.post-created');" +
                                    "return element?.textContent.trim() || 'No Data Found';"
                    );
                    String formattedDate = rawDate;
                    boolean isToday = false;

                    try {
                        if (!"No Data Found".equals(rawDate)) {
                            Date postDate = inputFormat.parse(rawDate);
                            String postDateOnly = dateOnlyFormat.format(postDate);
                            if (postDateOnly.equals(todayStr)) {
                                isToday = true;
                                formattedDate = outputFormat.format(postDate);
                            }
                        }
                    } catch (ParseException pe) {
                        pe.printStackTrace();
                    }

                    if (this.facebookCount_ == 0 && !isToday) {
                        break;
                    }

                    String title = (String) jsExecutor.executeScript(
                            "let element = document.querySelectorAll('div.post')[" + i + "]?.querySelector('h2.post-title');" +
                                    "return element?.textContent || 'No Data Found';"
                    );
                    String content = (String) jsExecutor.executeScript(
                            "let element = document.querySelectorAll('div.post')[" + i + "]?.querySelector('div.post-content');" +
                                    "return element?.textContent.trim() || 'No Data Found';"
                    );
                    String link = (String) jsExecutor.executeScript(
                            "let element = document.querySelectorAll('div.post')[" + i + "]?.querySelector('a.post-link');" +
                                    "return element?.href || 'No Data Found';"
                    );

                    String sourceCategory;
                    String matchedKeyword;

                    if (link.contains("facebook.com")) {
                        if (fbCount >= fbLimit) continue;
                        sourceCategory = "4";
                        matchedKeyword = "FB Data";
                        fbCount++;
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("Facebook Title: " + title);
                            System.out.println("Facebook Content: " + content);
                            System.out.println("Facebook Date: " + formattedDate);
                            System.out.println("Facebook Link: " + link);
                        }
                    } else if (link.contains("youtube.com") || link.contains("youtu.be")) {
                        if (ytCount >= ytLimit) continue;
                        sourceCategory = "3";
                        matchedKeyword = "Youtube Data";
                        ytCount++;
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("YouTube Title: " + title);
                            System.out.println("YouTube Content: " + content);
                            System.out.println("YouTube Date: " + formattedDate);
                            System.out.println("YouTube Link: " + link);
                        }
                    } else if (link.contains("instagram.com")) {
                        if (igCount >= igLimit) continue;
                        sourceCategory = "2";
                        matchedKeyword = "Instagram Data";
                        igCount++;
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("Instagram Title: " + title);
                            System.out.println("Instagram Content: " + content);
                            System.out.println("Instagram Date: " + formattedDate);
                            System.out.println("Instagram Link: " + link);
                        }
                    } else {
                        sourceCategory = "1";
                        matchedKeyword = "RSS Data";
                    }

                    titleArray.add(title);
                    contentArray.add(content);
                    dateArray.add(formattedDate);
                    hrefArray.add(link);
                    sourceArray.add(sourceCategory);
                    matchedKeywordArray.add(matchedKeyword);

                    if ("1".equals(this.DEBUG_)) {
                        System.out.println(matchedKeyword + " Title: " + title);
                        System.out.println(matchedKeyword + " Content: " + content);
                        System.out.println(matchedKeyword + " Date: " + formattedDate);
                        System.out.println(matchedKeyword + " Link: " + link);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (WebDriverException wde) {
            if ("1".equals(this.DEBUG_)) {
                System.err.println("WebDriver error: " + wde.getMessage());
                wde.printStackTrace();
            }
        } catch (Exception e) {
            if ("1".equals(this.DEBUG_)) {
                System.err.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            if (this.driver != null) {
                try {
                    this.driver.quit();
                    if ("1".equals(this.DEBUG_)) {
                        System.out.println("Browser closed.");
                    }
                } catch (Exception e) {
                    if ("1".equals(this.DEBUG_)) {
                        System.err.println("Error while closing the browser: " + e.getMessage());
                    }
                }
            }
        }
    }



    // Scraper for alternative structure at http://scraper.abitdigital.hk/
    private void fbAltScraper() throws InterruptedException {
        try {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

            // Number of posts to scrape
            int fbCount = this.facebookCount_;

            // Adjust if fbCount is 0 to scrape today's post
            List<WebElement> posts = driver.findElements(By.cssSelector("div.post"));
            if (fbCount == 0) {
                fbCount = Math.min(1, posts.size()); // Scrape today's post (first one)
            }

            for (int i = 0; i < fbCount; i++) {
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Processing post #" + (i + 1));
                }

                // Scroll to the specific post to make it visible
                jsExecutor.executeScript(
                        "document.querySelectorAll('div.post')[" + i + "].scrollIntoView({behavior: 'smooth', block: 'center'});");
                Thread.sleep(1000); // Wait for the post to load

                // Extract the title
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Extracting post title...");
                }
                String title = (String) jsExecutor.executeScript(
                        "let element = document.querySelectorAll('div.post')[" + i + "]?.querySelector('h2.post-title');" +
                                "return element?.textContent || 'No Data Found';");
                titleArray.add(title);

                // Extract the content
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Extracting post content...");
                }
                String content = (String) jsExecutor.executeScript(
                        "let element = document.querySelectorAll('div.post')[" + i + "]?.querySelector('div.post-content');" +
                                "return element?.textContent.trim() || 'No Data Found';");
                contentArray.add(content);

                if (this.DEBUG_.equals("1")) {
                    System.out.println("Extracting post date...");
                }
                String rawDate = (String) jsExecutor.executeScript(
                        "let element = document.querySelectorAll('div.post')[" + i + "]?.querySelector('span.post-created');" +
                                "return element?.textContent.trim() || 'No Data Found';");

                if (!rawDate.equals("No Data Found")) {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    dateArray.add(outputFormat.format(inputFormat.parse(rawDate)));
                } else {
                    dateArray.add(rawDate);
                }



                // Extract the link
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Extracting post link...");
                }
                String link = (String) jsExecutor.executeScript(
                        "let element = document.querySelectorAll('div.post')[" + i + "]?.querySelector('a.post-link');" +
                                "return element?.href || 'No Data Found';");
                hrefArray.add(link);

                this.sourceArray.add(String.valueOf("4"));
                this.matchedKeywordArray.add("FB Data");
            }

            // Format and print the extracted data
            for (int i = 0; i < hrefArray.size(); i++) {
                if (sourceArray.get(i).equals("4")) {
                    if (this.DEBUG_.equals("1")) {// Only process Facebook data
                        System.out.println("--- Facebook Link: " + hrefArray.get(i));
                        System.out.println("--- Facebook Title: " + titleArray.get(i));
                        System.out.println("--- Facebook Date: " + dateArray.get(i));
                        System.out.println("--- Facebook Content: " + contentArray.get(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fbScraper() throws InterruptedException {
        try {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            int fbCount = this.facebookCount_;
            final Function<String, Boolean> isTodayPost = (rawTime) -> {
                if (rawTime == null) return false;
                String lower = rawTime.toLowerCase();
                return lower.contains("hour ago") || lower.contains("hours ago")
                        || lower.contains("minute ago") || lower.contains("minutes ago")
                        || lower.contains("second ago") || lower.contains("seconds ago")
                        || lower.matches(".*\\d+h.*") || lower.matches(".*\\d+m.*") || lower.matches(".*\\d+s.*");
            };
            Thread.sleep(1000);
            if (fbCount == 0) {
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Scraping Facebook Posts From Today...........");
                }
                boolean foundAnyToday = false;
                int i = 0;
                while (true) {
                    int elementIndex = 15 + (i * 4);
                    int linkIndex = 17 + (i * 4);
                    String scrollScript = "setTimeout(() => document.querySelector('div[id=\"screen-root\"] > div > div:nth-of-type(2) > "
                            + "div:nth-of-type(" + elementIndex + ") > div > div > div').scrollIntoView({behavior: 'smooth', block: 'center'}), 0);";
                    jsExecutor.executeScript(scrollScript);
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("---Scrolling to element: " + elementIndex);
                    }
                    Thread.sleep(2000);
                    String clickScript = "setTimeout(() => document.querySelector('div[id=\"screen-root\"] > div > div:nth-of-type(2) > "
                            + "div:nth-of-type(" + elementIndex + ") > div > div > div').click(), 100);";
                    jsExecutor.executeScript(clickScript);
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("Clicked the See More button (if exists) for elementIndex " + elementIndex);
                    }
                    Thread.sleep(2000);
                    String extractScript = "let elem = document.querySelector("
                            + "\"div[id='screen-root'] > div > div:nth-of-type(2) > "
                            + "div:nth-of-type(" + elementIndex + ") > div > div > div\"); "
                            + "return elem ? elem.textContent : null;";
                    String content = (String) jsExecutor.executeScript(extractScript);
                    String timeScript = "return document.querySelectorAll('span.f5')[" + (i + 1) + "]?.textContent;";
                    String rawTime = (String) jsExecutor.executeScript(timeScript);
                    if (!isTodayPost.apply(rawTime)) {
                        if (!foundAnyToday) {
                            if (this.DEBUG_.equals("1")) {
                                System.out.println("No post found for today.");
                            }
                        } else {
                            if (this.DEBUG_.equals("1")) {
                                System.out.println("No more post from today.");
                            }
                        }
                        break;
                    }
                    foundAnyToday = true;
                    String formattedDate = formatFacebookDate(rawTime);
                    dateArray.add(formattedDate);
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("----- Facebook Date (Today): " + formattedDate);
                    }
                    String linkScript = "document.querySelector('div[id=\"screen-root\"] > div > div:nth-of-type(2) > "
                            + "div:nth-of-type(" + linkIndex + ") > div > div:nth-of-type(3)').click();";
                    jsExecutor.executeScript(linkScript);
                    Thread.sleep(3000);
                    String postLinkScript = "return window.location.href;";
                    String postLink = (String) jsExecutor.executeScript(postLinkScript);
                    String editedPostLink = postLink.replace("m.facebook.com", "www.facebook.com");
                    hrefArray.add(editedPostLink);
                    Thread.sleep(3000);
                    String linkRemovalScript = "document.querySelector(\"div[id='screen-root'] > div > div > div > div > div > div\").click();";
                    jsExecutor.executeScript(linkRemovalScript);
                    Thread.sleep(3000);
                    if (content != null && !content.isEmpty()) {
                        int startIndex = content.indexOf("【");
                        int endIndex = content.indexOf("】") + 1;
                        if (startIndex != -1 && endIndex > startIndex) {
                            String title = content.substring(startIndex, endIndex);
                            String remainingContent = content.substring(endIndex).trim();
                            titleArray.add(title);
                            contentArray.add(remainingContent);
                        } else {
                            if (this.DEBUG_.equals("1")) {
                                System.out.println("Content does not have a title format with 【 and 】. Skipping.");
                            }
                        }
                    } else {
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("No content found for index: " + elementIndex);
                        }
                    }
                    this.sourceArray.add("4");
                    this.matchedKeywordArray.add("FB Data");
                    if ((i + 1) % 4 == 0) {
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("Pausing for 10 seconds after processing 4 posts...");
                        }
                        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("Scrolled to the bottom of the page.");
                        }
                        Thread.sleep(10000);
                    }
                    i++;
                }
                if (this.DEBUG_.equals("1")) {
                    for (int idx = 0; idx < hrefArray.size(); idx++) {
                        if (sourceArray.get(idx).equals("4")) {
                            System.out.println("--- Facebook Link: " + hrefArray.get(idx));
                            System.out.println("--- Facebook Title: " + titleArray.get(idx));
                            System.out.println("--- Facebook Date: " + dateArray.get(idx));
                            System.out.println("--- Facebook Content: " + contentArray.get(idx));
                        }
                    }
                }
            } else {
                Thread.sleep(1000);
                for (int i = 0; i < fbCount; i++) {
                    int elementIndex = 15 + (i * 4);
                    int linkIndex = 17 + (i * 4);
                    String scrollScript = "setTimeout(() => document.querySelector('div[id=\"screen-root\"] > div > div:nth-of-type(2) > "
                            + "div:nth-of-type(" + elementIndex + ") > div > div > div').scrollIntoView({behavior: 'smooth', block: 'center'}), 0);";
                    jsExecutor.executeScript(scrollScript);
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("---Scrolling to element: " + elementIndex);
                    }
                    Thread.sleep(2000);
                    String clickScript = "setTimeout(() => document.querySelector('div[id=\"screen-root\"] > div > div:nth-of-type(2) > "
                            + "div:nth-of-type(" + elementIndex + ") > div > div > div').click(), 100);";
                    jsExecutor.executeScript(clickScript);
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("Clicked the See More button");
                    }
                    Thread.sleep(2000);
                    String extractScript = "let elem = document.querySelector("
                            + "\"div[id='screen-root'] > div > div:nth-of-type(2) > "
                            + "div:nth-of-type(" + elementIndex + ") > div > div > div\"); "
                            + "return elem ? elem.textContent : null;";
                    String content = (String) jsExecutor.executeScript(extractScript);
                    String timeScript = "return document.querySelectorAll('span.f5')[" + (i + 1) + "]?.textContent;";
                    String rawTime = (String) jsExecutor.executeScript(timeScript);
                    String formattedDate = formatFacebookDate(rawTime);
                    dateArray.add(formattedDate);
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("----- Facebook Date: " + formattedDate);
                    }
                    String linkScript = "document.querySelector('div[id=\\\"screen-root\\\"] > div > div:nth-of-type(2) > "
                            + "div:nth-of-type(" + linkIndex + ") > div > div:nth-of-type(3)').click();";
                    jsExecutor.executeScript(linkScript);
                    Thread.sleep(3000);
                    String postLinkScript = "return window.location.href;";
                    String postLink = (String) jsExecutor.executeScript(postLinkScript);
                    String editedPostLink = postLink.replace("m.facebook.com", "www.facebook.com");
                    hrefArray.add(editedPostLink);
                    Thread.sleep(3000);
                    String linkRemovalScript = "document.querySelector(\"div[id='screen-root'] > div > div > div > div > div > div\").click();";
                    jsExecutor.executeScript(linkRemovalScript);
                    Thread.sleep(3000);
                    if (content != null && !content.isEmpty()) {
                        int startIndex = content.indexOf("【");
                        int endIndex = content.indexOf("】") + 1;
                        if (startIndex != -1 && endIndex > startIndex) {
                            String title = content.substring(startIndex, endIndex);
                            String remainingContent = content.substring(endIndex).trim();
                            titleArray.add(title);
                            contentArray.add(remainingContent);
                        } else {
                            if (this.DEBUG_.equals("1")) {
                                System.out.println("Content does not have a title format with 【 and 】. Skipping.");
                            }
                        }
                    } else {
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("No content found for index: " + elementIndex);
                        }
                    }
                    this.sourceArray.add("4");
                    this.matchedKeywordArray.add("FB Data");
                    if ((i + 1) % 4 == 0) {
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("Pausing for 10 seconds after processing 4 posts...");
                        }
                        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("Scrolled to the bottom of the page.");
                        }
                        Thread.sleep(10000);
                    }
                }
                if (this.DEBUG_.equals("1")) {
                    for (int i = 0; i < hrefArray.size(); i++) {
                        if (sourceArray.get(i).equals("4")) {
                            System.out.println("--- Facebook Link: " + hrefArray.get(i));
                            System.out.println("--- Facebook Title: " + titleArray.get(i));
                            System.out.println("--- Facebook Date: " + dateArray.get(i));
                            System.out.println("--- Facebook Content: " + contentArray.get(i));
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (this.DEBUG_.equals("1")) {
                System.out.println("Error occurred during FB scraper");
                System.out.println("Stopping FB scraper...");
            }
            e.printStackTrace();
            Thread.sleep(2000);
            if (this.DEBUG_.equals("1")) {
                System.out.println("Starting FB Alt scraper...");
            }
            dateArray.removeIf(date -> date.equals("Unknown date"));
            RssScraper(rssUrl_,RssCount_);
        }
    }

    // Helper method to parse and format the date
    private String formatFacebookDate(String rawTime) {
        try {
            if (rawTime == null || rawTime.isEmpty()) return "Unknown date";

            // Remove spaces and Unicode characters
            rawTime = rawTime.replaceAll("[^\\d\\w]", "");

            // If no space between month and day, add it
            if (rawTime.length() == 5 && rawTime.charAt(3) != ' ') {
                rawTime = rawTime.substring(0, 3) + " " + rawTime.substring(3);
            }

            LocalDateTime now = LocalDateTime.now();

            // Handle time formats like "10s", "5m", "3h", "2d"
            if (rawTime.endsWith("s")) {
                int seconds = Integer.parseInt(rawTime.replace("s", ""));
                return now.minusSeconds(seconds).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else if (rawTime.endsWith("m")) {
                int minutes = Integer.parseInt(rawTime.replace("m", ""));
                return now.minusMinutes(minutes).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else if (rawTime.endsWith("h")) {
                int hours = Integer.parseInt(rawTime.replace("h", ""));
                return now.minusHours(hours).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else if (rawTime.endsWith("d")) {
                int days = Integer.parseInt(rawTime.replace("d", ""));
                return now.minusDays(days).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else {
                // Handle month-day format, such as "Dec 11" or "Dec11"
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

// Ensure there is a space between the month and day
                if (!rawTime.matches(".*\\s\\d+")) {
                    rawTime = rawTime.replaceAll("([a-zA-Z]+)(\\d+)", "$1 $2");
                }

// Append the current year to the rawTime before parsing
                String formattedRawTime = rawTime + " " + now.getYear();

// Parse the raw time
                LocalDate date = LocalDate.parse(formattedRawTime, inputFormatter);

                if (date.isAfter(now.toLocalDate())) {
                    date = date.minusYears(1);
                }
                // today
                return date.format(outputFormatter);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown date";
        }
    }





    // Helper method to scrape each post
    private void scrapePost(WebElement post, String uploaddate) {
        String posttitle = "";
        String postlink = "";

        WebElement TitleDescriptionWrapperDiv = post.findElement(By.className("_5rgn"));
        List<WebElement> ptags = TitleDescriptionWrapperDiv.findElements(By.tagName("p"));
        posttitle = ptags.get(0).getText();

        int start = posttitle.indexOf('【');
        int end = posttitle.indexOf('】');
        posttitle = "【" + posttitle.substring(start + 1, end) + "】";

        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
        posttitle = posttitle.replaceAll(characterFilter, "");

        List<WebElement> childElements = post.findElements(By.tagName("footer"));
        for (WebElement kk : childElements) {
            List<WebElement> threedivs = kk.findElements(By.tagName("div"));
            uploaddate = threedivs.get(0).getText();
            uploaddate = uploaddate.split("·")[0];

            if (uploaddate.contains("hrs") || uploaddate.contains("小時")) {
                uploaddate = findrelativetime(uploaddate);
            } else if (uploaddate.contains("Yesterday")) {
                uploaddate = getYesterdayDateString();
            } else if (uploaddate.contains("mins")) {
                uploaddate = calculateDateTimeFromMinutesAgo(uploaddate);
            } else {
                if (!uploaddate.isEmpty()) {
                    uploaddate = yousufDateConversion(uploaddate);
                }
            }

            List<WebElement> atags = threedivs.get(2).findElements(By.tagName("a"));
            for (WebElement a : atags) {
                String temp = a.getText();
                if (temp.equalsIgnoreCase("full story")) {
                    postlink = a.getAttribute("href");
                }
            }
        }

        if (!postlink.isBlank() || !uploaddate.isBlank()) {
            this.hrefArray.add(postlink);
            this.dateArray.add(uploaddate);
            this.titleArray.add(posttitle);
            this.matchedKeywordArray.add("FB Data");
            this.contentArray.add("");  // Content will be added after scraping individual URLs
            this.sourceArray.add(String.valueOf("4"));
        }
    }

    public Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    public String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(yesterday());
    }

    public static String calculateDateTimeFromMinutesAgo(String minsAgo) {

        minsAgo = minsAgo.split(" ")[0];

        int minutesAgo = Integer.parseInt(minsAgo);

        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Subtract the specified minutes from the current date and time
        LocalDateTime calculatedDateTime = currentDateTime.minusMinutes(minutesAgo);

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Format the calculated date and time
        return calculatedDateTime.format(formatter);
    }
    public String yousufDateConversion(String str) {

        Map<String, String> monthMap = new HashMap<>();

        // note to me: leading 0 ints are interpreted as octal
        monthMap.put("January", String.format("%02d", 1));
        monthMap.put("February", String.format("%02d", 2));
        monthMap.put("March", String.format("%02d", 3));
        monthMap.put("April", String.format("%02d", 4));
        monthMap.put("May", String.format("%02d", 5));
        monthMap.put("June", String.format("%02d", 6));
        monthMap.put("July", String.format("%02d", 7));
        monthMap.put("August", String.format("%02d", 8));
        monthMap.put("September", String.format("%02d", 9));
        monthMap.put("October", String.format("%02d", 10));
        monthMap.put("November", String.format("%02d", 11));
        monthMap.put("December", String.format("%02d", 12));


        String strArr[]  = str.split(" ");
        String day = strArr[0];
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String month = monthMap.get(strArr[1]);

        String formatted = year+"-"+month+"-"+day + " "+ strArr[3];
        return formatted;


    }
    public String findrelativetime(String timeString) {

        int hoursAgo = Integer.parseInt(timeString.split(" ")[0]);

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime pastTime = now.minusHours(hoursAgo);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedTime = pastTime.format(formatter);

        return formattedTime;
    }
    private void instagramScraper(final String instaUrl) throws InterruptedException {
        Logger logger = Logger.getLogger(SocialMediaScraper.class.getName());
        if (this.DEBUG_.equals("1")) {
            System.out.println("Navigating to Instagram URL: " + instaUrl);
        }

        // Set Chrome options to use the default profile
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + UserDataDir_);
        options.addArguments("profile-directory=" + Profile_);
        if (this.DEBUG_.equals("0")) {
            options.addArguments("--headless=new");
        }
        // (Optional) Add additional flags if needed
        // options.addArguments("--disable-dev-shm-usage", "--no-sandbox", "--remote-debugging-port=9222");

        try {
            // Initialize the ChromeDriver with the options
            this.driver = new ChromeDriver(options);

            driver.manage().window().maximize();

            // Navigate to the Instagram page
            this.driver.get(instaUrl);
            Thread.sleep(5000); // Wait for the page to load completely

            // Check if login is required by detecting the login form
            try {
                WebElement usernameField = driver.findElement(By.name("username"));
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Login required. Entering username...");
                }

                // Login process if login form is found
                usernameField.sendKeys(new CharSequence[]{this.fbigUsername});
                Thread.sleep(620);

                WebElement passwordField = driver.findElement(By.name("password"));
                passwordField.sendKeys(new CharSequence[]{this.fbigPassword});

                // Click the login button
                WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
                loginButton.click();
                logger.log(Level.INFO, "Clicked login button");

                // Wait for login processing
                Thread.sleep(20000);

                // Click the "Save info" button if it appears
                try {
                    WebElement saveInfoButton = driver.findElement(By.xpath("//button[text()='Save info']"));
                    saveInfoButton.click();
                    logger.log(Level.INFO, "Clicked 'Save info' button");
                } catch (NoSuchElementException e) {
                    logger.log(Level.WARNING, "'Save info' button not found");
                }

                // Wait for another 15 seconds to ensure everything is properly processed
                Thread.sleep(35000);

            } catch (NoSuchElementException e) {
                // Login form not found, which means we're already logged in
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Already logged in, proceeding to scraping...");
                }
            }

            // Begin scraping process
            if (this.DEBUG_.equals("1")) {
                System.out.println("Starting scraping process...");
            }
            WebDriverWait wait = new WebDriverWait(driver, 120);

            if (this.instaCount_ == 0) {
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Scraping Today's Post Only...");
                }
                int totalPosts = 0;
                int divIndex = 1; // Start from the first div

                while (true) {
                    // Locate the target div
                    String divSelector = String.format("div._ac7v.x1f01sob.xcghwft.xat24cr.xzboxd6:nth-of-type(%d)", divIndex);
                    WebElement targetDiv = driver.findElement(By.cssSelector(divSelector));
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("Target div found: " + divIndex);
                    }

                    // Find all the post elements within the target div
                    List<WebElement> postElements = targetDiv.findElements(By.cssSelector(
                            "div.x1lliihq.x1n2onr6.xh8yej3.x4gyw5p.x1ntc13c.x9i3mqj.x11i5rnm.x2pgyrj"));
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("Post elements found: " + postElements.size());
                    }

                    boolean postFound = false; // Flag to check if a post is from today

                    for (int i = 0; i < postElements.size(); i++) {
                        WebElement postElement = postElements.get(i);

                        // Click the post to navigate to its detailed view
                        WebElement postLink = postElement.findElement(By.cssSelector("a"));
                        String relativePostUrl = postLink.getAttribute("href");
                        postLink.click();
                        Thread.sleep(this.waitSec_);

                        // Locate the time element to get the post date
                        WebElement specificDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//time[contains(@class, 'x1p4m5qa')]")));
                        String timestampText = specificDiv.getText().toLowerCase();

                        // Check if the post is from today
                        if (timestampText.contains("hour ago") || timestampText.contains("hours ago") ||
                                timestampText.contains("minute ago") || timestampText.contains("minutes ago") ||
                                timestampText.contains("second ago") || timestampText.contains("seconds ago")) {

                            postFound = true;

                            // Extract datetime to format it
                            String datetime = specificDiv.getAttribute("datetime");
                            String postDate = "";

                            // Convert datetime to desired format 'yyyy-MM-dd HH:mm:ss'
                            if (datetime != null) {
                                DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                LocalDateTime dateTime = LocalDateTime.parse(datetime, inputFormatter);
                                postDate = dateTime.format(outputFormatter);
                            }

                            // Extract title and content
                            WebElement titleElement = driver.findElement(By.cssSelector(
                                    "h1._ap3a._aaco._aacu._aacx._aad7._aade"));
                            String text = titleElement.getText();
                            String regex = "\\【(.*?)\\】"; // regular expression to match 【title】
                            Pattern pattern = Pattern.compile(regex);
                            Matcher titleMatch = pattern.matcher(text);
                            String title = "";
                            String content = "";
                            if (titleMatch.find()) {
                                title = "【" + titleMatch.group(1) + "】";
                                int contentStartIndex = titleMatch.start() + titleMatch.group(0).length();
                                content = text.substring(contentStartIndex).trim();
                            }

                            String fullPostUrl = relativePostUrl;
                            this.hrefArray.add(fullPostUrl);
                            this.dateArray.add(postDate);
                            this.titleArray.add(removeEmoji(title));
                            this.matchedKeywordArray.add("Instagram Data");
                            this.contentArray.add(removeEmoji(content));
                            this.sourceArray.add("2"); // Assuming 2 represents Instagram

                            if (this.DEBUG_.equals("1")) {
                                System.out.println("---Instagram Link(" + totalPosts + "): " + fullPostUrl);
                                System.out.println("---Instagram Title(" + totalPosts + "): " + title);
                                System.out.println("---Instagram Content(" + totalPosts + "): " + content);
                                System.out.println("---Instagram Date(" + totalPosts + "): " + postDate);
                            }

                            ++totalPosts;

                            // Navigate back to the main Instagram page
                            this.driver.navigate().back();
                            Thread.sleep(this.waitSec_);
                        } else {
                            if (totalPosts == 0) {
                                if (this.DEBUG_.equals("1")) {
                                    System.out.println("No post found for today.");
                                }
                            } else {
                                if (this.DEBUG_.equals("1")) {
                                    System.out.println("No more posts from today found in this div.");
                                }
                            }
                            // Return early; driver will be closed in the finally block.
                            return;
                        }
                    }

                    // If no posts from today are found, exit the loop
                    if (!postFound) {
                        if (totalPosts == 0 && this.DEBUG_.equals("1")) {
                            System.out.println("No post found for today.");
                        }
                        break;
                    }

                    // Move to the next div
                    divIndex++;
                }
            } else {
                // Scraping logic when instaCount_ > 0
                int totalPosts = 0;
                int scrollCount = 0;
                int divIndex = 0;

                while (totalPosts < this.instaCount_) {
                    // Adjust for 1-based indexing in the CSS selector
                    String divSelector = String.format("div._ac7v.x1f01sob.xcghwft.xat24cr.xzboxd6:nth-of-type(%d)", divIndex + 1);
                    WebElement targetDiv = driver.findElement(By.cssSelector(divSelector));
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("Target div found: " + (divIndex + 1));
                    }

                    List<WebElement> postElements = targetDiv.findElements(By.cssSelector(
                            "div.x1lliihq.x1n2onr6.xh8yej3.x4gyw5p.x1ntc13c.x9i3mqj.x11i5rnm.x2pgyrj"));
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("Post elements found: " + postElements.size());
                    }

                    for (int i = 0; i < postElements.size() && totalPosts < this.instaCount_; i++) {
                        WebElement postElement = postElements.get(i);

                        // Click the post to view details
                        WebElement postLink = postElement.findElement(By.cssSelector("a"));
                        String relativePostUrl = postLink.getAttribute("href");
                        postLink.click();
                        Thread.sleep(this.waitSec_);

                        WebElement specificDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//time[contains(@class, 'x1p4m5qa')]")));
                        String datetime = specificDiv.getAttribute("datetime");
                        String postDate = "";

                        if (datetime != null) {
                            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime dateTime = LocalDateTime.parse(datetime, inputFormatter);
                            postDate = dateTime.format(outputFormatter);
                        }

                        WebElement titleElement = driver.findElement(By.cssSelector(
                                "h1._ap3a._aaco._aacu._aacx._aad7._aade"));
                        String text = titleElement.getText();
                        String regex = "\\【(.*?)\\】";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher titleMatch = pattern.matcher(text);
                        String title = "";
                        String content = "";
                        if (titleMatch.find()) {
                            title = "【" + titleMatch.group(1) + "】";
                            int contentStartIndex = titleMatch.start() + titleMatch.group(0).length();
                            content = text.substring(contentStartIndex).trim();
                        }

                        String fullPostUrl = relativePostUrl;
                        this.hrefArray.add(fullPostUrl);
                        this.dateArray.add(postDate);
                        this.titleArray.add(removeEmoji(title));
                        this.matchedKeywordArray.add("Instagram Data");
                        this.contentArray.add(removeEmoji(content));
                        this.sourceArray.add("2");

                        if (this.DEBUG_.equals("1")) {
                            System.out.println("---Instagram Link(" + totalPosts + "): " + fullPostUrl);
                            System.out.println("---Instagram Title(" + totalPosts + "): " + title);
                            System.out.println("---Instagram Content(" + totalPosts + "): " + content);
                            System.out.println("---Instagram Date(" + totalPosts + "): " + postDate);
                        }

                        ++totalPosts;

                        // Navigate back to continue scraping
                        this.driver.navigate().back();
                        Thread.sleep(this.waitSec_);
                    }

                    if (totalPosts < this.instaCount_) {
                        ((JavascriptExecutor) this.driver).executeScript("window.scrollBy(0,1000)");
                        Thread.sleep(this.waitSec_ * 2);
                        ++scrollCount;
                        ++divIndex;
                    }
                }

                if (this.DEBUG_.equals("1")) {
                    System.out.println("Instagram scraping completed.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Always close the driver, regardless of how the try block is exited.
            if (this.driver != null) {
                this.driver.quit();
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Browser closed.");
                }
            }
        }
    }private void InstagramReelsScraper(final String instaReelsUrl) throws InterruptedException {
        Logger logger = Logger.getLogger(SocialMediaScraper.class.getName());
        if (this.DEBUG_.equals("1")) {
            System.out.println("Navigating to Instagram Reels URL: " + instaReelsUrl);
        }


        // Set Chrome options to use the default profile
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + UserDataDir_);
        options.addArguments("profile-directory=" + Profile_);
        if (this.DEBUG_.equals("0")) {
            options.addArguments("--headless=new");
        }
        // Optionally, you can add other arguments like:
        // options.addArguments("--disable-dev-shm-usage", "--no-sandbox", "--remote-debugging-port=9222");

        try {
            // Initialize the ChromeDriver with the options
            this.driver = new ChromeDriver(options);

            driver.manage().window().maximize();

            // Navigate to the Instagram Reels page
            this.driver.get(instaReelsUrl);
            Thread.sleep(5000); // Wait for the page to load completely

            WebDriverWait wait = new WebDriverWait(driver, 30);
            int totalPosts = 0; // Counter to track scraped posts
            int divIndex = 1;   // Start with the first div

            if (this.instaCount_ == 0) {
                // Logic for scraping all posts from today
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Scraping all posts from today...");
                }
                while (true) {
                    try {
                        // Locate the target div by its CSS selector
                        String divSelector = String.format("div._ac7v.x12nagc.xn8zkq8:nth-of-type(%d)", divIndex);
                        WebElement targetDiv = driver.findElement(By.cssSelector(divSelector));
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("Target div found: " + divIndex);
                        }
                        // Find all post elements within the target div
                        List<WebElement> postElements = targetDiv.findElements(By.cssSelector(
                                "div.x1qjc9v5.x972fbf.xcfux6l.x1qhh985.xm0m39n.x9f619.x78zum5.xdt5ytf.x2lah0s.xln7xf2.xk390pu.xdj266r.xat24cr.x1mh8g0r.xexx8yu.x4uap5.x18d9i69.xkhd6sd.x1n2onr6.x11njtxf.xpzaatj.xw3qccf"
                        ));
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("Post elements found: " + postElements.size());
                        }

                        boolean postFound = false; // Flag to indicate if any post from today was found

                        for (WebElement postElement : postElements) {
                            // Click the post to view its details
                            WebElement postLink = postElement.findElement(By.cssSelector("a"));
                            String relativePostUrl = postLink.getAttribute("href");
                            postLink.click();
                            Thread.sleep(this.waitSec_);

                            // Wait for the time element that shows when the post was made
                            WebElement specificDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                    By.xpath("//time[contains(@class, 'x1p4m5qa')]")
                            ));
                            String timestampText = specificDiv.getText().toLowerCase();

                            // Check if the post is from today based on the relative time text
                            if (timestampText.contains("hour ago") || timestampText.contains("hours ago") ||
                                    timestampText.contains("minute ago") || timestampText.contains("minutes ago") ||
                                    timestampText.contains("second ago") || timestampText.contains("seconds ago")) {

                                postFound = true;

                                // Format the post date from the datetime attribute
                                String datetime = specificDiv.getAttribute("datetime");
                                String postDate = "";
                                if (datetime != null) {
                                    DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    LocalDateTime dateTime = LocalDateTime.parse(datetime, inputFormatter);
                                    postDate = dateTime.format(outputFormatter);
                                }

                                // Extract title and content using a regular expression
                                WebElement titleElement = driver.findElement(By.cssSelector(
                                        "h1._ap3a._aaco._aacu._aacx._aad7._aade"
                                ));
                                String text = titleElement.getText();
                                String regex = "\\【(.*?)\\】"; // Matches text between 【 and 】
                                Pattern pattern = Pattern.compile(regex);
                                Matcher titleMatch = pattern.matcher(text);
                                String title = "";
                                String content = "";
                                if (titleMatch.find()) {
                                    title = "【" + titleMatch.group(1) + "】";
                                    int contentStartIndex = titleMatch.start() + titleMatch.group(0).length();
                                    content = text.substring(contentStartIndex).trim();
                                }

                                // Save the scraped data
                                String fullPostUrl = relativePostUrl;
                                this.hrefArray.add(fullPostUrl);
                                this.dateArray.add(postDate);
                                this.titleArray.add(removeEmoji(title));
                                this.matchedKeywordArray.add("Instagram Reels Data");
                                this.contentArray.add(removeEmoji(content));
                                this.sourceArray.add("2"); // Assuming 2 represents Instagram

                                if (this.DEBUG_.equals("1")) {
                                    System.out.println("---Instagram Reels Link(" + totalPosts + "): " + fullPostUrl);
                                    System.out.println("---Instagram Reels Title(" + totalPosts + "): " + title);
                                    System.out.println("---Instagram Reels Content(" + totalPosts + "): " + content);
                                    System.out.println("---Instagram Reels Date(" + totalPosts + "): " + postDate);
                                }

                                totalPosts++; // Increment the count of scraped posts

                                // Navigate back to the main page to continue scraping
                                this.driver.navigate().back();
                                Thread.sleep(this.waitSec_);
                            } else {
                                if (this.DEBUG_.equals("1")) {
                                    System.out.println("No more posts from today in this div.");
                                }
                                // Early exit; the finally block will close the browser.
                                return;
                            }
                        }

                        if (!postFound) {
                            if (this.DEBUG_.equals("1")) {
                                System.out.println("No post found for today.");
                            }
                            break;
                        }

                        divIndex++; // Move to the next div
                    } catch (NoSuchElementException e) {
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("No more divs found. Scraping complete.");
                        }
                        break;
                    }
                }
            } else {
                // Logic for scraping a fixed number of posts (instaCount_ > 0)
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Scraping up to " + this.instaCount_ + " posts...");
                }
                while (totalPosts < this.instaCount_) {
                    try {
                        String divSelector = String.format("div._ac7v.x12nagc.xn8zkq8:nth-of-type(%d)", divIndex);
                        WebElement targetDiv = driver.findElement(By.cssSelector(divSelector));
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("Target div found: " + divIndex);
                        }
                        List<WebElement> postElements = targetDiv.findElements(By.cssSelector(
                                "div.x1qjc9v5.x972fbf.xcfux6l.x1qhh985.xm0m39n.x9f619.x78zum5.xdt5ytf.x2lah0s.xln7xf2.xk390pu.xdj266r.xat24cr.x1mh8g0r.xexx8yu.x4uap5.x18d9i69.xkhd6sd.x1n2onr6.x11njtxf.xpzaatj.xw3qccf"
                        ));
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("Post elements found: " + postElements.size());
                        }

                        for (WebElement postElement : postElements) {
                            if (totalPosts >= this.instaCount_) {
                                if (this.DEBUG_.equals("1")) {
                                    System.out.println("Reached the desired number of posts: " + this.instaCount_);
                                }
                                return;
                            }

                            WebElement postLink = postElement.findElement(By.cssSelector("a"));
                            String relativePostUrl = postLink.getAttribute("href");
                            postLink.click();
                            Thread.sleep(this.waitSec_);

                            WebElement specificDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                    By.xpath("//time[contains(@class, 'x1p4m5qa')]")
                            ));
                            String datetime = specificDiv.getAttribute("datetime");
                            String postDate = "";
                            if (datetime != null) {
                                DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                LocalDateTime dateTime = LocalDateTime.parse(datetime, inputFormatter);
                                postDate = dateTime.format(outputFormatter);
                            }

                            WebElement titleElement = driver.findElement(By.cssSelector(
                                    "h1._ap3a._aaco._aacu._aacx._aad7._aade"
                            ));
                            String text = titleElement.getText();
                            String regex = "\\【(.*?)\\】";
                            Pattern pattern = Pattern.compile(regex);
                            Matcher titleMatch = pattern.matcher(text);
                            String title = "";
                            String content = "";
                            if (titleMatch.find()) {
                                title = "【" + titleMatch.group(1) + "】";
                                int contentStartIndex = titleMatch.start() + titleMatch.group(0).length();
                                content = text.substring(contentStartIndex).trim();
                            }

                            String fullPostUrl = relativePostUrl;
                            this.hrefArray.add(fullPostUrl);
                            this.dateArray.add(postDate);
                            this.titleArray.add(removeEmoji(title));
                            this.matchedKeywordArray.add("Instagram Reels Data");
                            this.contentArray.add(removeEmoji(content));
                            this.sourceArray.add("2");

                            if (this.DEBUG_.equals("1")) {
                                System.out.println("---Instagram Reels Link(" + totalPosts + "): " + fullPostUrl);
                                System.out.println("---Instagram Reels Title(" + totalPosts + "): " + title);
                                System.out.println("---Instagram Reels Content(" + totalPosts + "): " + content);
                                System.out.println("---Instagram Reels Date(" + totalPosts + "): " + postDate);
                            }

                            totalPosts++; // Increment the total posts count

                            // Navigate back to continue scraping
                            this.driver.navigate().back();
                            Thread.sleep(this.waitSec_);
                        }
                    } catch (NoSuchElementException e) {
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("No more divs found. Scraping complete.");
                        }
                        break;
                    }
                    divIndex++; // Move to the next div
                }
            }

            if (this.DEBUG_.equals("1")) {
                System.out.println("Instagram Reels scraping completed. Total posts scraped: " + totalPosts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Always quit the driver, even if an early return or exception occurred.
            if (this.driver != null) {
                this.driver.quit();
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Browser closed.");
                }
            }
        }
    }

    private void RssScraper(final String RssUrl, final int RssCount_) throws InterruptedException {
        try {
            ChromeOptions options = new ChromeOptions();
            if (this.DEBUG_.equals("0")) {
                options.addArguments("--headless=new");
            }
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");

            this.driver = new ChromeDriver(options);
            this.driver.get(RssUrl);
            Thread.sleep(5000);

            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            List<WebElement> items = driver.findElements(By.tagName("item"));
            int itemCount = items.size();
            int fbCount = 0, ytCount = 0, igCount = 0;
            boolean foundTodayPost = false;

            if (this.DEBUG_.equals("1")) {
                System.out.println("RssScraper Started. Found " + itemCount + " RSS Items.");
            }

            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
            SimpleDateFormat checkDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String todayDate = checkDateFormat.format(new Date());

            if (RssCount_ == 0) {
                for (int i = 0; i < itemCount; i++) {
                    WebElement item = items.get(i);
                    String rawDate = (String) jsExecutor.executeScript(
                            "return document.querySelectorAll('item > pubDate')[" + i + "].textContent.trim();"
                    );

                    try {
                        Date parsedDate = inputFormat.parse(rawDate);
                        String postDate = checkDateFormat.format(parsedDate);

                        if (!postDate.equals(todayDate)) {
                            if (!foundTodayPost) {
                                if (this.DEBUG_.equals("1")) {
                                    System.out.println("No more posts from today.");
                                }
                            }
                            break;
                        } else {
                            foundTodayPost = true;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (!foundTodayPost) {
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("No Posts from today.");
                    }
                }
            } else {
                for (int i = 0; i < itemCount; i++) {
                    WebElement item = items.get(i);
                    String rawTitle = (String) jsExecutor.executeScript(
                            "return document.querySelectorAll('item > title')[" + i + "].textContent.trim();"
                    );
                    String title = extractTitle(rawTitle);
                    String link = (String) jsExecutor.executeScript(
                            "return document.querySelectorAll('item > link')[" + i + "].textContent.trim();"
                    );
                    String contentHtml = (String) jsExecutor.executeScript(
                            "return document.querySelectorAll('item > description')[" + i + "].textContent.trim();"
                    );
                    String content = contentHtml.replaceAll("<.*?>", "").trim();
                    content = removeTitleFromContent(content, title);
                    String rawDate = (String) jsExecutor.executeScript(
                            "return document.querySelectorAll('item > pubDate')[" + i + "].textContent.trim();"
                    );

                    String formattedDate = "Unknown Date";
                    try {
                        Date parsedDate = inputFormat.parse(rawDate);
                        formattedDate = outputFormat.format(parsedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String sourceCategory;
                    String matchedKeyword;
                    if (link.contains("facebook.com")) {
                        if (fbCount >= RssCount_) continue;
                        sourceCategory = "4";
                        matchedKeyword = "FB Data";
                        fbCount++;
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("Facebook Title: " + title);
                            System.out.println("Facebook Content: " + content);
                            System.out.println("Facebook Date: " + formattedDate);
                            System.out.println("Facebook Link: " + link);
                        }
                    } else if (link.contains("youtube.com") || link.contains("youtu.be")) {
                        if (ytCount >= RssCount_) continue;
                        sourceCategory = "3";
                        matchedKeyword = "Youtube Data";
                        ytCount++;
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("YouTube Title: " + title);
                            System.out.println("YouTube Content: " + content);
                            System.out.println("YouTube Date: " + formattedDate);
                            System.out.println("YouTube Link: " + link);
                        }
                    } else if (link.contains("instagram.com")) {
                        if (igCount >= RssCount_) continue;
                        sourceCategory = "2";
                        matchedKeyword = "Instagram Data";
                        igCount++;
                        if (this.DEBUG_.equals("1")) {
                            System.out.println("Instagram Title: " + title);
                            System.out.println("Instagram Content: " + content);
                            System.out.println("Instagram Date: " + formattedDate);
                            System.out.println("Instagram Link: " + link);
                        }
                    } else {
                        sourceCategory = "1";
                        matchedKeyword = "RSS Data";
                    }

                    this.hrefArray.add(link);
                    this.dateArray.add(formattedDate);
                    this.titleArray.add(title);
                    this.matchedKeywordArray.add(matchedKeyword);
                    this.contentArray.add(content);
                    this.sourceArray.add(sourceCategory);

                    if (fbCount >= RssCount_ && ytCount >= RssCount_ && igCount >= RssCount_) break;
                }
            }

            if (this.DEBUG_.equals("1")) {
                System.out.println("RssScraper Completed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (this.driver != null) {
                this.driver.quit();
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Browser closed.");
                }
            }
        }
    }

    public void xmllinkScraper(final String xmllink) {
        try {
            ChromeOptions options = new ChromeOptions();
            if (this.DEBUG_.equals("0")) {
                options.addArguments("--headless=new");
            }
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            this.driver = new ChromeDriver(options);
            this.driver.get(xmllink);
            Thread.sleep(5000);
            JavascriptExecutor js = (JavascriptExecutor) this.driver;
            List<WebElement> items = driver.findElements(By.cssSelector("div#items > div"));
            int itemCount = items.size();
            if (this.DEBUG_.equals("1")) {
                System.out.println("xmllinkScraper Started. Found " + itemCount + " item(s).");
            }
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            inputFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            SimpleDateFormat checkDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String todayDate = checkDateFormat.format(new Date());
            boolean foundTodayPost = false;
            for (int i = 0; i < itemCount; i++) {
                String rawDate = (String) js.executeScript(
                        "return document.querySelector('div#items > div:nth-of-type(" + (i + 1) + ") > p')?.textContent.trim();"
                );
                if (rawDate == null || rawDate.isEmpty()) {
                    continue;
                }
                Date parsedDate = inputFormat.parse(rawDate);
                String postDate = checkDateFormat.format(parsedDate);
                if (!postDate.equals(todayDate)) {
                    if (!foundTodayPost && this.DEBUG_.equals("1")) {
                        System.out.println("No more posts from today.");
                    }
                    break;
                }
                foundTodayPost = true;
                String formattedDate = outputFormat.format(parsedDate);
                String title = (String) js.executeScript(
                        "return document.querySelector('div#items > div:nth-of-type(" + (i + 1) + ") > h3 > a')?.textContent.trim();"
                );
                String content = (String) js.executeScript(
                        "return document.querySelector('div#items > div:nth-of-type(" + (i + 1) + ") > p:nth-of-type(2)')?.textContent.trim();"
                );
                String link = (String) js.executeScript(
                        "return document.querySelector('div#items > div:nth-of-type(" + (i + 1) + ") > h3 > a')?.href;"
                );
                if (title == null) {
                    title = "";
                }
                if (content == null) {
                    content = "";
                }
                boolean matched = false;
                String matchedKw = "";
                for (String kw : this.keywords_) {
                    if (title.contains(kw) || content.contains(kw)) {
                        matched = true;
                        matchedKw = kw;
                        break;
                    }
                }
                if (matched) {
                    if (this.DEBUG_.equals("1")) {
                        System.out.println("Title: " + title);
                        System.out.println("Content: " + content);
                        System.out.println("Date: " + formattedDate);
                        System.out.println("Link: " + link);
                        System.out.println("Matched Keyword: " + matchedKw);
                    }
                    this.hrefArray.add(link);
                    this.dateArray.add(formattedDate);
                    this.titleArray.add(title);
                    this.matchedKeywordArray.add(matchedKw);
                    this.contentArray.add(content);
                    this.sourceArray.add("1");
                }
            }
            if (!foundTodayPost && this.DEBUG_.equals("1")) {
                System.out.println("No Posts from today.");
            }
            if (this.DEBUG_.equals("1")) {
                System.out.println("xmllinkScraper Completed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (this.driver != null) {
                this.driver.quit();
                if (this.DEBUG_.equals("1")) {
                    System.out.println("Browser closed.");
                }
            }
        }
    }


    private String extractTitle(String rawTitle) {
        Pattern pattern = Pattern.compile("【(.*?)】");
        Matcher matcher = pattern.matcher(rawTitle);
        if (matcher.find()) {
            return "【" + matcher.group(1) + "】";
        }
        return rawTitle;
    }

    private String removeTitleFromContent(String content, String title) {
        return content.replace(title, "").trim();
    }


    public String removeEmoji(final String str) {
        // Define the regex pattern to match emojis
        final String emojiPattern = "[\\p{So}]";
        final Pattern emojiPatternCompiled = Pattern.compile(emojiPattern);
        final Matcher emojiMatcher = emojiPatternCompiled.matcher(str);

        // Remove all emojis from the input
        String cleanedInput = emojiMatcher.replaceAll("");

        // Define a regex to match trailing English text, numbers, and special characters
        final String trailingPattern = "(?<=\\p{IsHan})[a-zA-Z0-9\\s]+$|^[a-zA-Z0-9\\s]+(?=\\p{IsHan})";

        // Replace trailing or leading English text, numbers, and special characters if not surrounded by Chinese characters
        cleanedInput = cleanedInput.replaceAll(trailingPattern, "");

        // Define a regex to match lines that are only English text or numbers at the end
        final String linePattern = "(?m)^[a-zA-Z0-9\\s]+$";

        // Replace such lines with an empty string
        cleanedInput = cleanedInput.replaceAll(linePattern, "").trim();

        return cleanedInput;
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

    public String getDateArray(final int i) {
        return this.dateArray.get(i);
    }

    public String getMatchedKeywordArray(final int i) {
        return this.matchedKeywordArray.get(i);
    }

    public String getContentArray(final int i) {
        return this.contentArray.get(i);
    }

    public String getSourceArray(final int i) {
        return this.sourceArray.get(i);
    }
}