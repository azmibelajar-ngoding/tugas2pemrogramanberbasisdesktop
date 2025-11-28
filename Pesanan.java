package tugas2;

public class Pesanan {
    private final Menu item;
    private int jumlah;

    public Pesanan(Menu item, int jumlah) {
        this.item = item;
        this.jumlah = jumlah;
    }

    public double hitungSubtotal() {
        return item.getHarga() * jumlah;
    }

    public Menu getItem() {
        return item;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}