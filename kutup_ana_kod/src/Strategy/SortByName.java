package Strategy;

import java.util.List;

public class SortByName extends BookSorter {
    @Override
    public List<String> sort(List<String> books) {
        books = cleanBookList(books);
        books.sort((b1, b2) -> {
            String name1 = b1.split(",")[0].trim();
            String name2 = b2.split(",")[0].trim();
            return name1.compareToIgnoreCase(name2);
        });
        return books;
    }
}
