/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penjualan;

import java.sql.*;

/**
 *
 * @author yohan
 */
public class Koneksi {
    public Connection koneksi;
    public Statement stmt;
    public Koneksi(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/danna", "root", "");
            stmt = koneksi.createStatement();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
