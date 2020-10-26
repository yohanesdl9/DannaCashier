/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penjualan;

import java.sql.ResultSet;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class ViewModel extends Koneksi {
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
}
