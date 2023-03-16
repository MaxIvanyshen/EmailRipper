import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.DocFlavor;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ripper {

    private static Document doc;

    private static void connect(String url) {
        try {
            doc = Jsoup.connect(url).get();
        } catch (SocketTimeoutException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }

    private static void connect(String url, int pageNumber) {
        try {
            doc = Jsoup.connect(url).get();
            if (pageNumber > 10) {
                connect(url, 10);
                doc = Jsoup.connect("https://www.google.com"
                        + doc.getElementsByAttributeValue("aria-label", "Page 10").get(0).attr("href")).get();
            }
            if (pageNumber > 1) {
                Elements els = doc.getElementsByAttributeValue("aria-label", "Page " + pageNumber);
                if(els.size() > 0) {
                    String newUrl = "https://www.google.com" + els.get(0).attr("href");

                    doc = Jsoup.connect(newUrl).get();
                }
                else
                    return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] ripWebsiteAddresses(String url, int pageNumber) {

        connect(url, pageNumber);

        Elements addresses = doc.getElementsByTag("a");
        ArrayList<String> linkList = new ArrayList<>();
        for (Element link : addresses) {
            String s = link.attr("href");
            boolean notNeededLink = s.startsWith("https://www.google.com") || s.startsWith("/search")
                    || s.startsWith("https://support")
                    || s.startsWith("https://policies") || s.equals("#") || s.equals("");
            if (notNeededLink)
                continue;
            linkList.add(s);
        }
        return linkList.toArray(new String[0]);
    }

    public static String[] ripCompanyNames(String url, int pageNumber) {

        connect(url, pageNumber);

        ArrayList<String> websiteList = new ArrayList<>();

        Elements s = doc.getElementsByAttributeValue("role", "heading");
        for (Element e : s) {
            if (e.child(0).ownText().equals(""))
                continue;
            websiteList.add(e.child(0).ownText());
        }

        return websiteList.toArray(new String[0]);

    }

    public static ArrayList<String> ripEmails(String url) {

        ArrayList<String> emails = new ArrayList<>();

        addToCollectionFoundEmails(url, emails);
        addToCollectionFoundEmails(getContactPage(url), emails);

        return emails;
    }

    private static void addToCollectionFoundEmails(String url, ArrayList<String> emails) {
        try {
            connect(url);
        } catch (IllegalArgumentException e) {
        }

        Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
        Matcher matcher = p.matcher(doc.text());
        while (matcher.find()) {
            String s = matcher.group();
            if (s.endsWith("."))
                s = s.substring(0, s.length() - 2);
            if (!emails.contains(s))
                emails.add(s);
        }
    }

    private static String getContactPage(String url) {
        String contactPageLink = "";

        connect(url);

        Elements links = doc.select("a");

        for (Element link : links) {
            boolean needed = link.ownText().equalsIgnoreCase("contact") || link.ownText().equalsIgnoreCase("contact us")
                    ||
                    link.ownText().equalsIgnoreCase("kontakt");
            if (needed) {
                String text = link.ownText();
                if (link.attr("href").startsWith("https://") || link.attr("href").startsWith("http://")) {
                    contactPageLink = link.attr("href");
                } else {
                    contactPageLink = url + (link.attr("href").replaceFirst("/", ""));
                }
            }
        }

        return contactPageLink;
    }

}
