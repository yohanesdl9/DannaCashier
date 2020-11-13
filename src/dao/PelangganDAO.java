/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Pelanggan;
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class PelangganDAO extends Koneksi {
    
    /* Class untuk meng-handle proses CRUD di database yang berkaitan dengan tabel tb_pelanggan */
    
    static PelangganDAO instance;
    private PreparedStatement statement;
    List<Pelanggan> listPelanggan;
    ViewModel vm = new ViewModel();
    
    /* Ekivalen dengan PelangganDAO [nama_variabel] = new PelangganDAO(); */
    public static PelangganDAO getInstance(){
        if (instance == null) instance = new PelangganDAO();
        return instance;
    }
    
    public Pelanggan getPelanggan(String nama) throws Exception {
        Pelanggan sup = new Pelanggan();
        ResultSet rs = vm.getDataByParameter("nama = '" + nama + "'", "tb_pelanggan");
        if (rs.next()){
            sup.setId(rs.getString("id"));
            sup.setKode(rs.getString("kode"));
            sup.setNama(rs.getString("nama"));
            sup.setAlamat(rs.getString("alamat"));
            sup.setTelepon(rs.getString("telepon"));
            sup.setKeterangan(rs.getString("keterangan"));
        }
        return sup;
    }
    
    /* Mendapatkan data pelanggan, dimasukkan ke dalam array list Pelanggan */
    public List<Pelanggan> getListPelanggan() throws Exception{
        listPelanggan = new ArrayList<>();
        ResultSet rs = vm.getAllDataFromTable("tb_pelanggan");
        int i = 1;
        while (rs.next()) {
            Pelanggan p = new Pelanggan();
            p.setId(Integer.toString(i));
            p.setKode(rs.getString("kode"));
            p.setNama(rs.getString("nama"));
            p.setAlamat(rs.getString("alamat"));
            p.setTelepon(rs.getString("telepon"));
            p.setKeterangan(rs.getString("keterangan"));
            listPelanggan.add(p);
            i++;
        }
        return listPelanggan;
    }
    
    /* Mendapatkan list semua pelanggan dengan mengetikkan keyword di form pencarian */
    public List<Pelanggan> getListPelanggan(String keyword) throws Exception{
        listPelanggan = new ArrayList<>();
        ResultSet rs = vm.getDataByParameter("nama LIKE '%" + keyword + "%'", "tb_pelanggan");
        int i = 1;
        while (rs.next()) {
            Pelanggan p = new Pelanggan();
            p.setId(Integer.toString(i));
            p.setKode(rs.getString("kode"));
            p.setNama(rs.getString("nama"));
            p.setAlamat(rs.getString("alamat"));
            p.setTelepon(rs.getString("telepon"));
            p.setKeterangan(rs.getString("keterangan"));
            listPelanggan.add(p);
            i++;
        }
        return listPelanggan;
    }
    
    public int insertPelanggan(String[] data) throws Exception {
        String sql = "INSERT INTO tb_pelanggan VALUES (?, ?, ?, ?, ?, ?)";
        statement = koneksi.prepareStatement(sql);
        for (int i = 0; i < data.length; i++) {
            statement.setString(i + 1, data[i]);
        }
        return statement.executeUpdate();
    }
    
    public int updatePelanggan(String id, String[] data) throws Exception{
        String sql = "UPDATE tb_pelanggan SET kode = ?, nama = ?, alamat = ?, telepon = ?, keterangan = ? WHERE id = " + id;
        statement = koneksi.prepareStatement(sql);
        for (int i = 0; i < data.length; i++) {
            statement.setString(i + 1, data[i]);
        }
        return statement.executeUpdate();
    }
    
    public int deletePelanggan(String id) throws Exception {
        String sql = "DELETE FROM tb_pelanggan WHERE id = " + id;
        statement = koneksi.prepareStatement(sql);
        return statement.executeUpdate();
    }
}
