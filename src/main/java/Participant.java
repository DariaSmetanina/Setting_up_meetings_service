public class Participant {
    String name;
    String email;

    public Participant(String n, String e){
        name=n;
        email=e;
    }

    public String getNameAndEmail(){
        return name+" ("+email+"); ";
    }
}
