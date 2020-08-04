import java.util.Date;
import java.util.ArrayList;

public class Meeting {
    int id;
    Date datetime;
    String place;
    String organizer;
    ArrayList<Participant> participants;

    public Meeting(int i, Date dt, String p, String o){
        id=i;
        datetime=dt;
        place=p;
        organizer=o;
        participants=new ArrayList<Participant>();
    }

    public int getId(){
        return id;
    }

    public String printMainInfo(){
        return "id=" + id + "; datetime="+datetime+" ; place="+place+"; ";
    }

    public String printOrg(){
        return organizer+";\n";
    }

    public String printParticipants(){
        String part="";
        for(Participant p:participants){
            part=part+p.getNameAndEmail();
        }
        return "\n PARTICIPANTS: \n"+
                part+"\n";

    }

}
