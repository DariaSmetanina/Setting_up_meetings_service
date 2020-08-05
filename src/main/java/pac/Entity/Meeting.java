package pac.Entity;

import java.util.Date;
import java.util.ArrayList;

public class Meeting implements Comparable<Meeting> {
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
    public Date getDatetime(){
        return datetime;
    }
    public String getPlace(){return place;}
    public String getOrg(){return organizer;}
    public String getParticipants(){
        if(participants.size()==0){
            return "NO PARTICIPANTS";
        }
        String res="";
        for(Participant p:participants){
            res=res+p.getNameAndEmail();
        }
        return res;
    }

    public void addParticipant(Participant p){
        participants.add(p);
    }

    @Override
    public int compareTo(Meeting m) {
        return getDatetime().compareTo(m.getDatetime());
    }
}
