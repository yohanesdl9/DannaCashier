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
import model.PenjualanDetail;
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
    BarangDAO barangDAO = BarangDAO.getInstance();
    
    public static PenjualanDAO getInstance(){
        if (instance == null) instance = new PenjualanDAO();
        return instance;
    }
    
    public List<ViewPenjualan> getListPenjualan(Date start, Date end, String pelanggan, String sales) throws Exception {
        String dateStart = new SimpleDateFormat("yyyy-MM-dd").format(start);
        String dateEnd = new SimpleDateFormat("yyyy-MM-dd").format(end);
        listPenjualan = new ArrayList<>();
        String sql = "SELECT tp.*, ts.nama AS nama_sales, tpel.nama AS nama_pelanggan\n" +
            "FROM tb_penjualan AS tp\n" +
            "INNER JOIN tb_sales AS ts ON tp.id_sales = ts.id\n" +
            "INNER JOIN tb_pelanggan AS tpel ON tp.id_pelanggan = tpel.id\n" +
            "WHERE tp.tanggal BETWEEN '" + dateStart + "' AND '" + dateEnd + "'";
        if (!pelanggan.equals("Semua Pelanggan")) sql += " AND tpel.nama = '" + pelanggan + "'";
        if (!sales.equals("Semua Sales")) sql += " AND ts.nama = '" + sales + "'";
        ResultSet rs = stmt.executeQuery(sql);
        int i = 1;
        while (rs.next()) {
            ViewPenjualan vp = new ViewPenjualan();
            vp.setNo(String.valueOf(i));
            vp.setFaktur(rs.getString("kode"));
            vp.setTanggal(dateIndo(rs.getString("tanggal")));
            vp.setTunai_kredit(rs.getString("tunai_kredit"));
            vp.setGrand_total(rs.getString("grand_total"));
            vp.setSales(rs.getString("nama_sales"));
            vp.setPelanggan(rs.getString("nama_pelanggan"));
            if (!rs.getString("tunai_kredit").equals("TUNAI")) {
                vp.setHari(rs.getString("tempo"));
            }
            vp.setJatuh_tempo(dateIndo(rs.getString("jatuh_tempo")));
            listPenjualan.add(vp);
            i++;
        }
        return listPenjualan;
    }
    
    public ViewPenjualan getDataPenjualan(String faktur) throws Exception {
        ViewPenjualan vp = new ViewPenjualan();
        ResultSet rs = vm.getDataByParameter("kode = '" + faktur + "'", "tb_penjualan");
        while (rs.next()) {
            vp.setNo(rs.getString("id"));
            vp.setFaktur(rs.getString("kode"));
            vp.setTanggal(dateIndo(rs.getString("tanggal")));
            vp.setTunai_kredit(rs.getString("tunai_kredit"));
            vp.setGrand_total(rs.getString("grand_total"));
            if (rs.getString("tunai_kredit").equals("TUNAI")) {
                vp.setHari(rs.getString("tempo"));
            }
            vp.setJatuh_tempo(dateIndo(rs.getString("jatuh_tempo")));
        }
        return vp;
    }
     
    public List<PenjualanDetail> getListPembelianDetail(String faktur) throws Exception {
        List<PenjualanDetail> detail_jual = new ArrayList<>();
        String sql = "SELECT tpd.* FROM tb_penjualan_detail AS tpd\n"
                + "INNER JOIN tb_penjualan AS tp ON tpd.id_penjualan = tp.id\n"
                + "WHERE tp.kode = '" + faktur + "'";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            PenjualanDetail pd = new PenjualanDetail();
            pd.setId(rs.getString("id"));
            pd.setId_penjualan(rs.getString("id_penjualan"));
            pd.setId_barang(rs.getString("id_barang"));
            pd.setKode_barang(rs.getString("kode_barang"));
            pd.setNama_barang(rs.getString("nama_barang"));
            pd.setJumlah(rs.getString("jumlah"));
            pd.setSatuan(rs.getString("satuan"));
            pd.setHarga_jual(rs.getString("harga_jual"));
            pd.setDiskon(rs.getString("diskon"));
            pd.setHarga_bersih(rs.getString("harga_bersih"));
            pd.setTotal(rs.getString("total"));
            detail_jual.add(pd);
        }
        return detail_jual;
    }
    
    public int insertPenjualan(String[] data_penjualan, ArrayList<PenjualanDetail> detail_penjualan) throws Exception {
        String sql = "INSERT INTO tb_penjualan VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        statement = koneksi.prepareStatement(sql);
        for (int i = 0; i < data_penjualan.length; i++){
            statement.setString(i + 1, data_penjualan[i]);
        }
        int status = statement.executeUpdate();
        if (status > 0) {
            int id_detail = vm.getLatestId("id", "tb_pembelian_detail");
            sql = "INSERT INTO tb_penjualan_detail VALUES ";
            for (int i = 0; i < detail_penjualan.size(); i++){
                sql += ("('" + detail_penjualan.get(i).getId() + "', '" + detail_penjualan.get(i).getId_penjualan() + "', '" +
                        detail_penjualan.get(i).getId_barang() + "', '" + detail_penjualan.get(i).getKode_barang() + "', '" +
                        detail_penjualan.get(i).getNama_barang() + "', '" + detail_penjualan.get(i).getJumlah() + "', '" +
                        detail_penjualan.get(i).getSatuan() + "', '" + detail_penjualan.get(i).getHarga_jual() + "', '" + 
                        detail_penjualan.get(i).getDiskon() + "', '" + detail_penjualan.get(i).getHarga_bersih()+ "', '" + detail_penjualan.get(i).getTotal() + "')");
                if ((i + 1) < detail_penjualan.size()) {
                    sql += ", ";
                } else {
                    sql += ";";
                }
            }
            status = stmt.executeUpdate(sql);
            // Update stock barang
            for (int i = 0; i < detail_penjualan.size(); i++) {
                int stok_keluar = Integer.parseInt(detail_penjualan.get(i).getJumlah());
                status = barangDAO.updateStockBarang(detail_penjualan.get(i).getId_barang(), 0, stok_keluar, "Pengurangan stok dari transaksi penjualan " + data_penjualan[2]);
            }
        }
        return status;
    }
    
    public String dateIndo(String date){
        String[] dates = date.split("-");
        String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return Integer.parseInt(dates[2]) + " " + bulan[Integer.parseInt(dates[1]) - 1] + " " + Integer.parseInt(dates[0]);
    }
}
