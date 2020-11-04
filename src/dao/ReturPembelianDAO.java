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
import model.ReturPembelianDetail;
import model.ViewReturPembelian;
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class ReturPembelianDAO extends Koneksi {
    
    /* Meng-handle semua proses CRUD di database yang berkaitan dengan tb_retur_pembelian dan tb_retur_pembelian_detail */
    
    BarangDAO barangDAO = BarangDAO.getInstance();
    private PreparedStatement statement;
    static ReturPembelianDAO instance;
    List<ViewReturPembelian> listReturPembelian;
    ViewModel vm = new ViewModel();
    
    /* Ekivalen dengan ReturPembelianDAO [nama_variabel] = new ReturPembelianDAO(); */
    public static ReturPembelianDAO getInstance(){
        if (instance == null) instance = new ReturPembelianDAO();
        return instance;
    }
    
    /* Mendapatkan list retur pembelian berdasarkan rentang tanggal retur pembelian tertentu */
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
            vrp.setNo_retur(rs.getString("no_retur"));
            vrp.setFaktur(rs.getString("faktur"));
            vrp.setTanggal(dateIndo(rs.getString("tanggal")));
            vrp.setKode_supplier(rs.getString("kode_supplier"));
            vrp.setNama_supplier(rs.getString("nama_supplier"));
            vrp.setTotal_nilai_retur(rs.getString("total_retur"));
            vrp.setTotal_dibayar(rs.getString("total_dibayar"));
            vrp.setTotal_mengurangi_hutang(rs.getString("total_kurang_hutang"));
            listReturPembelian.add(vrp);
        }
        return listReturPembelian;
    }
    
    /* Melakukan insert data retur pembelian */
    public int insertReturPembelian(String[] data_retur_beli, ArrayList<ReturPembelianDetail> detail_retur_beli) throws Exception {
        String sql = "INSERT INTO tb_retur_pembelian VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        statement = koneksi.prepareStatement(sql);
        for (int i = 0; i < data_retur_beli.length; i++){
            statement.setString(i + 1, data_retur_beli[i]);
        }
        int status = statement.executeUpdate();
        if (status > 0) {
            int id_detail = vm.getLatestId("id", "tb_pembelian_detail");
            sql = "INSERT INTO tb_retur_pembelian_detail VALUES ";
            for (int i = 0; i < detail_retur_beli.size(); i++){
                sql += ("('" + detail_retur_beli.get(i).getId() + "', '" + detail_retur_beli.get(i).getId_retur_pembelian() + 
                        "', '" + detail_retur_beli.get(i).getId_pembelian_detail() + "', '" + detail_retur_beli.get(i).getKode_barang() + 
                        "', '" + detail_retur_beli.get(i).getNama_barang() + "', '" + detail_retur_beli.get(i).getJumlah() + 
                        "', '" + detail_retur_beli.get(i).getSatuan() + "', '" + detail_retur_beli.get(i).getHarga_beli() + 
                        "', '" + detail_retur_beli.get(i).getTotal() + "')");
                if ((i + 1) < detail_retur_beli.size()) {
                    sql += ", ";
                } else {
                    sql += ";";
                }
            }
            System.out.println(sql);
            status = stmt.executeUpdate(sql);
            // Update stock barang
            for (int i = 0; i < detail_retur_beli.size(); i++) {
                int stok_keluar = Integer.parseInt(detail_retur_beli.get(i).getJumlah());
                String id_barang = vm.getDataByParameter("kode = '" + detail_retur_beli.get(i).getKode_barang() + "'", "tb_barang", "id");
                status = barangDAO.updateStockBarang(id_barang, 0, stok_keluar, "Pengurangan stok dari retur pembelian " + data_retur_beli[1]);
                status = returDetailPenjualan(detail_retur_beli.get(i).getId_pembelian_detail(), stok_keluar);
            }
        }
        return status;
    }
    
    /* Saat proses retur pembelian, jumlah kuantitas barang di tb_pembelian_detail dikurangi */
    public int returDetailPenjualan(String id_pembelian, int jumlah_retur) throws Exception {
        int jumlah_asal = Integer.parseInt(vm.getDataByParameter("id = " + id_pembelian, "tb_pembelian_detail", "jumlah"));
        jumlah_asal -= jumlah_retur;
        String sql = "UPDATE tb_pembelian_detail SET jumlah = ? WHERE id = " + id_pembelian;
        statement = koneksi.prepareStatement(sql);
        statement.setString(1, String.valueOf(jumlah_asal));
        return statement.executeUpdate();
    }
    
    public String dateIndo(String date){
        String[] dates = date.split("-");
        String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return Integer.parseInt(dates[2]) + " " + bulan[Integer.parseInt(dates[1]) - 1] + " " + Integer.parseInt(dates[0]);
    }
    
}
