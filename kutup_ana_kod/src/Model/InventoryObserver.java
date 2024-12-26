package Model;

public class InventoryObserver {
    private String name;

    public InventoryObserver(String name) {
        this.name = name;
    }

    public void update(String message) {
        System.out.println(name + " bildirimi aldı: " + message);
    }
}

