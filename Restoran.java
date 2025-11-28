package tugas2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Restoran {
    private static final List<Menu> daftarMenu = new ArrayList<>();
    private static final List<Pesanan> pesananPelanggan = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inisialisasiMenu();

        boolean exit = false;
        while (!exit) {
            tampilkanMenuUtama();
            System.out.print("Pilih opsi (1-3): ");
            String pilihan = scanner.nextLine();
            switch (pilihan) {
                case "1" -> inputPesanan();
                case "2" -> hitungDanCetakStruk();
                case "3" -> {
                    System.out.println("Terima kasih telah menggunakan layanan Restoran.");
                    exit = true;
                }
                default -> System.out.println("Opsi tidak valid. Silakan coba lagi.");
            }
            System.out.println("\n--- Tekan ENTER untuk melanjutkan ---");
            scanner.nextLine();
        }
    }


    private static void inisialisasiMenu() {
        daftarMenu.add(new Menu("Nasi Goreng Spesial", 25000, "Makanan"));
        daftarMenu.add(new Menu("Sate Ayam", 30000, "Makanan"));
        daftarMenu.add(new Menu("Es Teh Manis", 5000, "Minuman"));
        daftarMenu.add(new Menu("Jus Alpukat", 15000, "Minuman"));
    }

    private static void tampilkanMenuUtama() {
        System.out.println("\n===== RESTORAN SEDERHANA =====");
        System.out.println("1. Input Pesanan");
        System.out.println("2. Proses Pembayaran dan Cetak Struk");
        System.out.println("3. Keluar");
        System.out.println("==============================");
    }
    
    
    private static void tampilkanMenuInput() {
        System.out.println("\n--- Menu Input Pesanan ---");
        System.out.println("1. Tambah Pesanan Baru");
        if (!pesananPelanggan.isEmpty()) {
            System.out.println("2. Lihat & Kelola Pesanan");
        }
        System.out.println("3. Selesai Input Pesanan");
        System.out.println("--------------------------");
    }

    private static void inputPesanan() {
        System.out.println("\n===== INPUT & KELOLA PESANAN =====");

        boolean selesai = false;
        while (!selesai) {
            tampilkanMenuInput();
            System.out.print("Pilih opsi (1-3): ");
            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1":
                    tambahPesananBaru();
                    break;
                case "2":
                    if (!pesananPelanggan.isEmpty()) {
                        kelolaPesanan();
                    } else {
                        System.out.println("❌ Belum ada pesanan yang bisa dikelola.");
                    }
                    break;
                case "3":
                    selesai = true;
                    break;
                default:
                    System.out.println("Opsi tidak valid.");
            }
        }
    }
    
    private static void tambahPesananBaru() {
        boolean tambahLagi = true;
        while (tambahLagi) {
            tampilkanDaftarMenu();
            
            System.out.print("\nMasukkan No. Menu yang dipesan (0 untuk batal): ");
            String inputMenu = scanner.nextLine();
            
            if (inputMenu.equals("0")) {
                tambahLagi = false;
                continue;
            }

            try {
                int noMenu = Integer.parseInt(inputMenu);
                if (noMenu > 0 && noMenu <= daftarMenu.size()) {
                    Menu menuPilihan = daftarMenu.get(noMenu - 1);
                    System.out.print("Masukkan jumlah pesanan untuk " + menuPilihan.getNama() + ": ");
                    int jumlah = Integer.parseInt(scanner.nextLine());

                    if (jumlah > 0) {
                        pesananPelanggan.add(new Pesanan(menuPilihan, jumlah));
                        System.out.println("✅ " + menuPilihan.getNama() + " (" + jumlah + ") ditambahkan.");
                    } else {
                        System.out.println("Jumlah harus lebih dari 0.");
                    }
                } else {
                    System.out.println("No. Menu tidak valid.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Masukkan angka.");
            }
            System.out.print("Tambah pesanan lagi? (y/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
                tambahLagi = false;
            }
        }
    }

    private static void tampilkanDaftarMenu() {
        System.out.println("\n--- Daftar Menu ---");
        for (int i = 0; i < daftarMenu.size(); i++) {
            System.out.printf("%d. ", (i + 1));
            daftarMenu.get(i).tampilDetail();
        }
    }

    
    private static void kelolaPesanan() {
        System.out.println("\n--- KELOLA PESANAN SAAT INI ---");
        tampilkanPesananSaatIni();
        if (pesananPelanggan.isEmpty()) return;

        System.out.print("\nMasukkan No. Urut pesanan yang ingin diubah/dihapus (0 untuk batal): ");
        try {
            int noUrut = Integer.parseInt(scanner.nextLine());

            if (noUrut == 0) return;

            if (noUrut > 0 && noUrut <= pesananPelanggan.size()) {
                Pesanan pesananDipilih = pesananPelanggan.get(noUrut - 1);
                
                System.out.println("Anda memilih: " + pesananDipilih.getItem().getNama() + " (x" + pesananDipilih.getJumlah() + ")");
                System.out.print("Pilih Aksi: [U]bah Jumlah / [H]apus Pesanan / [B]atal: ");
                String aksi = scanner.nextLine().trim().toUpperCase();

                switch (aksi) {
                    case "U" -> ubahJumlahPesanan(pesananDipilih);
                    case "H" -> hapusPesanan(noUrut, pesananDipilih);
                    default -> System.out.println("Aksi dibatalkan.");
                }
            } else {
                System.out.println("Nomor urut tidak valid.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid.");
        }
    }

    private static void tampilkanPesananSaatIni() {
        if (pesananPelanggan.isEmpty()) {
            System.out.println("Daftar pesanan kosong.");
            return;
        }
        System.out.println("\n--- Daftar Pesanan (Konfirmasi) ---");
        for (int i = 0; i < pesananPelanggan.size(); i++) {
            Pesanan p = pesananPelanggan.get(i);
            double subtotal = p.hitungSubtotal();
            System.out.printf("%d. %-20s (x%d) -> Rp %.0f%n", (i + 1), p.getItem().getNama(), p.getJumlah(), subtotal);
        }
        System.out.println("-----------------------------------");
    }

    private static void ubahJumlahPesanan(Pesanan pesananDipilih) {
        System.out.print("Masukkan Jumlah BARU untuk " + pesananDipilih.getItem().getNama() + ": ");
        try {
            int jumlahBaru = Integer.parseInt(scanner.nextLine());
            
            if (jumlahBaru > 0) {
                // 1. Tampilkan perubahan yang akan terjadi
                System.out.println("Perubahan: " + pesananDipilih.getJumlah() + " -> " + jumlahBaru);
                // 2. Minta konfirmasi SEBELUM perubahan diterapkan ke List
                System.out.print("Anda yakin ingin menyimpan perubahan ini? (Ya/Tidak): ");
                
                String konfirmasi = scanner.nextLine().trim();

                if (konfirmasi.equalsIgnoreCase("ya")) {
                    // 3. Hanya update JIKA konfirmasi "ya"
                    pesananDipilih.setJumlah(jumlahBaru); 
                    System.out.println("✅ Jumlah pesanan berhasil diubah.");
                } else {
                    // 4. Jika "tidak", tidak ada perubahan yang terjadi
                    System.out.println("Perubahan dibatalkan. Jumlah pesanan tetap " + pesananDipilih.getJumlah() + ".");
                }
            } else {
                System.out.println("Jumlah harus lebih dari 0. Pembatalan perubahan.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid. Pembatalan perubahan.");
        }
    }

    private static void hapusPesanan(int noUrut, Pesanan pesananDipilih) {
        System.out.print("Yakin ingin HAPUS " + pesananDipilih.getItem().getNama() + "? (Ya/Tidak): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("ya")) {
            pesananPelanggan.remove(noUrut - 1);
            System.out.println("🗑️ Pesanan berhasil dihapus.");
        } else {
            System.out.println("Pembatalan penghapusan.");
        }
    }


    private static void hitungDanCetakStruk() {
        if (pesananPelanggan.isEmpty()) {
            System.out.println("❌ Belum ada pesanan yang diinput.");
            return;
        }

        double totalItem = 0;
        for (Pesanan p : pesananPelanggan) {
            totalItem += p.hitungSubtotal();
        }

        double diskon = hitungDiskon(totalItem);
        double pajak = totalItem * 0.10;
        double totalSetelahPajak = totalItem + pajak;
        double biayaPelayanan = 20000;

        if (totalItem >= 100000) {
            biayaPelayanan = 0;
            System.out.println("\n🎉 Selamat! Total Item mencapai Rp 100.000, Biaya Pelayanan GRATIS!");
        }

        double totalBayar = totalSetelahPajak - diskon + biayaPelayanan;

        cetakStruk(totalItem, pajak, biayaPelayanan, diskon, totalBayar);
    }

    private static double hitungDiskon(double total) {
        double diskon = 0;
        if (total >= 50000 && total < 100000) {
            diskon = total * 0.05;
            System.out.println("Potongan: Diskon 5% diterapkan!");
        } else if (total >= 100000) {
            diskon = total * 0.10;
            System.out.println("Potongan: Diskon 10% diterapkan!");
        }
        return diskon;
    }

    private static void cetakStruk(double totalItem, double pajak, double biayaPelayanan, double diskon,
            double totalBayar) {
        System.out.println("\n=======================================");
        System.out.println("          STRUK PEMBAYARAN RESTORAN    ");
        System.out.println("=======================================");

        System.out.println("Item-item Pesanan:");
        for (Pesanan p : pesananPelanggan) {
            double subtotal = p.hitungSubtotal();
            System.out.printf("  - %-20s (x%d): Rp %.0f%n", p.getItem().getNama(), p.getJumlah(), subtotal);
        }

        System.out.println("---------------------------------------");
        System.out.printf("%-25s: Rp %.0f%n", "Total Harga Item", totalItem);
        System.out.printf("%-25s: Rp %.0f%n", "Diskon/Penawaran", diskon);
        System.out.printf("%-25s: Rp %.0f%n", "Pajak (10%)", pajak);
        System.out.printf("%-25s: Rp %.0f%n", "Biaya Pelayanan", biayaPelayanan);
        System.out.println("---------------------------------------");
        System.out.printf("**%-23s**: **Rp %.0f**%n", "TOTAL PEMBAYARAN", totalBayar);
        System.out.println("=======================================");
        System.out.println("Terima kasih atas kunjungan Anda!");
    }
}