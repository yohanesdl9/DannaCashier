/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penjualan.penjualan;

import com.sun.glass.events.KeyEvent;
import dao.BarangDAO;
import dao.PenjualanDAO;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.Barang;
import model.PenjualanDetail;
import penjualan.CustomCombo;
import penjualan.ViewModel;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class PenjualanTunai extends javax.swing.JFrame {

    /**
     * Creates new form PenjualanTunai
     */
    
    String hari;
    int id;
    ViewModel vm = new ViewModel();
    Calendar cal = Calendar.getInstance();
    DefaultTableModel model;
    int totalPenjualan = 0;
    String faktur;
    PenjualanDAO penjualanDAO = PenjualanDAO.getInstance();
    
    public PenjualanTunai() {
        initComponents();
        fillDataBarang(inputBarcode);
        initDropdown(selectPelanggan, "tb_pelanggan", "nama");
        initDropdown(selectSales, "tb_sales", "nama");
        model = (DefaultTableModel) tblPenjualan.getModel();
        try {
            id = vm.getLatestId("id", "tb_penjualan");
            cal.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            tanggalJual.setDate(cal.getTime());
            jatuhTempo.setText(new SimpleDateFormat("MMM dd, YYYY").format(tanggalJual.getDate()));
            faktur = vm.getLatestIdPenjualan();
        } catch (Exception e){
            e.printStackTrace();
        }
        jLabel5.setVisible(false);
        jLabel6.setVisible(false);
        jumlahHari.setVisible(false);
        jatuhTempo.setVisible(false);
        jLabel20.setVisible(true);
        jLabel21.setVisible(true);
        tunai.setVisible(true);
        kembalian.setVisible(true);
    }
    
    public boolean validateFormFilled(){
        boolean is_invalid = false;
        // First, check if supplier code has been filled
        is_invalid = selectPelanggan.getSelectedIndex() == 0 || selectSales.getSelectedIndex() == 0;
        // Second, if user choose custom payment method, check if days has been filled
        if (metodeBayar.getSelectedItem().toString().equals("Custom")) {
            is_invalid = jumlahHari.getText().toString().equals("");
        }
        return is_invalid;
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
    
    public void fillDataBarang(JComboBox combo) {
        try {
            combo.removeAllItems();
            ResultSet rs = vm.getAllDataFromTable("tb_barang");
            while (rs.next()) {
                new CustomCombo(combo).custom(combo, rs.getString("kode"));
                combo.setSelectedItem(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        inputDiskon.setText("");
        inputNama.setText("");
        inputJumlah.setText("");
        inputSatuan.setText("");
        inputHargaJual.setText("");
        inputNominalDiskon.setText("");
        inputHargaBersih.setText("");
        inputTotal.setText("");
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
    
    public void addToCart(){
        String jbkb = inputBarcode.getSelectedItem().toString();
        String nama = inputNama.getText();
        String jumlah = inputJumlah.getText();
        String satuan = inputSatuan.getText();
        String hargaJual = inputHargaJual.getText();
        String disc = inputDiskon.getText();
        String hargaBersih = inputHargaBersih.getText();
        String total = inputTotal.getText();

        if (getRowByValue(tblPenjualan, jbkb, 0) > -1) {
            int row = getRowByValue(tblPenjualan, jbkb, 0);
            int current_qty = Integer.parseInt(tblPenjualan.getValueAt(row, 2).toString());
            int current_total = Integer.parseInt(tblPenjualan.getValueAt(row, 7).toString());
            current_qty += Integer.parseInt(jumlah);
            tblPenjualan.setValueAt(String.valueOf(current_qty), row, 2);
            int total_new = current_qty * Integer.parseInt(tblPenjualan.getValueAt(row, 6).toString());
            tblPenjualan.setValueAt(String.valueOf(total_new), row, 7);
            totalPenjualan += (total_new - current_total);
        } else {
            Object[] data = {jbkb, nama, jumlah, satuan, hargaJual, disc, hargaBersih, total};
            model.addRow(data);
            totalPenjualan += Integer.parseInt(total);
        }
        inputSubTotal.setText(Integer.toString(totalPenjualan));
        if (inputNominalDiskon.getText().equals("")) {
            inputGrandTotal.setText(Integer.toString(totalPenjualan));
        } else {
            int grandTotal = totalPenjualan * Integer.parseInt(inputNominalDiskon.getText());
            inputGrandTotal.setText(Integer.toString(grandTotal));
        }
            
        inputBarcode.setSelectedItem(null);
        inputDiskon.setText("");
        inputNama.setText("");
        inputJumlah.setText("");
        inputSatuan.setText("");
        inputHargaJual.setText("");
        inputNominalDiskon.setText("");
        inputHargaBersih.setText("");
        inputTotal.setText("");
    }
    
    public void adjustTableColumnWidth(JTable table, int[] columnSizes) {
        for (int i = 0; i < columnSizes.length; i++){
            table.getColumnModel().getColumn(i).setPreferredWidth(columnSizes[i]);
        }
    }
    
    public void resetAll(){
        totalPenjualan = 0;
        inputSubTotal.setText("");
        inputPembulatan.setText("");
        inputNominalDiskon.setText("");
        metodeBayar.setSelectedItem("TUNAI");
        inputGrandTotal.setText("");
        tunai.setText("");
        kembalian.setText("");
        model.setRowCount(0);
        try {
            id = vm.getLatestId("id", "tb_penjualan");
            faktur = vm.getLatestIdPenjualan();
        } catch (Exception e) {
            e.printStackTrace();
        }
        jLabel5.setVisible(false);
        jLabel6.setVisible(false);
        jumlahHari.setVisible(false);
        jatuhTempo.setVisible(false);
        jLabel20.setVisible(true);
        jLabel21.setVisible(true);
        tunai.setVisible(true);
        kembalian.setVisible(true);
    }

    public void hitungKembalian() {
        if (inputGrandTotal.getText().length() > 0) {
            if (tunai.getText().length() > 0) {
                int grand_total = Integer.parseInt(inputGrandTotal.getText());
                int uang_tunai = Integer.parseInt(tunai.getText());
                kembalian.setText(String.valueOf(uang_tunai - grand_total));
            } else {
                kembalian.setText("");
            }
        }
    }
    
    public void checkout(){
        try {
            boolean isTunai = metodeBayar.getSelectedItem().toString().equals("TUNAI");
            String[] data = {
                String.valueOf(id),
                faktur,
                new SimpleDateFormat("yyyy-MM-dd").format(tanggalJual.getDate()),
                metodeBayar.getSelectedItem().toString(),
                isTunai ? null : hari,
                isTunai ? new SimpleDateFormat("yyyy-MM-dd").format(tanggalJual.getDate()) : jatuhTempo.getText(),
                vm.getDataByParameter("nama = '" + selectPelanggan.getSelectedItem().toString() + "'", "tb_pelanggan", "id"),
                vm.getDataByParameter("nama = '" + selectSales.getSelectedItem().toString() + "'", "tb_sales", "id"),
                inputSubTotal.getText(),
                inputPembulatan.getText().length() > 0 ? inputPembulatan.getText() : "0",
                inputDiskon.getText().length() > 0 ? inputDiskon.getText() : "0",
                inputGrandTotal.getText(),
                isTunai ? tunai.getText() : null,
                isTunai ? kembalian.getText() : null
            };
            ArrayList<PenjualanDetail> detail_jual = new ArrayList<>(); 
            int id_detail = vm.getLatestId("id", "tb_penjualan_detail");
            for (int i = 0; i < tblPenjualan.getRowCount(); i++) {
                PenjualanDetail pd = new PenjualanDetail();
                pd.setId(String.valueOf(id_detail + i));
                pd.setId_penjualan(String.valueOf(id));
                pd.setId_barang(vm.getDataByParameter("kode = '" + tblPenjualan.getValueAt(i, 0).toString() + "'", "tb_barang", "id"));
                pd.setKode_barang(tblPenjualan.getValueAt(i, 0).toString());
                pd.setNama_barang(tblPenjualan.getValueAt(i, 1).toString());
                pd.setJumlah(tblPenjualan.getValueAt(i, 2).toString());
                pd.setSatuan(tblPenjualan.getValueAt(i, 3).toString());
                pd.setHarga_jual(tblPenjualan.getValueAt(i, 4).toString());
                pd.setDiskon(tblPenjualan.getValueAt(i, 5).toString());
                pd.setHarga_bersih(tblPenjualan.getValueAt(i, 6).toString());
                pd.setTotal(tblPenjualan.getValueAt(i, 7).toString());
                detail_jual.add(pd);
            }
            int status = penjualanDAO.insertPenjualan(data, detail_jual);
            if (status > 0) {
                JOptionPane.showMessageDialog(null, "Transaksi berhasil diinputkan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                resetAll();
            } else {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan yang tidak diketahui", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        tanggalJual = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        metodeBayar = new javax.swing.JComboBox<>();
        jumlahHari = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jatuhTempo = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        selectPelanggan = new javax.swing.JComboBox<>();
        inputNama = new javax.swing.JTextField();
        inputJumlah = new javax.swing.JTextField();
        inputSatuan = new javax.swing.JTextField();
        inputHargaJual = new javax.swing.JTextField();
        inputDiskon = new javax.swing.JTextField();
        inputHargaBersih = new javax.swing.JTextField();
        inputTotal = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPenjualan = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        inputSubTotal = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        inputPembulatan = new javax.swing.JTextField();
        inputNominalDiskon = new javax.swing.JTextField();
        inputGrandTotal = new javax.swing.JTextField();
        tunai = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        kembalian = new javax.swing.JTextField();
        inputBarcode = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        selectSales = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 135, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PENJUALAN TUNAI");

        jLabel2.setText("Tanggal");

        jLabel4.setText("Tunai/Kredit");

        metodeBayar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TUNAI", "1 Minggu", "2 Minggu", "3 Minggu", "4 Minggu", "Custom" }));
        metodeBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metodeBayarActionPerformed(evt);
            }
        });

        jumlahHari.setEditable(false);
        jumlahHari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jumlahHariKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jumlahHariKeyReleased(evt);
            }
        });

        jLabel5.setText("Hari");

        jatuhTempo.setText("24 Oktober 2020");

        jLabel6.setText("Jatuh Tempo");

        jLabel3.setText("Pelanggan");

        selectPelanggan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Pelanggan" }));

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

        inputHargaJual.setEditable(false);

        inputDiskon.setEditable(false);

        inputHargaBersih.setEditable(false);

        inputTotal.setEditable(false);

        jLabel8.setText("Kode Barang");

        jLabel9.setText("Nama");

        jLabel10.setText("Jumlah");

        jLabel11.setText("Satuan");

        jLabel12.setText("Harga Jual");

        jLabel13.setText("Diskon");

        jLabel14.setText("Harga Bersih");

        jLabel17.setText("Total");

        tblPenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode", "Nama", "Jumlah", "Satuan", "Harga", "Diskon", "Netto", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblPenjualan);

        jLabel18.setText("Diskon");

        jLabel15.setText("Subtotal");

        inputSubTotal.setEditable(false);

        jLabel16.setText("Pembulatan");

        jLabel19.setText("Grand Total");

        jLabel20.setText("Tunai");

        jLabel21.setText("Kembalian");

        inputPembulatan.setEditable(false);
        inputPembulatan.setText("0");

        inputNominalDiskon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputNominalDiskonKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputNominalDiskonKeyReleased(evt);
            }
        });

        inputGrandTotal.setEditable(false);

        tunai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tunaiKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tunaiKeyReleased(evt);
            }
        });

        btnSave.setText("Simpan");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnNew.setText("Baru");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        btnNew.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnNewKeyPressed(evt);
            }
        });

        kembalian.setEditable(false);

        inputBarcode.setEditable(true);
        inputBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputBarcodeActionPerformed(evt);
            }
        });

        jLabel7.setText("Sales");

        selectSales.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Sales" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21))
                        .addGap(52, 52, 52)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inputGrandTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addComponent(tunai)
                            .addComponent(kembalian))
                        .addGap(376, 376, 376)
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNew))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(selectPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(selectSales, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel15)
                                    .addGap(60, 60, 60)
                                    .addComponent(inputSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel19)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel16)
                                    .addGap(44, 44, 44)
                                    .addComponent(inputPembulatan, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel18)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(inputNominalDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addGap(18, 18, 18)
                                    .addComponent(jatuhTempo))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel2))
                                    .addGap(22, 22, 22)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(metodeBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jumlahHari, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(tanggalJual, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel5))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(inputBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addGap(47, 47, 47)
                                            .addComponent(jLabel9)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(inputJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel10))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(inputSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel12)
                                        .addComponent(inputHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGap(9, 9, 9)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(inputDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(inputHargaBersih, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel17)
                                .addComponent(inputTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(tanggalJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(metodeBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jumlahHari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(27, 27, 27))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addGap(27, 27, 27))
                                .addComponent(selectSales, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(selectPelanggan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jatuhTempo))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12)
                    .addComponent(jLabel17)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputHargaBersih, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(inputSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputNominalDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(inputPembulatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(inputGrandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tunai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNew)
                        .addComponent(btnSave))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(4, 4, 4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        resetAll();
    }//GEN-LAST:event_btnNewKeyPressed

    private void inputJumlahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputJumlahKeyPressed
        // TODO add your handling code here:
        inputJumlah.setEditable((evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9' || evt.getKeyCode() == 8));
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (inputBarcode.getSelectedItem().equals("") || inputJumlah.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Masukkan data dengan benar!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            } else {
                addToCart();
            }
        }
    }//GEN-LAST:event_inputJumlahKeyPressed

    private void inputBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputBarcodeActionPerformed
        // TODO add your handling code here:
        if (inputBarcode.getSelectedItem() != null){
            try {
                Barang barang = BarangDAO.getInstance().getBarang("tb.kode = '" + inputBarcode.getSelectedItem().toString() + "'");
                inputHargaJual.setText(barang.getHarga_jual());
                inputNama.setText(barang.getNama());
                inputSatuan.setText(barang.getId_satuan());
                if (barang.getDiskon_nominal() == null) {
                    inputDiskon.setText("0");
                    inputHargaBersih.setText(barang.getHarga_jual());
                    inputTotal.setText(barang.getHarga_jual());
                } else {
                    int diskon = Integer.parseInt(barang.getHarga_jual()) * (100 - Integer.parseInt(barang.getDiskon_nominal()));
                    inputDiskon.setText(barang.getDiskon_nominal());
                    inputHargaBersih.setText(String.valueOf(diskon));
                    inputTotal.setText(String.valueOf(diskon));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_inputBarcodeActionPerformed

    private void metodeBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metodeBayarActionPerformed
        // TODO add your handling code here:
        Date date = tanggalJual.getDate();
        cal.setTime(date);
        String metode = metodeBayar.getSelectedItem().toString();
        jLabel20.setVisible(metode.equals("TUNAI"));
        jLabel21.setVisible(metode.equals("TUNAI"));
        tunai.setVisible(metode.equals("TUNAI"));
        kembalian.setVisible(metode.equals("TUNAI"));
        jLabel6.setVisible(!metode.equals("TUNAI"));
        jatuhTempo.setVisible(!metode.equals("TUNAI"));
        jumlahHari.setVisible(metode.equals("Custom"));
        jumlahHari.setEditable(metode.equals("Custom"));
        jLabel5.setVisible(metode.equals("Custom"));
        if (metode.equals("TUNAI")) {
            jumlahHari.setText("");
            jatuhTempo.setText(new SimpleDateFormat("MMM dd, YYYY").format(date));
        } else {
            tunai.setText("");
            kembalian.setText("");
            if (metode.equals("1 Minggu")) {
                cal.add(Calendar.DATE, 7);
                hari = "7";
            } else if (metode.equals("2 Minggu")) {
                cal.add(Calendar.DATE, 14);
                hari = "14";
            } else if (metode.equals("3 Minggu")) {
                cal.add(Calendar.DATE, 21);
                hari = "21";
            } else if (metode.equals("4 Minggu")) {
                cal.add(Calendar.DATE, 28);
                hari = "28";
            } else if (metode.equals("1 Bulan")) {
                cal.add(Calendar.DATE, 30);
                hari = "30";
            }
            jatuhTempo.setText(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
        }
    }//GEN-LAST:event_metodeBayarActionPerformed

    private void jumlahHariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahHariKeyPressed
        // TODO add your handling code here:
        jumlahHari.setEditable((evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9' || evt.getKeyCode() == 8));
    }//GEN-LAST:event_jumlahHariKeyPressed

    private void jumlahHariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahHariKeyReleased
        // TODO add your handling code here:
        Date date = tanggalJual.getDate();
        if (jumlahHari.getText().length() > 0) {
            int jumlah_hari = Integer.parseInt(jumlahHari.getText());
            cal.setTime(date);
            cal.add(Calendar.DATE, jumlah_hari);
            hari = jumlahHari.getText();
            jatuhTempo.setText(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
        } else {
            jatuhTempo.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        }
    }//GEN-LAST:event_jumlahHariKeyReleased

    private void inputNominalDiskonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputNominalDiskonKeyPressed
        // TODO add your handling code here:
        inputNominalDiskon.setEditable((evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9' || evt.getKeyCode() == 8));
    }//GEN-LAST:event_inputNominalDiskonKeyPressed

    private void inputNominalDiskonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputNominalDiskonKeyReleased
        // TODO add your handling code here:
        if (inputNominalDiskon.getText().length() > 0) {
            int diskon = Integer.parseInt(inputNominalDiskon.getText());
            if (diskon < Integer.parseInt(inputSubTotal.getText())) {
                int sub_total = Integer.parseInt(inputSubTotal.getText());
                inputGrandTotal.setText(String.valueOf(sub_total - diskon)); 
            } else {
                JOptionPane.showMessageDialog(null, "Diskon tidak boleh melebihi subtotal", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            inputGrandTotal.setText(inputSubTotal.getText());
        }
    }//GEN-LAST:event_inputNominalDiskonKeyReleased

    private void tunaiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tunaiKeyPressed
        // TODO add your handling code here:
        tunai.setEditable((evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9' || evt.getKeyCode() == 8));
    }//GEN-LAST:event_tunaiKeyPressed

    private void tunaiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tunaiKeyReleased
        // TODO add your handling code here:
        hitungKembalian();
    }//GEN-LAST:event_tunaiKeyReleased

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (tblPenjualan.getRowCount() > 0) {
            if (validateFormFilled()) {
                JOptionPane.showMessageDialog(null, "Harap masukkan data dengan benar!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            } else {
                if (metodeBayar.getSelectedItem().toString().equals("TUNAI")) {
                    if (Integer.parseInt(kembalian.getText()) < 0 || kembalian.getText().length() == 0) {
                        JOptionPane.showMessageDialog(null, "Uang tunai kurang", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                    } else {
                        checkout();
                    }
                } else {
                    checkout();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Anda belum memilih barang satupun", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void inputJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputJumlahKeyReleased
        // TODO add your handling code here:
        if (inputJumlah.getText().length() > 0) {
            int jumlah = Integer.parseInt(inputJumlah.getText());
            int hargaBeli = Integer.parseInt(inputHargaJual.getText());
            int diskon = Integer.parseInt(inputDiskon.getText());
            int hargaBersih = hargaBeli * (100 - diskon) / 100;
            int total = jumlah * hargaBersih;
            inputTotal.setText(String.valueOf(total));
        } else {
            if (inputHargaJual.getText().length() > 0 && inputDiskon.getText().length() > 0) {
                int hargaBeli = Integer.parseInt(inputHargaJual.getText());
                int diskon = Integer.parseInt(inputDiskon.getText());
                int hargaBersih = hargaBeli * (100 - diskon) / 100;
                inputTotal.setText(String.valueOf(hargaBersih));
            }
        }
    }//GEN-LAST:event_inputJumlahKeyReleased

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        resetAll();
    }//GEN-LAST:event_btnNewActionPerformed

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
            java.util.logging.Logger.getLogger(PenjualanTunai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PenjualanTunai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PenjualanTunai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PenjualanTunai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PenjualanTunai().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> inputBarcode;
    private javax.swing.JTextField inputDiskon;
    private javax.swing.JTextField inputGrandTotal;
    private javax.swing.JTextField inputHargaBersih;
    private javax.swing.JTextField inputHargaJual;
    private javax.swing.JTextField inputJumlah;
    private javax.swing.JTextField inputNama;
    private javax.swing.JTextField inputNominalDiskon;
    private javax.swing.JTextField inputPembulatan;
    private javax.swing.JTextField inputSatuan;
    private javax.swing.JTextField inputSubTotal;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jatuhTempo;
    private javax.swing.JTextField jumlahHari;
    private javax.swing.JTextField kembalian;
    private javax.swing.JComboBox<String> metodeBayar;
    private javax.swing.JComboBox<String> selectPelanggan;
    private javax.swing.JComboBox<String> selectSales;
    private com.toedter.calendar.JDateChooser tanggalJual;
    private javax.swing.JTable tblPenjualan;
    private javax.swing.JTextField tunai;
    // End of variables declaration//GEN-END:variables
}
