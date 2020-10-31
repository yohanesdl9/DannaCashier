/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penjualan.pembelian;

import com.sun.glass.events.KeyEvent;
import dao.BarangDAO;
import dao.PembelianDAO;
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
import model.Barang;
import model.PembelianDetail;
import penjualan.CustomCombo;
import penjualan.ViewModel;
import static penjualan.pembelian.Pembeliantunai_frame.getDate;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class PembelianTunai extends javax.swing.JFrame {

    /**
     * Creates new form PembelianTunai
     */
    
    String hari;
    int id;
    int totalPembelian = 0;
    DefaultTableModel model;
    Calendar cal = Calendar.getInstance();
    ViewModel vm = new ViewModel();
    PembelianDAO pembelianDAO = PembelianDAO.getInstance();
    
    public PembelianTunai() {
        initComponents();
        fillDataBarang(inputBarcode);
        initDropdown(pilihSupplier, "tb_supplier", "nama");
        model = (DefaultTableModel) tblPembelian.getModel();
        try {
            id = vm.getLatestId("id", "tb_pembelian");
            cal.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            tanggalBeli.setDate(cal.getTime());
            jatuhTempo.setText(new SimpleDateFormat("MMM dd, YYYY").format(tanggalBeli.getDate()));
            noFaktur.setText(vm.getLatestIdPembelian());
        } catch (Exception e){
            e.printStackTrace();
        }
        jLabel5.setVisible(false);
        jLabel6.setVisible(false);
        jumlahHari.setVisible(false);
        jatuhTempo.setVisible(false);
        jLabel19.setVisible(true);
        jLabel20.setVisible(true);
        tunai.setVisible(true);
        kembalian.setVisible(true);
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
    
    public void addToCart(){
        try {
            String barcode = inputBarcode.getSelectedItem().toString();
            String jumlah = inputJumlah.getText();
            String harga_beli = inputHargaBeli.getText();
            String nama = inputNama.getText();
            String satuan = inputSatuan.getText();
            String total = inputTotal.getText();
            String harga_jual = vm.getDataByParameter("kode = '" + barcode + "'", "tb_barang", "harga_jual");

            Object[] data = {barcode, nama, jumlah, satuan, harga_beli, total, harga_jual};
            model.addRow(data);

            totalPembelian += Integer.parseInt(total);
            subtotal.setText(Integer.toString(totalPembelian));

            if (diskonPersen.getText().equals("")) {
                grandtotal.setText(Integer.toString(totalPembelian));
            } else {
                diskonNominal.setText(Integer.toString(totalPembelian * Integer.parseInt(diskonPersen.getText()) / 100));
                int grandTotal = totalPembelian - Integer.parseInt(diskonNominal.getText());
                grandtotal.setText(Integer.toString(grandTotal));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        inputBarcode.setSelectedItem(null);
        inputJumlah.setText("");
        inputHargaBeli.setText("");
        inputNama.setText("");
        inputSatuan.setText("");
        inputTotal.setText("");
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
        inputJumlah.setText("");
        inputHargaBeli.setText("");
        inputNama.setText("");
        inputSatuan.setText("");
        inputTotal.setText("");
    }
    
    public void adjustTableColumnWidth(JTable table, int[] columnSizes) {
        for (int i = 0; i < columnSizes.length; i++){
            table.getColumnModel().getColumn(i).setPreferredWidth(columnSizes[i]);
        }
    }
    
    public void resetAll(){
        subtotal.setText("");
        diskonPersen.setText("");
        diskonNominal.setText("");
        metodeBayar.setSelectedItem("TUNAI");
        grandtotal.setText("");
        tunai.setText("");
        kembalian.setText("");
        model.setRowCount(0);
        try {
            id = vm.getLatestId("id", "tb_penjualan");
            noFaktur.setText(vm.getLatestIdPembelian());
        } catch (Exception e) {
            e.printStackTrace();
        }
        jLabel5.setVisible(false);
        jLabel6.setVisible(false);
        jumlahHari.setVisible(false);
        jatuhTempo.setVisible(false);
        jLabel19.setVisible(true);
        jLabel20.setVisible(true);
        tunai.setVisible(true);
        kembalian.setVisible(true);
    }

    public void hitungKembalian() {
        if (grandtotal.getText().length() > 0) {
            if (tunai.getText().length() > 0) {
                int grand_total = Integer.parseInt(grandtotal.getText());
                int uang_tunai = Integer.parseInt(tunai.getText());
                kembalian.setText(String.valueOf(uang_tunai - grand_total));
            } else {
                kembalian.setText("");
            }
        }
    }
    
    public boolean validateFormFilled(){
        boolean is_invalid = false;
        // First, check if supplier code has been filled
        is_invalid = pilihSupplier.getSelectedIndex() == 0;
        // Second, if user choose custom payment method, check if days has been filled
        if (metodeBayar.getSelectedItem().toString().equals("Custom")) {
            is_invalid = jumlahHari.getText().toString().equals("");
        }
        return is_invalid;
    }
    
    public void checkout(){
        try {
            boolean isTunai = metodeBayar.getSelectedItem().toString().equals("TUNAI");
            String[] data = {
                String.valueOf(id),
                new SimpleDateFormat("yyyy-MM-dd").format(tanggalBeli.getDate()),
                noFaktur.getText(),
                metodeBayar.getSelectedItem().toString(),
                isTunai ? null : hari,
                isTunai ? new SimpleDateFormat("yyyy-MM-dd").format(tanggalBeli.getDate()) : jatuhTempo.getText(),
                vm.getDataByParameter("nama = '" + pilihSupplier.getSelectedItem().toString() + "'", "tb_supplier", "id"),
                subtotal.getText(),
                diskonPersen.getText().length() > 0 ? diskonPersen.getText() : "0" ,
                diskonNominal.getText().length() > 0 ? diskonNominal.getText() : "0",
                grandtotal.getText(),
                isTunai ? tunai.getText() : null,
                isTunai ? kembalian.getText() : null
            };
            ArrayList<PembelianDetail> detail_beli = new ArrayList<>();
            int id_detail = vm.getLatestId("id", "tb_pembelian_detail");
            for (int i = 0; i < tblPembelian.getRowCount(); i++){
                PembelianDetail pd = new PembelianDetail();
                pd.setId(String.valueOf(id_detail + i));
                pd.setId_pembelian(String.valueOf(id));
                pd.setId_barang(vm.getDataByParameter("kode = '" + tblPembelian.getValueAt(i, 0).toString() + "'", "tb_barang", "id"));
                pd.setKode_barang(tblPembelian.getValueAt(i, 0).toString());
                pd.setNama_barang(tblPembelian.getValueAt(i, 1).toString());
                pd.setJumlah(tblPembelian.getValueAt(i, 2).toString());
                pd.setSatuan(tblPembelian.getValueAt(i, 3).toString());
                pd.setHarga_beli(tblPembelian.getValueAt(i, 4).toString());
                pd.setTotal(tblPembelian.getValueAt(i, 5).toString());
                pd.setHarga_jual(tblPembelian.getValueAt(i, 6).toString());
                detail_beli.add(pd);
            }
            int status = pembelianDAO.insertPembelian(data, detail_beli);
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
        jLabel2 = new javax.swing.JLabel();
        tanggalBeli = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        noFaktur = new javax.swing.JTextField();
        metodeBayar = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jumlahHari = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jatuhTempo = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        inputJumlah = new javax.swing.JTextField();
        inputHargaBeli = new javax.swing.JTextField();
        inputNama = new javax.swing.JTextField();
        inputSatuan = new javax.swing.JTextField();
        inputTotal = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPembelian = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        subtotal = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        diskonPersen = new javax.swing.JTextField();
        diskonNominal = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        grandtotal = new javax.swing.JTextField();
        tunai = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        kembalian = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnBaru = new javax.swing.JButton();
        inputBarcode = new javax.swing.JComboBox<>();
        pilihSupplier = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 135, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("PEMBELIAN TUNAI");

        jLabel1.setText("Tanggal");

        jLabel3.setText("No. Faktur");

        noFaktur.setEditable(false);

        metodeBayar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TUNAI", "1 Minggu", "2 Minggu", "3 Minggu", "4 Minggu", "Custom" }));
        metodeBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metodeBayarActionPerformed(evt);
            }
        });

        jLabel4.setText("Tunai/Kredit");

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

        jLabel6.setText("Jatuh Tempo");

        jatuhTempo.setText("24 Oktober 2020");

        jLabel8.setText("Kode Supplier");

        inputJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputJumlahKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputJumlahKeyReleased(evt);
            }
        });

        inputHargaBeli.setEditable(false);

        inputNama.setEditable(false);

        inputSatuan.setEditable(false);

        inputTotal.setEditable(false);

        jLabel9.setText("Kode/Barcode");

        jLabel10.setText("Jumlah");

        jLabel11.setText("Harga Beli");

        jLabel12.setText("Nama");

        jLabel13.setText("Satuan");

        jLabel14.setText("Total");

        tblPembelian.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode", "Nama", "Jumlah", "Satuan", "Harga Beli", "Total", "Harga Jual"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblPembelian);

        jLabel16.setText("Subtotal");

        subtotal.setEditable(false);

        jLabel17.setText("Diskon (%)");

        diskonPersen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                diskonPersenKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                diskonPersenKeyReleased(evt);
            }
        });

        diskonNominal.setEditable(false);

        jLabel18.setText("Grand Total");

        grandtotal.setEditable(false);

        tunai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tunaiKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tunaiKeyReleased(evt);
            }
        });

        jLabel19.setText("Tunai");

        kembalian.setEditable(false);

        jLabel20.setText("Kembalian");

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnPrint.setText("Cetak");

        btnBaru.setText("Baru");
        btnBaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruActionPerformed(evt);
            }
        });

        inputBarcode.setEditable(true);
        inputBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputBarcodeActionPerformed(evt);
            }
        });

        pilihSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Supplier" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(jatuhTempo)
                        .addGap(545, 547, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(tunai, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(grandtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(diskonPersen, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(diskonNominal, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(74, 74, 74)
                                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(173, 173, 173)
                                .addComponent(btnBaru, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel9)
                                                        .addComponent(inputBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel10)
                                                        .addComponent(inputJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(inputHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel11)))
                                                .addComponent(jLabel1))
                                            .addGap(4, 4, 4)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(jLabel8)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pilihSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel12))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(inputSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel13))))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel14)
                                                .addComponent(inputTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel3)
                                                .addComponent(jLabel4))
                                            .addGap(22, 22, 22)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(tanggalBeli, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                                                .addComponent(noFaktur, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(metodeBayar, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jumlahHari, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel5))))
                                .addGap(0, 4, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tanggalBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(pilihSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(noFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(metodeBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jumlahHari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jatuhTempo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(diskonPersen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(diskonNominal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(grandtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(tunai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(btnSimpan)
                    .addComponent(btnBaru)
                    .addComponent(btnPrint))
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

    private void btnBaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaruActionPerformed
        // TODO add your handling code here:
        resetAll();
    }//GEN-LAST:event_btnBaruActionPerformed

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
                inputHargaBeli.setText(barang.getHarga_beli());
                inputNama.setText(barang.getNama());
                inputSatuan.setText(barang.getId_satuan());
                inputTotal.setText(barang.getHarga_beli());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }//GEN-LAST:event_inputBarcodeActionPerformed

    private void inputJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputJumlahKeyReleased
        // TODO add your handling code here:
        if (inputJumlah.getText().length() > 0) {
            int jumlah = Integer.parseInt(inputJumlah.getText());
            int hargaBeli = Integer.parseInt(inputHargaBeli.getText());
            int total = jumlah * hargaBeli;
            inputTotal.setText(String.valueOf(total));
        } else {
            inputTotal.setText(inputHargaBeli.getText());
        }
    }//GEN-LAST:event_inputJumlahKeyReleased

    private void diskonPersenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_diskonPersenKeyPressed
        // TODO add your handling code here:
        diskonPersen.setEditable((evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9' || evt.getKeyCode() == 8));
    }//GEN-LAST:event_diskonPersenKeyPressed

    private void tunaiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tunaiKeyPressed
        // TODO add your handling code here:
        tunai.setEditable((evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9' || evt.getKeyCode() == 8));
    }//GEN-LAST:event_tunaiKeyPressed

    private void jumlahHariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahHariKeyReleased
        // TODO add your handling code here:
        Date date = tanggalBeli.getDate();
        if (jumlahHari.getText().length() > 0) {
            int jumlah_hari = Integer.parseInt(jumlahHari.getText());
            cal.setTime(date);
            cal.add(Calendar.DATE, jumlah_hari);
            hari = jumlahHari.getText();
            jatuhTempo.setText(new SimpleDateFormat("MMM dd, YYYY").format(cal.getTime()));
        } else {
            hari = null;
            jatuhTempo.setText(new SimpleDateFormat("MMM dd, YYYY").format(date));
        }
    }//GEN-LAST:event_jumlahHariKeyReleased

    private void jumlahHariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahHariKeyPressed
        // TODO add your handling code here:
        jumlahHari.setEditable((evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9' || evt.getKeyCode() == 8));
    }//GEN-LAST:event_jumlahHariKeyPressed

    private void diskonPersenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_diskonPersenKeyReleased
        // TODO add your handling code here:
        if (diskonPersen.getText().length() > 0) {
            int diskon = Integer.parseInt(diskonPersen.getText());
            if (diskon > 100 && diskon < 0) {
                int sub_total = Integer.parseInt(subtotal.getText());
                int diskon_nominal = sub_total * (100 - diskon) / 100;
                diskonNominal.setText(String.valueOf(diskon_nominal));
                grandtotal.setText(String.valueOf(sub_total - diskon_nominal));
            } else {
                JOptionPane.showMessageDialog(null, "Diskon harus berada di rentang 0-100%", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            grandtotal.setText(subtotal.getText());
        }
    }//GEN-LAST:event_diskonPersenKeyReleased

    private void tunaiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tunaiKeyReleased
        // TODO add your handling code here:
        hitungKembalian();
    }//GEN-LAST:event_tunaiKeyReleased

    private void metodeBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metodeBayarActionPerformed
        // TODO add your handling code here:
        Date date = tanggalBeli.getDate();
        cal.setTime(date);
        String metode = metodeBayar.getSelectedItem().toString();
        jLabel19.setVisible(metode.equals("TUNAI"));
        jLabel20.setVisible(metode.equals("TUNAI"));
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
                hari = "14";
                cal.add(Calendar.DATE, 14);
            } else if (metode.equals("3 Minggu")) {
                hari = "21";
                cal.add(Calendar.DATE, 21);
            } else if (metode.equals("4 Minggu")) {
                hari = "28";
                cal.add(Calendar.DATE, 28);
            }
            jatuhTempo.setText(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
        }
    }//GEN-LAST:event_metodeBayarActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        if (tblPembelian.getRowCount() > 0) {            
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

    }//GEN-LAST:event_btnSimpanActionPerformed

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
            java.util.logging.Logger.getLogger(PembelianTunai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PembelianTunai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PembelianTunai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PembelianTunai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PembelianTunai().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBaru;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JTextField diskonNominal;
    private javax.swing.JTextField diskonPersen;
    private javax.swing.JTextField grandtotal;
    private javax.swing.JComboBox<String> inputBarcode;
    private javax.swing.JTextField inputHargaBeli;
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
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jatuhTempo;
    private javax.swing.JTextField jumlahHari;
    private javax.swing.JTextField kembalian;
    private javax.swing.JComboBox<String> metodeBayar;
    private javax.swing.JTextField noFaktur;
    private javax.swing.JComboBox<String> pilihSupplier;
    private javax.swing.JTextField subtotal;
    private com.toedter.calendar.JDateChooser tanggalBeli;
    private javax.swing.JTable tblPembelian;
    private javax.swing.JTextField tunai;
    // End of variables declaration//GEN-END:variables
}
