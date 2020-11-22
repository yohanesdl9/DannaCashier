/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penjualan;

import dao.PembelianDAO;
import dao.PenjualanDAO;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


/**
 *
 * @author hannah fitri nur aisyah
 */
public class MenuUtama extends javax.swing.JFrame {

    /**
     * Creates new form MenuUtama
     */
    ViewModel vm = new ViewModel();
    PembelianDAO pembelianDAO = PembelianDAO.getInstance();
    PenjualanDAO penjualanDAO = PenjualanDAO.getInstance();
    
    public MenuUtama() {
        initComponents();
        
        this.setLocationRelativeTo(null);
        panelGrafikPembelian.setLayout(new java.awt.BorderLayout());
        initGrafik();
        try {
            statsPembelianToday.setText(String.format("%,d", vm.getStatistik("pengeluaran_today")).replace(',', '.'));
            statsPembelianMonth.setText(String.format("%,d", vm.getStatistik("pengeluaran_month")).replace(',', '.'));
            statsPenjualanToday.setText(String.format("%,d", vm.getStatistik("omset_today")).replace(',', '.'));
            statsPenjualanMonth.setText(String.format("%,d", vm.getStatistik("omset_month")).replace(',', '.'));
            statsRtbToday.setText(String.format("%,d", vm.getStatistik("retur_beli_today")).replace(',', '.'));
            statsRtbMonth.setText(String.format("%,d", vm.getStatistik("retur_beli_month")).replace(',', '.'));
            statsRtjToday.setText(String.format("%,d", vm.getStatistik("retur_jual_today")).replace(',', '.'));
            statsRtjMonth.setText(String.format("%,d", vm.getStatistik("retur_jual_month")).replace(',', '.'));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void initGrafik(){
        CategoryPlot plot = new CategoryPlot();

        // Add the first dataset and render as lines
        CategoryItemRenderer lineRenderer = new LineAndShapeRenderer();
        plot.setDataset(0, createDataset());
        plot.setRenderer(0, lineRenderer);

        // Set Axis
        plot.setDomainAxis(new CategoryAxis("Bulan"));
        plot.setRangeAxis(new NumberAxis("Nominal"));

        JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("Grafik Pembelian dan Penjualan Tahun Ini");

        ChartPanel panel = new ChartPanel(chart);
        
        panelGrafikPembelian.removeAll();    // clear panel before add new chart
        panelGrafikPembelian.add(panel, BorderLayout.CENTER);
        panelGrafikPembelian.validate();       // refresh panel to display new chart
    }
    
    
    private DefaultCategoryDataset createDataset(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            ResultSet rsBeli = pembelianDAO.datasetPembelianTahunIni();
            ResultSet rsJual = penjualanDAO.datasetPenjualanTahunIni();
            String series1 = "Pembelian";
            String series2 = "Penjualan";
            while (rsBeli.next()) {
                dataset.addValue(rsBeli.getInt("value"), series1, rsBeli.getString("label"));
            }
            while (rsJual.next()) {
                dataset.addValue(rsJual.getInt("value"), series2, rsJual.getString("label"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataset;
    }
    
    private XYDataset createDatasetPembelian(){
        TimeSeries s1 = new TimeSeries("Pembelian", Month.class);
        
        try {
            ResultSet rsBeli = pembelianDAO.datasetPembelianTahunIni();
            while (rsBeli.next()) {
                int bulan = rsBeli.getInt("MONTH");
                int tahun = rsBeli.getInt("tahun");
                int value = rsBeli.getInt("value");
                s1.add(new Month(bulan, tahun), value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        return dataset;
    }
    
    private XYDataset createDatasetPenjualan(){
        TimeSeries s2 = new TimeSeries("Penjualan", Month.class);
        
        try {
            ResultSet rsJual = penjualanDAO.datasetPenjualanTahunIni();
            while (rsJual.next()) {
                int bulan = rsJual.getInt("MONTH");
                int tahun = rsJual.getInt("tahun");
                int value = rsJual.getInt("value");
                s2.add(new Month(bulan, tahun), value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s2);
        return dataset;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        statsPenjualanToday = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        statsRtjToday = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        statsRtjMonth = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        statsPembelianToday = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        statsPenjualanMonth = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        statsRtbMonth = new javax.swing.JLabel();
        panelGrafikPembelian = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        statsRtbToday = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        statsPembelianMonth = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        smLogout = new javax.swing.JMenuItem();
        smKeluarApl = new javax.swing.JMenuItem();
        menuKasir = new javax.swing.JMenu();
        smBukaKasir = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        smPembelian = new javax.swing.JMenuItem();
        smTbPembelian = new javax.swing.JMenuItem();
        smReturPembelian = new javax.swing.JMenuItem();
        smTbReturPembelian = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        smPenjualan = new javax.swing.JMenuItem();
        smTbPenjualan = new javax.swing.JMenuItem();
        smReturPenjualan = new javax.swing.JMenuItem();
        smTbReturPenjualan = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        smUtang = new javax.swing.JMenuItem();
        smPiutang = new javax.swing.JMenuItem();

        jMenu2.setText("File");
        jMenuBar2.add(jMenu2);

        jMenu5.setText("Edit");
        jMenuBar2.add(jMenu5);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel7.setBackground(new java.awt.Color(0, 135, 255));

        jLabel3.setText("PENJUALAN HARI INI");

        statsPenjualanToday.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        statsPenjualanToday.setText("0");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(statsPenjualanToday, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statsPenjualanToday)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(0, 135, 255));

        jLabel7.setText("RETUR PENJUALAN HARI INI");

        statsRtjToday.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        statsRtjToday.setText("0");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(statsRtjToday, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statsRtjToday)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(0, 135, 255));

        jLabel8.setText("RETUR PENJUALAN BULAN INI");

        statsRtjMonth.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        statsRtjMonth.setText("0");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(statsRtjMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statsRtjMonth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(0, 135, 255));

        jLabel1.setText("PEMBELIAN HARI INI");

        statsPembelianToday.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        statsPembelianToday.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(statsPembelianToday, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsPembelianToday)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(0, 135, 255));

        jLabel4.setText("PENJUALAN BULAN INI");

        statsPenjualanMonth.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        statsPenjualanMonth.setText("0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(statsPenjualanMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statsPenjualanMonth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(0, 135, 255));

        jLabel6.setText("RETUR PEMBELIAN BULAN INI");

        statsRtbMonth.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        statsRtbMonth.setText("0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(statsRtbMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statsRtbMonth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addContainerGap())
        );

        panelGrafikPembelian.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelGrafikPembelianLayout = new javax.swing.GroupLayout(panelGrafikPembelian);
        panelGrafikPembelian.setLayout(panelGrafikPembelianLayout);
        panelGrafikPembelianLayout.setHorizontalGroup(
            panelGrafikPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelGrafikPembelianLayout.setVerticalGroup(
            panelGrafikPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 386, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(0, 135, 255));

        jLabel5.setText("RETUR PEMBELIAN HARI INI");

        statsRtbToday.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        statsRtbToday.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(statsRtbToday, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statsRtbToday)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(0, 135, 255));

        jLabel2.setText("PEMBELIAN BULAN INI");

        statsPembelianMonth.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        statsPembelianMonth.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(statsPembelianMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsPembelianMonth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        jMenu1.setText("File");

        smLogout.setText("Logout");
        smLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smLogoutActionPerformed(evt);
            }
        });
        jMenu1.add(smLogout);

        smKeluarApl.setText("Keluar Aplikasi");
        smKeluarApl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smKeluarAplActionPerformed(evt);
            }
        });
        jMenu1.add(smKeluarApl);

        jMenuBar1.add(jMenu1);

        menuKasir.setText("Master");

        smBukaKasir.setText("Buka Menu Master");
        smBukaKasir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smBukaKasirActionPerformed(evt);
            }
        });
        menuKasir.add(smBukaKasir);

        jMenuBar1.add(menuKasir);

        jMenu3.setText("Transaksi Pembelian");

        smPembelian.setText("Pembelian");
        smPembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smPembelianActionPerformed(evt);
            }
        });
        jMenu3.add(smPembelian);

        smTbPembelian.setText("Tabel Pembelian");
        smTbPembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smTbPembelianActionPerformed(evt);
            }
        });
        jMenu3.add(smTbPembelian);

        smReturPembelian.setText("Retur Pembelian");
        smReturPembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smReturPembelianActionPerformed(evt);
            }
        });
        jMenu3.add(smReturPembelian);

        smTbReturPembelian.setText("Tabel Retur Pembelian");
        smTbReturPembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smTbReturPembelianActionPerformed(evt);
            }
        });
        jMenu3.add(smTbReturPembelian);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Transaksi Penjualan");

        smPenjualan.setText("Penjualan");
        smPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smPenjualanActionPerformed(evt);
            }
        });
        jMenu4.add(smPenjualan);

        smTbPenjualan.setText("Tabel Penjualan");
        smTbPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smTbPenjualanActionPerformed(evt);
            }
        });
        jMenu4.add(smTbPenjualan);

        smReturPenjualan.setText("Tabel Retur Penjualan");
        smReturPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smReturPenjualanActionPerformed(evt);
            }
        });
        jMenu4.add(smReturPenjualan);

        smTbReturPenjualan.setText("Retur Penjualan");
        smTbReturPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smTbReturPenjualanActionPerformed(evt);
            }
        });
        jMenu4.add(smTbReturPenjualan);

        jMenuBar1.add(jMenu4);

        jMenu6.setText("Utang/Piutang");

        smUtang.setText("Utang");
        smUtang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smUtangActionPerformed(evt);
            }
        });
        jMenu6.add(smUtang);

        smPiutang.setText("Piutang");
        smPiutang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smPiutangActionPerformed(evt);
            }
        });
        jMenu6.add(smPiutang);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelGrafikPembelian, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelGrafikPembelian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void smLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smLogoutActionPerformed
        // TODO add your handling code here:
        int input = JOptionPane.showConfirmDialog(null, "Logout dari aplikasi?", "Yakin ingin logout?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (input == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_smLogoutActionPerformed

    private void smKeluarAplActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smKeluarAplActionPerformed
        // TODO add your handling code here:
    int input = JOptionPane.showConfirmDialog(null, "Keluar dari aplikasi?", "Yakin ingin keluar?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (input == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_smKeluarAplActionPerformed

    private void smTbReturPembelianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smTbReturPembelianActionPerformed
        // TODO add your handling code here:
        new penjualan.pembelian.TabelReturPembelian().setVisible(true);
    }//GEN-LAST:event_smTbReturPembelianActionPerformed

    private void smPembelianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smPembelianActionPerformed
        // TODO add your handling code here:
        new penjualan.pembelian.PembelianTunai().setVisible(true);
    }//GEN-LAST:event_smPembelianActionPerformed

    private void smTbPembelianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smTbPembelianActionPerformed
        // TODO add your handling code here:
        new penjualan.pembelian.TabelPembelian().setVisible(true);
    }//GEN-LAST:event_smTbPembelianActionPerformed

    private void smReturPembelianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smReturPembelianActionPerformed
        // TODO add your handling code here:
        new penjualan.pembelian.ReturPembelian().setVisible(true);
    }//GEN-LAST:event_smReturPembelianActionPerformed

    private void smPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smPenjualanActionPerformed
        // TODO add your handling code here:
        new penjualan.penjualan.PenjualanTunai().setVisible(true);
    }//GEN-LAST:event_smPenjualanActionPerformed

    private void smTbPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smTbPenjualanActionPerformed
        // TODO add your handling code here:
        new penjualan.penjualan.TabelPenjualan().setVisible(true);
    }//GEN-LAST:event_smTbPenjualanActionPerformed

    private void smReturPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smReturPenjualanActionPerformed
        // TODO add your handling code here:
        new penjualan.penjualan.TabelReturPenjualan().setVisible(true);
    }//GEN-LAST:event_smReturPenjualanActionPerformed

    private void smTbReturPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smTbReturPenjualanActionPerformed
        // TODO add your handling code here:
        new penjualan.penjualan.ReturPenjualan().setVisible(true);
    }//GEN-LAST:event_smTbReturPenjualanActionPerformed

    private void smBukaKasirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smBukaKasirActionPerformed
        // TODO add your handling code here:
        new penjualan.kasir.Master().setVisible(true);
    }//GEN-LAST:event_smBukaKasirActionPerformed

    private void smUtangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smUtangActionPerformed
        // TODO add your handling code here:
        new penjualan.pembelian.Hutang().setVisible(true);
    }//GEN-LAST:event_smUtangActionPerformed

    private void smPiutangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smPiutangActionPerformed
        // TODO add your handling code here:
        new penjualan.penjualan.Piutang().setVisible(true);
    }//GEN-LAST:event_smPiutangActionPerformed

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
            java.util.logging.Logger.getLogger(MenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuUtama().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JMenu menuKasir;
    private javax.swing.JPanel panelGrafikPembelian;
    private javax.swing.JMenuItem smBukaKasir;
    private javax.swing.JMenuItem smKeluarApl;
    private javax.swing.JMenuItem smLogout;
    private javax.swing.JMenuItem smPembelian;
    private javax.swing.JMenuItem smPenjualan;
    private javax.swing.JMenuItem smPiutang;
    private javax.swing.JMenuItem smReturPembelian;
    private javax.swing.JMenuItem smReturPenjualan;
    private javax.swing.JMenuItem smTbPembelian;
    private javax.swing.JMenuItem smTbPenjualan;
    private javax.swing.JMenuItem smTbReturPembelian;
    private javax.swing.JMenuItem smTbReturPenjualan;
    private javax.swing.JMenuItem smUtang;
    private javax.swing.JLabel statsPembelianMonth;
    private javax.swing.JLabel statsPembelianToday;
    private javax.swing.JLabel statsPenjualanMonth;
    private javax.swing.JLabel statsPenjualanToday;
    private javax.swing.JLabel statsRtbMonth;
    private javax.swing.JLabel statsRtbToday;
    private javax.swing.JLabel statsRtjMonth;
    private javax.swing.JLabel statsRtjToday;
    // End of variables declaration//GEN-END:variables
}
