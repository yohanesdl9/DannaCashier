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
import model.ViewAngsuranPiutang;
import model.ViewPiutang;
import model.ViewUtang;
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class PiutangDAO extends Koneksi {
    
    /* Class untuk meng-handle proses CRUD di database yang berkaitan dengan tabel tb_piutang */
    
    static PiutangDAO instance;
    private PreparedStatement statement;
    ViewModel vm = new ViewModel();
    List<ViewPiutang> listPiutang;
    List<ViewAngsuranPiutang> listAngsuranPiutang;
    
    public static PiutangDAO getInstance(){
        if (instance == null) instance = new PiutangDAO();
        return instance;
    }
    
    /* Mendapatkan list piutang dari rentang tanggal penjualan tertentu yang belum lunas */
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
    
    /* Mendapatkan list dari angsuran piutang yang dibayarkan oleh pelanggan */
    public List<ViewAngsuranPiutang> getListAngsuranPiutang(Date start, Date end, String pelanggan) throws Exception {
        String dateStart = new SimpleDateFormat("yyyy-MM-dd").format(start);
        String dateEnd = new SimpleDateFormat("yyyy-MM-dd").format(end);
        listAngsuranPiutang = new ArrayList<>();
        String sql = "SELECT th.id, tp.kode, th.tanggal, th.faktur_piutang, tpel.kode AS kode_pelanggan, tpel.nama AS nama_pelanggan, th.tunai\n" +
            "FROM tb_piutang AS th\n" +
            "INNER JOIN tb_penjualan AS tp ON th.id_penjualan = tp.id\n" +
            "INNER JOIN tb_pelanggan AS tpel ON tp.id_pelanggan = tpel.id\n" +
            "WHERE th.tanggal BETWEEN '" + dateStart + "' AND '" + dateEnd + "'";
        if (!pelanggan.equals("Semua Pelanggan")) sql += "\nAND tpel.nama = '" + pelanggan + "'";
        ResultSet rs = stmt.executeQuery(sql);
        int i = 1;
        while (rs.next()) {
            ViewAngsuranPiutang vu = new ViewAngsuranPiutang();
            vu.setNo(String.valueOf(i));
            vu.setFaktur(rs.getString("kode"));
            vu.setTanggal(dateIndo(rs.getString("tanggal")));
            vu.setFaktur_piutang(rs.getString("faktur_piutang"));
            vu.setKode_pelanggan(rs.getString("kode_pelanggan"));
            vu.setNama_pelanggan(rs.getString("nama_pelanggan"));
            vu.setTunai(rs.getString("tunai"));
            vu.setOperator("danna");
            listAngsuranPiutang.add(vu);
            i++;
        }
        return listAngsuranPiutang;
    }
    
    /* Melakukan insert data ke tb_piutang untuk membayar piutang */
    public int bayarPiutang(String[] data) throws Exception {
        String sql = "INSERT INTO tb_piutang VALUES (?, ?, ?, ?, ?)";
        statement = koneksi.prepareStatement(sql);
        for (int i = 0; i < data.length; i++) {
            statement.setString(i + 1, data[i]);
        }
        return statement.executeUpdate();
    }
    
    public String dateIndo(String date){
        String[] dates = date.split("-");
        String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return Integer.parseInt(dates[2]) + " " + bulan[Integer.parseInt(dates[1]) - 1] + " " + Integer.parseInt(dates[0]);
    }
}