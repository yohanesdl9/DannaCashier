/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penjualan;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class ViewModel extends Koneksi {
    
    Calendar cal = Calendar.getInstance();
    
    public ResultSet getDataByParameter(String condition, String table) throws Exception {
        String query = "SELECT * FROM " + table + " WHERE " + condition + ";";
        return stmt.executeQuery(query);
    }
    
    public String getDataByParameter(String condition, String table, String field) throws Exception {
        String query = "SELECT " + field + " FROM " + table + " WHERE " + condition + ";";
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getString(field);
        } else {
            return null;
        }
    }
    
    public ResultSet getAllDataFromTable(String table) throws Exception {
        String query = "SELECT * FROM " + table + ";";
        return stmt.executeQuery(query);
    }
    
    public int getLatestId(String field, String table) throws Exception {
        String query = "SELECT MAX(" + field + ") AS id FROM " + table + ";";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        return rs.getInt("id") + 1;
    }
    
    public String getIdFromKode(String field_kode, String value_kode, String table) throws Exception {
        String query = "SELECT id FROM " + table + " WHERE " + field_kode + " = '" + value_kode + "'";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        return rs.getString("id");
    }
    
    public String getLatestIdPenjualan() throws Exception {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        String query = "SELECT COUNT(id) AS id FROM tb_penjualan WHERE tanggal = '" + date + "';";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        return "PENJ" + new SimpleDateFormat("ddMMyy").format(cal.getTime()) + String.format("%05d", rs.getInt("id") + 1);
    }
    
    public String getLatestIdPembelian() throws Exception {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        String query = "SELECT COUNT(id) AS id FROM tb_pembelian WHERE tanggal = '" + date + "';";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        return "PEMB" + new SimpleDateFormat("ddMMyy").format(cal.getTime()) + String.format("%05d", rs.getInt("id") + 1);
    }
    
    public String getLatestIdReturPenjualan() throws Exception {
        String query = "SELECT COUNT(id) AS id FROM tb_retur_penjualan;";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        return "RTJ" + String.format("%07d", rs.getInt("id") + 1);
    }
    
    public String getLatestIdReturPembelian() throws Exception {
        String query = "SELECT COUNT(id) AS id FROM tb_retur_pembelian;";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        return "RTB" + String.format("%07d", rs.getInt("id") + 1);
    }
    
    public String getLatestIdUtang(String faktur_penjualan) throws Exception{
        String query = "SELECT Count(th.id) AS id\n" +
            "FROM tb_hutang AS th\n" +
            "INNER JOIN tb_pembelian AS tp ON th.id_pembelian = tp.id\n" +
            "WHERE tp.faktur = '" + faktur_penjualan + "'";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        return "FU" + faktur_penjualan.substring(4) + String.format("%05d", rs.getInt("id") + 1);
    }
    
    public String getLatestIdPiutang(String faktur_penjualan) throws Exception{
        String query = "SELECT Count(tp.id) AS id\n" +
            "FROM tb_piutang AS tp\n" +
            "INNER JOIN tb_penjualan AS tj ON tp.id_penjualan = tj.id\n" +
            "WHERE tj.kode = '" + faktur_penjualan + "'";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        return "FP" + faktur_penjualan.substring(4) + String.format("%05d", rs.getInt("id") + 1);
    }
}
