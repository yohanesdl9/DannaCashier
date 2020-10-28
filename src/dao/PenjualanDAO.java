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
import model.ViewPembelian;
import model.ViewPenjualan;
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class PenjualanDAO extends Koneksi {
    static PenjualanDAO instance;
    private PreparedStatement statement;
    ViewModel vm = new ViewModel();
    List<ViewPenjualan> listPenjualan;
    
    public static PenjualanDAO getInstance(){
        if (instance == null) instance = new PenjualanDAO();
        return instance;
    }
    
    public List<ViewPenjualan> getListPenjualan(Date start, Date end) throws Exception {
        String dateStart = new SimpleDateFormat("yyyy-MM-dd").format(start);
        String dateEnd = new SimpleDateFormat("yyyy-MM-dd").format(end);
        listPenjualan = new ArrayList<>();
        String sql = "SELECT tp.*, ts.nama AS nama_sales, tpel.nama AS nama_pelanggan\n" +
            "FROM tb_penjualan AS tp\n" +
            "INNER JOIN tb_sales AS ts ON tp.id_sales = ts.id\n" +
            "INNER JOIN tb_pelanggan AS tpel ON tp.id_pelanggan = tpel.id\n" +
            "WHERE tp.tanggal BETWEEN '" + dateStart + "' AND '" + dateEnd + "'";
        ResultSet rs = stmt.executeQuery(sql);
        int i = 1;
        while (rs.next()) {
            Date tanggal = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("tanggal"));
            ViewPenjualan vp = new ViewPenjualan();
            vp.setNo(String.valueOf(i));
            vp.setFaktur(rs.getString("faktur"));
            vp.setTanggal(dateIndo(rs.getString("tanggal")));
            vp.setTunai_kredit(rs.getString("tunai_kredit"));
            vp.setGrand_total(rs.getString("grand_total"));
            vp.setSales(rs.getString("nama_sales"));
            vp.setPelanggan(rs.getString("pelanggan"));
            if (rs.getString("jatuh_tempo") != null) {
                Date jatuh_tempo = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("jatuh_tempo"));
                long duration = (jatuh_tempo.getTime() - tanggal.getTime()) / (1000 * 60 * 60 * 24);
                vp.setHari(String.valueOf(duration));
                vp.setJatuh_tempo(dateIndo(rs.getString("jatuh_tempo")));
            } else {
                vp.setHari("");
                vp.setJatuh_tempo("");
            }
            listPenjualan.add(vp);
            i++;
        }
        return listPenjualan;
    }
    
    public String dateIndo(String date){
        String[] dates = date.split("-");
        String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return Integer.parseInt(dates[2]) + " " + bulan[Integer.parseInt(dates[1]) - 1] + " " + Integer.parseInt(dates[0]);
    }
}
