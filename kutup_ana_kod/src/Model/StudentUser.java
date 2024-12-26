package Model;

public class StudentUser extends User {
    @Override
    public void getPrivileges() {
        System.out.println("Student yetkileri: Kitap ödünç alabilir ve arama yapabilir.");
    }
}
