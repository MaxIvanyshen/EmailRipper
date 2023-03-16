package ua.ivanyshen.emailripper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class Controller {

    @RequestMapping(value = "/getEmail",
            method = RequestMethod.POST,
            produces = "application/json")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity getEmail(@RequestBody String url) {
        ArrayList<String> result = new ArrayList<>();
        String[] websites = Ripper.ripWebsiteAddresses(url, 1);
        for(String s : websites) {
            Ripper.ripEmails(s).stream().forEach(x -> result.add(x));
        }

        return ResponseEntity.ok(result);
    }
}
