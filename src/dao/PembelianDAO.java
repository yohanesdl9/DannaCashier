/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static dao.BarangDAO.instance;
import java.sql.PreparedStatement;
import penjualan.Koneksi;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class PembelianDAO extends Koneksi {
    static PembelianDAO instance;
    private PreparedStatement statement;
    ViewModel vm = new ViewModel();
    
    public static PembelianDAO getInstance(){
        if (instance == null) instance = new PembelianDAO();
        return instance;
    }
}
