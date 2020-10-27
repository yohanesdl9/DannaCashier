/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatable;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.ViewPenjualan;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class PenjualanDataTable extends AbstractTableModel {
    
    private static final String[] columnNames = {"No.", "Faktur", "Tanggal", "Tunai/Kredit", "Hari", "Jatuh Tempo", "Grand Total", "Sales", "Pelanggan"};

    List<ViewPenjualan> data;
    
    public PenjualanDataTable(List<ViewPenjualan> data) {
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
        ViewPenjualan asisten = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return asisten.getNo();
            case 1:
                return asisten.getFaktur();
            case 2:
                return asisten.getTanggal();
            case 3:
                return asisten.getTunai_kredit();
            case 4:
                return asisten.getHari();
            case 5:
                return asisten.getJatuh_tempo();
            case 6:
                return asisten.getGrand_total();
            case 7:
                return asisten.getSales();
            case 8:
                return asisten.getPelanggan();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
