/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatable;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.ViewAngsuranUtang;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class AngsuranUtangDataTable extends AbstractTableModel {
    private static final String[] columnNames = {"No.", "Faktur", "Tanggal", "Faktur Utang", "Kode Supplier", "Nama Supplier", "Tunai", "Operator"};
    
    List<ViewAngsuranUtang> data;

    public AngsuranUtangDataTable(List<ViewAngsuranUtang> data) {
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
        ViewAngsuranUtang asisten = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return asisten.getNo();
            case 1:
                return asisten.getFaktur();
            case 2:
                return asisten.getTanggal();
            case 3:
                return asisten.getFaktur_utang();
            case 4:
                return asisten.getKode_supplier();
            case 5:
                return asisten.getNama_supplier();
            case 6:
                return asisten.getTunai();
            case 7:
                return asisten.getOperator();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
