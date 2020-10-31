/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.ViewUtang;
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class UtangDAO extends Koneksi {
    
    static UtangDAO instance;
    private PreparedStatement statement;
    ViewModel vm = new ViewModel();
    List<ViewUtang> listUtang;
    
    public static UtangDAO getInstance(){
        if (instance == null) instance = new UtangDAO();
        return instance;
    }
    
    public List<ViewUtang> getListUtang(Date start, Date end) throws Exception {
        String dateStart = new SimpleDateFormat("yyyy-MM-dd").format(start);
        String dateEnd = new SimpleDateFormat("yyyy-MM-dd").format(end);
        listUtang = new ArrayList<>();
        String sql = "SELECT tp.id, tp.faktur, tp.tanggal, tp.tempo, tp.jatuh_tempo, ts.kode AS kode_supplier, ts.nama AS nama_supplier,\n" +
            "tp.grand_total, IFNULL(Sum(tu.tunai), 0) AS telah_dibayar, (tp.grand_total - IFNULL(Sum(tu.tunai), 0)) AS sisa_utang,\n" +
            "IF((tp.grand_total - IFNULL(Sum(tu.tunai), 0)) > 0, 'Belum Lunas', 'Lunas') AS status\n" +
            "FROM tb_pembelian AS tp\n" +
            "LEFT JOIN tb_hutang AS tu ON tu.id_pembelian = tp.id\n" +
            "INNER JOIN tb_supplier AS ts ON tp.id_supplier = ts.id\n" +
            "WHERE tp.tunai_kredit <> 'TUNAI'\n" +
            "AND tp.tanggal BETWEEN '" + dateStart + "' AND '" + dateEnd + "'\n" +
            "GROUP BY tp.id HAVING (tp.grand_total - IFNULL(Sum(tu.tunai), 0)) > 0";
        ResultSet rs = stmt.executeQuery(sql);
        int i = 1;
        while (rs.next()) {
            ViewUtang vu = new ViewUtang();
            vu.setNo(String.valueOf(i));
            vu.setFaktur(rs.getString("faktur"));
            vu.setTanggal(dateIndo(rs.getString("tanggal")));
            vu.setTempo(rs.getString("tempo"));
            vu.setJatuh_tempo(dateIndo(rs.getString("jatuh_tempo")));
            vu.setKode_supplier(rs.getString("kode_supplier"));
            vu.setNama_supplier(rs.getString("nama_supplier"));
            vu.setUtang_awal(rs.getString("grand_total"));
            vu.setTelah_dibayar(rs.getString("telah_dibayar"));
            vu.setSisa_utang(rs.getString("sisa_utang"));
            vu.setOperator("danna");
            vu.setStatus(rs.getString("status"));
            listUtang.add(vu);
            i++;
        }
        return listUtang;
    }
    
    public String dateIndo(String date){
        String[] dates = date.split("-");
        String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return Integer.parseInt(dates[2]) + " " + bulan[Integer.parseInt(dates[1]) - 1] + " " + Integer.parseInt(dates[0]);
    }
    
    
}