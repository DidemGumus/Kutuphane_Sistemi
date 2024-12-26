package Strategy;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BookSorter {
    public abstract List<String> sort(List<String> books);


    protected List<String> cleanBookList(List<String> books) {
        return books.stream()
                .filter(book -> book != null && !book.isEmpty())
                .collect(Collectors.toList());
    }
}
