/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penjualan.penjualan;

import com.sun.glass.events.KeyEvent;
import dao.PelangganDAO;
import dao.PenjualanDAO;
import dao.ReturPenjualanDAO;
import datatable.DetailPenjualanDataTable;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.Pelanggan;
import model.PenjualanDetail;
import model.ReturPenjualanDetail;
import model.ViewPenjualan;
import penjualan.CustomCombo;
import penjualan.ViewModel;

/**
 *
 * @author hannah fitri nur aisyah
 */
public class ReturPenjualan extends javax.swing.JFrame {

    ViewModel vm = new ViewModel();
    PenjualanDAO penjualanDAO = PenjualanDAO.getInstance();
    ReturPenjualanDAO returPenjualanDAO = ReturPenjualanDAO.getInstance();
    PelangganDAO pelangganDAO = PelangganDAO.getInstance();
    DefaultTableModel model;
    List<PenjualanDetail> penjualanDetail;
    ViewPenjualan vp;
    int total_retur = 0;
    Calendar cal = Calendar.getInstance();
    
    public ReturPenjualan() {
        initComponents();
        model = (DefaultTableModel) tabelReturPenjualan.getModel();
        fillDataFakturPembelian(noFaktur);
        jLabel15.setVisible(false);
        totalKurangPiutang.setVisible(false);
        try {
            noRetur.setText(vm.getLatestIdReturPenjualan());
            cal.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            tanggalReturJual.setDate(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        kodePelanggan.setText("XXXX");
        namaPelanggan.setText("XXXX");
        alamatPelanggan.setText("XXXX");
        teleponPelanggan.setText("XXXX");
    }
    
    public void fillDataFakturPembelian(JComboBox combo) {
        try {
            combo.removeAllItems();
            ResultSet rs = vm.getAllDataFromTable("tb_penjualan");
            while (rs.next()) {
                new CustomCombo(combo).custom(combo, rs.getString("kode"));
                combo.setSelectedItem(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<PenjualanDetail> emptyList = new ArrayList<>();
        tabelPenjualan.setModel(new DetailPenjualanDataTable(emptyList));
    }
    
    public void initDropdown(JComboBox comboBox, String param, String table, String field) {
        try {
            ResultSet rs = vm.getDataByParameter(param, table);
            while (rs.next()) {
                comboBox.addItem(rs.getString(field));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void initDropdown(JComboBox comboBox, String table, String field) {
        try {
            ResultSet rs = vm.getAllDataFromTable(table);
            while (rs.next()) {
                comboBox.addItem(rs.getString(field));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void adjustTableColumnWidth(JTable table, int[] columnSizes) {
        for (int i = 0; i < columnSizes.length; i++){
            table.getColumnModel().getColumn(i).setPreferredWidth(columnSizes[i]);
        }
    }
    
    public int getRowByValue(JTable table, String value, int columnIndex) {
        TableModel model = table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, columnIndex).equals(value)) {
                return i;
            }
        }
        return -1;
    }
    
    public void resetAll(){
        noFaktur.setSelectedItem(null);
        inputBarcode.setText("");
        inputNama.setText("");
        inputSatuan.setText("");
        inputJumlah.setText("");
        inputHargaBersih.setText("");
        inputTotal.setText("");
        totalRetur.setText("");
        totalBayar.setText("");
        totalKurangPiutang.setText("");
        List<PenjualanDetail> emptyList = new ArrayList<>();
        tabelPenjualan.setModel(new DetailPenjualanDataTable(emptyList));
        model.setRowCount(0);
        jLabel15.setVisible(false);
        totalKurangPiutang.setVisible(false);
        try {
            noRetur.setText(vm.getLatestIdReturPembelian());
        } catch (Exception e) {
            e.printStackTrace();
        }
        kodePelanggan.setText("XXXX");
        namaPelanggan.setText("XXXX");
        alamatPelanggan.setText("XXXX");
        teleponPelanggan.setText("XXXX");
    }
    
    public void addToCart(){
        String kode = inputBarcode.getText();
        String nama = inputNama.getText();
        String satuan = inputSatuan.getText();
        String jumlah = inputJumlah.getText();
        String harga_bersih = inputHargaBersih.getText();
        String total = inputTotal.getText();
        
        if (getRowByValue(tabelReturPenjualan, kode, 0) > -1){
            int row = getRowByValue(tabelPenjualan, kode, 0);
            int row_retur = getRowByValue(tabelReturPenjualan, kode, 0);
            int current_qty = Integer.parseInt(tabelReturPenjualan.getValueAt(row_retur, 2).toString());
            int current_total = Integer.parseInt(tabelReturPenjualan.getValueAt(row_retur, 5).toString());
            int actual_qty = Integer.parseInt(tabelPenjualan.getValueAt(row, 2).toString());
            current_qty += Integer.parseInt(jumlah);
            if (current_qty > actual_qty) {
                JOptionPane.showMessageDialog(null, "Kuantitas item yang di-retur melebihi kuantitas item yang telah dibeli.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            } else {
                tabelReturPenjualan.setValueAt(String.valueOf(current_qty), row_retur, 2);
                int total_new = current_qty * Integer.parseInt(tabelReturPenjualan.getValueAt(row_retur, 4).toString());
                tabelReturPenjualan.setValueAt(String.valueOf(total_new), row_retur, 5);
                total_retur += (total_new - current_total);
                totalRetur.setText(String.valueOf(total_retur));

                if (!totalBayar.getText().equals("")) {
                    int total_bayar = Integer.parseInt(totalBayar.getText());
                    if (!vp.getTunai_kredit().equals("TUNAI")) totalKurangPiutang.setText(String.valueOf(total_retur - total_bayar));
                } else {
                    if (!vp.getTunai_kredit().equals("TUNAI")) totalKurangPiutang.setText(String.valueOf("0"));
                }
            }
        } else {
            Object[] data = {kode, nama, jumlah, satuan, harga_bersih, total};
            model.addRow(data);
            total_retur += Integer.parseInt(total);
            totalRetur.setText(String.valueOf(total_retur));

            if (!totalBayar.getText().equals("")) {
                int total_bayar = Integer.parseInt(totalBayar.getText());
                if (!vp.getTunai_kredit().equals("TUNAI")) totalKurangPiutang.setText(String.valueOf(total_retur - total_bayar));
            } else {
                if (!vp.getTunai_kredit().equals("TUNAI")) totalKurangPiutang.setText("0");
            }
        }
        
        inputBarcode.setText("");
        inputNama.setText("");
        inputSatuan.setText("");
        inputJumlah.setText("");
        inputHargaBersih.setText("");
        inputTotal.setText("");
    }
    
    public void doReturPenjualan(){
        try {
            String id_pembelian = vm.getDataByParameter("kode = '" + noFaktur.getSelectedItem().toString() + "'", "tb_penjualan", "id");
            boolean is_piutang = vm.getDataByParameter("kode = '" + noFaktur.getSelectedItem().toString() + "'", "tb_penjualan", "tunai_kredit").equals("TUNAI") ? false : true;
            String id_retur_pembelian = String.valueOf(vm.getLatestId("id", "tb_retur_penjualan"));
            String[] data = {
                id_retur_pembelian,
                id_pembelian,
                noRetur.getText(),
                new SimpleDateFormat("yyyy-MM-dd").format(tanggalReturJual.getDate()),
                noFaktur.getSelectedItem().toString(),
                totalRetur.getText(),
                totalBayar.getText(),
                is_piutang ? totalKurangPiutang.getText() : "0", "0"
            };
            ArrayList<ReturPenjualanDetail> detail_retur = new ArrayList<>();
            for (int i = 0; i < tabelReturPenjualan.getRowCount(); i++) {
                int id = vm.getLatestId("id", "tb_retur_penjualan_detail") + i;
                String id_dp = vm.getDataByParameter("kode_barang = '" + tabelReturPenjualan.getValueAt(i, 0).toString() + "' AND id_penjualan = '" + id_pembelian + "'", "tb_penjualan_detail", "id");
                ReturPenjualanDetail rpd = new ReturPenjualanDetail();
                rpd.setId(String.valueOf(id));
                rpd.setId_retur_penjualan(id_retur_pembelian);
                rpd.setId_penjualan_detail(id_dp);
                rpd.setKode_barang(tabelReturPenjualan.getValueAt(i, 0).toString());
                rpd.setNama_barang(tabelReturPenjualan.getValueAt(i, 1).toString());
                rpd.setJumlah(tabelReturPenjualan.getValueAt(i, 2).toString());
                rpd.setSatuan(tabelReturPenjualan.getValueAt(i, 3).toString());
                rpd.setHarga_beli(tabelReturPenjualan.getValueAt(i, 4).toString());
                rpd.setTotal(tabelReturPenjualan.getValueAt(i, 5).toString());
                detail_retur.add(rpd);
            }
            int status = returPenjualanDAO.insertReturPenjualan(data, detail_retur);
            if (status > 0) {
                JOptionPane.showMessageDialog(null, "Sukses memasukkan retur penjualan", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                resetAll();
            } else {
                JOptionPane.showMessageDialog(null, "Terjadi Kesalahan", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean verifyFormFilled(){
        return noFaktur.getSelectedItem() == null || tabelReturPenjualan.getRowCount() == 0 || totalBayar.getText().equals("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tanggalReturJual = new com.toedter.calendar.JDateChooser();
        noRetur = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelPenjualan = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelReturPenjualan = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        totalRetur = new javax.swing.JTextField();
        totalBayar = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        inputBarcode = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JButton();
        inputNama = new javax.swing.JTextField();
        inputJumlah = new javax.swing.JTextField();
        btnBaru = new javax.swing.JButton();
        inputHargaBersih = new javax.swing.JTextField();
        inputSatuan = new javax.swing.JTextField();
        inputTotal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        totalKurangPiutang = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        noFaktur = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        kodePelanggan = new javax.swing.JLabel();
        namaPelanggan = new javax.swing.JLabel();
        teleponPelanggan = new javax.swing.JLabel();
        alamatPelanggan = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 135, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("RETUR PENJUALAN ");

        jLabel2.setText("Tanggal");

        noRetur.setEditable(false);

        jLabel3.setText("No. Retur");

        jLabel4.setText("No. Faktur");

        tabelPenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode", "Nama", "Jumlah", "Satuan", "Harga Bersih", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelPenjualan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelPenjualanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelPenjualan);

        jLabel12.setText("Total");

        tabelReturPenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode", "Nama", "Jumlah", "Satuan", "Harga Bersih", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tabelReturPenjualan);

        jLabel13.setText("Total Nilai Retur");

        totalRetur.setEditable(false);

        totalBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                totalBayarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                totalBayarKeyReleased(evt);
            }
        });

        jLabel14.setText("Total Uang Dibayarkan");

        inputBarcode.setEditable(false);

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        inputNama.setEditable(false);

        inputJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputJumlahKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputJumlahKeyReleased(evt);
            }
        });

        btnBaru.setText("Baru");
        btnBaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruActionPerformed(evt);
            }
        });

        inputHargaBersih.setEditable(false);

        inputSatuan.setEditable(false);

        inputTotal.setEditable(false);

        jLabel5.setText("Kode/Barcode");

        jLabel6.setText("Nama");

        jLabel7.setText("Jumlah");

        jLabel8.setText("Satuan");

        jLabel9.setText("Harga Bersih");

        totalKurangPiutang.setEditable(false);

        jLabel15.setText("Total Mengurangi Piutang");

        noFaktur.setEditable(true);
        noFaktur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noFakturActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(0, 135, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel24.setText("DATA PELANGGAN");

        jLabel25.setText("Kode");

        jLabel26.setText("Nama");

        jLabel27.setText("Alamat");

        jLabel28.setText("Telepon");

        kodePelanggan.setText("XXXX");

        namaPelanggan.setText("XXXX");

        teleponPelanggan.setText("XXXX");

        alamatPelanggan.setText("XXXX");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kodePelanggan, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                            .addComponent(namaPelanggan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alamatPelanggan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(teleponPelanggan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(kodePelanggan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(namaPelanggan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(alamatPelanggan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(teleponPelanggan)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(totalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(totalKurangPiutang, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(totalRetur, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnSimpan)
                        .addGap(192, 192, 192)
                        .addComponent(btnBaru, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(noRetur)
                            .addComponent(tanggalReturJual, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(noFaktur, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(inputBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputHargaBersih, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(inputTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(tanggalReturJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(noRetur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(noFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputHargaBersih, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(totalRetur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(totalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(totalKurangPiutang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnBaru))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        if (verifyFormFilled()) {
            JOptionPane.showMessageDialog(null, "Harap masukkan data dengan benar!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        } else {
            doReturPenjualan();
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaruActionPerformed
        // TODO add your handling code here:
        resetAll();
    }//GEN-LAST:event_btnBaruActionPerformed

    private void noFakturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noFakturActionPerformed
        // TODO add your handling code here:
        if (noFaktur.getSelectedItem() != null) {
            try {
                String no_faktur = noFaktur.getSelectedItem().toString();
                penjualanDetail = penjualanDAO.getListPembelianDetail(no_faktur);
                tabelPenjualan.setModel(new DetailPenjualanDataTable(penjualanDetail));
                model.setRowCount(0);
                vp = penjualanDAO.getDataPenjualan(no_faktur);
                jLabel15.setVisible(!vp.getTunai_kredit().equals("TUNAI"));
                totalKurangPiutang.setVisible(!vp.getTunai_kredit().equals("TUNAI"));
                Pelanggan sup = pelangganDAO.getPelanggan(vp.getPelanggan());
                kodePelanggan.setText(sup.getKode());
                namaPelanggan.setText(vp.getPelanggan());
                alamatPelanggan.setText(sup.getAlamat());
                teleponPelanggan.setText(sup.getTelepon());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_noFakturActionPerformed

    private void tabelPenjualanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelPenjualanMouseClicked
        // TODO add your handling code here:
        JTable source = (JTable) evt.getSource();
        int row = source.rowAtPoint(evt.getPoint());
        TableModel tm = source.getModel();
        if (row > -1) {
            inputBarcode.setText(tm.getValueAt(row, 0).toString());
            inputNama.setText(tm.getValueAt(row, 1).toString());
            inputSatuan.setText(tm.getValueAt(row, 3).toString());
            inputHargaBersih.setText(tm.getValueAt(row, 4).toString());
            inputTotal.setText(tm.getValueAt(row, 5).toString()); 
        }
    }//GEN-LAST:event_tabelPenjualanMouseClicked

    private void inputJumlahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputJumlahKeyPressed
        // TODO add your handling code here:
        inputJumlah.setEditable((evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9' || evt.getKeyCode() == 8));
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (inputBarcode.getText().equals("") || inputJumlah.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Masukkan data dengan benar!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            } else {
                String kode_barang = inputBarcode.getText();
                int rowIndex = getRowByValue(tabelPenjualan, kode_barang, 0);
                int actual_qty = Integer.parseInt(tabelPenjualan.getValueAt(rowIndex, 2).toString());
                int input_qty = Integer.parseInt(inputJumlah.getText().toString());
                if (input_qty > actual_qty) {
                    JOptionPane.showMessageDialog(null, "Kuantitas item yang di-retur melebihi kuantitas item yang telah dibeli.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                } else if (input_qty == 0) {
                    JOptionPane.showMessageDialog(null, "Kuantitas item yang di-retur kosong", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                } else {
                    addToCart();
                }
            }
        }
    }//GEN-LAST:event_inputJumlahKeyPressed

    private void inputJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputJumlahKeyReleased
        // TODO add your handling code here:
        if (inputJumlah.getText().length() > 0) {
            int jumlah = Integer.parseInt(inputJumlah.getText());
            int hargaBeli = Integer.parseInt(inputHargaBersih.getText());
            int total = jumlah * hargaBeli;
            inputTotal.setText(String.valueOf(total));
        } else {
            inputTotal.setText(inputHargaBersih.getText());
        }
    }//GEN-LAST:event_inputJumlahKeyReleased

    private void totalBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalBayarKeyPressed
        // TODO add your handling code here:
        totalBayar.setEditable((evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9' || evt.getKeyCode() == 8));
    }//GEN-LAST:event_totalBayarKeyPressed

    private void totalBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalBayarKeyReleased
        // TODO add your handling code here:
        if (!totalBayar.getText().equals("")) {
            int total_bayar = Integer.parseInt(totalBayar.getText());
            if (vp != null && !vp.getTunai_kredit().equals("TUNAI")) totalKurangPiutang.setText(String.valueOf(total_retur - total_bayar));
        } else {
            if (vp != null && !vp.getTunai_kredit().equals("TUNAI")) totalKurangPiutang.setText(String.valueOf("0"));
        }
    }//GEN-LAST:event_totalBayarKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReturPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReturPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReturPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReturPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReturPenjualan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel alamatPelanggan;
    private javax.swing.JLabel alamatSupplier;
    private javax.swing.JLabel alamatSupplier1;
    private javax.swing.JButton btnBaru;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JTextField inputBarcode;
    private javax.swing.JTextField inputHargaBersih;
    private javax.swing.JTextField inputJumlah;
    private javax.swing.JTextField inputNama;
    private javax.swing.JTextField inputSatuan;
    private javax.swing.JTextField inputTotal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel kodePelanggan;
    private javax.swing.JLabel kodeSupplier;
    private javax.swing.JLabel kodeSupplier1;
    private javax.swing.JLabel namaPelanggan;
    private javax.swing.JLabel namaSupplier;
    private javax.swing.JLabel namaSupplier1;
    private javax.swing.JComboBox<String> noFaktur;
    private javax.swing.JTextField noRetur;
    private javax.swing.JTable tabelPenjualan;
    private javax.swing.JTable tabelReturPenjualan;
    private com.toedter.calendar.JDateChooser tanggalReturJual;
    private javax.swing.JLabel teleponPelanggan;
    private javax.swing.JLabel teleponSupplier;
    private javax.swing.JLabel teleponSupplier1;
    private javax.swing.JTextField totalBayar;
    private javax.swing.JTextField totalKurangPiutang;
    private javax.swing.JTextField totalRetur;
    // End of variables declaration//GEN-END:variables
}
