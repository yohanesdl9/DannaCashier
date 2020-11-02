/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatable;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.ViewAngsuranPiutang;
import model.ViewReturPembelian;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class ReturPembelianDataTable extends AbstractTableModel {
    private static final String[] columnNames = {"No.", "No. Retur", "Faktur", "Tanggal", "Kode Supplier", "Nama Supplier", "Total Nilai Retur", "Total Dibayar", "Total Mengurangi Utang"};
    
    List<ViewReturPembelian> data;
    
    public ReturPembelianDataTable(List<ViewReturPembelian> data){
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
        ViewReturPembelian asisten = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return asisten.getNo();
            case 1:
                return asisten.getNo_retur();
            case 2:
                return asisten.getFaktur();
            case 3:
                return asisten.getTanggal();
            case 4:
                return asisten.getKode_supplier();
            case 5:
                return asisten.getNama_supplier();
            case 6:
                return asisten.getTotal_nilai_retur();
            case 7:
                return asisten.getTotal_dibayar();
            case 8:
                return asisten.getTotal_mengurangi_hutang();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
