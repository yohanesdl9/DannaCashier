/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
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
    
    public static PenjualanDAO getInstance(){
        if (instance == null) instance = new PenjualanDAO();
        return instance;
    }
}
