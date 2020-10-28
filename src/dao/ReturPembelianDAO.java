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
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class ReturPembelianDAO extends Koneksi {
    
    static ReturPembelianDAO instance;
    List<ViewReturPembelian> listReturPembelian;
    ViewModel vm = new ViewModel();
    
    public static ReturPembelianDAO getInstance(){
        if (instance == null) instance = new ReturPembelianDAO();
        return instance;
    }
    
    public List<ViewReturPembelian> getListReturPembelian(Date start, Date end) throws Exception {
        String dateStart = new SimpleDateFormat("yyyy-MM-dd").format(start);
        String dateEnd = new SimpleDateFormat("yyyy-MM-dd").format(end);
        listReturPembelian = new ArrayList<>();
        String sql = "SELECT trp.*, ts.kode AS kode_supplier, ts.nama AS nama_supplier\n" +
            "FROM tb_retur_pembelian AS trp\n" +
            "INNER JOIN tb_pembelian AS tp ON trp.id_pembelian = tp.id\n" +
            "INNER JOIN tb_supplier AS ts ON tp.id_supplier = ts.id\n" +
            "WHERE trp.tanggal BETWEEN '" + dateStart + "' AND '" + dateEnd + "'";
        ResultSet rs = stmt.executeQuery(sql);
        int i = 1;
        while (rs.next()) {
            ViewReturPembelian vrp = new ViewReturPembelian();
            vrp.setNo(String.valueOf(i));
            vrp.setFaktur(rs.getString("faktur"));
            vrp.setTanggal(dateIndo(rs.getString("tanggal")));
            vrp.setKode_supplier(rs.getString("kode_supplier"));
            vrp.setNama_supplier(rs.getString("nama_supplier"));
            vrp.setTotal_nilai_retur(rs.getString("total_nilai_retur"));
            vrp.setTotal_dibayar(rs.getString("total_dibayar"));
            vrp.setTotal_mengurangi_hutang("0");
            listReturPembelian.add(vrp);
        }
        return listReturPembelian;
    }
    
    public String dateIndo(String date){
        String[] dates = date.split("-");
        String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return Integer.parseInt(dates[2]) + " " + bulan[Integer.parseInt(dates[1]) - 1] + " " + Integer.parseInt(dates[0]);
    }
    
}
