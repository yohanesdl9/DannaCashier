/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatable;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.PembelianDetail;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class DetailPembelianDataTable extends AbstractTableModel {
    
    private static final String[] columnNames = {"Kode", "Nama", "Jumlah", "Satuan", "Harga Beli", "Total"};
    
    List<PembelianDetail> data;
    
    public DetailPembelianDataTable(List<PembelianDetail> data) {
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
        PembelianDetail pd = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return pd.getKode_barang();
            case 1:
                return pd.getNama_barang();
            case 2:
                return pd.getJumlah();
            case 3:
                return pd.getSatuan();
            case 4:
                return pd.getHarga_beli();
            case 5:
                return pd.getTotal();
        }
        return null;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
