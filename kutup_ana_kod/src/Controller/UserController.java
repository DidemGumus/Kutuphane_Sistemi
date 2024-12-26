package Controller;

import Model.User;
import Model.UserFactory;
import Model.UserModel;

public class UserController {
    private UserModel userModel;
    private int currentUserId; // Dinamik kullanıcı ID'sini tutmak için bir değişken atadık
    private User currentUser; // Kullanıcı türünü dinamik tuttuk

    public UserController() {
        this.userModel = new UserModel();
        this.currentUserId = -1; // Başlangıçta geçersiz bir kullanıcı ID'si atanır
        this.currentUser = null; // Başlangıçta kullanıcı atanmaz
    }

    // Kullanıcı giriş işlemi burada yapılır
    public int login(String username, String password) {
        int userId = userModel.verifyUser(username, password);
        if (userId == -1) {
            System.err.println("Hata: Kullanıcı doğrulama başarısız. Geçersiz kullanıcı adı veya şifre.");
        } else {
            this.currentUserId = userId; // Başarılı giriş durumunda currentUserId güncellenir
            String userType = userModel.getUserType(username); // Kullanıcı türü alınır
            assignUserRole(userType); // Kullanıcı türü dinamik olarak atanır
            System.out.println("Kullanıcı başarıyla giriş yaptı. Kullanıcı ID: " + userId + ", Tür: " + userType);
        }
        return userId;
    }

    // Yeni kullanıcı kaydı için geçerli geçersiz kullanıcıya bakılır
    public boolean register(String username, String password, String email, String userType) {
        if (!isValidUserType(userType)) {
            System.err.println("Hata: Geçersiz kullanıcı türü. Kayıt işlemi iptal edildi.");
            return false;
        }
        boolean isRegistered = userModel.addUser(username, password, userType, email);
        if (!isRegistered) {
            System.err.println("Hata: Kullanıcı kaydı başarısız.");
        } else {
            System.out.println("Kullanıcı başarıyla kaydedildi: " + username + ", Tür: " + userType);
        }
        return isRegistered;
    }


    private boolean isValidUserType(String userType) {
        // Artık sadece "Personnel" ve "Student" türlerini kabul ediyor
        return userType.equals("Personnel") || userType.equals("Student");
    }
    // Kullanıcı türünü almak için metot
    public String getUserType(String username) {
        return userModel.getUserType(username); // UserModel'den kullanıcı türü alınır
    }
    private void assignUserRole(String userType) {
        try {
            this.currentUser = UserFactory.createUser(userType); // Factory deseni ile kullanıcı atanır
            currentUser.getPrivileges(); // Kullanıcının yetkileri gösterilir
        } catch (IllegalArgumentException e) {
            System.err.println("Hata: " + e.getMessage());
        }
    }

    public int getCurrentUserId() {
        return this.currentUserId; // Dinamik kullanıcı ID'si döndürülür
    }

    // Kullanıcı oturumunu kapatır
    public void logout() {
        this.currentUserId = -1; // Oturum kapatıldığında kullanıcı ID'si sıfırlanır
        this.currentUser = null; // Kullanıcı nesnesi sıfırlanır
        System.out.println("Kullanıcı oturumu kapatıldı.");
    }
}
