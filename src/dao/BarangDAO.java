/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import model.Barang;
import model.ViewBarang;
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class BarangDAO extends Koneksi {
    
    /* Class untuk meng-handle proses CRUD di database yang berkaitan dengan tabel tb_barang */
    
    static BarangDAO instance;
    private PreparedStatement statement;
    List<ViewBarang> listBarang;
    ViewModel vm = new ViewModel();
    
    /* Function ini ekivalen dengan BarangDAO [nama_variabel] = new BarangDAO(); */
    public static BarangDAO getInstance(){
        if (instance == null) instance = new BarangDAO();
        return instance;
    }
    
    /* Mengambil data barang dengan parameter bebas (dispesifikkan melalui variabel where
    misalnya where = "ts.id = 2"
    Hasil data query akan menjadi nilai dari masing-masing atribut dari objek kelas Barang
    yang menajdi return value
    */
    public Barang getBarang(String where) throws Exception {
        Barang vb = new Barang();
        String sql = "SELECT tb.*, ts.nama AS supplier, kategori.keterangan AS kategori, satuan.keterangan AS satuan\n" +
        "FROM tb_barang AS tb\n" +
        "INNER JOIN tb_supplier AS ts ON tb.id_supplier = ts.id\n" +
        "INNER JOIN tb_general AS kategori ON tb.id_kategori = kategori.id\n" +
        "INNER JOIN tb_general AS satuan ON tb.id_satuan = satuan.id WHERE " + where;
        statement = koneksi.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            vb.setId(rs.getString("id"));
            vb.setKode(rs.getString("kode"));
            vb.setNama(rs.getString("nama"));
            vb.setId_kategori(rs.getString("kategori"));
            vb.setStok(rs.getString("stok"));
            vb.setId_satuan(rs.getString("satuan"));
            vb.setHarga_jual(format(rs.getInt("harga_jual")));
            vb.setHarga_beli(format(rs.getInt("harga_beli")));
            vb.setId_supplier(rs.getString("supplier"));
            vb.setStok_minimal(rs.getString("stok_minimal"));
            vb.setTgl_expired(rs.getString("tgl_expired"));
            vb.setLokasi(rs.getString("lokasi"));
            vb.setDiskon_nominal(rs.getString("diskon_nominal"));
            return vb;
        } else {
            return null;
        }

    }
    
    /* Mendapatkan semua list Barang dari tb_barang yang ada di database. Hasil query akan dimasukkan ke
    dalam sebuah arraylist bertipe data kelas Barang */
    public List<ViewBarang> getListBarang() throws Exception {
        listBarang = new ArrayList<>();
        String sql = "SELECT tb.*, ts.nama AS supplier, kategori.keterangan AS kategori, satuan.keterangan AS satuan\n" +
        "FROM tb_barang AS tb\n" +
        "INNER JOIN tb_supplier AS ts ON tb.id_supplier = ts.id\n" +
        "INNER JOIN tb_general AS kategori ON tb.id_kategori = kategori.id\n" +
        "INNER JOIN tb_general AS satuan ON tb.id_satuan = satuan.id";
        statement = koneksi.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        int i = 1;
        while (rs.next()){
            ViewBarang vb = new ViewBarang();
            vb.setId(Integer.toString(i));
            vb.setKode(rs.getString("kode"));
            vb.setNama(rs.getString("nama"));
            vb.setKategori(rs.getString("kategori"));
            vb.setJumlah_stok(rs.getString("stok"));
            vb.setSatuan(rs.getString("satuan"));
            vb.setHarga_jual(format(rs.getInt("harga_jual")));
            vb.setHarga_beli(format(rs.getInt("harga_beli")));
            vb.setSupplier(rs.getString("supplier"));
            listBarang.add(vb);
            i++;
        }
        return listBarang;
    }
    
    /* Sama dengan function getListBarang() tetapi terdapat parameter-parameter tertentu yang dispesifikkan
    di nilai variabel where */
    public List<ViewBarang> getListBarang(String where) throws Exception {
        listBarang = new ArrayList<>();
        String sql = "SELECT tb.*, ts.nama AS supplier, kategori.keterangan AS kategori, satuan.keterangan AS satuan\n" +
        "FROM tb_barang AS tb\n" +
        "INNER JOIN tb_supplier AS ts ON tb.id_supplier = ts.id\n" +
        "INNER JOIN tb_general AS kategori ON tb.id_kategori = kategori.id\n" +
        "INNER JOIN tb_general AS satuan ON tb.id_satuan = satuan.id\n" + where;
        statement = koneksi.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        int i = 1;
        while (rs.next()){
            ViewBarang vb = new ViewBarang();
            vb.setId(Integer.toString(i));
            vb.setKode(rs.getString("kode"));
            vb.setNama(rs.getString("nama"));
            vb.setKategori(rs.getString("kategori"));
            vb.setJumlah_stok(rs.getString("stok"));
            vb.setSatuan(rs.getString("satuan"));
            vb.setHarga_jual(format(rs.getInt("harga_jual")));
            vb.setHarga_beli(format(rs.getInt("harga_beli")));
            vb.setSupplier(rs.getString("supplier"));
            listBarang.add(vb);
            i++;
        }
        return listBarang;
    }
    
    /* Melakukan insert data ke tabel tb_barang */
    public int insertBarang(String[] data) throws Exception {
        String sql = "INSERT INTO tb_barang VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        statement = koneksi.prepareStatement(sql);
        for (int i = 0; i < data.length; i++) {
            statement.setString(i + 1, data[i]);
        }
        return statement.executeUpdate();
    }
    
    /* Melakukan update data ke tabel tb_barang */
    public int updateBarang(String id, String[] data) throws Exception {
        String sql = "UPDATE tb_barang SET kode = ?, nama = ?, id_kategori = ?, id_satuan = ?, stok = ?, "
                + "harga_beli = ?, harga_jual = ?, id_supplier = ?, stok_minimal = ?, tgl_expired = ?, "
                + "lokasi = ?, diskon_nominal = ? WHERE id = " + id;
        statement = koneksi.prepareStatement(sql);
        for (int i = 0; i < data.length; i++) {
            statement.setString(i + 1, data[i]);
        }
        return statement.executeUpdate();
    }
    
    /* Menghapus data di tabel tb_barang */
    public int deleteBarang(String id) throws Exception {
        String sql = "DELETE FROM tb_barang WHERE id = " + id;
        statement = koneksi.prepareStatement(sql);
        return statement.executeUpdate();
    }
    
    public String format(int number){
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        DecimalFormat formatter = new DecimalFormat("###.###", symbols);
        return formatter.format(number);
    }
    
    /* Fungsi untuk melakukan update stock barang di tb_barang dan menambahkan riwayat keluar-masuk
    stok barang di tb_stok_barang. Fungsi ini biasanya dipanggil pada proses transaksi pembelian, penjualan,
    serta retur pembelian dan retur penjualan */
    public int updateStockBarang(String id_barang, int stock_masuk, int stock_keluar, String keterangan) throws Exception {
        String sql = "INSERT INTO tb_stok_barang VALUES (?, ?, ?, ?, ?)";
        statement = koneksi.prepareStatement(sql);
        statement.setString(1, String.valueOf(vm.getLatestId("id", "tb_stok_barang")));
        statement.setString(2, id_barang);
        statement.setString(3, String.valueOf(stock_masuk));
        statement.setString(4, String.valueOf(stock_keluar));
        statement.setString(5, keterangan);
        int status = statement.executeUpdate();
        if (status > 0) {
            int stock = Integer.parseInt(vm.getDataByParameter("id = " + id_barang, "tb_barang", "stok"));
            stock += (stock_masuk - stock_keluar);
            sql = "UPDATE tb_barang SET stok = ? WHERE id = " + id_barang;
            statement = koneksi.prepareStatement(sql);
            statement.setString(1, String.valueOf(stock));
            return statement.executeUpdate();
        } else {
            return status;
        }
    }
}
