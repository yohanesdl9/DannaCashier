/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatable;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Pelanggan;
import model.ViewBarang;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class BarangDataTable extends AbstractTableModel {
    
    private static final String[] columnNames = {"No.", "Kode", "Nama", "Kategori", "Jumlah Stok", "Satuan", "Harga Jual", "Harga Beli", "Supplier"};
    
    List<ViewBarang> data;

    public BarangDataTable(List<ViewBarang> data) {
        super();
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ViewBarang asisten = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return asisten.getId();
            case 1:
                return asisten.getKode();
            case 2:
                return asisten.getNama();
            case 3:
                return asisten.getKategori();
            case 4:
                return asisten.getJumlah_stok();
            case 5:
                return asisten.getSatuan();
            case 6:
                return asisten.getHarga_jual();
            case 7:
                return asisten.getHarga_beli();
            case 8:
                return asisten.getSupplier();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
