package Model;

public class UserFactory {
    public static User createUser(String userType) {
        if (userType == null) {
            throw new IllegalArgumentException("Kullanıcı türü null olamaz!");
        }

        if (userType.equalsIgnoreCase("Personnel")) {
            return new PersonnelUser();
        } else if (userType.equalsIgnoreCase("Student")) {
            return new StudentUser();
        } else {
            throw new IllegalArgumentException("Geçersiz kullanıcı türü: " + userType);
        }
    }}
