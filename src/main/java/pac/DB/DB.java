package pac.DB;
import javafx.util.Pair;
import pac.Entity.*;

import java.sql.*;
import java.util.Collections;
import java.util.Date;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DB {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/Avito?serverTimezone=Europe/Moscow&useSSL=false";
    private static final String user = "root";
    private static final String password = "@WSX2wsx";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;


    public DB(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            System.out.print(" correct conection");

        } catch (Exception e) {
            System.out.print(" error connection");
            System.out.print(e.toString());
        }
    }

    public void addMeeting(String email, java.sql.Date datetime, String place){
        int id=getIdByEmail(email);
    if(id==-1){
        System.out.print("no email");
        return;
    }
    String query = "INSERT INTO meeting (datetime, place, idorganizer)" +
                "VALUES('" +datetime.toString()+"','"+place+"','"+id+"');";
        try {
            stmt.executeUpdate(query);
        }
        catch (Exception e) {
            System.out.print(" error ");
            System.out.print(e.toString());
        }
    }

    private int getIdByEmail(String email){
        String query ="SELECT idparticipant FROM participant WHERE (`email` = '"+email+"');";
        int id=-1;
        try {
            rs = stmt.executeQuery(query);
            if(rs.next()){
                id = rs.getInt(1);
                rs.close();
            }
        }
        catch (Exception e){
            System.out.print(e+"\n");
        }
        return id;
    }

    private Timestamp getMeetingDate(int id){
        ArrayList<Meeting> meetings=new ArrayList<Meeting>();
        String query = "SELECT datetime from meeting " +
                "WHERE idmeeting="+id+";";
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        try {

            rs = stmt.executeQuery(query);

            if (rs.next()) {
                timestamp = rs.getTimestamp(1);
                }
            rs.close();}

        catch (Exception e) {
            System.out.print("error place");
            System.out.print(e.toString());
        }

        return timestamp;
    }

    public String participantIsFree(int idmeeting, String email){
        String res="";
        Timestamp dt=getMeetingDate(idmeeting);
        Timestamp before=new Timestamp(dt.getTime()-(1000 * 60 * 60 ));
        Timestamp after=new Timestamp(dt.getTime()+(1000 * 60 * 60 ));
        ArrayList<Meeting> meetings=new ArrayList<Meeting>();
        String query = "SELECT met.datetime, met.idmeeting, pnm.idparticipant, pnm.idmeeting " +
                "from meeting met " +
                "RIGHT OUTER JOIN participantsandmeeting pnm " +
                "ON pnm.idmeeting=met.idmeeting " +
                "WHERE pnm.idparticipant="+getIdByEmail(email)+" AND met.datetime BETWEEN '"+before+"' AND '"+after+"';";
        try {
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                res="May not be able to come";
            }
            rs.close();}

        catch (Exception e) {
            System.out.print("error place");
            System.out.print(e.toString());
        }

        return res;
    }

    public void addParticipant(int idmeeting, String email){

        String query = "INSERT INTO participantsandmeeting (idmeeting,idparticipant)" +
                "VALUES('" +idmeeting+"','"+getIdByEmail(email)+"');";
        try {
            stmt.executeUpdate(query);
        }
        catch (Exception e) {
            System.out.print(" error ");
            System.out.print(e.toString());
        }
    }

    public void delParticipant(int idmeeting, String email){

        String query = "DELETE FROM participantsandmeeting " +
                "WHERE(idmeeting='" +idmeeting+"' AND idparticipant='"+getIdByEmail(email)+"');";
        try {
            stmt.executeUpdate(query);
        }
        catch (Exception e) {
            System.out.print(" error ");
            System.out.print(e.toString());
        }
    }

    public void delMeeting(String email, int idmeeting){
        int myid=getIdByEmail(email);
        String query ="SELECT idorganizer FROM meeting WHERE (`idmeeting` = '"+idmeeting+"');";
        int id=-1;
        try {
            rs = stmt.executeQuery(query);
            if(rs.next()){
                id = rs.getInt(1);
                rs.close();
            }
        }
        catch (Exception e){
            System.out.print(e+"\n");
        }

        if(id==myid){
            query = "DELETE FROM meeting " +
                            "WHERE(idmeeting='" +idmeeting+"');";
        }
        else{
            query="DELETE FROM participantsandmeeting "+
                    "WHERE(idmeeting='" +idmeeting+"' AND idparticipant='"+myid+"');";
        }

        try {
            stmt.executeUpdate(query);
        }
        catch (Exception e) {
            System.out.print(" error ");
            System.out.print(e.toString());
        }

    }

    private ArrayList<Meeting> myMeetingSQL(int id){
        ArrayList<Meeting> meetings=new ArrayList<Meeting>();
        String query = "SELECT * from meeting " +
                "WHERE idorganizer="+id+";";

        try {

            rs = stmt.executeQuery(query);

            while (rs.next()) {

                int i=rs.getInt(1);
                Timestamp timestamp = rs.getTimestamp(2);
                Date dt = new Date(timestamp.getTime());
                String pl=rs.getString(3);
                String idn="YOU";
                meetings.add(new Meeting(i,dt,pl,idn));
            }
                rs.close();}

        catch (Exception e) {
                System.out.print("error place");
                System.out.print(e.toString());
            }

        return meetings;
    }

    public ArrayList<Meeting> myMeetings(String email){
        ArrayList<Meeting> meetings=myMeetingSQL(getIdByEmail(email));

        String query = "";

        try {
            for (Meeting m : meetings){
                query = "SELECT participant.name, participant.email, participantsandmeeting.idmeeting " +
                        "FROM participant " +
                        "RIGHT OUTER JOIN participantsandmeeting " +
                        "ON participant.idparticipant=participantsandmeeting.idparticipant "+
                        "WHERE idmeeting="+m.getId()+";";

                rs = stmt.executeQuery(query);

                while (rs.next()) {
                    m.addParticipant(new Participant(rs.getString(1),rs.getString(2)));
                }
                rs.close();
            }


        }
             catch (Exception e) {
                System.out.print("error place");
                System.out.print(e.toString());
            }

        return meetings;
    }

    public ArrayList<Meeting> timetable(String email){
        int myid=getIdByEmail(email);
        ArrayList<Meeting> meetings=myMeetingSQL(myid);

        String query = "SELECT " +
                "par.idparticipant, par.name, " +
                "met.idmeeting, met.datetime, met.place, met.idorganizer, " +
                "pnm.idmeeting, pnm.idparticipant " +
                "FROM participantsandmeeting pnm " +
                "LEFT OUTER JOIN meeting met " +
                "ON pnm.idmeeting=met.idmeeting " +
                "LEFT OUTER JOIN participant par " +
                "ON met.idorganizer=par.idparticipant "+
                "WHERE pnm.idparticipant="+myid+" "+
                "ORDER by met.datetime;";



        try {
            rs = stmt.executeQuery(query);

            while (rs.next()) {

                int i = rs.getInt(3);
                Timestamp timestamp = rs.getTimestamp(4);
                Date dt = new Date(timestamp.getTime());
                String pl = rs.getString(5);
                String idn=rs.getString(2);
                meetings.add(new Meeting(i, dt, pl, idn));
            }
        }
        catch (Exception e) {
                System.out.print("error place");
                System.out.print(e.toString());
            }
        Collections.sort(meetings);
            return meetings;
    }

    public Boolean findMeeting(String email, java.sql.Date dt, String place){
        String query ="SELECT * FROM Meeting WHERE (`idorganizer` = '"+getIdByEmail(email)+"' AND `datetime` = '"+dt.toString()+"' AND `place` = '"+place+"');";
        Boolean res=FALSE;
        try {
            rs = stmt.executeQuery(query);
            if(rs.next()) {
                res=TRUE;
            }
                rs.close();
        }
        catch (Exception e){
            System.out.print(e+"\n");
        }
        return res;
    }
    public Boolean findParticipant(int idMeeting, String email){
        String query ="SELECT * FROM participantsandmeeting WHERE (`idparticipant` = '"+getIdByEmail(email)+"' AND `idmeeting` = '"+idMeeting+"');";
        Boolean res=FALSE;
        try {
            rs = stmt.executeQuery(query);
            if(rs.next()) {
                res=TRUE;
            }
            rs.close();
        }
        catch (Exception e){
            System.out.print(e+"\n");
        }
        return res;
    }

    public void exit(){
        try { con.close(); } catch(SQLException se) { /*can't do anything */ }
        try { stmt.close();  System.out.print("exit ");} catch(SQLException se) { /*can't do anything */ }
    }

}
