/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Sales;
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class SalesDAO extends Koneksi {
    static SalesDAO instance;
    private PreparedStatement statement;
    List<Sales> listSales;
    ViewModel vm = new ViewModel();
    
    public static SalesDAO getInstance(){
        if (instance == null) instance = new SalesDAO();
        return instance;
    }
    
    public List<Sales> getListSales() throws Exception{
        listSales = new ArrayList<>();
        ResultSet rs = vm.getAllDataFromTable("tb_sales");
        int i = 1;
        while (rs.next()) {
            Sales p = new Sales();
            p.setId(Integer.toString(i));
            p.setKode(rs.getString("kode"));
            p.setNama(rs.getString("nama"));
            p.setAlamat(rs.getString("alamat"));
            p.setEmail(rs.getString("email"));
            p.setContact_person(rs.getString("contact_person"));
            p.setTelepon(rs.getString("telepon"));
            p.setKeterangan(rs.getString("keterangan"));
            listSales.add(p);
            i++;
        }
        return listSales;
    }
    
    public List<Sales> getListSales(String keyword) throws Exception{
        listSales = new ArrayList<>();
        ResultSet rs = vm.getDataByParameter("nama LIKE '%" + keyword + "%'", "tb_sales");
        int i = 1;
        while (rs.next()) {
            Sales p = new Sales();
            p.setId(Integer.toString(i));
            p.setKode(rs.getString("kode"));
            p.setNama(rs.getString("nama"));
            p.setAlamat(rs.getString("alamat"));
            p.setTelepon(rs.getString("telepon"));
            p.setEmail(rs.getString("email"));
            p.setContact_person(rs.getString("contact_person"));
            p.setKeterangan(rs.getString("keterangan"));
            listSales.add(p);
            i++;
        }
        return listSales;
    }
    
    public int insertSales(String[] data) throws Exception {
        String sql = "INSERT INTO tb_sales VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        statement = koneksi.prepareStatement(sql);
        for (int i = 0; i < data.length; i++) {
            statement.setString(i + 1, data[i]);
        }
        return statement.executeUpdate();
    }
    
    public int updateSales(String id, String[] data) throws Exception{
        String sql = "UPDATE tb_sales SET kode = ?, nama = ?, alamat = ?, telepon = ?, email = ?, contact_person = ?, rekening = ?, keterangan = ? WHERE id = " + id;
        statement = koneksi.prepareStatement(sql);
        for (int i = 0; i < data.length; i++) {
            statement.setString(i + 1, data[i]);
        }
        return statement.executeUpdate();
    }
    
    public int deleteSales(String id) throws Exception {
        String sql = "DELETE FROM tb_sales WHERE id = " + id;
        statement = koneksi.prepareStatement(sql);
        return statement.executeUpdate();
    }
}
