package ua.ivanyshen.emailripper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class Controller {

    @RequestMapping(value = "/getEmail",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity getEmail(@RequestBody EmailRipperRequest req) {
        ArrayList<String> result = new ArrayList<>();
        for(int i = req.getStart(); i <= req.getEnd(); i++) {
            String[] websites = Ripper.ripWebsiteAddresses(req.getUrl(), i);
            System.out.println(Arrays.toString(websites));
            for(String s : websites)
                Ripper.ripEmails(s).stream().forEach(x -> result.add(x));
        }
        return ResponseEntity.ok(result);
    }
}
