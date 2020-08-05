package pac.DB;

import org.junit.AfterClass;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DBTest {

    private static DB db=new DB();


    @AfterClass
    public  static void afterClass() {
        db.exit();
    }

    @Test
    void addMeeting() {
        String email="email@mail.ru";
        Date dt=new Date(new java.util.Date().getTime());
        String place="TestHome";
        db.addMeeting(email, dt, place);

        assertTrue(db.findMeeting(email, dt, place));
    }

    @Test
    void addParticipant() {
        String email="smetanina.03@mail.ru";
        int idMeeting=15;
        db.addParticipant(idMeeting, email);

        assertTrue(db.findParticipant(idMeeting, email));
    }

    @Test
    void delMeeting() throws ParseException {
        String email="email@mail.ru";
        String place="TestHome";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.ENGLISH);

        java.util.Date date = formatter.parse("05-08-2020 00:00:00 AM");
        Date dt=new Date(date.getTime());
        db.delMeeting(email, 14);

        assertFalse(db.findMeeting(email, dt, place));
    }

    @Test
    void delParticipant() {
        String email="smetanina.03@mail.ru";
        int idMeeting=15;
        db.delParticipant(idMeeting, email);

        assertFalse(db.findParticipant(idMeeting, email));
    }

}
