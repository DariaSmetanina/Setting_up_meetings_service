package pac.Controller;

import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import pac.BL.BL;

@RestController
@RequestMapping("participant")
public class ParticipantController {
    @GetMapping
    public String list(){
        return "fetch(\n" +
                "  '/participant', \n" +
                "  { \n" +
                "    method: 'POST', \n" +
                "    headers: { 'Content-Type': 'application/json' },\n" +
                "    body: JSON.stringify({ id: 5, email: 'smetanina.03@mail.ru'})\n" +
                "  }\n" +
                ").then(result => result.json().then(console.log))";
    }

    @PostMapping
    public Map<String, String>  addParticipant(@RequestBody Map<String, String> message) throws ParseException {
        BL.addParticipant(message);
        return message;
    }
    @DeleteMapping
    public Map<String, String> deleteParticipant(@RequestBody Map<String, String> message){
        BL.delParticipant(message);
        return message;
    }
}
