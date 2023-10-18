import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Example {
    public static void main(String[] args) throws IOException {
//        HashMap<String, int[]> map = new HashMap<>();
//
//        System.out.print("Enter the number of queries: ");
//        int n = new Scanner(System.in).nextInt();
//
//        for(int i = 0 ;i < n ; i++) {
//            Scanner sc = new Scanner(System.in);
//            System.out.print("Enter the url: ");
//            String s = sc.nextLine();
//            System.out.print("Enter the start page: ");
//            int a = sc.nextInt();
//            System.out.print("Enter the end page: ");
//            int b = sc.nextInt();
//            map.put(s, new int[]{a, b});
//        }
//
//        ArrayList<String> websites = new ArrayList<>();
//        for(String url : map.keySet()) {
//            for(int i = map.get(url)[0]; i <= map.get(url)[1]; i++) {
//                ArrayList<String> w = Ripper.ripWebsiteAddresses(url, i);
//                System.out.println(w.size());
//                w.stream().forEach(x -> websites.add(x));
//            }
//        }

        ArrayList<String> websites = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        while(!s.equals("done")) {
            websites.add("http://" + s);
            s = sc.nextLine();
        }

        HashMap<String, ArrayList<String>> csv = new HashMap<>();
        for(String websiteURL : websites) {
            ArrayList<String> emails = Ripper.ripEmails(websiteURL); // emails found on the website
            csv.put(websiteURL, emails);
        }

        BufferedWriter wr = new BufferedWriter(new FileWriter(new File("./emails.csv")));

        for(String url : csv.keySet()) {
            if(csv.get(url).size() != 0) {
                wr.write( url + ",");
                int emailCount = 0;
                for(int i = 0; i < 3; i++) {
                    String email = "";
                    try {
                        email = csv.get(url).get(i);
                    } catch (IndexOutOfBoundsException e) {
                        email = "";
                    }
                    wr.write(email);
                    emailCount++;
                    if(i == 2)
                        continue;
                    if(emailCount < 3)
                        wr.write(",");
                }
            }
            wr.newLine();
        }
        wr.close();
    }
}

