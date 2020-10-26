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
import model.ViewBarang;
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class BarangDAO extends Koneksi {
    static BarangDAO instance;
    private PreparedStatement statement;
    List<ViewBarang> listBarang;
    ViewModel vm = new ViewModel();
    
    public static BarangDAO getInstance(){
        if (instance == null) instance = new BarangDAO();
        return instance;
    }
    
    public List<ViewBarang> getListBarang() throws Exception {
        listBarang = new ArrayList<>();
        String sql = "SELECT tb.*, ts.nama AS supplier, kategori.keterangan AS kategori, satuan.keterangan AS satuan\n" +
        "FROM tb_barang AS tb\n" +
        "INNER JOIN tb_supplier AS ts ON tb.id_supplier = ts.id\n" +
        "INNER JOIN tb_general AS kategori ON tb.id_kategori = kategori.id\n" +
        "INNER JOIN tb_general AS satuan ON tb.id_satuan = satuan.id";
        ResultSet rs = statement.executeQuery(sql);
        int i = 1;
        while (rs.next()){
            ViewBarang vb = new ViewBarang();
            vb.setId(Integer.toString(i));
            vb.setKode(rs.getString("kode"));
            vb.setKode(rs.getString("nama"));
            vb.setKode(rs.getString("kategori"));
            vb.setKode(rs.getString("jumlah_stok"));
            vb.setKode(rs.getString("satuan"));
            vb.setKode(rs.getString("harga_jual"));
            vb.setKode(rs.getString("harga_beli"));
            vb.setKode(rs.getString("supplier"));
        }
        return listBarang;
    }
    
    public String format(int number){
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        DecimalFormat formatter = new DecimalFormat("###.###", symbols);
        return formatter.format(number);
    }
}
