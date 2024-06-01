import java.util.Scanner;
import java.util.Map;

public class AksiUser extends Aksi {
    @Override
    public void tampilanAksi() {
        System.out.println("Aksi User:");
        System.out.println("1. Pesan Film");
        System.out.println("2. Lihat Saldo");
        System.out.println("3. Lihat List Film");
        System.out.println("4. Lihat Pesanan");
        System.out.println("5. Logout");
        System.out.println("6. Tutup Aplikasi");
    }

    @Override
    public void keluar() {
        Akun.logout();
        System.out.println("Anda telah logout.");
    }

    @Override
    public void tutupAplikasi() {
        System.out.println("Aplikasi ditutup.");
        System.exit(0);
    }

    @Override
    public void lihatListFilm() {
        for (Film film : Film.getFilms().values()) {
            System.out.println("Film: " + film.getName() +
                    " - Deskripsi: " + film.getDescription() +
                    " - Harga: " + film.getPrice() +
                    " - Stok: " + film.getStock());
        }
    }

    public void lihatSaldo() {
        User currentUser = Akun.getCurrentUser();
        if (currentUser != null) {
            System.out.println("Saldo anda: " + currentUser.getSaldo());
        } else {
            System.out.println("Tidak ada user yang sedang login.");
        }
    }

    public void pesanFilm() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nama Film yang ingin dipesan: ");
        String namaFilm = scanner.nextLine();
        Film film = Film.getFilms().get(namaFilm);

        if (film == null) {
            System.out.println("Film yang dicari tidak ditemukan.");
            return;
        }

        System.out.print("Jumlah tiket yang ingin dipesan: ");
        int jumlahTiket = scanner.nextInt();
        if (jumlahTiket > film.getStock()) {
            System.out.println("Stok tiket tidak mencukupi.");
            return;
        }

        double totalHarga = jumlahTiket * film.getPrice();
        User currentUser = Akun.getCurrentUser();
        if (currentUser.getSaldo() < totalHarga) {
            System.out.println("Harga satuan tiket: " + film.getPrice());
            System.out.println("Total harga: " + totalHarga);
            System.out.println("Saldo tidak mencukupi, saldo yang dimiliki " + currentUser.getSaldo() + ".");
            return;
        }

        System.out.println("Harga satuan tiket: " + film.getPrice());
        System.out.println("Total harga: " + totalHarga);
        film.setStock(film.getStock() - jumlahTiket);
        currentUser.setSaldo(currentUser.getSaldo() - totalHarga);
        currentUser.addPesanan(film, jumlahTiket);
        System.out.println("Tiket berhasil dipesan.");
    }

    public void lihatPesanan() {
        User currentUser = Akun.getCurrentUser();
        if (currentUser != null) {
            Map<String, Pesanan> pesananMap = currentUser.getPesanan();
            if (pesananMap.isEmpty()) {
                System.out.println("Kamu belum pernah melakukan pemesanan.");
            } else {
                for (Pesanan pesanan : pesananMap.values()) {
                    System.out.println("Film: " + pesanan.getFilm().getName() +
                                       " - Jumlah: " + pesanan.getKuantitas() +
                                       " - Total Harga: " + (pesanan.getKuantitas() * pesanan.getFilm().getPrice()));
                }
            }
        } else {
            System.out.println("Tidak ada user yang sedang login.");
        }
    }
}
