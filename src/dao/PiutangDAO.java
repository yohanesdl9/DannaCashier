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
import model.ViewPiutang;
import model.ViewUtang;
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class PiutangDAO extends Koneksi {
    
    static PiutangDAO instance;
    private PreparedStatement statement;
    ViewModel vm = new ViewModel();
    List<ViewPiutang> listPiutang;
    
    public static PiutangDAO getInstance(){
        if (instance == null) instance = new PiutangDAO();
        return instance;
    }
    
    public List<ViewPiutang> getListPiutang(Date start, Date end) throws Exception {
        String dateStart = new SimpleDateFormat("yyyy-MM-dd").format(start);
        String dateEnd = new SimpleDateFormat("yyyy-MM-dd").format(end);
        listPiutang = new ArrayList<>();
        String sql = "SELECT tp.id, tp.kode, tp.tanggal, tp.tempo, tp.jatuh_tempo, tpel.kode AS kode_pelanggan, tpel.nama AS nama_pelanggan,\n" +
            "tp.grand_total, IFNULL(Sum(tpi.tunai), 0) AS telah_dibayar, (tp.grand_total - IFNULL(Sum(tpi.tunai), 0)) AS sisa_piutang,\n" +
            "IF((tp.grand_total - IFNULL(Sum(tpi.tunai), 0)) > 0, 'Belum Lunas', 'Lunas') AS status\n" +
            "FROM tb_penjualan AS tp\n" +
            "LEFT JOIN tb_piutang AS tpi ON tpi.id_penjualan = tp.id\n" +
            "INNER JOIN tb_pelanggan AS tpel ON tp.id_pelanggan = tpel.id\n" +
            "WHERE tp.tunai_kredit <> 'TUNAI'\n" +
            "AND tp.tanggal BETWEEN '" + dateStart + "' AND '" + dateEnd + "'\n" +
            "GROUP BY tp.id HAVING (tp.grand_total - IFNULL(Sum(tpi.tunai), 0)) > 0";
        ResultSet rs = stmt.executeQuery(sql);
        int i = 1;
        while (rs.next()) {
            ViewPiutang vu = new ViewPiutang();
            vu.setNo(String.valueOf(i));
            vu.setFaktur(rs.getString("kode"));
            vu.setTanggal(dateIndo(rs.getString("tanggal")));
            vu.setTempo(rs.getString("tempo"));
            vu.setJatuh_tempo(dateIndo(rs.getString("jatuh_tempo")));
            vu.setKode_pelanggan(rs.getString("kode_pelanggan"));
            vu.setNama_pelanggan(rs.getString("nama_pelanggan"));
            vu.setPiutang_awal(rs.getString("grand_total"));
            vu.setTelah_dibayar(rs.getString("telah_dibayar"));
            vu.setSisa_piutang(rs.getString("sisa_piutang"));
            vu.setOperator("danna");
            vu.setStatus(rs.getString("status"));
            listPiutang.add(vu);
            i++;
        }
        return listPiutang;
    }
    
    public String dateIndo(String date){
        String[] dates = date.split("-");
        String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return Integer.parseInt(dates[2]) + " " + bulan[Integer.parseInt(dates[1]) - 1] + " " + Integer.parseInt(dates[0]);
    }
}