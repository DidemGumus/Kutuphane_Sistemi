package Model;

import java.util.ArrayList;
import java.util.List;

public class InventorySubject {
    private List<InventoryObserver> observers = new ArrayList<>();

    public void addObserver(InventoryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (InventoryObserver observer : observers) {
            observer.update(message);
        }
    }
}

