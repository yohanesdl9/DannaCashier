/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penjualan.pembelian;

import com.sun.glass.events.KeyEvent;
import dao.PembelianDAO;
import dao.ReturPembelianDAO;
import dao.SupplierDAO;
import datatable.DetailPembelianDataTable;
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
import model.PembelianDetail;
import model.ReturPembelianDetail;
import model.Supplier;
import model.ViewPembelian;
import penjualan.CustomCombo;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class ReturPembelian extends javax.swing.JFrame {

    ViewModel vm = new ViewModel();
    PembelianDAO pembelianDAO = PembelianDAO.getInstance();
    ReturPembelianDAO returPembelianDAO = ReturPembelianDAO.getInstance();
    SupplierDAO supplierDAO = SupplierDAO.getInstance();
    DefaultTableModel model;
    List<PembelianDetail> pembelianDetail;
    ViewPembelian vp;
    int total_retur = 0;
    Calendar cal = Calendar.getInstance();
    
    public ReturPembelian() {
        initComponents();
        model = (DefaultTableModel) tabelReturPembelian.getModel();
        fillDataFakturPembelian(noFaktur);
        jLabel15.setVisible(false);
        totalKurangUtang.setVisible(false);
        try {
            noRetur.setText(vm.getLatestIdReturPembelian());
            cal.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            tanggalReturBeli.setDate(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        kodeSupplier.setText("XXXX");
        namaSupplier.setText("XXXX");
        alamatSupplier.setText("XXXX");
        teleponSupplier.setText("XXXX");
    }
    
    public void fillDataFakturPembelian(JComboBox combo) {
        try {
            combo.removeAllItems();
            ResultSet rs = vm.getAllDataFromTable("tb_pembelian");
            while (rs.next()) {
                new CustomCombo(combo).custom(combo, rs.getString("faktur"));
                combo.setSelectedItem(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<PembelianDetail> emptyList = new ArrayList<>();
        tabelPembelian.setModel(new DetailPembelianDataTable(emptyList));
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
        totalKurangUtang.setText("");
        List<PembelianDetail> emptyList = new ArrayList<>();
        tabelPembelian.setModel(new DetailPembelianDataTable(emptyList));
        model.setRowCount(0);
        jLabel15.setVisible(false);
        totalKurangUtang.setVisible(false);
        try {
            noRetur.setText(vm.getLatestIdReturPembelian());
        } catch (Exception e) {
            e.printStackTrace();
        }
        kodeSupplier.setText("XXXX");
        namaSupplier.setText("XXXX");
        alamatSupplier.setText("XXXX");
        teleponSupplier.setText("XXXX");
    }
    
    public void addToCart(){
        String kode = inputBarcode.getText();
        String nama = inputNama.getText();
        String satuan = inputSatuan.getText();
        String jumlah = inputJumlah.getText();
        String harga_bersih = inputHargaBersih.getText();
        String total = inputTotal.getText();
        
        if (getRowByValue(tabelReturPembelian, kode, 0) > -1){
            int row = getRowByValue(tabelPembelian, kode, 0);
            int row_retur = getRowByValue(tabelReturPembelian, kode, 0);
            int current_qty = Integer.parseInt(tabelReturPembelian.getValueAt(row_retur, 2).toString());
            int current_total = Integer.parseInt(tabelReturPembelian.getValueAt(row_retur, 5).toString());
            int actual_qty = Integer.parseInt(tabelPembelian.getValueAt(row, 2).toString());
            current_qty += Integer.parseInt(jumlah);
            if (current_qty > actual_qty) {
                JOptionPane.showMessageDialog(null, "Kuantitas item yang di-retur melebihi kuantitas item yang telah dibeli.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            } else {
                tabelReturPembelian.setValueAt(String.valueOf(current_qty), row_retur, 2);
                int total_new = current_qty * Integer.parseInt(tabelReturPembelian.getValueAt(row_retur, 4).toString());
                tabelReturPembelian.setValueAt(String.valueOf(total_new), row_retur, 5);
                total_retur += (total_new - current_total);
                totalRetur.setText(String.valueOf(total_retur));

                if (!totalBayar.getText().equals("")) {
                    int total_bayar = Integer.parseInt(totalBayar.getText());
                    if (!vp.getTunai_kredit().equals("TUNAI")) totalKurangUtang.setText(String.valueOf(total_retur - total_bayar));
                } else {
                    if (!vp.getTunai_kredit().equals("TUNAI")) totalKurangUtang.setText(String.valueOf("0"));
                }
            }
        } else {
            Object[] data = {kode, nama, jumlah, satuan, harga_bersih, total};
            model.addRow(data);
            total_retur += Integer.parseInt(total);
            totalRetur.setText(String.valueOf(total_retur));

            if (!totalBayar.getText().equals("")) {
                int total_bayar = Integer.parseInt(totalBayar.getText());
                if (!vp.getTunai_kredit().equals("TUNAI")) totalKurangUtang.setText(String.valueOf(total_retur - total_bayar));
            } else {
                if (!vp.getTunai_kredit().equals("TUNAI")) totalKurangUtang.setText(String.valueOf("0"));
            }
        }

        inputBarcode.setText("");
        inputNama.setText("");
        inputSatuan.setText("");
        inputJumlah.setText("");
        inputHargaBersih.setText("");
        inputTotal.setText("");
    }
    
    public void doReturPembelian(){
        try {
            String id_pembelian = vm.getDataByParameter("faktur = '" + noFaktur.getSelectedItem().toString() + "'", "tb_pembelian", "id");
            boolean is_hutang = vm.getDataByParameter("faktur = '" + noFaktur.getSelectedItem().toString() + "'", "tb_pembelian", "tunai_kredit").equals("TUNAI") ? false : true;
            String id_retur_pembelian = String.valueOf(vm.getLatestId("id", "tb_retur_pembelian"));
            String[] data = {
                id_retur_pembelian,
                noRetur.getText(),
                new SimpleDateFormat("yyyy-MM-dd").format(tanggalReturBeli.getDate()),
                id_pembelian,
                noFaktur.getSelectedItem().toString(),
                totalRetur.getText(),
                totalBayar.getText(),
                is_hutang ? totalKurangUtang.getText() : "0"
            };
            ArrayList<ReturPembelianDetail> detail_retur = new ArrayList<>();
            for (int i = 0; i < tabelReturPembelian.getRowCount(); i++) {
                int id = vm.getLatestId("id", "tb_retur_pembelian_detail") + i;
                String id_dp = vm.getDataByParameter("kode_barang = '" + tabelReturPembelian.getValueAt(i, 0).toString() + "' AND id_pembelian = '" + id_pembelian + "'", "tb_pembelian_detail", "id");
                ReturPembelianDetail rpd = new ReturPembelianDetail();
                rpd.setId(String.valueOf(id));
                rpd.setId_retur_pembelian(id_retur_pembelian);
                rpd.setId_pembelian_detail(id_dp);
                rpd.setKode_barang(tabelReturPembelian.getValueAt(i, 0).toString());
                rpd.setNama_barang(tabelReturPembelian.getValueAt(i, 1).toString());
                rpd.setJumlah(tabelReturPembelian.getValueAt(i, 2).toString());
                rpd.setSatuan(tabelReturPembelian.getValueAt(i, 3).toString());
                rpd.setHarga_beli(tabelReturPembelian.getValueAt(i, 4).toString());
                rpd.setTotal(tabelReturPembelian.getValueAt(i, 5).toString());
                detail_retur.add(rpd);
            }
            int status = returPembelianDAO.insertReturPembelian(data, detail_retur);
            if (status > 0) {
                JOptionPane.showMessageDialog(null, "Sukses memasukkan retur pembelian", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                resetAll();
            } else {
                JOptionPane.showMessageDialog(null, "Terjadi Kesalahan", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean verifyFormFilled(){
        return noFaktur.getSelectedItem() == null || tabelReturPembelian.getRowCount() == 0 || totalBayar.getText().equals("");
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
        jLabel2 = new javax.swing.JLabel();
        tanggalReturBeli = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        noRetur = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelPembelian = new javax.swing.JTable();
        inputBarcode = new javax.swing.JTextField();
        inputNama = new javax.swing.JTextField();
        inputJumlah = new javax.swing.JTextField();
        inputSatuan = new javax.swing.JTextField();
        inputTotal = new javax.swing.JTextField();
        inputHargaBersih = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelReturPembelian = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        totalRetur = new javax.swing.JTextField();
        totalBayar = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JButton();
        btnBaru = new javax.swing.JButton();
        noFaktur = new javax.swing.JComboBox<>();
        totalKurangUtang = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        kodeSupplier = new javax.swing.JLabel();
        namaSupplier = new javax.swing.JLabel();
        teleponSupplier = new javax.swing.JLabel();
        alamatSupplier = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 135, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("RETUR PEMBELIAN");

        jLabel1.setText("Tanggal");

        jLabel3.setText("No. Faktur");

        noRetur.setEditable(false);

        jLabel4.setText("No. Retur");

        tabelPembelian.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelPembelian.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelPembelianMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelPembelian);

        inputBarcode.setEditable(false);

        inputNama.setEditable(false);

        inputJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputJumlahKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputJumlahKeyReleased(evt);
            }
        });

        inputSatuan.setEditable(false);

        inputTotal.setEditable(false);

        inputHargaBersih.setEditable(false);

        jLabel5.setText("Kode/Barcode");

        jLabel6.setText("Nama");

        jLabel7.setText("Jumlah");

        jLabel8.setText("Satuan");

        jLabel11.setText("Harga Bersih");

        jLabel12.setText("Total");

        tabelReturPembelian.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode", "Nama", "Jumlah", "Satuan", "Harga Beli", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tabelReturPembelian);

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

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnBaru.setText("Baru");
        btnBaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruActionPerformed(evt);
            }
        });

        noFaktur.setEditable(true);
        noFaktur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noFakturActionPerformed(evt);
            }
        });

        totalKurangUtang.setEditable(false);

        jLabel15.setText("Total Mengurangi Utang");

        jPanel2.setBackground(new java.awt.Color(0, 135, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel9.setText("DATA SUPPLIER");

        jLabel10.setText("Kode");

        jLabel16.setText("Nama");

        jLabel17.setText("Alamat");

        jLabel18.setText("Telepon");

        kodeSupplier.setText("XXXX");

        namaSupplier.setText("XXXX");

        teleponSupplier.setText("XXXX");

        alamatSupplier.setText("XXXX");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel16))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kodeSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                            .addComponent(namaSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alamatSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(teleponSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(kodeSupplier))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(namaSupplier))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(alamatSupplier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(teleponSupplier)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(totalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalRetur, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(inputBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(44, 44, 44)
                                .addComponent(jLabel8)
                                .addGap(46, 46, 46))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(inputJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(inputHargaBersih, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel12))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(totalKurangUtang, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(btnSimpan)
                            .addGap(233, 233, 233)
                            .addComponent(btnBaru))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel4)
                                .addComponent(jLabel3))
                            .addGap(22, 22, 22)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tanggalReturBeli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(noRetur)
                                .addComponent(noFaktur, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 682, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(tanggalReturBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(noRetur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(noFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel11)
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
                    .addComponent(totalKurangUtang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnBaru))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaruActionPerformed
        // TODO add your handling code here:
        resetAll();
    }//GEN-LAST:event_btnBaruActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        if (verifyFormFilled()) {
            JOptionPane.showMessageDialog(null, "Harap masukkan data dengan benar!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        } else {
            doReturPembelian();
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void noFakturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noFakturActionPerformed
        // TODO add your handling code here:
        if (noFaktur.getSelectedItem() != null) {
            try {
                String no_faktur = noFaktur.getSelectedItem().toString();
                pembelianDetail = pembelianDAO.getListPembelianDetail(no_faktur);
                tabelPembelian.setModel(new DetailPembelianDataTable(pembelianDetail));
                model.setRowCount(0);
                vp = pembelianDAO.getDataPembelian(no_faktur);
                jLabel15.setVisible(!vp.getTunai_kredit().equals("TUNAI"));
                totalKurangUtang.setVisible(!vp.getTunai_kredit().equals("TUNAI"));
                Supplier sup = supplierDAO.getSupplier(vp.getKode_supplier());
                kodeSupplier.setText(vp.getKode_supplier());
                namaSupplier.setText(vp.getNama_supplier());
                alamatSupplier.setText(sup.getAlamat());
                teleponSupplier.setText(sup.getTelepon());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_noFakturActionPerformed

    private void tabelPembelianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelPembelianMouseClicked
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
    }//GEN-LAST:event_tabelPembelianMouseClicked

    private void inputJumlahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputJumlahKeyPressed
        // TODO add your handling code here:
        inputJumlah.setEditable((evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9' || evt.getKeyCode() == 8));
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (inputBarcode.getText().equals("") || inputJumlah.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Masukkan data dengan benar!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            } else {
                String kode_barang = inputBarcode.getText();
                int rowIndex = getRowByValue(tabelPembelian, kode_barang, 0);
                int actual_qty = Integer.parseInt(tabelPembelian.getValueAt(rowIndex, 2).toString());
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
            if (vp != null && !vp.getTunai_kredit().equals("TUNAI")) totalKurangUtang.setText(String.valueOf(total_retur - total_bayar));
        } else {
            if (vp != null && !vp.getTunai_kredit().equals("TUNAI")) totalKurangUtang.setText(String.valueOf(total_retur));
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
            java.util.logging.Logger.getLogger(ReturPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReturPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReturPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReturPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReturPembelian().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel alamatSupplier;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel kodeSupplier;
    private javax.swing.JLabel namaSupplier;
    private javax.swing.JComboBox<String> noFaktur;
    private javax.swing.JTextField noRetur;
    private javax.swing.JTable tabelPembelian;
    private javax.swing.JTable tabelReturPembelian;
    private com.toedter.calendar.JDateChooser tanggalReturBeli;
    private javax.swing.JLabel teleponSupplier;
    private javax.swing.JTextField totalBayar;
    private javax.swing.JTextField totalKurangUtang;
    private javax.swing.JTextField totalRetur;
    // End of variables declaration//GEN-END:variables
}
