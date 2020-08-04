import java.sql.Date;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        DB db=new DB();
     //   long millis=System.currentTimeMillis();
     //   db.addMeeting("email@mail.ru",new Date(millis),"Home1");
        ArrayList<Meeting> ar=db.timetable("email@mail.ru");
        for(Meeting m : ar){
            System.out.print(m.printMainInfo()+m.printOrg());
        }


        db.exit();
    }
}
