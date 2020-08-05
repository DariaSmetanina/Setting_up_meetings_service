package pac.Controller;

import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import pac.BL.BL;

@RestController
@RequestMapping("meeting")
public class MeetingController {
    @GetMapping
    public String list(){
        return "fetch(\n" +
                "  '/meeting', \n" +
                "  { \n" +
                "    method: 'POST', \n" +
                "    headers: { 'Content-Type': 'application/json' },\n" +
                "    body: JSON.stringify({ email: 'smetanina.03@mail.ru', datetime: '30-03-2015 10:15:55 AM', place:'Somewhere' })\n" +
                "  }\n" +
                ").then(result => result.json().then(console.log))";
    }
    @GetMapping("{email}")
    public List<Map<String, String>> getTimetable(@PathVariable String email) {
        return BL.timetable(email);
    }

    @GetMapping("my/{email}")
    public List<Map<String, String>> geMyMeetings(@PathVariable String email) {
        return BL.myMeetings(email);
    }

    @PostMapping
    public Map<String, String>  createMeeting(@RequestBody Map<String, String> message) throws ParseException {
        BL.createMeeting(message);
        return message;
    }

    @DeleteMapping
    public Map<String, String> deleteMeeting(@RequestBody Map<String, String> message){
        BL.delMeeting(message);
        return message;
    }

}
