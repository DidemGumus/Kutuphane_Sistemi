package Model;

public class PersonnelUser extends User {
    @Override
    public void getPrivileges() {
        System.out.println("Personnel yetkileri: Kitap ekleyebilir, silebilir, güncelleyebilir.");
    }
}
