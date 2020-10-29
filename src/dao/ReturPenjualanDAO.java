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
import model.ReturPenjualanDetail;
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
    private PreparedStatement statement;
    List<ViewReturPenjualan> listReturPenjualan;
    ViewModel vm = new ViewModel();
    BarangDAO barangDAO = BarangDAO.getInstance();
    
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
    
    public int insertReturPenjualan(String[] data_retur_jual, ArrayList<ReturPenjualanDetail> detail_retur_jual) throws Exception {
        String sql = "INSERT INTO tb_retur_penjualan VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        statement = koneksi.prepareStatement(sql);
        for (int i = 0; i < data_retur_jual.length; i++){
            statement.setString(i + 1, data_retur_jual[i]);
        }
        int status = statement.executeUpdate();
        if (status > 0) {
            int id_detail = vm.getLatestId("id", "tb_pembelian_detail");
            sql = "INSERT INTO tb_penjualan_detail VALUES ";
            for (int i = 0; i < detail_retur_jual.size(); i++){
                sql += ("('" + detail_retur_jual.get(i).getId() + "', '" + detail_retur_jual.get(i).getId_retur_penjualan() + 
                        "', '" + detail_retur_jual.get(i).getId_penjualan_detail() + "', '" + detail_retur_jual.get(i).getKode_barang() + 
                        "', '" + detail_retur_jual.get(i).getNama_barang() + "', '" + detail_retur_jual.get(i).getJumlah() + 
                        "', '" + detail_retur_jual.get(i).getSatuan() + "', '" + detail_retur_jual.get(i).getHarga_beli() + 
                        "', '" + detail_retur_jual.get(i).getTotal() + "')");
                if ((i + 1) < detail_retur_jual.size()) {
                    sql += ", ";
                } else {
                    sql += ";";
                }
            }
            status = stmt.executeUpdate(sql);
            // Update stock barang
            for (int i = 0; i < detail_retur_jual.size(); i++) {
                int stok_masuk = Integer.parseInt(detail_retur_jual.get(i).getJumlah());
                String id_barang = vm.getDataByParameter("kode_barang = '" + detail_retur_jual.get(i).getKode_barang() + "'", "tb_barang", "id");
                status = barangDAO.updateStockBarang(id_barang, stok_masuk, 0, "Penambahan stok dari retur penjualan " + data_retur_jual[2]);
                status = returDetailPenjualan(detail_retur_jual.get(i).getId_penjualan_detail(), stok_masuk);
            }
        }
        return status;
    }
    
    public int returDetailPenjualan(String id_pembelian, int jumlah_retur) throws Exception {
        int jumlah_asal = Integer.parseInt(vm.getDataByParameter("id = " + id_pembelian, "tb_penjualan_detail", "jumlah"));
        jumlah_asal -= jumlah_retur;
        String sql = "UPDATE tb_penjualan_detail SET jumlah = ? WHERE id = " + id_pembelian;
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
