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
import model.Supplier;
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class SupplierDAO extends Koneksi {
    static SupplierDAO instance;
    private PreparedStatement statement;
    List<Supplier> listSupplier;
    ViewModel vm = new ViewModel();
    
    public static SupplierDAO getInstance(){
        if (instance == null) instance = new SupplierDAO();
        return instance;
    }
    
    public List<Supplier> getListSupplier() throws Exception{
        listSupplier = new ArrayList<>();
        ResultSet rs = vm.getAllDataFromTable("tb_supplier");
        int i = 1;
        while (rs.next()) {
            Supplier p = new Supplier();
            p.setId(Integer.toString(i));
            p.setKode(rs.getString("kode"));
            p.setNama(rs.getString("nama"));
            p.setAlamat(rs.getString("alamat"));
            p.setEmail(rs.getString("email"));
            p.setContact_person(rs.getString("contact_person"));
            p.setTelepon(rs.getString("telepon"));
            p.setKeterangan(rs.getString("keterangan"));
            listSupplier.add(p);
            i++;
        }
        return listSupplier;
    }
    
    public List<Supplier> getListSupplier(String keyword) throws Exception{
        listSupplier = new ArrayList<>();
        ResultSet rs = vm.getDataByParameter("nama LIKE '%" + keyword + "%'", "tb_supplier");
        int i = 1;
        while (rs.next()) {
            Supplier p = new Supplier();
            p.setId(Integer.toString(i));
            p.setKode(rs.getString("kode"));
            p.setNama(rs.getString("nama"));
            p.setAlamat(rs.getString("alamat"));
            p.setEmail(rs.getString("email"));
            p.setContact_person(rs.getString("contact_person"));
            p.setTelepon(rs.getString("telepon"));
            p.setKeterangan(rs.getString("keterangan"));
            listSupplier.add(p);
            i++;
        }
        return listSupplier;
    }
    
    public int insertSupplier(String[] data) throws Exception {
        String sql = "INSERT INTO tb_supplier VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        statement = koneksi.prepareStatement(sql);
        for (int i = 0; i < data.length; i++) {
            statement.setString(i + 1, data[i]);
        }
        return statement.executeUpdate();
    }
    
    public int updateSupplier(String id, String[] data) throws Exception{
        String sql = "UPDATE tb_supplier SET kode = ?, nama = ?, alamat = ?, telepon = ?, email = ?, contact_person = ?, rekening = ?, keterangan = ? WHERE id = " + id;
        statement = koneksi.prepareStatement(sql);
        for (int i = 0; i < data.length; i++) {
            statement.setString(i + 1, data[i]);
        }
        return statement.executeUpdate();
    }
    
    public int deleteSupplier(String id) throws Exception {
        String sql = "DELETE FROM tb_supplier WHERE id = " + id;
        statement = koneksi.prepareStatement(sql);
        return statement.executeUpdate();
    }
}
