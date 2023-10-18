
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for getting website addresses and emails from these addresses
 */
public class Ripper {

    private static Document doc;

    /**
     * Connects to the specific website address
     * @param url
     */
    private static void connect(String url) {
        try {
            doc = Jsoup.connect(url).get();
        } catch (SocketTimeoutException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }

    /**
     * Overloading of the original connect(String url) method
     * Connects to google search url and opens a specific page
     * @param url
     * @param pageNumber
     */
    private static void connect(String url, int pageNumber) {
        try {
            doc = Jsoup.connect(url).get();
            if(pageNumber > 1) {
                Elements els = doc.getElementsByAttributeValue("aria-label", "Page " + pageNumber);
                String newUrl = "https://www.google.com" + els.get(0).attr("href");
                doc = Jsoup.connect(newUrl).get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all the website addresses on the google search page
     * except for addresses that start with "https://www.google.com"
     * or "/search" or "https://support" or "https://policies"
     * @param url
     * @param pageNumber
     * @return ArrayList of found website addresses
     */
    public static ArrayList<String> ripWebsiteAddresses(String url, int pageNumber) {

        connect(url, pageNumber);

        Elements addresses = doc.getElementsByTag("a");
        ArrayList<String> linkList = new ArrayList<>();
        for(Element link : addresses) {
            String s = link.attr("href");
            boolean notNeededLink = s.startsWith("https://www.google.com") || s.startsWith("/search") || s.startsWith("https://support")
                    || s.startsWith("https://policies")|| s.equals("#") || s.equals("");
            if(notNeededLink)
                continue;
            linkList.add(s);
        }
        return new ArrayList<String>(Arrays.asList(linkList.toArray(new String[0])));
    }

    /**
     * Gets the names of the companies on the google search page
     * @param url
     * @param pageNumber
     * @return ArrayList of found company names
     */
    public static ArrayList<String> ripCompanyNames(String url, int pageNumber) {

        connect(url, pageNumber);

        ArrayList<String> websiteList = new ArrayList<>();

        Elements s = doc.getElementsByAttributeValue("role", "heading");
        for(Element e : s) {
            if(e.child(0).ownText().equals(""))
                continue;
            websiteList.add(e.child(0).ownText());
        }

        return new ArrayList<>(Arrays.asList(websiteList.toArray(new String[0])));

    }

    /**
     * Gets the emails found on the homepage and contacts pages of the website
     * @param url
     * @return ArrayList of the emails found on the specific website
     */
    public static ArrayList<String> ripEmails(String url) {

        ArrayList<String> emails = new ArrayList<>();

        addFoundEmailsToList(url, emails);
        String contactPage = getContactPage(url);
        if(!contactPage.equals(""))
            addFoundEmailsToList(contactPage, emails);

        return emails;
    }

    /**
     * Checks if found string are really emails and add them to the provided list
     * @param url
     * @param emails
     */
    private static void addFoundEmailsToList(String url, List<String> emails) {
        try {
            connect(url);
        } catch (IllegalArgumentException e) {}

        Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
        Matcher matcher;
        try {
            matcher = p.matcher(doc.text());
        } catch(NullPointerException e) {
            return;
        }

        while (matcher.find()) {
            String s = matcher.group();
            if(s.endsWith("."))
                s = s.substring(0, s.length() - 2);
            if(!emails.contains(s))
                emails.add(s);
        }
    }

    /**
     * Founds contact page of the website
     * <h3>WORKS ONLY WITH ENGLISH WEBSITES VERSIONS</h3>
     * @param url
     * @return url of the contact page
     */
    private static String getContactPage(String url) {
        String contactPageLink = "";

        connect(url);
        Elements links;
        try {
            links = doc.select("a");
        } catch(NullPointerException e) {
            return "";
        }

        for(Element link : links) {
            boolean needed = link.ownText().equalsIgnoreCase("contact") || link.ownText().equalsIgnoreCase("contact us");
            if(needed) {
                String text = link.ownText();
                if(link.attr("href").startsWith("https://") || link.attr("href").startsWith("http://")) {
                    contactPageLink = link.attr("href");
                } else {
                    contactPageLink = url + (link.attr("href").replaceFirst("/", ""));
                }
            }
        }

        return contactPageLink;
    }

}
