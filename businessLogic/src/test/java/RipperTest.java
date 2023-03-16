import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RipperTest {

    @Test
    public void testRippingWebsiteAddresses() {
        String url = "https://www.google.com/search?q=architecture+firms+switzerland&biw=1886&bih=920&sz=0&tbm=lcl&sxsrf=ALiCzsbMAMD-NcwAi_ZIbxmfCW4gW1iSSw%3A1654636900534&ei=ZMGfYtqRIJTzrgTuvZjwCQ&oq=architecture+firms+switzerland&gs_l=psy-ab.3...0.0.0.1663.0.0.0.0.0.0.0.0..0.0....0...1c..64.psy-ab..0.0.0....0.wZ-gvkTbta8#rlfi=hd:;si:;mv:[[47.6596465,9.115573999999999],[45.915672199999996,5.960416899999999]];tbs:lrf:!1m4!1u3!2m2!3m1!1e1!1m4!1u2!2m2!2m1!1e1!2m1!1e2!2m1!1e3,lf:1";
        String[] result = Ripper.ripWebsiteAddresses(url, 1);
        assertEquals("http://www.herzogdemeuron.com/", result[0]);
    }

    @Test
    public void testRippingWebsiteAddresses_secondPage() {
        String url = "https://www.google.com/search?q=architecture+firms+greece&sz=0&biw=1886&bih=920&tbm=lcl&sxsrf=ALiCzsYtN31AiZ3gkoRlVaH6FJ1I18qVpg%3A1654951169224&ei=AY2kYoamDYbSrgSagoGACg&oq=architecture+firms+greece&gs_l=psy-ab.3...0.0.0.3151.0.0.0.0.0.0.0.0..0.0....0...1c..64.psy-ab..0.0.0....0.aisx6Q6WoF0#rlfi=hd:;si:;mv:[[40.7766945,25.564568400000002],[37.2570815,21.5141685]];start:0";
        String[] result = Ripper.ripWebsiteAddresses(url, 2);
        assertEquals("http://stirixis.com/", result[0]);
    }

    @Test
    public void testRippingWebsiteAddresses_thirdPage() {
        String url = "https://www.google.com/search?q=architecture+firms+greece&sz=0&biw=1886&bih=920&tbm=lcl&sxsrf=ALiCzsYtN31AiZ3gkoRlVaH6FJ1I18qVpg%3A1654951169224&ei=AY2kYoamDYbSrgSagoGACg&oq=architecture+firms+greece&gs_l=psy-ab.3...0.0.0.3151.0.0.0.0.0.0.0.0..0.0....0...1c..64.psy-ab..0.0.0....0.aisx6Q6WoF0#rlfi=hd:;si:;mv:[[40.7766945,25.564568400000002],[37.2570815,21.5141685]];start:0";
        String[] result = Ripper.ripWebsiteAddresses(url, 3);
        assertEquals("http://www.rctech.gr/", result[0]);
    }

    @Test
    public void testRippingWebsiteAddresses_11thPage() {
        String url = "https://www.google.com/search?tbs=lrf:!1m4!1u3!2m2!3m1!1e1!1m4!1u2!2m2!2m1!1e1!2m1!1e2!2m1!1e3,lf:1&tbm=lcl&sxsrf=ALiCzsbPPckGVoSPG3XtZBG1CnCsYq73wg:1655382027308&q=architectural+firms+italy&rflfq=1&num=10&sa=X&ved=2ahUKEwi_19HN-rH4AhWA8rsIHWUGBAgQjGp6BAgFEEY&biw=1920&bih=929&dpr=1&rlst=f";
        String[] result = Ripper.ripWebsiteAddresses(url, 11);
        assertEquals("https://www.arckeo.it/", result[0]);
    }

    @Test
    public void testRippingCompanyNames() {
        String url = "https://www.google.com/search?q=architecture+firms+greece&sz=0&biw=1886&bih=920&tbm=lcl&sxsrf=ALiCzsYtN31AiZ3gkoRlVaH6FJ1I18qVpg%3A1654951169224&ei=AY2kYoamDYbSrgSagoGACg&oq=architecture+firms+greece&gs_l=psy-ab.3...0.0.0.3151.0.0.0.0.0.0.0.0..0.0....0...1c..64.psy-ab..0.0.0....0.aisx6Q6WoF0#rlfi=hd:;si:;mv:[[40.7766945,25.564568400000002],[37.2570815,21.5141685]];start:0";
        String[] result = Ripper.ripCompanyNames(url, 2);
        assertEquals("Stirixis Group", result[0]);
        assertEquals("Parti Architecture", result[1]);
    }

    @Test
    public void testRippingEmailFromWebsite() {
        String url = "https://www.evolution-design.info/";
        ArrayList<String> result = Ripper.ripEmails(url);
        assertTrue(result.contains("zurich@evolution-design.info"));
    }

    @Test
    public void testWritingToCSV() throws IOException {
        String url = "http://norellrodhe.se/";
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        map.put(url, Ripper.ripEmails(url));
        File f = new File("./test.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        writer.write(url + ",");
        ArrayList<String> emails = map.get(url);

        for (int i = 0; i < 3; i++) {
            if (i < emails.size())
                writer.write(emails.get(i));
            if (i != 2)
                writer.write(",");
        }

        writer.write("\n");
        writer.close();

        Scanner sc = new Scanner(f);
        String s = sc.nextLine();
        assertFalse(sc.hasNextLine());

        f.delete();
    }
}
