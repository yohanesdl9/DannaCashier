/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatable;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Pelanggan;
import model.Sales;
import model.Supplier;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class SalesDataTable extends AbstractTableModel {
    
    private static final String[] columnNames = {"No.", "Kode", "Nama", "Alamat", "Telepon", "Email", "Contact Person", "Keterangan"};
    
    List<Sales> data;

    public SalesDataTable(List<Sales> data) {
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
        Sales asisten = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return asisten.getId();
            case 1:
                return asisten.getKode();
            case 2:
                return asisten.getNama();
            case 3:
                return asisten.getAlamat();
            case 4:
                return asisten.getTelepon();
            case 5:
                return asisten.getEmail();
            case 6:
                return asisten.getContact_person();
            case 7:
                return asisten.getKeterangan();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
