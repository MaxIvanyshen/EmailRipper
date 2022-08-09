# EmailRipper
 A tool for parsing website addresses and company names found on google search maps tab and email addresses found on these websites. Written using Jsoup Java library
 
 ## How to run
 - clone the repository on your machine
 - read the javadoc for ```Ripper``` class
 - run the [example code](#example-code) or use ```Ripper``` class methods as you wish

## <p id="example-code">Example code<p/>
This code takes the emails of the architectural firms in Belgium found by this link: https://bit.ly/3QetS9S
 ```java
public class Example {
    public static void main(String[] args) {
        String url = "https://bit.ly/3QetS9S";
        ArrayList<String> websites = Ripper.ripWebsiteAddresses(url, 1); //taking website addresses only from the first page

        ArrayList<String> foundEmails = new ArrayList<>(); // ArrayList to store all found email addresses
        for(String websiteURL : websites) {
            ArrayList<String> emails = Ripper.ripEmails(websiteURL); // emails found on the website
            for(String email : emails)
                foundEmails.add(email);
        }

        foundEmails.stream().forEach(s -> System.out.println(s));
    }
}
 ```
 Output of this code should look similar to this:
 ```
 info@lucasfreire.be
hofmans@architecthofmans.be
info@oyo.eu
oyobcn@oyo.eu
jobs@oyo.eu
business@oyo.eu
press@oyo.eu
gandert@oyo.eu
jobs@oyo.e
info@wax-architecture.be
info@conixrdbm.com
g.vidts@conixrdbm.com
cv@conixrdbm.com
info@b-ild.com
studio@studiofarris.com
office@bc-as.org
info@low-architecten.be
info@archi2000.be
20info@archi2000.be
```
