/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatable;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.ViewReturPenjualan;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class ReturPenjualanDataTable extends AbstractTableModel {
    private static final String[] columnNames = {"No.", "Faktur", "Tanggal", "Kode Pelanggan", "Nama Pelanggan", "Total Nilai Retur", "Total Dibayar", "Total Mengurangi Piutang", "Operator"};
    
    List<ViewReturPenjualan> data;
    
    public ReturPenjualanDataTable(List<ViewReturPenjualan> data){
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
        ViewReturPenjualan asisten = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return asisten.getNo();
            case 1:
                return asisten.getFaktur();
            case 2:
                return asisten.getTanggal();
            case 3:
                return asisten.getKode_pelanggan();
            case 4:
                return asisten.getNama_pelanggan();
            case 5:
                return asisten.getTotal_nilai_retur();
            case 6:
                return asisten.getTotal_dibayar();
            case 7:
                return asisten.getTotal_mengurangi_piutang();
            case 8:
                return asisten.getOperator();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
