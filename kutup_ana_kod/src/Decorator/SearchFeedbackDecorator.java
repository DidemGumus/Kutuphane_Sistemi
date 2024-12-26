package Decorator;

import javax.swing.*;
import java.util.List;

public class SearchFeedbackDecorator implements SearchResult {
    private final SearchResult decoratedResult;

    public SearchFeedbackDecorator(SearchResult decoratedResult) {
        this.decoratedResult = decoratedResult;
    }

    @Override //mevcur davranış korunur (SearchResult un işlevselliğini korur)
    public List<String> getResults() {
        List<String> results = decoratedResult.getResults();

        // Geri bildirim mesajları eklendi.
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Arama sonucunda hiçbir kitap bulunamadı.");
        } else {
            JOptionPane.showMessageDialog(null, "Toplam " + results.size() + " kitap bulundu.");
        }
        return results;
    }
}
