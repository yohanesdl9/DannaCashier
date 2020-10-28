/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.ViewReturPembelian;
import model.ViewReturPenjualan;
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class ReturPenjualanDAO extends Koneksi {
    
    static ReturPenjualanDAO instance;
    List<ViewReturPenjualan> listReturPenjualan;
    ViewModel vm = new ViewModel();
    
    public static ReturPenjualanDAO getInstance(){
        if (instance == null) instance = new ReturPenjualanDAO();
        return instance;
    }
    
    public List<ViewReturPenjualan> getListReturPenjualan(Date start, Date end) throws Exception {
        String dateStart = new SimpleDateFormat("yyyy-MM-dd").format(start);
        String dateEnd = new SimpleDateFormat("yyyy-MM-dd").format(end);
        listReturPenjualan = new ArrayList<>();
        String sql = "SELECT trp.*, tpel.kode AS kode_pelanggan, tpel.nama AS nama_pelanggan\n" +
            "FROM tb_retur_penjualan AS trp\n" +
            "INNER JOIN tb_penjualan AS tp ON trp.id_penjualan = tp.id\n" +
            "INNER JOIN tb_pelanggan AS tpel ON tp.id_pelanggan = tpel.id\n" + 
            "WHERE trp.tanggal BETWEEN '" + dateStart + "' AND '" + dateEnd + "';";
        ResultSet rs = stmt.executeQuery(sql);
        int i = 1;
        while (rs.next()) {
            ViewReturPenjualan vrp = new ViewReturPenjualan();
            vrp.setNo(String.valueOf(i));
            listReturPenjualan.add(vrp);
            vrp.setNo(String.valueOf(i));
            vrp.setFaktur(rs.getString("faktur"));
            vrp.setTanggal(dateIndo(rs.getString("tanggal")));
            vrp.setKode_pelanggan(rs.getString("kode_pelanggan"));
            vrp.setNama_pelanggan(rs.getString("nama_pelanggan"));
            vrp.setTotal_nilai_retur(rs.getString("total_nilai_retur"));
            vrp.setTotal_dibayar(rs.getString("total_dibayar"));
            vrp.setTotal_mengurangi_piutang(rs.getString("total_kurang_piutang"));
        }
        return listReturPenjualan;
    }
    
    public String dateIndo(String date){
        String[] dates = date.split("-");
        String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return Integer.parseInt(dates[2]) + " " + bulan[Integer.parseInt(dates[1]) - 1] + " " + Integer.parseInt(dates[0]);
    }
    
}
