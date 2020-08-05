package pac.BL;

import pac.Entity.Meeting;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import pac.DB.*;
import pac.Entity.*;


public class BL {

    public static List<Map<String, String>> timetable(String email){
        DB db=new DB();
        ArrayList<Meeting> ar=db.timetable(email);
        List<Map<String, String>> res=new ArrayList<Map<String, String>>();
        for(Meeting m : ar){
            res.add(new HashMap<String, String>() {{ put("id", Integer.toString(m.getId())); put("date&time", m.getDatetime().toString());put("place", m.getPlace());put("organizer", m.getOrg());}});
        }

        db.exit();

        return res;
    }

    public static List<Map<String, String>> myMeetings(String email){
        DB db=new DB();
        ArrayList<Meeting> ar=db.myMeetings(email);
        List<Map<String, String>> res=new ArrayList<Map<String, String>>();
        for(Meeting m : ar){
            res.add(new HashMap<String, String>() {{ put("id", Integer.toString(m.getId())); put("date&time", m.getDatetime().toString());put("place", m.getPlace());put("participants", m.getParticipants());}});
        }

        db.exit();

        return res;
    }

    public static void createMeeting(Map<String, String> message) throws ParseException {
        DB db=new DB();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        java.util.Date date = formatter.parse(message.get("datetime"));
        Date dt=new Date(date.getTime());
        db.addMeeting(message.get("email"),dt,message.get("place"));

        db.exit();
    }

    public static void addParticipant(Map<String, String> message){
        DB db=new DB();

        db.addParticipant(Integer.parseInt(message.get("id")), message.get("email"));

        db.exit();
    }

    public static void delMeeting(Map<String, String> message){
        DB db=new DB();
        db.delMeeting(message.get("email"),Integer.parseInt(message.get("id")));
        db.exit();
    }

    public static void delParticipant(Map<String, String> message){
        DB db=new DB();
        db.delParticipant(Integer.parseInt(message.get("id")), message.get("email"));
        db.exit();
    }

}
