/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static dao.BarangDAO.instance;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.PembelianDetail;
import model.ViewPembelian;
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class PembelianDAO extends Koneksi {
    static PembelianDAO instance;
    BarangDAO barangDAO = BarangDAO.getInstance();
    private PreparedStatement statement;
    ViewModel vm = new ViewModel();
    List<ViewPembelian> listPembelian;
    
    public static PembelianDAO getInstance(){
        if (instance == null) instance = new PembelianDAO();
        return instance;
    }
    
    public List<ViewPembelian> getListPembelian(Date start, Date end) throws Exception {
        String dateStart = new SimpleDateFormat("yyyy-MM-dd").format(start);
        String dateEnd = new SimpleDateFormat("yyyy-MM-dd").format(end);
        listPembelian = new ArrayList<>();
        String sql = "SELECT tp.*, ts.kode, ts.nama FROM tb_pembelian AS tp\n" +
            "INNER JOIN tb_supplier AS ts ON tp.id_supplier = ts.id\n" +
            "WHERE tp.tanggal BETWEEN '" + dateStart + "' AND '" + dateEnd + "'";
        ResultSet rs = stmt.executeQuery(sql);
        int i = 1;
        while (rs.next()) {
            ViewPembelian vp = new ViewPembelian();
            vp.setNo(String.valueOf(i));
            vp.setFaktur(rs.getString("faktur"));
            vp.setTanggal(dateIndo(rs.getString("tanggal")));
            vp.setTunai_kredit(rs.getString("tunai_kredit"));
            vp.setKode_supplier(rs.getString("kode"));
            vp.setNama_supplier(rs.getString("nama"));
            vp.setGrand_total(rs.getString("grand_total"));
            vp.setOperator("danna");
            if (!rs.getString("tunai_kredit").equals("TUNAI")) {
                vp.setHari(rs.getString("tempo"));
            }
            vp.setJatuh_tempo(dateIndo(rs.getString("jatuh_tempo")));
            listPembelian.add(vp);
            i++;
        }
        return listPembelian;
    }
    
    public ViewPembelian getDataPembelian(String faktur) throws Exception {
        ResultSet rs = vm.getDataByParameter("faktur = '" + faktur + "'", "tb_pembelian");
        ViewPembelian vp = new ViewPembelian();
        while (rs.next()) {
            vp.setNo(rs.getString("id"));
            vp.setFaktur(rs.getString("faktur"));
            vp.setTanggal(dateIndo(rs.getString("tanggal")));
            vp.setTunai_kredit(rs.getString("tunai_kredit"));
            vp.setGrand_total(rs.getString("grand_total"));
            vp.setOperator("danna");
            if (rs.getString("tunai_kredit").equals("TUNAI")) {
                vp.setHari(rs.getString("tempo"));
            }
            vp.setJatuh_tempo(dateIndo(rs.getString("jatuh_tempo")));
        }
        return vp;
    }
    
    public List<PembelianDetail> getListPembelianDetail(String faktur) throws Exception {
        List<PembelianDetail> detail_beli = new ArrayList<>();
        String sql = "SELECT tpd.* FROM tb_pembelian_detail AS tpd\n"
                + "INNER JOIN tb_pembelian AS tp ON tpd.id_pembelian = tp.id\n"
                + "WHERE tp.faktur = '" + faktur + "';";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            PembelianDetail pd = new PembelianDetail();
            pd.setId(rs.getString("id"));
            pd.setId_pembelian(rs.getString("id_pembelian"));
            pd.setId_barang(rs.getString("id_barang"));
            pd.setKode_barang(rs.getString("kode_barang"));
            pd.setNama_barang(rs.getString("nama_barang"));
            pd.setJumlah(rs.getString("jumlah"));
            pd.setSatuan(rs.getString("satuan"));
            pd.setHarga_beli(rs.getString("harga_beli"));
            pd.setTotal(rs.getString("total"));
            pd.setHarga_jual(rs.getString("harga_jual"));
            detail_beli.add(pd);
        }
        return detail_beli;
    }
    
    public int insertPembelian(String[] data_pembelian, ArrayList<PembelianDetail> detail_pembelian) throws Exception {
        String sql = "INSERT INTO tb_pembelian VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        statement = koneksi.prepareStatement(sql);
        for (int i = 0; i < data_pembelian.length; i++){
            statement.setString(i + 1, data_pembelian[i]);
        }
        int status = statement.executeUpdate();
        if (status > 0) {
            int id_detail = vm.getLatestId("id", "tb_pembelian_detail");
            sql = "INSERT INTO tb_pembelian_detail VALUES ";
            for (int i = 0; i < detail_pembelian.size(); i++){
                sql += ("('" + detail_pembelian.get(i).getId() + "', '" + detail_pembelian.get(i).getId_pembelian() + "', '" +
                        detail_pembelian.get(i).getId_barang() + "', '" + detail_pembelian.get(i).getKode_barang() + "', '" +
                        detail_pembelian.get(i).getNama_barang() + "', '" + detail_pembelian.get(i).getJumlah() + "', '" +
                        detail_pembelian.get(i).getSatuan() + "', '" + detail_pembelian.get(i).getHarga_beli() + "', '" + 
                        detail_pembelian.get(i).getTotal() + "', '" + detail_pembelian.get(i).getHarga_jual() + "')");
                if ((i + 1) < detail_pembelian.size()) {
                    sql += ", ";
                } else {
                    sql += ";";
                }
            }
            status = stmt.executeUpdate(sql);
            // Update stock barang
            for (int i = 0; i < detail_pembelian.size(); i++) {
                int stok_masuk = Integer.parseInt(detail_pembelian.get(i).getJumlah());
                status = barangDAO.updateStockBarang(detail_pembelian.get(i).getId_barang(), stok_masuk, 0, "Penambahan stok dari transaksi pembelian " + data_pembelian[2]);
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
