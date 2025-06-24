package csd_newscorner2_auto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.sql.DriverManager;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class main {
    private static ArrayList<String> titleArray;
    private static ArrayList<String> hrefArray;
    private static ArrayList<String> matchedKeywordArray;
    private static ArrayList<String> dateArray;
    private static ArrayList<String> contentArray;
    private static ArrayList<String> categoryArray;

    static {
        main.titleArray = new ArrayList<>();
        main.hrefArray = new ArrayList<>();
        main.matchedKeywordArray = new ArrayList<>();
        main.dateArray = new ArrayList<>();
        main.contentArray = new ArrayList<>();
        main.categoryArray = new ArrayList<>();
    }

    public static void main(final String[] args) throws IOException, SQLException, InterruptedException {
        final String currentDir = "";
        final String[] keywords = readKeywordsFromFile(currentDir + "csd_newscorner2_keyword.txt");
        Date currentDate = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        String formattedDate = formatter.format(currentDate);
        System.out.println("\n\n****************CSD-SCRAPER-2025****************\n");
        System.out.println(formattedDate + "Keyword(s):");
        int index = 0;
        for (String keyword : keywords) {
            if (index != 0) {
                System.out.print(",");
            }
            System.out.print(keyword);
            ++index;
        }
        System.out.println("\n");
        final Properties prop = new Properties();
        InputStream input = null;
        Label_0277: {
            try {
                input = new FileInputStream(currentDir + "csd_config.properties");
                prop.load(input);
            } catch (IOException ex) {
                ex.printStackTrace();
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break Label_0277;
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        final String dbUrl = prop.getProperty("db.url");
        final String username = prop.getProperty("db.username");
        final String password = prop.getProperty("db.password");
        final String fbigUsername = prop.getProperty("fbig.username");
        final String fbigPassword = prop.getProperty("fbig.password");
        final String proxyProtocol = prop.getProperty("proxy.protocol");
        final String proxyHost = prop.getProperty("proxy.host");
        final String proxyPort = prop.getProperty("proxy.port");
        final String createdBy = prop.getProperty("createdBy.user");
        final String writeDB = prop.getProperty("writeDB");
        final String DEBUG = prop.getProperty("DEBUG");
        final String linkEnable = prop.getProperty("linkEnable");
        final String xmlEnable = prop.getProperty("xmlEnable");
        final String fbEnable = prop.getProperty("fbEnable");
        final String fbAltScraperAbitEnable = prop.getProperty("fbAltScraperAbitEnable");
        final String youtubeEnable = prop.getProperty("youtubeEnable");
        final String youtubeShortsEnable = prop.getProperty("youtubeShortsEnable");
        final String instaEnable = prop.getProperty("instaEnable");
        final String instaReelsEnable = prop.getProperty("instaReelsEnable");
        final String RssEnable = prop.getProperty("RssEnable");
        final String youtubeUrl = prop.getProperty("youtubeUrl");
        final String youtubeShortsUrl = prop.getProperty("youtubeShortsUrl");
        final String fbUrl = prop.getProperty("fbUrl");
        final String fbAltScraperAbitUrl = prop.getProperty("fbAltScraperAbitUrl");
        final String instaUrl = prop.getProperty("instaUrl");
        final String instaReelsUrl = prop.getProperty("instaReelsUrl");
        final String RssUrl = prop.getProperty("RssUrl");
        final int youtubeCount = Integer.parseInt(prop.getProperty("youtubeCount"));
        final int fbCount = Integer.parseInt(prop.getProperty("fbCount"));
        final int instaCount = Integer.parseInt(prop.getProperty("instaCount"));
        final int RssCount = Integer.parseInt(prop.getProperty("RssCount"));
        final int waitSec = Integer.parseInt(prop.getProperty("waitSec"));
        final String url = prop.getProperty("link");
        final String xmllink = prop.getProperty("xmllink");
        final String chromeDriverPath = prop.getProperty("chromeDriverPath");
        final String Binary = prop.getProperty("Binary");
        final String FacebookBinary = prop.getProperty("FacebookBinary");
        final String UserDataDir = prop.getProperty("UserDataDir");
        final String Profile = prop.getProperty("Profile");
        final String facebookUserDataDir = prop.getProperty("facebookUserDataDir");
        final String facebookProfile = prop.getProperty("facebookProfile");
        final String deviceName = prop.getProperty("deviceName");
        if (DEBUG.contentEquals("1")) {
            System.out.println("dbUrl:" + dbUrl);
            System.out.println("username:" + username);
            System.out.println("password:" + password);
            System.out.println("fbig.username:" + fbigUsername);
            System.out.println("fbig.password:" + fbigPassword);
            System.out.println("proxyProtocol:" + proxyProtocol);
            System.out.println("proxyHost:" + proxyHost);
            System.out.println("proxyPort:" + proxyPort);
            System.out.println("linkEnable:" + linkEnable);
            System.out.println("xmlEnable:" + xmlEnable);
            System.out.println("fbEnable:" + fbEnable);
            System.out.println("fbAltScraperAbitEnable:" + fbAltScraperAbitEnable);
            System.out.println("youtubeEnable:" + youtubeEnable);
            System.out.println("youtubeShortsEnable:" + youtubeShortsEnable);
            System.out.println("instaEnable:" + instaEnable);
            System.out.println("instaReelsEnable:" + instaReelsEnable);
            System.out.println("RssEnable:" + RssEnable);
            System.out.println("xmllink:" + xmllink);
            System.out.println("fbUrl:" + fbUrl);
            System.out.println("fbAltScraperAbitUrl:" + fbAltScraperAbitUrl);
            System.out.println("youtubeUrl:" + youtubeUrl);
            System.out.println("youtubeShortsUrl:" + youtubeShortsUrl);
            System.out.println("instaUrl:" + instaUrl);
            System.out.println("instaReelsUrl:" + instaReelsUrl);
            System.out.println("RssUrl:" + RssUrl);
            System.out.println("Created by userid:" + createdBy);
            System.out.println("Binary:" + Binary);
            System.out.println("chromeDriverPath:" + chromeDriverPath);
            System.out.println("FacebookBinary:" + FacebookBinary);
            System.out.println("UserDataDir_:" + UserDataDir);
            System.out.println("Profile:" + Profile);
            System.out.println("facebookUserDataDir:" + facebookUserDataDir);
            System.out.println("facebookProfile:" + facebookProfile);
            System.out.println("deviceName:" + deviceName);
        }
        SocialMediaScraper youtubeFbInstaData;
        if (xmlEnable.contentEquals("1") || youtubeEnable.contentEquals("1") || youtubeShortsEnable.equals("1") || fbEnable.equals("1") || fbAltScraperAbitEnable.equals("1") || instaEnable.equals("1") || instaReelsEnable.equals("1") || RssEnable.equals("1")) {
            youtubeFbInstaData = new SocialMediaScraper(xmlEnable, youtubeEnable, youtubeShortsEnable, fbEnable, fbAltScraperAbitEnable,instaEnable, instaReelsEnable, RssEnable, xmllink, youtubeUrl, youtubeShortsUrl, fbUrl, fbAltScraperAbitUrl, instaUrl, instaReelsUrl, RssUrl, youtubeCount, fbCount, instaCount, RssCount, proxyHost, proxyPort, DEBUG, waitSec, fbigUsername, fbigPassword, chromeDriverPath, Binary, FacebookBinary, UserDataDir, Profile, facebookUserDataDir, facebookProfile, deviceName, keywords);
            for (int i = 0; i < youtubeFbInstaData.sizeOfArray(); ++i) {
                main.hrefArray.add(youtubeFbInstaData.getHrefArray(i));
                main.titleArray.add(youtubeFbInstaData.getTitleArray(i));
                main.matchedKeywordArray.add(youtubeFbInstaData.getMatchedKeywordArray(i));
                main.dateArray.add(youtubeFbInstaData.getDateArray(i));
                main.contentArray.add(EmojiCharacterUtil.filter(youtubeFbInstaData.getContentArray(i)));
                main.categoryArray.add(youtubeFbInstaData.getSourceArray(i));
            }
        }
        youtubeFbInstaData = null;
        if (linkEnable.contentEquals("1")) {
            JsoupGetHtml jsoupGetHtml = new JsoupGetHtml(proxyHost, proxyPort, url, keywords, DEBUG, chromeDriverPath, Binary);
            for (int i = 0; i < jsoupGetHtml.sizeOfArray(); ++i) {
                String href = jsoupGetHtml.getHrefArray(i);
                String title = jsoupGetHtml.getTitleArray(i);
                String matchedKeyword = jsoupGetHtml.getMatchedKeywordArray(i);
                String date = jsoupGetHtml.getDateArray(i);
                String content = jsoupGetHtml.getContentArray(i);
                String source = jsoupGetHtml.getSourceArray(i);
                String filteredContent = EmojiCharacterUtil.filter(content);
                main.hrefArray.add(href);
                main.titleArray.add(title);
                main.matchedKeywordArray.add(matchedKeyword);
                main.dateArray.add(date);
                main.contentArray.add(filteredContent);
                main.categoryArray.add(source);
            }
            jsoupGetHtml = null;
        }
        try {
            if (DEBUG.equals("1")) {
                System.out.println("Attempting to close Google Portable Chrome and ChromeDriver...");
            }
            String[] processes = {"chromedriver.exe", "GoogleChromePortable.exe"};
            for (String process : processes) {
                Process checkProcess = Runtime.getRuntime().exec("tasklist");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(checkProcess.getInputStream()))) {
                    String line;
                    boolean found = false;
                    while ((line = reader.readLine()) != null) {
                        if (line.toLowerCase().contains(process.toLowerCase())) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        Process killProcess = Runtime.getRuntime().exec("taskkill /F /IM " + process + " /T");
                        if (killProcess.waitFor() == 0) {
                            if (DEBUG.equals("1")) {
                                System.out.println(process + " closed successfully.");
                            }
                        } else {
                            if (DEBUG.equals("1")) {
                                System.out.println("Failed to close " + process + ". Check manually.");
                            }
                        }
                    } else {
                        if (DEBUG.equals("1")) {
                            System.out.println(process + " not found running.");
                        }
                    }
                } catch (IOException e) {
                    if (DEBUG.equals("1")) {
                        System.out.println("Error reading tasklist or executing process.");
                    }
                    e.printStackTrace();
                }
            }
        } catch (IOException | InterruptedException e) {
            if (DEBUG.equals("1")) {
                System.out.println("An error occurred while closing Chrome processes.");
            }
            e.printStackTrace();
        }
        if (writeDB.contentEquals("1")) {
            System.out.println();
            if (DEBUG.equals("1")) {
                System.out.println("--Begin to connect database1--");
            }
        }
        final Connection conn = DriverManager.getConnection(dbUrl, username, password);
        index = 0;
        for (int j = 0; j < main.hrefArray.size(); ++j) {
            final String query = "SELECT COUNT(*) FROM newscorner2_post WHERE link=?";
            final PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, main.hrefArray.get(j));
            final ResultSet rs = stmt.executeQuery();
            rs.next();
            final int count = rs.getInt(1);
            if (count == 0) {
                if (main.hrefArray.get(j).contains("facebook")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MONTH, -3);
                    java.util.Date threeMonthsAgo = calendar.getTime();
                    final String queryfb = "SELECT COUNT(*) FROM newscorner2_post WHERE post_title=? AND publish_at > ?";
                    final PreparedStatement stmtfb = conn.prepareStatement(queryfb);
                    stmtfb.setString(1, main.titleArray.get(j));
                    stmtfb.setDate(2, new java.sql.Date(threeMonthsAgo.getTime()));
                    final ResultSet rsfb = stmtfb.executeQuery();
                    rsfb.next();
                    final int countfb = rsfb.getInt(1);
                    if (countfb == 0) {
                        index++;
                        if (writeDB.contentEquals("1")) {
                            System.out.println("System is Adding no.(" + j + ") record to database");
                            System.out.println("link:" + main.hrefArray.get(j));
                            System.out.println("title:" + main.titleArray.get(j));
                            System.out.println("date:" + main.dateArray.get(j));
                            System.out.println("Matched Keyword:" + main.matchedKeywordArray.get(j) + "\n");
                        }
                        final String _tmpquery = "INSERT INTO `newscorner2_post` (`id`, `category_id`, `post_title`, `created_by`, `created_at`, `content`, `link`, `is_deleted`, `deleted_by`, `deleted_at`, `last_post_at`, `modified_by`, `modified_at`, `publish_at`, `is_public`) VALUES (NULL, ?, ?, ?, ?, ?, ?, 0, NULL, NULL, ?, NULL, NOW(), NOW(), 1)";
                        final PreparedStatement _tmpstmt = conn.prepareStatement(_tmpquery);
                        _tmpstmt.setString(1, main.categoryArray.get(j));
                        _tmpstmt.setString(2, main.titleArray.get(j));
                        _tmpstmt.setString(3, createdBy);
                        _tmpstmt.setString(4, main.dateArray.get(j));
                        _tmpstmt.setString(5, main.contentArray.get(j));
                        _tmpstmt.setString(6, main.hrefArray.get(j));
                        _tmpstmt.setString(7, main.dateArray.get(j));
                        _tmpstmt.executeUpdate();
                        _tmpstmt.close();
                    }
                } else {
                    index++;
                    if (writeDB.contentEquals("1")) {
                        System.out.println("System is Adding no.(" + j + ") record to database");
                        System.out.println("link:" + main.hrefArray.get(j));
                        System.out.println("title:" + main.titleArray.get(j));
                        System.out.println("date:" + main.dateArray.get(j));
                        System.out.println("Matched Keyword:" + main.matchedKeywordArray.get(j) + "\n");
                    }
                    final String _tmpquery = "INSERT INTO `newscorner2_post` (`id`, `category_id`, `post_title`, `created_by`, `created_at`, `content`, `link`, `is_deleted`, `deleted_by`, `deleted_at`, `last_post_at`, `modified_by`, `modified_at`, `publish_at`, `is_public`) VALUES (NULL, ?, ?, ?, ?, ?, ?, 0, NULL, NULL, ?, NULL, NOW(), NOW(), 1)";
                    final PreparedStatement _tmpstmt = conn.prepareStatement(_tmpquery);
                    _tmpstmt.setString(1, main.categoryArray.get(j));
                    _tmpstmt.setString(2, main.titleArray.get(j));
                    _tmpstmt.setString(3, createdBy);
                    _tmpstmt.setString(4, main.dateArray.get(j));
                    _tmpstmt.setString(5, main.contentArray.get(j));
                    _tmpstmt.setString(6, main.hrefArray.get(j));
                    _tmpstmt.setString(7, main.dateArray.get(j));
                    _tmpstmt.executeUpdate();
                    _tmpstmt.close();
                }
            } else {
                if (writeDB.contentEquals("1")) {
                    System.out.println("link:" + main.hrefArray.get(j));
                    System.out.println("title:" + main.titleArray.get(j));
                    System.out.println("date:" + main.dateArray.get(j));
                    System.out.println("Matched Keyword:" + main.matchedKeywordArray.get(j) + "\n");
                }
            }
        }
        conn.close();
        System.out.println("**Inserted " + index + " record(s) to database**");
        System.out.println("**Completed.**");
    }

    private static String[] readKeywordsFromFile(final String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder keywords = new StringBuilder();
            while ((line = br.readLine()) != null) {
                keywords.append(line).append(",");
            }
            return keywords.toString().split(",");
        }
    }
}
