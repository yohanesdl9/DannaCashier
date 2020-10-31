/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatable;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.ViewPiutang;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class PiutangDataTable extends AbstractTableModel {
    private static final String[] columnNames = {"No.", "Faktur", "Tanggal", "Tempo", "Jatuh Tempo", "Kode Pelanggan", "Nama Pelanggan",
    "Piutang Awal", "Telah Dibayar", "Sisa Piutang", "Keterangan", "Operator", "Status"};
    
    List<ViewPiutang> data;

    public PiutangDataTable(List<ViewPiutang> data) {
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
        ViewPiutang asisten = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return asisten.getNo();
            case 1:
                return asisten.getFaktur();
            case 2:
                return asisten.getTanggal();
            case 3:
                return asisten.getTempo();
            case 4:
                return asisten.getJatuh_tempo();
            case 5:
                return asisten.getKode_pelanggan();
            case 6:
                return asisten.getNama_pelanggan();
            case 7:
                return asisten.getPiutang_awal();
            case 8:
                return asisten.getTelah_dibayar();
            case 9:
                return asisten.getSisa_piutang();
            case 10:
                return asisten.getKeterangan();
            case 11:
                return asisten.getOperator();
            case 12:
                return asisten.getStatus();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
