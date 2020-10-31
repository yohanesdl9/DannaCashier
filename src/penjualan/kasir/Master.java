/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penjualan.kasir;

import dao.BarangDAO;
import dao.PelangganDAO;
import dao.SalesDAO;
import dao.SupplierDAO;
import datatable.BarangDataTable;
import datatable.PelangganDataTable;
import datatable.SalesDataTable;
import datatable.SupplierDataTable;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Barang;
import model.Pelanggan;
import model.Sales;
import model.Supplier;
import model.ViewBarang;
import penjualan.ViewModel;

/**
 *
 * @author Dandi
 */
public class Master extends javax.swing.JFrame {

    /**
     * Creates new form Master
     */
    ViewModel vm = new ViewModel();
    PelangganDAO pelangganDAO = PelangganDAO.getInstance();
    SupplierDAO supplierDAO = SupplierDAO.getInstance();
    SalesDAO salesDAO = SalesDAO.getInstance();
    BarangDAO barangDAO = BarangDAO.getInstance();
    List<Pelanggan> listPelanggan;
    List<Supplier> listSupplier;
    List<Sales> listSales;
    List<ViewBarang> listBarang;
    
    public Master() {
        initComponents();
        initDropdown(listKategori, "id_tipe = 1", "tb_general", "keterangan");
        initDropdown(listSatuan, "id_tipe = 2", "tb_general", "keterangan");
        initDropdown(listSuplier, "tb_supplier", "nama");
        initTabelSupplier();
        initTabelPelanggan();
        initTabelSales();
        initTabelBarang();
        initTabelHutang();
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
    
    public void initTabelHutang(){
        int[] size = {5, 80, 120, 60, 60, 60, 80};
        adjustTableColumnWidth(tabelHutang, size);
    }
    
    public void initTabelSupplier(){
        try {
            listSupplier = supplierDAO.getListSupplier();
            tabelSupplier.setModel(new SupplierDataTable(listSupplier));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        int[] size = {5, 40, 80, 120, 42, 50, 60, 55};
        adjustTableColumnWidth(tabelSupplier, size);
    }
    
    public void initTabelSales(){
        try {
            listSales = salesDAO.getListSales();
            tabelSales.setModel(new SalesDataTable(listSales));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        int[] size = {5, 40, 80, 120, 42, 50, 60, 55};
        adjustTableColumnWidth(tabelSales, size);
    }
    
    public void initTabelPelanggan(){
        try {
            listPelanggan = pelangganDAO.getListPelanggan();
            tabelPelanggan.setModel(new PelangganDataTable(listPelanggan));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        int[] size = {5, 40, 100, 150, 80, 82};
        adjustTableColumnWidth(tabelPelanggan, size);
    }
    
    public void initTabelBarang() {        
        String keyword = searchBarang.getText();
        String kategori = listKategori.getSelectedItem().toString();
        String satuan = listSatuan.getSelectedItem().toString();
        String supplier = listSuplier.getSelectedItem().toString();
        try {
            HashMap<String, String> where = new HashMap<String, String>();
            if (!keyword.equals("")) where.put("tb.nama LIKE ", "'" + keyword + "'");
            if (!kategori.equals("Semua")) where.put("kategori.keterangan = ", "'" + kategori + "'");
            if (!satuan.equals("Semua")) where.put("satuan.keterangan = ", "'" + satuan + "'");
            if (!supplier.equals("Semua")) where.put("ts.nama = ", "'" + supplier + "'");
            if (where.isEmpty()) {
                listBarang = barangDAO.getListBarang();
            } else {
                String where_query = "WHERE ";
                int i = 1; int count = where.size();
                for (Map.Entry<String, String> entry : where.entrySet()) {
                    where_query += (entry.getKey() + entry.getValue() + (i < count ? " AND " : ""));
                    i++;
                }
                listBarang = barangDAO.getListBarang(where_query);
            }
            tabelBarang.setModel(new BarangDataTable(listBarang));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[] size = {5, 45, 180, 70, 45, 70, 50, 50, 100};
        adjustTableColumnWidth(tabelBarang, size);
    }
    
    public void update(){
        initTabelSupplier();
        initTabelSales();
        initTabelPelanggan();
        initTabelBarang();
        initTabelHutang();
    }
    
    public void adjustTableColumnWidth(JTable table, int[] columnSizes) {
        for (int i = 0; i < columnSizes.length; i++){
            table.getColumnModel().getColumn(i).setPreferredWidth(columnSizes[i]);
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

        bodypanel = new javax.swing.JPanel();
        menuPanel = new javax.swing.JPanel();
        btnBarang = new javax.swing.JButton();
        btnSuplier = new javax.swing.JButton();
        btnPelanggan = new javax.swing.JButton();
        btnSales = new javax.swing.JButton();
        btnPengaturan = new javax.swing.JButton();
        btnRefreshAll = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        panelBarang = new javax.swing.JPanel();
        botNav = new javax.swing.JPanel();
        hapusBarang = new javax.swing.JButton();
        editBarang = new javax.swing.JButton();
        tambahBarang = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        searchBarang = new javax.swing.JTextField();
        content = new javax.swing.JPanel();
        labelbarang = new javax.swing.JLabel();
        listKategori = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        listSuplier = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        listSatuan = new javax.swing.JComboBox<>();
        btnRefresh = new javax.swing.JButton();
        importExcel = new javax.swing.JButton();
        eksportExcel = new javax.swing.JButton();
        copyBarcode = new javax.swing.JButton();
        cetakBarcode = new javax.swing.JButton();
        scrollTableBarang = new javax.swing.JScrollPane();
        tabelBarang = new javax.swing.JTable();
        panelSuplier = new javax.swing.JPanel();
        botNav1 = new javax.swing.JPanel();
        hapusSuplier = new javax.swing.JButton();
        editSuplier = new javax.swing.JButton();
        tambahSuplier = new javax.swing.JButton();
        exit1 = new javax.swing.JButton();
        searchSupplier = new javax.swing.JTextField();
        content1 = new javax.swing.JPanel();
        labelbarang1 = new javax.swing.JLabel();
        scrollTableSupplier = new javax.swing.JScrollPane();
        tabelSupplier = new javax.swing.JTable();
        panelPelanggan = new javax.swing.JPanel();
        botNav3 = new javax.swing.JPanel();
        hapusPelanggan = new javax.swing.JButton();
        editPelanggan = new javax.swing.JButton();
        tambahPelanggan = new javax.swing.JButton();
        exit3 = new javax.swing.JButton();
        searchPelanggan = new javax.swing.JTextField();
        content3 = new javax.swing.JPanel();
        labelbarang3 = new javax.swing.JLabel();
        scrollTablePelanggan = new javax.swing.JScrollPane();
        tabelPelanggan = new javax.swing.JTable();
        panelSales = new javax.swing.JPanel();
        botNav2 = new javax.swing.JPanel();
        hapusSales = new javax.swing.JButton();
        editSales = new javax.swing.JButton();
        tambahSales = new javax.swing.JButton();
        exit2 = new javax.swing.JButton();
        searchSales = new javax.swing.JTextField();
        content2 = new javax.swing.JPanel();
        labelbarang2 = new javax.swing.JLabel();
        scrollTableSales = new javax.swing.JScrollPane();
        tabelSales = new javax.swing.JTable();
        panelHutang = new javax.swing.JPanel();
        botNav4 = new javax.swing.JPanel();
        hapusHutang = new javax.swing.JButton();
        editHutang = new javax.swing.JButton();
        tambahHutang = new javax.swing.JButton();
        exit4 = new javax.swing.JButton();
        searchHutang = new javax.swing.JTextField();
        content4 = new javax.swing.JPanel();
        labelbarang4 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabelHutang = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        bodypanel.setBackground(new java.awt.Color(75, 87, 247));

        btnBarang.setText("BARANG");
        btnBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBarangActionPerformed(evt);
            }
        });

        btnSuplier.setText("SUPLIER");
        btnSuplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuplierActionPerformed(evt);
            }
        });

        btnPelanggan.setText("PELANGGAN");
        btnPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPelangganActionPerformed(evt);
            }
        });

        btnSales.setText("SALES");
        btnSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesActionPerformed(evt);
            }
        });

        btnPengaturan.setText("PENGATURAN");
        btnPengaturan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPengaturanActionPerformed(evt);
            }
        });

        btnRefreshAll.setText("REFRESH DATA");
        btnRefreshAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBarang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSuplier)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPelanggan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSales)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPengaturan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRefreshAll)
                .addContainerGap(143, Short.MAX_VALUE))
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBarang)
                    .addComponent(btnSuplier)
                    .addComponent(btnPelanggan)
                    .addComponent(btnSales)
                    .addComponent(btnPengaturan)
                    .addComponent(btnRefreshAll))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainPanel.setBackground(new java.awt.Color(75, 87, 247));
        mainPanel.setLayout(new java.awt.CardLayout());

        panelBarang.setBackground(new java.awt.Color(51, 102, 255));

        botNav.setBackground(new java.awt.Color(51, 102, 255));

        hapusBarang.setText("Hapus");
        hapusBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusBarangActionPerformed(evt);
            }
        });

        editBarang.setText("Edit");
        editBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBarangActionPerformed(evt);
            }
        });

        tambahBarang.setText("Tambah");
        tambahBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahBarangActionPerformed(evt);
            }
        });

        exit.setText("Keluar");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        searchBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchBarangKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout botNavLayout = new javax.swing.GroupLayout(botNav);
        botNav.setLayout(botNavLayout);
        botNavLayout.setHorizontalGroup(
            botNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botNavLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tambahBarang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editBarang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hapusBarang)
                .addGap(94, 94, 94)
                .addComponent(searchBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 588, Short.MAX_VALUE)
                .addComponent(exit)
                .addContainerGap())
        );
        botNavLayout.setVerticalGroup(
            botNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botNavLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(botNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tambahBarang)
                    .addComponent(editBarang)
                    .addComponent(hapusBarang)
                    .addComponent(searchBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exit))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        content.setBackground(new java.awt.Color(51, 102, 255));

        labelbarang.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        labelbarang.setForeground(new java.awt.Color(255, 255, 255));
        labelbarang.setText("BARANG");

        listKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));
        listKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listKategoriActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("KATEGORI");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("SUPLIER");

        listSuplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));
        listSuplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listSuplierActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("SATUAN");

        listSatuan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));
        listSatuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listSatuanActionPerformed(evt);
            }
        });

        btnRefresh.setText("Refresh");

        importExcel.setText("Import dari Excel");

        eksportExcel.setText("Eksport dari Excel");

        copyBarcode.setText("Copy Barcode");

        cetakBarcode.setText("Cetak Barcode");

        tabelBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Kode", "Nama", "Kategori", "Jumlah Stok", "Satuan", "Harga Jual", "Harga Beli", "Suplier"
            }
        ));
        scrollTableBarang.setViewportView(tabelBarang);
        if (tabelBarang.getColumnModel().getColumnCount() > 0) {
            tabelBarang.getColumnModel().getColumn(0).setPreferredWidth(3);
        }

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelbarang, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listSuplier, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(contentLayout.createSequentialGroup()
                        .addComponent(listSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRefresh)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(eksportExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(importExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cetakBarcode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(copyBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(contentLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollTableBarang, javax.swing.GroupLayout.DEFAULT_SIZE, 1267, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel2)
                        .addComponent(jLabel4))
                    .addGroup(contentLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(contentLayout.createSequentialGroup()
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(importExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(copyBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(7, 7, 7)
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(eksportExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cetakBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelbarang)
                                .addComponent(listKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(listSuplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(listSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnRefresh)))))
                .addContainerGap(363, Short.MAX_VALUE))
            .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(contentLayout.createSequentialGroup()
                    .addGap(47, 47, 47)
                    .addComponent(scrollTableBarang, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        scrollTableBarang.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout panelBarangLayout = new javax.swing.GroupLayout(panelBarang);
        panelBarang.setLayout(panelBarangLayout);
        panelBarangLayout.setHorizontalGroup(
            panelBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1327, Short.MAX_VALUE)
            .addGroup(panelBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBarangLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(botNav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        panelBarangLayout.setVerticalGroup(
            panelBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
            .addGroup(panelBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBarangLayout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(botNav, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)))
        );

        mainPanel.add(panelBarang, "card3");

        panelSuplier.setBackground(new java.awt.Color(51, 102, 255));

        botNav1.setBackground(new java.awt.Color(51, 102, 255));

        hapusSuplier.setText("Hapus");
        hapusSuplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusSuplierActionPerformed(evt);
            }
        });

        editSuplier.setText("Edit");
        editSuplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSuplierActionPerformed(evt);
            }
        });

        tambahSuplier.setText("Tambah");
        tambahSuplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahSuplierActionPerformed(evt);
            }
        });

        exit1.setText("Keluar");
        exit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exit1ActionPerformed(evt);
            }
        });

        searchSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchSupplierKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout botNav1Layout = new javax.swing.GroupLayout(botNav1);
        botNav1.setLayout(botNav1Layout);
        botNav1Layout.setHorizontalGroup(
            botNav1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botNav1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tambahSuplier)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editSuplier)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hapusSuplier)
                .addGap(94, 94, 94)
                .addComponent(searchSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 588, Short.MAX_VALUE)
                .addComponent(exit1)
                .addContainerGap())
        );
        botNav1Layout.setVerticalGroup(
            botNav1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botNav1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(botNav1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tambahSuplier)
                    .addComponent(editSuplier)
                    .addComponent(hapusSuplier)
                    .addComponent(searchSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exit1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        content1.setBackground(new java.awt.Color(51, 102, 255));

        labelbarang1.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        labelbarang1.setForeground(new java.awt.Color(255, 255, 255));
        labelbarang1.setText("SUPLIER");

        tabelSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode", "Nama", "Alamat", "Telepon", "Email", "Kontak Person", "Keterangan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scrollTableSupplier.setViewportView(tabelSupplier);
        if (tabelSupplier.getColumnModel().getColumnCount() > 0) {
            tabelSupplier.getColumnModel().getColumn(0).setPreferredWidth(3);
        }

        javax.swing.GroupLayout content1Layout = new javax.swing.GroupLayout(content1);
        content1.setLayout(content1Layout);
        content1Layout.setHorizontalGroup(
            content1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(content1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelbarang1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(content1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(content1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollTableSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 1287, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        content1Layout.setVerticalGroup(
            content1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(content1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(labelbarang1)
                .addContainerGap(363, Short.MAX_VALUE))
            .addGroup(content1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(content1Layout.createSequentialGroup()
                    .addGap(47, 47, 47)
                    .addComponent(scrollTableSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout panelSuplierLayout = new javax.swing.GroupLayout(panelSuplier);
        panelSuplier.setLayout(panelSuplierLayout);
        panelSuplierLayout.setHorizontalGroup(
            panelSuplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1327, Short.MAX_VALUE)
            .addGroup(panelSuplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSuplierLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelSuplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(botNav1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(content1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        panelSuplierLayout.setVerticalGroup(
            panelSuplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
            .addGroup(panelSuplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSuplierLayout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(content1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(botNav1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)))
        );

        mainPanel.add(panelSuplier, "card2");

        panelPelanggan.setBackground(new java.awt.Color(51, 102, 255));

        botNav3.setBackground(new java.awt.Color(51, 102, 255));

        hapusPelanggan.setText("Hapus");
        hapusPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusPelangganActionPerformed(evt);
            }
        });

        editPelanggan.setText("Edit");
        editPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPelangganActionPerformed(evt);
            }
        });

        tambahPelanggan.setText("Tambah");
        tambahPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahPelangganActionPerformed(evt);
            }
        });

        exit3.setText("Keluar");

        searchPelanggan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchPelangganKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout botNav3Layout = new javax.swing.GroupLayout(botNav3);
        botNav3.setLayout(botNav3Layout);
        botNav3Layout.setHorizontalGroup(
            botNav3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botNav3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tambahPelanggan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editPelanggan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hapusPelanggan)
                .addGap(94, 94, 94)
                .addComponent(searchPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 588, Short.MAX_VALUE)
                .addComponent(exit3)
                .addContainerGap())
        );
        botNav3Layout.setVerticalGroup(
            botNav3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botNav3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(botNav3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tambahPelanggan)
                    .addComponent(editPelanggan)
                    .addComponent(hapusPelanggan)
                    .addComponent(searchPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exit3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        content3.setBackground(new java.awt.Color(51, 102, 255));

        labelbarang3.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        labelbarang3.setForeground(new java.awt.Color(255, 255, 255));
        labelbarang3.setText("PELANGGAN");

        tabelPelanggan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode", "Nama", "Alamat", "Telpon", "Keterangan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scrollTablePelanggan.setViewportView(tabelPelanggan);
        if (tabelPelanggan.getColumnModel().getColumnCount() > 0) {
            tabelPelanggan.getColumnModel().getColumn(0).setPreferredWidth(3);
        }

        javax.swing.GroupLayout content3Layout = new javax.swing.GroupLayout(content3);
        content3.setLayout(content3Layout);
        content3Layout.setHorizontalGroup(
            content3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(content3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelbarang3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(content3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(content3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollTablePelanggan, javax.swing.GroupLayout.DEFAULT_SIZE, 1287, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        content3Layout.setVerticalGroup(
            content3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(content3Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(labelbarang3)
                .addContainerGap(363, Short.MAX_VALUE))
            .addGroup(content3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(content3Layout.createSequentialGroup()
                    .addGap(47, 47, 47)
                    .addComponent(scrollTablePelanggan, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout panelPelangganLayout = new javax.swing.GroupLayout(panelPelanggan);
        panelPelanggan.setLayout(panelPelangganLayout);
        panelPelangganLayout.setHorizontalGroup(
            panelPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1327, Short.MAX_VALUE)
            .addGroup(panelPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelPelangganLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(botNav3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(content3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        panelPelangganLayout.setVerticalGroup(
            panelPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
            .addGroup(panelPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelPelangganLayout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(content3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(botNav3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)))
        );

        mainPanel.add(panelPelanggan, "card5");

        panelSales.setBackground(new java.awt.Color(51, 102, 255));

        botNav2.setBackground(new java.awt.Color(51, 102, 255));

        hapusSales.setText("Hapus");
        hapusSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusSalesActionPerformed(evt);
            }
        });

        editSales.setText("Edit");
        editSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSalesActionPerformed(evt);
            }
        });

        tambahSales.setText("Tambah");
        tambahSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahSalesActionPerformed(evt);
            }
        });

        exit2.setText("Keluar");

        searchSales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchSalesKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout botNav2Layout = new javax.swing.GroupLayout(botNav2);
        botNav2.setLayout(botNav2Layout);
        botNav2Layout.setHorizontalGroup(
            botNav2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botNav2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tambahSales)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editSales)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hapusSales)
                .addGap(94, 94, 94)
                .addComponent(searchSales, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 588, Short.MAX_VALUE)
                .addComponent(exit2)
                .addContainerGap())
        );
        botNav2Layout.setVerticalGroup(
            botNav2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botNav2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(botNav2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tambahSales)
                    .addComponent(editSales)
                    .addComponent(hapusSales)
                    .addComponent(searchSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exit2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        content2.setBackground(new java.awt.Color(51, 102, 255));

        labelbarang2.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        labelbarang2.setForeground(new java.awt.Color(255, 255, 255));
        labelbarang2.setText("SALES");

        tabelSales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode", "Nama", "Alamat", "Telpon", "Email", "Kontak Person", "Rekening", "Keterangan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scrollTableSales.setViewportView(tabelSales);
        if (tabelSales.getColumnModel().getColumnCount() > 0) {
            tabelSales.getColumnModel().getColumn(0).setPreferredWidth(3);
        }

        javax.swing.GroupLayout content2Layout = new javax.swing.GroupLayout(content2);
        content2.setLayout(content2Layout);
        content2Layout.setHorizontalGroup(
            content2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(content2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelbarang2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(content2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(content2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollTableSales, javax.swing.GroupLayout.DEFAULT_SIZE, 1287, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        content2Layout.setVerticalGroup(
            content2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(content2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(labelbarang2)
                .addContainerGap(363, Short.MAX_VALUE))
            .addGroup(content2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(content2Layout.createSequentialGroup()
                    .addGap(47, 47, 47)
                    .addComponent(scrollTableSales, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout panelSalesLayout = new javax.swing.GroupLayout(panelSales);
        panelSales.setLayout(panelSalesLayout);
        panelSalesLayout.setHorizontalGroup(
            panelSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1327, Short.MAX_VALUE)
            .addGroup(panelSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSalesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(botNav2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(content2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        panelSalesLayout.setVerticalGroup(
            panelSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
            .addGroup(panelSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSalesLayout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(content2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(botNav2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)))
        );

        mainPanel.add(panelSales, "card4");

        panelHutang.setBackground(new java.awt.Color(51, 102, 255));

        botNav4.setBackground(new java.awt.Color(51, 102, 255));

        hapusHutang.setText("Hapus");
        hapusHutang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusHutangActionPerformed(evt);
            }
        });

        editHutang.setText("Edit");
        editHutang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editHutangActionPerformed(evt);
            }
        });

        tambahHutang.setText("Tambah");
        tambahHutang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahHutangActionPerformed(evt);
            }
        });

        exit4.setText("Keluar");

        searchHutang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchHutangKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout botNav4Layout = new javax.swing.GroupLayout(botNav4);
        botNav4.setLayout(botNav4Layout);
        botNav4Layout.setHorizontalGroup(
            botNav4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botNav4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tambahHutang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editHutang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hapusHutang)
                .addGap(94, 94, 94)
                .addComponent(searchHutang, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 588, Short.MAX_VALUE)
                .addComponent(exit4)
                .addContainerGap())
        );
        botNav4Layout.setVerticalGroup(
            botNav4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botNav4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(botNav4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tambahHutang)
                    .addComponent(editHutang)
                    .addComponent(hapusHutang)
                    .addComponent(searchHutang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exit4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        content4.setBackground(new java.awt.Color(51, 102, 255));

        labelbarang4.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        labelbarang4.setForeground(new java.awt.Color(255, 255, 255));
        labelbarang4.setText("HUTANG/PIUTANG");

        tabelHutang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama Pelanggan", "Alamat", "Telpon", "Tanggal Hutang", "Tanggal Bayar", "Keterangan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tabelHutang);
        if (tabelHutang.getColumnModel().getColumnCount() > 0) {
            tabelHutang.getColumnModel().getColumn(0).setPreferredWidth(3);
        }

        javax.swing.GroupLayout content4Layout = new javax.swing.GroupLayout(content4);
        content4.setLayout(content4Layout);
        content4Layout.setHorizontalGroup(
            content4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(content4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelbarang4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(content4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(content4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1287, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        content4Layout.setVerticalGroup(
            content4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(content4Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(labelbarang4)
                .addContainerGap(363, Short.MAX_VALUE))
            .addGroup(content4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(content4Layout.createSequentialGroup()
                    .addGap(47, 47, 47)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout panelHutangLayout = new javax.swing.GroupLayout(panelHutang);
        panelHutang.setLayout(panelHutangLayout);
        panelHutangLayout.setHorizontalGroup(
            panelHutangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1327, Short.MAX_VALUE)
            .addGroup(panelHutangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelHutangLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelHutangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(botNav4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(content4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        panelHutangLayout.setVerticalGroup(
            panelHutangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
            .addGroup(panelHutangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelHutangLayout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(content4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(botNav4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)))
        );

        mainPanel.add(panelHutang, "card6");

        javax.swing.GroupLayout bodypanelLayout = new javax.swing.GroupLayout(bodypanel);
        bodypanel.setLayout(bodypanelLayout);
        bodypanelLayout.setHorizontalGroup(
            bodypanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodypanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
        bodypanelLayout.setVerticalGroup(
            bodypanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodypanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bodypanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bodypanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPengaturanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPengaturanActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnPengaturanActionPerformed

    private void btnSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesActionPerformed
        // TODO add your handling code here:
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        //add panel
        mainPanel.add(panelSales);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btnSalesActionPerformed

    private void btnPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPelangganActionPerformed
        // TODO add your handling code here:
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        //add panel
        mainPanel.add(panelPelanggan);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btnPelangganActionPerformed

    private void listSatuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listSatuanActionPerformed
        // TODO add your handling code here:
        initTabelBarang();
    }//GEN-LAST:event_listSatuanActionPerformed

    private void listSuplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listSuplierActionPerformed
        // TODO add your handling code here:
        initTabelBarang();
    }//GEN-LAST:event_listSuplierActionPerformed

    private void listKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listKategoriActionPerformed
        // TODO add your handling code here:
        initTabelBarang();
    }//GEN-LAST:event_listKategoriActionPerformed

    private void btnBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBarangActionPerformed
        // TODO add your handling code here:
        //remove panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        //add panel
        mainPanel.add(panelBarang);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btnBarangActionPerformed

    private void btnSuplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuplierActionPerformed
        // TODO add your handling code here:
        //remove panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        //add panel
        mainPanel.add(panelSuplier);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btnSuplierActionPerformed

    private void tambahSuplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahSuplierActionPerformed
        // TODO add your handling code here:
        InputDataSuplier.mode_form = 0;
        new InputDataSuplier().setVisible(true);
    }//GEN-LAST:event_tambahSuplierActionPerformed

    private void tambahPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahPelangganActionPerformed
        // TODO add your handling code here:
        InputDataPelanggan.mode_form = 0;
        new InputDataPelanggan().setVisible(true);
    }//GEN-LAST:event_tambahPelangganActionPerformed

    private void tambahSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahSalesActionPerformed
        // TODO add your handling code here:
        InputDataSales.mode_form = 0;
        new InputDataSales().setVisible(true);
    }//GEN-LAST:event_tambahSalesActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_exitActionPerformed

    private void searchSupplierKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchSupplierKeyReleased
        // TODO add your handling code here:
        String keyword = searchSupplier.getText();
        try {
            if (keyword.equals("")){
                listSupplier = supplierDAO.getListSupplier();
            } else {
                listSupplier = supplierDAO.getListSupplier(keyword);
            }
            tabelSupplier.setModel(new SupplierDataTable(listSupplier));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_searchSupplierKeyReleased

    private void searchPelangganKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchPelangganKeyReleased
        // TODO add your handling code here:
        String keyword = searchPelanggan.getText();
        try {
            if (keyword.equals("")){
                listPelanggan = pelangganDAO.getListPelanggan();
            } else {
                listPelanggan = pelangganDAO.getListPelanggan(keyword);
            }
            tabelPelanggan.setModel(new PelangganDataTable(listPelanggan));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_searchPelangganKeyReleased

    private void searchSalesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchSalesKeyReleased
        // TODO add your handling code here:
        String keyword = searchSales.getText();
        try {
            if (keyword.equals("")){
                listSales = salesDAO.getListSales();
            } else {
                listSales = salesDAO.getListSales(keyword);
            }
            tabelSales.setModel(new SalesDataTable(listSales));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_searchSalesKeyReleased

    private void searchBarangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBarangKeyReleased
        // TODO add your handling code here:
        initTabelBarang();
    }//GEN-LAST:event_searchBarangKeyReleased

    private void editBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBarangActionPerformed
        // TODO add your handling code here:
        if (tabelBarang.getSelectedRow() >= 0) {
            String kode = tabelBarang.getValueAt(tabelBarang.getSelectedRow(), 1).toString();
            try {
                InputDataBarang.barangEdit = barangDAO.getBarang("tb.kode = '" + kode + "'");
                InputDataBarang.mode_form = 1;
                new InputDataBarang().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih baris tabel terlebih dahulu", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_editBarangActionPerformed

    private void hapusBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusBarangActionPerformed
        // TODO add your handling code here:
        if (tabelBarang.getSelectedRow() >= 0) {
            String kode = tabelBarang.getValueAt(tabelBarang.getSelectedRow(), 1).toString();
            try {
                String id = vm.getIdFromKode("kode", kode, "tb_barang");
                int result = barangDAO.deleteBarang(id);
                if (result >= 0) {
                    JOptionPane.showMessageDialog(null, "Berhasil menghapus data", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menghapus data, terjadi kesalahan", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih baris tabel terlebih dahulu", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
        update();
    }//GEN-LAST:event_hapusBarangActionPerformed

    private void editSuplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSuplierActionPerformed
        // TODO add your handling code here:
        if (tabelSupplier.getSelectedRow() >= 0) {
            String kode = tabelSupplier.getValueAt(tabelSupplier.getSelectedRow(), 1).toString();
            try {
                ResultSet rs = vm.getDataByParameter("kode = '" + kode + "'", "tb_supplier");
                if (rs.next()){
                    Supplier sp = new Supplier();
                    sp.setId(rs.getString("id"));
                    sp.setKode(rs.getString("kode"));
                    sp.setNama(rs.getString("nama"));
                    sp.setAlamat(rs.getString("alamat"));
                    sp.setTelepon(rs.getString("telepon"));
                    sp.setEmail(rs.getString("email"));
                    sp.setContact_person(rs.getString("contact_person"));
                    sp.setRekening(rs.getString("rekening"));
                    sp.setKeterangan(rs.getString("keterangan"));
                    InputDataSuplier.supplierEdit = sp;
                    InputDataSuplier.mode_form = 1;
                    new InputDataSuplier().setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih baris tabel terlebih dahulu", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_editSuplierActionPerformed

    private void hapusSuplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusSuplierActionPerformed
        // TODO add your handling code here:
        if (tabelSupplier.getSelectedRow() >= 0) {
            String kode = tabelSupplier.getValueAt(tabelSupplier.getSelectedRow(), 1).toString();
            try {
                String id = vm.getIdFromKode("kode", kode, "tb_supplier");
                int result = supplierDAO.deleteSupplier(id);
                if (result >= 0) {
                    JOptionPane.showMessageDialog(null, "Berhasil menghapus data", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menghapus data, terjadi kesalahan", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih baris tabel terlebih dahulu", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
        update();
    }//GEN-LAST:event_hapusSuplierActionPerformed

    private void editPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPelangganActionPerformed
        // TODO add your handling code here:
        if (tabelPelanggan.getSelectedRow() >= 0) {
            String kode = tabelPelanggan.getValueAt(tabelPelanggan.getSelectedRow(), 1).toString();
            try {
                ResultSet rs = vm.getDataByParameter("kode = '" + kode + "'", "tb_pelanggan");
                if (rs.next()){
                    Pelanggan sp = new Pelanggan();
                    sp.setId(rs.getString("id"));
                    sp.setKode(rs.getString("kode"));
                    sp.setNama(rs.getString("nama"));
                    sp.setAlamat(rs.getString("alamat"));
                    sp.setTelepon(rs.getString("telepon"));
                    sp.setKeterangan(rs.getString("keterangan"));
                    InputDataPelanggan.pelangganEdit = sp;
                    InputDataPelanggan.mode_form = 1;
                    new InputDataPelanggan().setVisible(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih baris tabel terlebih dahulu", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_editPelangganActionPerformed

    private void hapusPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusPelangganActionPerformed
        // TODO add your handling code here:
        if (tabelPelanggan.getSelectedRow() >= 0) {
            String kode = tabelPelanggan.getValueAt(tabelPelanggan.getSelectedRow(), 1).toString();
            try {
                String id = vm.getIdFromKode("kode", kode, "tb_pelanggan");
                int result = pelangganDAO.deletePelanggan(id);
                if (result >= 0) {
                    JOptionPane.showMessageDialog(null, "Berhasil menghapus data", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menghapus data, terjadi kesalahan", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih baris tabel terlebih dahulu", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
        update();
    }//GEN-LAST:event_hapusPelangganActionPerformed

    private void editSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSalesActionPerformed
        // TODO add your handling code here:
        if (tabelSales.getSelectedRow() >= 0) {
            String kode = tabelSales.getValueAt(tabelSales.getSelectedRow(), 1).toString();
            try {
                ResultSet rs = vm.getDataByParameter("kode = '" + kode + "'", "tb_sales");
                if (rs.next()){
                    Sales sp = new Sales();
                    sp.setId(rs.getString("id"));
                    sp.setKode(rs.getString("kode"));
                    sp.setNama(rs.getString("nama"));
                    sp.setAlamat(rs.getString("alamat"));
                    sp.setTelepon(rs.getString("telepon"));
                    sp.setEmail(rs.getString("email"));
                    sp.setContact_person(rs.getString("contact_person"));
                    sp.setRekening(rs.getString("rekening"));
                    sp.setKeterangan(rs.getString("keterangan"));
                    InputDataSales.salesEdit = sp;
                    InputDataSales.mode_form = 1;
                    new InputDataSales().setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih baris tabel terlebih dahulu", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_editSalesActionPerformed

    private void hapusSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusSalesActionPerformed
        // TODO add your handling code here:
        if (tabelSales.getSelectedRow() >= 0) {
            String kode = tabelSales.getValueAt(tabelSales.getSelectedRow(), 1).toString();
            try {
                String id = vm.getIdFromKode("kode", kode, "tb_sales");
                int result = salesDAO.deleteSales(id);
                if (result >= 0) {
                    JOptionPane.showMessageDialog(null, "Berhasil menghapus data", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menghapus data, terjadi kesalahan", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih baris tabel terlebih dahulu", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
        update();
    }//GEN-LAST:event_hapusSalesActionPerformed

    private void searchHutangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchHutangKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_searchHutangKeyReleased

    private void tambahHutangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahHutangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tambahHutangActionPerformed

    private void editHutangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editHutangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editHutangActionPerformed

    private void hapusHutangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusHutangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hapusHutangActionPerformed

    private void btnRefreshAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshAllActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnRefreshAllActionPerformed

    private void exit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exit1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_exit1ActionPerformed

    private void tambahBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahBarangActionPerformed
        // TODO add your handling code here:
        InputDataBarang.mode_form = 0;
        new InputDataBarang().setVisible(true);
    }//GEN-LAST:event_tambahBarangActionPerformed

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
            java.util.logging.Logger.getLogger(Master.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Master.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Master.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Master.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Master().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bodypanel;
    private javax.swing.JPanel botNav;
    private javax.swing.JPanel botNav1;
    private javax.swing.JPanel botNav2;
    private javax.swing.JPanel botNav3;
    private javax.swing.JPanel botNav4;
    private javax.swing.JButton btnBarang;
    private javax.swing.JButton btnPelanggan;
    private javax.swing.JButton btnPengaturan;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRefreshAll;
    private javax.swing.JButton btnSales;
    private javax.swing.JButton btnSuplier;
    private javax.swing.JButton cetakBarcode;
    private javax.swing.JPanel content;
    private javax.swing.JPanel content1;
    private javax.swing.JPanel content2;
    private javax.swing.JPanel content3;
    private javax.swing.JPanel content4;
    private javax.swing.JButton copyBarcode;
    private javax.swing.JButton editBarang;
    private javax.swing.JButton editHutang;
    private javax.swing.JButton editPelanggan;
    private javax.swing.JButton editSales;
    private javax.swing.JButton editSuplier;
    private javax.swing.JButton eksportExcel;
    private javax.swing.JButton exit;
    private javax.swing.JButton exit1;
    private javax.swing.JButton exit2;
    private javax.swing.JButton exit3;
    private javax.swing.JButton exit4;
    private javax.swing.JButton hapusBarang;
    private javax.swing.JButton hapusHutang;
    private javax.swing.JButton hapusPelanggan;
    private javax.swing.JButton hapusSales;
    private javax.swing.JButton hapusSuplier;
    private javax.swing.JButton importExcel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel labelbarang;
    private javax.swing.JLabel labelbarang1;
    private javax.swing.JLabel labelbarang2;
    private javax.swing.JLabel labelbarang3;
    private javax.swing.JLabel labelbarang4;
    private javax.swing.JComboBox<String> listKategori;
    private javax.swing.JComboBox<String> listSatuan;
    private javax.swing.JComboBox<String> listSuplier;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JPanel panelBarang;
    private javax.swing.JPanel panelHutang;
    private javax.swing.JPanel panelPelanggan;
    private javax.swing.JPanel panelSales;
    private javax.swing.JPanel panelSuplier;
    private javax.swing.JScrollPane scrollTableBarang;
    private javax.swing.JScrollPane scrollTablePelanggan;
    private javax.swing.JScrollPane scrollTableSales;
    private javax.swing.JScrollPane scrollTableSupplier;
    private javax.swing.JTextField searchBarang;
    private javax.swing.JTextField searchHutang;
    private javax.swing.JTextField searchPelanggan;
    private javax.swing.JTextField searchSales;
    private javax.swing.JTextField searchSupplier;
    private javax.swing.JTable tabelBarang;
    private javax.swing.JTable tabelHutang;
    private javax.swing.JTable tabelPelanggan;
    private javax.swing.JTable tabelSales;
    private javax.swing.JTable tabelSupplier;
    private javax.swing.JButton tambahBarang;
    private javax.swing.JButton tambahHutang;
    private javax.swing.JButton tambahPelanggan;
    private javax.swing.JButton tambahSales;
    private javax.swing.JButton tambahSuplier;
    // End of variables declaration//GEN-END:variables
}
