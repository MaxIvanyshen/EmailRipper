import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static HashMap<String, ArrayList<String>> emailsMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        // String url =
        // "https://www.google.com/search?q=architecture+firms+sweden&biw=2482&bih=981&tbm=lcl&sxsrf=ALiCzsaAVX_g5kFxiH0TH-FwAfujLF94hg%3A1654971936343&ei=IN6kYoHDFOyPwPAP3IuLiAo&oq=architecture+firms+sweden&gs_l=psy-ab.3..0i19k1j0i30i22i19k1l2.318794.320232.0.321030.6.6.0.0.0.0.109.591.3j3.6.0....0...1c.1.64.psy-ab..0.6.589...0i512k1j0i30i22k1j0i30i15i22k1j0i30i457i22i19k1.0.u7Su7wB4UjM#rlfi=hd:;si:;mv:[[59.570873500000005,18.5062394],[55.369439199999995,11.5916307]];tbs:lrf:!1m4!1u3!2m2!3m1!1e1!1m4!1u2!2m2!2m1!1e1!2m1!1e2!2m1!1e3,lf:1";

        // rip(Ripper.ripWebsiteAddresses(url, 2));
        // rip(Ripper.ripWebsiteAddresses(url, 3));
        // rip(Ripper.ripWebsiteAddresses(url, 4));
        // rip(Ripper.ripWebsiteAddresses(url, 5));
        // rip(Ripper.ripWebsiteAddresses(url, 6));

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the url: ");
        String url = sc.nextLine();




        System.out.println("Where to start?");
        int start = sc.nextInt();

        System.out.println("Where to end?");
        int end = sc.nextInt();

        sc.close();

        for (int i = start; i <= end; i++) {
            System.out.println("getting data from page " + i);
            rip(Ripper.ripWebsiteAddresses(url, i));
        }

        writeToFile();

        System.out.println(emailsMap.size());
    }

    public static void rip(String[] websites) {
        for (String website : websites) {
            System.out.println("Website: " + website + "; Emails: ");
            ArrayList<String> emailsResult = Ripper.ripEmails(website);
            for(String s : emailsResult)
                System.out.println(s);
            emailsMap.put(website, emailsResult);
        }
    }

    public static void writeToFile() throws IOException {
        File f = new File("./emails.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(f));

        for (Map.Entry<String, ArrayList<String>> entry : emailsMap.entrySet()) {
            ArrayList<String> emails = entry.getValue();
            if (emails.isEmpty())
                continue;
            writer.write(entry.getKey() + ",");
            for (int i = 0; i < 3; i++) {
                if (i < emails.size())
                    writer.write(emails.get(i));
                if (i != 2)
                    writer.write(",");
            }
            writer.write("\n");
        }

        writer.close();
    }
}
