package View;

import Controller.UserController;
import Controller.TransactionController;
import Controller.BookController;
import Model.InventorySubject;
import Model.InventoryObserver;
import Model.User;
import Model.PersonnelUser;
import Model.StudentUser;
import Model.UserFactory;
import Model.Book;
import Model.AvailableState;
import Model.BorrowedState;
import Model.LostState;
import Decorator.BasicSearchResult;
import Decorator.SearchFeedbackDecorator;
import Decorator.SearchResult;
import Strategy.BookSorter;
import Strategy.SortByName;
import Strategy.SortByDate;



import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int currentUserId = 1;

        UserController userController = new UserController();
        TransactionController transactionController = new TransactionController();

        InventorySubject inventorySubject = new InventorySubject();
        InventoryObserver observer1 = new InventoryObserver("Kütüphane Kullanıcısı 1");
        InventoryObserver observer2 = new InventoryObserver("Kütüphane Kullanıcısı 2");
        inventorySubject.addObserver(observer1);
        inventorySubject.addObserver(observer2);

        BookController bookController = new BookController(inventorySubject);


        JFrame frame = new JFrame("Kütüphane Yönetim Sistemi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        CardLayout cardLayout = new CardLayout(); // CardLayout oluşturuluyor
        frame.setLayout(cardLayout);


        JPanel loginPanel = new JPanel(new GridLayout(4, 2));
        JLabel usernameLabel = new JLabel("Kullanıcı Adı:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Şifre:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Giriş Yap");
        JButton goToRegisterButton = new JButton("Kayıt Ol");
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(goToRegisterButton);


        JPanel registerPanel = new JPanel(new GridLayout(7, 2));
        JLabel regUsernameLabel = new JLabel("Kullanıcı Adı:");
        JTextField regUsernameField = new JTextField();
        JLabel regPasswordLabel = new JLabel("Şifre:");
        JPasswordField regPasswordField = new JPasswordField();
        JLabel emailLabel = new JLabel("E-posta:");
        JTextField emailField = new JTextField();
        JLabel userTypeLabel = new JLabel("Kullanıcı Türü Seçin:");


        JRadioButton personnelButton = new JRadioButton("Personnel");
        JRadioButton studentButton = new JRadioButton("Student");
        ButtonGroup userTypeGroup = new ButtonGroup();
        userTypeGroup.add(personnelButton);
        userTypeGroup.add(studentButton);

        JPanel userTypePanel = new JPanel(new FlowLayout());
        userTypePanel.add(personnelButton);
        userTypePanel.add(studentButton);

        JButton registerButton = new JButton("Kaydol");
        JButton goToLoginButton = new JButton("Giriş Yap");

        registerPanel.add(regUsernameLabel);
        registerPanel.add(regUsernameField);
        registerPanel.add(regPasswordLabel);
        registerPanel.add(regPasswordField);
        registerPanel.add(emailLabel);
        registerPanel.add(emailField);
        registerPanel.add(userTypeLabel);
        registerPanel.add(userTypePanel);
        registerPanel.add(registerButton);
        registerPanel.add(goToLoginButton);

        // Student Paneli(ana menü)
        JPanel studentPanel = new JPanel();
        studentPanel.setLayout(new GridLayout(7, 1, 10, 10)); // 7 satır, 1 sütun, butonlar arasında boşluk ekler
        studentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Panelin kenar boşlukları

        JButton borrowBookButton = new JButton("Kitap Ödünç Al");
        JButton returnBookButton = new JButton("Kitap İade Et");
        JButton searchBookButton = new JButton("Kitap Ara");
        JButton showPopularBooksStudentButton = new JButton("Popüler Kitapları Göster");
        JButton reportLostBookButton = new JButton("Kayıp Kitap Bildir");
        JButton sortBooksButton = new JButton("Kitapları Sırala");
        JButton logoutStudentButton = new JButton("Çıkış Yap");

// Butonları ekle
        studentPanel.add(borrowBookButton);
        studentPanel.add(returnBookButton);
        studentPanel.add(searchBookButton);
        studentPanel.add(showPopularBooksStudentButton);
        studentPanel.add(reportLostBookButton);
        studentPanel.add(sortBooksButton);
        studentPanel.add(logoutStudentButton);


// Kayıp Kitap Bildir ActionListener
        reportLostBookButton.addActionListener(e -> {
            String bookName = JOptionPane.showInputDialog(frame, "Kayıp olarak bildirmek istediğiniz kitabın adını girin:");
            if (bookName != null && !bookName.trim().isEmpty()) {
                // BookController ile veritabanında kitabı arayıp kayıp olarak işaretle
                int bookId = bookController.getBookIdByName(bookName);
                if (bookId != -1) {
                    bookController.updateBookState(bookId, "Lost");
                    JOptionPane.showMessageDialog(frame, "Kitap kayıp olarak bildirildi: " + bookName);
                } else {
                    JOptionPane.showMessageDialog(frame, "Girilen isimle eşleşen bir kitap bulunamadı.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Lütfen geçerli bir kitap adı girin.");
            }
        });

        borrowBookButton.addActionListener(e -> {
            // Kullanıcı için ödünç alınabilir kitapları al
            List<String> availableBooks = bookController.getAvailableBooksForStudent(currentUserId);
            if (availableBooks.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Ödünç alınabilecek kitap bulunmamaktadır.");
                return;
            }

            // Kitap listesini göster
            JList<String> bookList = new JList<>(availableBooks.toArray(new String[0]));
            JScrollPane scrollPane = new JScrollPane(bookList);
            int option = JOptionPane.showConfirmDialog(frame, scrollPane, "Ödünç Alınacak Kitabı Seçin", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String selectedBook = bookList.getSelectedValue();
                if (selectedBook != null) {
                    try {
                        // Kitap detaylarını ayrıştır
                        String[] bookDetails = selectedBook.split(" - ");
                        int bookId = Integer.parseInt(bookDetails[0].trim());
                        String bookName = bookDetails[1].trim();

                        // Kitap nesnesi oluştur ve durumu güncelle
                        Book book = new Book(bookName);
                        book.borrow(); // State deseni: Borrow işlemi

                        // Veritabanı durumu 'Borrowed' olarak güncelle
                        bookController.updateBookState(bookId, "Borrowed");

                        // Transactions tablosuna kayıt ekle
                        transactionController.recordTransaction(bookId, currentUserId, "Borrowed");

                        JOptionPane.showMessageDialog(frame, "Kitap başarıyla ödünç alındı: " + bookName);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Hata oluştu: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Lütfen bir kitap seçin.");
                }
            }
        });

        returnBookButton.addActionListener(e -> {
            // Kullanıcının ödünç aldığı kitapları al
            List<String> borrowedBooks = transactionController.listBorrowedBooks(currentUserId);
            if (borrowedBooks.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "İade edilecek kitap bulunmamaktadır.");
                return;
            }

            // Kitap listesini göster
            JList<String> bookList = new JList<>(borrowedBooks.toArray(new String[0]));
            JScrollPane scrollPane = new JScrollPane(bookList);
            int option = JOptionPane.showConfirmDialog(frame, scrollPane, "İade Edilecek Kitabı Seçin", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String selectedBook = bookList.getSelectedValue();
                if (selectedBook != null) {
                    try {
                        // Kitap detaylarını ayrıştır
                        String[] bookDetails = selectedBook.split(" - ");
                        int bookId = Integer.parseInt(bookDetails[0].trim());
                        String bookName = bookDetails[1].trim();

                        // Kitap nesnesi oluştur ve iade işlemini yap
                        Book book = new Book(bookName);
                        book.returnBook(); // State deseni: Return işlemi

                        // Kitap durumu veritabanında "Available" olarak güncellenir
                        bookController.updateBookState(bookId, "Available");

                        // Transactions tablosunda status güncellenir
                        transactionController.updateTransactionStatus(bookId, currentUserId, "Returned");

                        JOptionPane.showMessageDialog(frame, "Kitap başarıyla iade edildi: " + bookName);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Hata oluştu: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Lütfen bir kitap seçin.");
                }
            }
        });

// Popüler kitapları gösterme
        showPopularBooksStudentButton.addActionListener(e -> {
            List<String> popularBooks = bookController.getPopularBooks();
            if (popularBooks.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Hiç popüler kitap bulunamadı.");
            } else {
                StringBuilder message = new StringBuilder("Popüler Kitaplar:\n");
                for (String bookInfo : popularBooks) {
                    message.append(bookInfo).append("\n");
                }
                JOptionPane.showMessageDialog(frame, message.toString());
            }
        });

        //kitapları sıralama
        sortBooksButton.addActionListener(e -> {
            List<String> allBooks = bookController.getAllBooksForPersonnel(); // Kitapları veritabanından çek
            if (allBooks.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Sıralanacak kitap bulunmamaktadır.");
                return;
            }

            String[] options = {"İsme Göre", "Yayın Tarihine Göre"};
            int choice = JOptionPane.showOptionDialog(frame, "Kitapları nasıl sıralamak istersiniz?",
                    "Kitap Sıralama Seçenekleri", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            BookSorter sorter = null;
            if (choice == 0) {
                sorter = new Strategy.SortByName();
            } else if (choice == 1) {
                sorter = new Strategy.SortByDate();
            }

            if (sorter != null) {
                List<String> sortedBooks = bookController.getSortedBooks(allBooks, sorter);
                StringBuilder message = new StringBuilder("Sıralanmış Kitaplar:\n");
                for (String book : sortedBooks) {
                    message.append(book).append("\n");
                }
                JOptionPane.showMessageDialog(frame, message.toString());
            }
        });



// Çıkış yapma
        logoutStudentButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Çıkış yapıldı.");
            cardLayout.show(frame.getContentPane(), "loginPanel");
        });



        // Personnel Paneli
        JPanel personnelPanel = new JPanel(new GridLayout(5, 1)); // Satır sayısını 4'ten 5'e çıkardık
        JButton addBookButton = new JButton("Kitap Ekle");
        JButton deleteBookButton = new JButton("Kitap Sil");
        JButton updateBookButton = new JButton("Kitap Güncelle");
        JButton showPopularBooksButton = new JButton("Popüler Kitapları Göster"); // Yeni buton
        JButton logoutPersonnelButton = new JButton("Çıkış Yap");
        personnelPanel.add(addBookButton);
        personnelPanel.add(deleteBookButton);
        personnelPanel.add(updateBookButton);
        personnelPanel.add(showPopularBooksButton); // Yeni buton ekleniyor
        personnelPanel.add(logoutPersonnelButton);

        showPopularBooksButton.addActionListener(e -> {
            List<String> popularBooks = bookController.getPopularBooks();
            if (popularBooks.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Hiç popüler kitap bulunamadı.");
            } else {
                StringBuilder message = new StringBuilder("Popüler Kitaplar:\n");
                for (String book : popularBooks) {
                    message.append(book).append("\n");
                }
                JOptionPane.showMessageDialog(frame, message.toString());
            }
        });

        // CardLayout için kartlar ekleniyor
        frame.add(loginPanel, "loginPanel");
        frame.add(registerPanel, "registerPanel");
        frame.add(studentPanel, "studentPanel");
        frame.add(personnelPanel, "personnelPanel");

        // Login işlemlerinde kullanıcı türü dinamik olarak atanır
        // Login işlemleri
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            int userId = userController.login(username, password);
            if (userId != -1) {
                String userType = userController.getUserType(username);
                User user = UserFactory.createUser(userType); // Factory kullanımı
                user.getPrivileges(); // Kullanıcının yetkilerini göster

                if (userType.equals("Personnel")) {
                    JOptionPane.showMessageDialog(frame, "Giriş başarılı! (Personnel)");
                    cardLayout.show(frame.getContentPane(), "personnelPanel");
                } else if (userType.equals("Student")) {
                    JOptionPane.showMessageDialog(frame, "Giriş başarılı! (Student)");
                    cardLayout.show(frame.getContentPane(), "studentPanel");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Kullanıcı adı veya şifre yanlış.");
            }
        });




        goToRegisterButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "registerPanel"));

        // Register işlemleri
        registerButton.addActionListener(e -> {
            String username = regUsernameField.getText();
            String password = new String(regPasswordField.getPassword());
            String email = emailField.getText();
            String userType = null;

            if (personnelButton.isSelected()) {
                userType = "Personnel";
            } else if (studentButton.isSelected()) {
                userType = "Student";
            }

            if (userType == null) {
                JOptionPane.showMessageDialog(frame, "Lütfen bir kullanıcı türü seçin.");
                return;
            }

            boolean registrationSuccessful = userController.register(username, password, email, userType);
            if (registrationSuccessful) {
                JOptionPane.showMessageDialog(frame, "Kayıt başarılı! Giriş yapabilirsiniz.");
                cardLayout.show(frame.getContentPane(), "loginPanel");
            } else {
                JOptionPane.showMessageDialog(frame, "Kayıt başarısız. Bilgileri kontrol edin.");
            }
        });

        goToLoginButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "loginPanel"));



        searchBookButton.addActionListener(e -> {
            String keyword = JOptionPane.showInputDialog(frame, "Aramak istediğiniz anahtar kelimeyi girin:");
            String author = JOptionPane.showInputDialog(frame, "Yazar filtresi (boş bırakabilirsiniz):");
            String genre = JOptionPane.showInputDialog(frame, "Tür filtresi (boş bırakabilirsiniz):");

            if ((keyword == null || keyword.isEmpty()) && (author == null || author.isEmpty()) && (genre == null || genre.isEmpty())) {
                JOptionPane.showMessageDialog(frame, "Lütfen en az bir arama kriteri girin.");
                return;
            }

            // Arama sonuçlarını Controller'dan çek
            List<String> rawResults = bookController.searchBooksWithFilters(
                    keyword == null || keyword.isEmpty() ? null : keyword,
                    author == null || author.isEmpty() ? null : author,
                    genre == null || genre.isEmpty() ? null : genre
            );

            // Decorator kullanarak geri bildirim ekle
            SearchResult searchResult = new BasicSearchResult(rawResults);
            SearchResult decoratedResult = new SearchFeedbackDecorator(searchResult);
            List<String> finalResults = decoratedResult.getResults();

            // Sonuçları göster
            if (!finalResults.isEmpty()) {
                JList<String> bookList = new JList<>(finalResults.toArray(new String[0]));
                JScrollPane scrollPane = new JScrollPane(bookList);
                int option = JOptionPane.showConfirmDialog(frame, scrollPane, "Arama Sonuçları", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String selectedBook = bookList.getSelectedValue();
                    if (selectedBook != null) {
                        JOptionPane.showMessageDialog(frame, "Seçilen kitap: " + selectedBook);
                    }
                }
            }
        });

        logoutStudentButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Çıkış yapıldı.");
            cardLayout.show(frame.getContentPane(), "loginPanel");
        });


        // Personnel Panel İşlemleri
        addBookButton.addActionListener(e -> {
            String bookName = JOptionPane.showInputDialog(frame, "Kitap Adı:");
            String author = JOptionPane.showInputDialog(frame, "Yazar:");
            String genre = JOptionPane.showInputDialog(frame, "Tür:");
            int publicationYear = Integer.parseInt(JOptionPane.showInputDialog(frame, "Yayın Yılı:"));
            String status = JOptionPane.showInputDialog(frame, "Durum:");
            String location = JOptionPane.showInputDialog(frame, "Konum:");
            bookController.addBook(bookName, author, genre, publicationYear, status, location);
        });

        deleteBookButton.addActionListener(e -> {
            List<String> availableBooks = bookController.getAllBooksForPersonnel();
            if (availableBooks.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Şu anda mevcut kitap bulunmamaktadır.");
                return;
            }

            JList<String> bookList = new JList<>(availableBooks.toArray(new String[0]));
            JScrollPane scrollPane = new JScrollPane(bookList);
            int option = JOptionPane.showConfirmDialog(frame, scrollPane, "Silinecek Kitabı Seçin", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String selectedBook = bookList.getSelectedValue();
                if (selectedBook != null) {
                    String[] bookDetails = selectedBook.split(" - "); // Kitap ID'yi almak için ayrıştırma
                    int bookId = Integer.parseInt(bookDetails[0].trim());
                    bookController.deleteBook(bookId);
                    JOptionPane.showMessageDialog(frame, "Seçilen kitap başarıyla silindi.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Lütfen bir kitap seçin.");
                }
            }
        });


        updateBookButton.addActionListener(e -> {
            List<String> availableBooks = bookController.getAllBooksForPersonnel();
            if (availableBooks.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Şu anda mevcut kitap bulunmamaktadır.");
                return;
            }

            JList<String> bookList = new JList<>(availableBooks.toArray(new String[0]));
            JScrollPane scrollPane = new JScrollPane(bookList);
            int option = JOptionPane.showConfirmDialog(frame, scrollPane, "Güncellenecek Kitabı Seçin", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String selectedBook = bookList.getSelectedValue();
                if (selectedBook != null) {
                    String[] bookDetails = selectedBook.split(" - ");
                    int bookId = Integer.parseInt(bookDetails[0].trim());
                    String bookName = JOptionPane.showInputDialog(frame, "Yeni Kitap Adı:", bookDetails[1].trim());
                    String author = JOptionPane.showInputDialog(frame, "Yeni Yazar Adı:");
                    String genre = JOptionPane.showInputDialog(frame, "Yeni Tür:");
                    int publicationYear = Integer.parseInt(JOptionPane.showInputDialog(frame, "Yeni Yayın Yılı:"));
                    String status = JOptionPane.showInputDialog(frame, "Yeni Durum:");
                    String location = JOptionPane.showInputDialog(frame, "Yeni Konum:");

                    bookController.updateBook(bookId, bookName, author, genre, publicationYear, status, location);
                    JOptionPane.showMessageDialog(frame, "Kitap başarıyla güncellendi.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Lütfen bir kitap seçin.");
                }
            }
        });

        logoutPersonnelButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Çıkış yapıldı.");
            cardLayout.show(frame.getContentPane(), "loginPanel");
        });

        // Frame görünür yapılıyor
        frame.setVisible(true);
    }
}
