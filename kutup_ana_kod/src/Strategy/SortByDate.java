package Strategy;

import java.util.List;

public class SortByDate extends BookSorter {
    @Override
    public List<String> sort(List<String> books) {
        books = cleanBookList(books);
        books.sort((b1, b2) -> {
            try {

                String[] details1 = b1.split(",");
                String[] details2 = b2.split(",");


                int year1 = Integer.parseInt(details1[2].replaceAll("[^0-9]", "").trim());
                int year2 = Integer.parseInt(details2[2].replaceAll("[^0-9]", "").trim());

                return Integer.compare(year1, year2);
            } catch (Exception e) {
                System.err.println("Hata: Yayın tarihi ayıklanırken sorun oluştu.");
                return 0;
            }
        });
        return books;
    }
}

