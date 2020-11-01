/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 100411
 Source Host           : 127.0.0.1:3306
 Source Schema         : danna

 Target Server Type    : MySQL
 Target Server Version : 100411
 File Encoding         : 65001

 Date: 01/11/2020 21:35:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_barang
-- ----------------------------
DROP TABLE IF EXISTS `tb_barang`;
CREATE TABLE `tb_barang`  (
  `id` int(11) NOT NULL,
  `kode` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nama` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_kategori` int(11) NULL DEFAULT NULL,
  `id_satuan` int(11) NULL DEFAULT NULL,
  `stok` int(11) NULL DEFAULT NULL,
  `harga_beli` int(11) NULL DEFAULT NULL,
  `harga_jual` int(11) NULL DEFAULT NULL,
  `id_supplier` int(11) NULL DEFAULT NULL,
  `stok_minimal` int(11) NULL DEFAULT NULL,
  `tgl_expired` date NULL DEFAULT NULL,
  `lokasi` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `diskon_nominal` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_kategori`(`id_kategori`) USING BTREE,
  INDEX `id_satuan`(`id_satuan`) USING BTREE,
  INDEX `id_supplier`(`id_supplier`) USING BTREE,
  CONSTRAINT `tb_barang_ibfk_1` FOREIGN KEY (`id_kategori`) REFERENCES `tb_general` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `tb_barang_ibfk_2` FOREIGN KEY (`id_satuan`) REFERENCES `tb_general` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `tb_barang_ibfk_3` FOREIGN KEY (`id_supplier`) REFERENCES `tb_supplier` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_barang
-- ----------------------------
INSERT INTO `tb_barang` VALUES (1, 'BRG0000001', 'Tango Wafer 104 gram', 1, 7, 95, 4800, 5200, 1, 1, NULL, '', NULL);
INSERT INTO `tb_barang` VALUES (2, 'BRG0000002', 'Mintz Chewy Bag 125 gram', 3, 7, 296, 3000, 3600, 2, 2, NULL, NULL, NULL);
INSERT INTO `tb_barang` VALUES (3, 'BRG0000003', 'Mr. Potato 60 gram', 1, 7, 196, 5000, 5400, 3, 1, NULL, NULL, NULL);
INSERT INTO `tb_barang` VALUES (4, 'BRG0000004', 'Vita Pudding 108 gram', 1, 7, 98, 2000, 2550, 1, 1, NULL, NULL, NULL);
INSERT INTO `tb_barang` VALUES (5, 'BRG0000005', 'TOP Coffee 20 x 25 gram', 4, 7, 248, 13600, 13900, 1, 2, NULL, NULL, NULL);
INSERT INTO `tb_barang` VALUES (6, 'BRG0000006', 'Sari Wangi Green Tea 25 x 25 gram', 5, 9, 150, 5500, 5900, 3, 2, NULL, NULL, NULL);
INSERT INTO `tb_barang` VALUES (7, 'BRG0000007', 'Sari Bunga Madu 650 mL', 6, 8, 120, 49000, 50000, 2, 1, '2021-04-27', NULL, NULL);
INSERT INTO `tb_barang` VALUES (8, 'BRG0000008', 'Nusantara Madu Murni', 6, 8, 120, 61500, 63000, 2, 2, '2021-04-27', NULL, NULL);
INSERT INTO `tb_barang` VALUES (9, 'BRG0000009', 'Teh Pucuk Harum 480 mL', 5, 8, 150, 3000, 3250, 1, 4, NULL, NULL, NULL);
INSERT INTO `tb_barang` VALUES (10, 'BRG0000010', 'Coolant Botol 350 mL', 2, 8, 100, 3600, 4200, 3, 2, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for tb_general
-- ----------------------------
DROP TABLE IF EXISTS `tb_general`;
CREATE TABLE `tb_general`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kode` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `keterangan` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_tipe` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_tipe`(`id_tipe`) USING BTREE,
  CONSTRAINT `tb_general_ibfk_1` FOREIGN KEY (`id_tipe`) REFERENCES `tb_tipe` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_general
-- ----------------------------
INSERT INTO `tb_general` VALUES (1, 'GN/0000001', 'Makanan', 1);
INSERT INTO `tb_general` VALUES (2, 'GN/0000002', 'Minuman', 1);
INSERT INTO `tb_general` VALUES (3, 'GN/0000003', 'Permen', 1);
INSERT INTO `tb_general` VALUES (4, 'GN/0000004', 'Kopi', 1);
INSERT INTO `tb_general` VALUES (5, 'GN/0000005', 'Teh', 1);
INSERT INTO `tb_general` VALUES (6, 'GN/0000006', 'Madu', 1);
INSERT INTO `tb_general` VALUES (7, 'GN/0000007', 'Bungkus', 2);
INSERT INTO `tb_general` VALUES (8, 'GN/0000008', 'Botol', 2);
INSERT INTO `tb_general` VALUES (9, 'GN/0000009', 'Box', 2);
INSERT INTO `tb_general` VALUES (10, 'GN/0000010', 'Tunai', 3);
INSERT INTO `tb_general` VALUES (11, 'GN/0000011', '1 Minggu', 3);
INSERT INTO `tb_general` VALUES (12, 'GN/0000012', '2 Minggu', 3);
INSERT INTO `tb_general` VALUES (13, 'GN/0000013', '3 Minggu', 3);
INSERT INTO `tb_general` VALUES (14, 'GN/0000014', '4 Minggu', 3);
INSERT INTO `tb_general` VALUES (15, 'GN/0000015', '1 Bulan', 3);
INSERT INTO `tb_general` VALUES (16, 'GN/0000016', 'Custom', 3);

-- ----------------------------
-- Table structure for tb_hutang
-- ----------------------------
DROP TABLE IF EXISTS `tb_hutang`;
CREATE TABLE `tb_hutang`  (
  `id` int(11) NOT NULL,
  `faktur_hutang` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_pembelian` int(11) NULL DEFAULT NULL,
  `tanggal` date NULL DEFAULT NULL,
  `tunai` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_pembelian`(`id_pembelian`) USING BTREE,
  CONSTRAINT `tb_hutang_ibfk_1` FOREIGN KEY (`id_pembelian`) REFERENCES `tb_pembelian` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_hutang
-- ----------------------------
INSERT INTO `tb_hutang` VALUES (1, 'FU3110200000100001', 3, '2020-11-01', 360000);

-- ----------------------------
-- Table structure for tb_pelanggan
-- ----------------------------
DROP TABLE IF EXISTS `tb_pelanggan`;
CREATE TABLE `tb_pelanggan`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kode` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nama` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `alamat` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `telepon` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `keterangan` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_pelanggan
-- ----------------------------
INSERT INTO `tb_pelanggan` VALUES (1, 'PLGN000001', 'Yohanes Dwi Listio', 'Jl. Kelapa Sawit no. 122 A Malang', '082234221832', NULL);

-- ----------------------------
-- Table structure for tb_pembelian
-- ----------------------------
DROP TABLE IF EXISTS `tb_pembelian`;
CREATE TABLE `tb_pembelian`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tanggal` date NULL DEFAULT NULL,
  `faktur` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tunai_kredit` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tempo` int(11) NULL DEFAULT NULL,
  `jatuh_tempo` date NULL DEFAULT NULL,
  `id_supplier` int(11) NULL DEFAULT NULL,
  `subtotal` int(11) NULL DEFAULT NULL,
  `diskon_persen` int(11) NULL DEFAULT NULL,
  `diskon_nominal` int(11) NULL DEFAULT NULL,
  `grand_total` int(11) NULL DEFAULT NULL,
  `tunai` int(11) NULL DEFAULT NULL,
  `kembali` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_supplier`(`id_supplier`) USING BTREE,
  CONSTRAINT `tb_pembelian_ibfk_1` FOREIGN KEY (`id_supplier`) REFERENCES `tb_supplier` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_pembelian
-- ----------------------------
INSERT INTO `tb_pembelian` VALUES (1, '2020-10-29', 'PEMB29102000001', 'TUNAI', NULL, '2020-10-29', 2, 825000, 0, 0, 825000, 1000000, 175000);
INSERT INTO `tb_pembelian` VALUES (2, '2020-10-29', 'PEMB29102000002', 'TUNAI', NULL, '2020-10-29', 3, 3870000, 0, 0, 3870000, 4000000, 130000);
INSERT INTO `tb_pembelian` VALUES (3, '2020-10-31', 'PEMB31102000001', '3 Minggu', 21, '2020-11-21', 2, 1080000, 0, 0, 1080000, NULL, NULL);

-- ----------------------------
-- Table structure for tb_pembelian_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_pembelian_detail`;
CREATE TABLE `tb_pembelian_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_pembelian` int(11) NULL DEFAULT NULL,
  `id_barang` int(11) NULL DEFAULT NULL,
  `kode_barang` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nama_barang` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `jumlah` int(11) NULL DEFAULT NULL,
  `satuan` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `harga_beli` int(11) NULL DEFAULT NULL,
  `total` int(11) NULL DEFAULT NULL,
  `harga_jual` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_barang`(`id_barang`) USING BTREE,
  INDEX `id_pembelian`(`id_pembelian`) USING BTREE,
  CONSTRAINT `tb_pembelian_detail_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `tb_barang` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `tb_pembelian_detail_ibfk_2` FOREIGN KEY (`id_pembelian`) REFERENCES `tb_pembelian` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_pembelian_detail
-- ----------------------------
INSERT INTO `tb_pembelian_detail` VALUES (1, 1, 2, 'BRG0000002', 'Mintz Chewy Bag 125 gram', 50, 'Bungkus', 3000, 150000, 3600);
INSERT INTO `tb_pembelian_detail` VALUES (2, 1, 3, 'BRG0000003', 'Mr. Potato 60 gram', 50, 'Bungkus', 5000, 250000, 5400);
INSERT INTO `tb_pembelian_detail` VALUES (3, 1, 6, 'BRG0000006', 'Sari Wangi Green Tea 25 x 25 gram', 50, 'Box', 5500, 275000, 5900);
INSERT INTO `tb_pembelian_detail` VALUES (4, 1, 9, 'BRG0000009', 'Teh Pucuk Harum 480 mL', 50, 'Botol', 3000, 150000, 3250);
INSERT INTO `tb_pembelian_detail` VALUES (5, 2, 2, 'BRG0000002', 'Mintz Chewy Bag 125 gram', 100, 'Bungkus', 3000, 300000, 3600);
INSERT INTO `tb_pembelian_detail` VALUES (6, 2, 5, 'BRG0000005', 'TOP Coffee 20 x 25 gram', 100, 'Bungkus', 13600, 1360000, 13900);
INSERT INTO `tb_pembelian_detail` VALUES (7, 2, 7, 'BRG0000007', 'Sari Bunga Madu 650 mL', 20, 'Botol', 49000, 980000, 50000);
INSERT INTO `tb_pembelian_detail` VALUES (8, 2, 8, 'BRG0000008', 'Nusantara Madu Murni', 20, 'Botol', 61500, 1230000, 63000);
INSERT INTO `tb_pembelian_detail` VALUES (9, 3, 2, 'BRG0000002', 'Mintz Chewy Bag 125 gram', 50, 'Bungkus', 3000, 150000, 3600);
INSERT INTO `tb_pembelian_detail` VALUES (10, 3, 3, 'BRG0000003', 'Mr. Potato 60 gram', 50, 'Bungkus', 5000, 250000, 5400);
INSERT INTO `tb_pembelian_detail` VALUES (11, 3, 5, 'BRG0000005', 'TOP Coffee 20 x 25 gram', 50, 'Bungkus', 13600, 680000, 13900);

-- ----------------------------
-- Table structure for tb_penjualan
-- ----------------------------
DROP TABLE IF EXISTS `tb_penjualan`;
CREATE TABLE `tb_penjualan`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tanggal` date NULL DEFAULT NULL,
  `tunai_kredit` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tempo` int(11) NULL DEFAULT NULL,
  `jatuh_tempo` date NULL DEFAULT NULL,
  `id_pelanggan` int(11) NULL DEFAULT NULL,
  `id_sales` int(11) NULL DEFAULT NULL,
  `subtotal` int(11) NULL DEFAULT NULL,
  `pembulatan` int(11) NULL DEFAULT NULL,
  `diskon_persen` int(11) NULL DEFAULT NULL,
  `grand_total` int(11) NULL DEFAULT NULL,
  `tunai` int(11) NULL DEFAULT NULL,
  `kembali` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_pelanggan`(`id_pelanggan`) USING BTREE,
  INDEX `id_sales`(`id_sales`) USING BTREE,
  CONSTRAINT `tb_penjualan_ibfk_1` FOREIGN KEY (`id_pelanggan`) REFERENCES `tb_pelanggan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tb_penjualan_ibfk_2` FOREIGN KEY (`id_sales`) REFERENCES `tb_sales` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_penjualan
-- ----------------------------
INSERT INTO `tb_penjualan` VALUES (1, 'PENJ29102000001', '2020-10-29', 'TUNAI', NULL, '2020-10-29', 1, 1, 26000, 0, 0, 26000, 26000, 0);
INSERT INTO `tb_penjualan` VALUES (2, 'PENJ29102000002', '2020-10-29', 'TUNAI', NULL, '2020-10-29', 1, 3, 28800, 0, 0, 28800, 30000, 1200);
INSERT INTO `tb_penjualan` VALUES (3, 'PENJ01112000001', '2020-11-01', '3 Minggu', 21, '2020-11-22', 1, 4, 40100, 0, 0, 40100, NULL, NULL);

-- ----------------------------
-- Table structure for tb_penjualan_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_penjualan_detail`;
CREATE TABLE `tb_penjualan_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_penjualan` int(11) NULL DEFAULT NULL,
  `id_barang` int(11) NULL DEFAULT NULL,
  `kode_barang` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nama_barang` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `jumlah` int(11) NULL DEFAULT NULL,
  `satuan` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `harga_jual` int(11) NULL DEFAULT NULL,
  `diskon` int(11) NULL DEFAULT NULL,
  `harga_bersih` int(11) NULL DEFAULT NULL,
  `total` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_penjualan`(`id_penjualan`) USING BTREE,
  INDEX `id_barang`(`id_barang`) USING BTREE,
  CONSTRAINT `tb_penjualan_detail_ibfk_1` FOREIGN KEY (`id_penjualan`) REFERENCES `tb_penjualan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tb_penjualan_detail_ibfk_2` FOREIGN KEY (`id_barang`) REFERENCES `tb_barang` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_penjualan_detail
-- ----------------------------
INSERT INTO `tb_penjualan_detail` VALUES (1, 1, 1, 'BRG0000001', 'Tango Wafer 104 gram', 5, 'Bungkus', 5200, 0, 5200, 26000);
INSERT INTO `tb_penjualan_detail` VALUES (2, 2, 2, 'BRG0000002', 'Mintz Chewy Bag 125 gram', 2, 'Bungkus', 3600, 0, 3600, 7200);
INSERT INTO `tb_penjualan_detail` VALUES (3, 2, 3, 'BRG0000003', 'Mr. Potato 60 gram', 4, 'Bungkus', 5400, 0, 5400, 21600);
INSERT INTO `tb_penjualan_detail` VALUES (4, 3, 2, 'BRG0000002', 'Mintz Chewy Bag 125 gram', 2, 'Bungkus', 3600, 0, 3600, 7200);
INSERT INTO `tb_penjualan_detail` VALUES (5, 3, 4, 'BRG0000004', 'Vita Pudding 108 gram', 2, 'Bungkus', 2550, 0, 2550, 5100);
INSERT INTO `tb_penjualan_detail` VALUES (6, 3, 5, 'BRG0000005', 'TOP Coffee 20 x 25 gram', 2, 'Bungkus', 13900, 0, 13900, 27800);

-- ----------------------------
-- Table structure for tb_piutang
-- ----------------------------
DROP TABLE IF EXISTS `tb_piutang`;
CREATE TABLE `tb_piutang`  (
  `id` int(11) NOT NULL,
  `faktur_piutang` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_penjualan` int(11) NULL DEFAULT NULL,
  `tanggal` date NULL DEFAULT NULL,
  `tunai` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_penjualan`(`id_penjualan`) USING BTREE,
  CONSTRAINT `tb_piutang_ibfk_1` FOREIGN KEY (`id_penjualan`) REFERENCES `tb_penjualan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_piutang
-- ----------------------------
INSERT INTO `tb_piutang` VALUES (1, 'FP0111200000100001', 3, '2020-11-01', 20050);

-- ----------------------------
-- Table structure for tb_retur_pembelian
-- ----------------------------
DROP TABLE IF EXISTS `tb_retur_pembelian`;
CREATE TABLE `tb_retur_pembelian`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `no_retur` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tanggal` date NULL DEFAULT NULL,
  `id_pembelian` int(11) NULL DEFAULT NULL,
  `faktur` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `total_retur` int(11) NULL DEFAULT NULL,
  `total_dibayar` int(11) NULL DEFAULT NULL,
  `total_kurang_hutang` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_pembelian`(`id_pembelian`) USING BTREE,
  CONSTRAINT `tb_retur_pembelian_ibfk_1` FOREIGN KEY (`id_pembelian`) REFERENCES `tb_pembelian` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_retur_pembelian_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_retur_pembelian_detail`;
CREATE TABLE `tb_retur_pembelian_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_retur_pembelian` int(11) NULL DEFAULT NULL,
  `id_pembelian_detail` int(11) NULL DEFAULT NULL,
  `kode_barang` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nama_barang` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `jumlah` int(11) NULL DEFAULT NULL,
  `satuan` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `harga_beli` int(11) NULL DEFAULT NULL,
  `total` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_retur_pembelian`(`id_retur_pembelian`) USING BTREE,
  INDEX `id_pembelian_detail`(`id_pembelian_detail`) USING BTREE,
  CONSTRAINT `tb_retur_pembelian_detail_ibfk_1` FOREIGN KEY (`id_retur_pembelian`) REFERENCES `tb_retur_pembelian` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tb_retur_pembelian_detail_ibfk_2` FOREIGN KEY (`id_pembelian_detail`) REFERENCES `tb_pembelian_detail` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_retur_penjualan
-- ----------------------------
DROP TABLE IF EXISTS `tb_retur_penjualan`;
CREATE TABLE `tb_retur_penjualan`  (
  `id` int(11) NOT NULL,
  `id_penjualan` int(11) NULL DEFAULT NULL,
  `no_retur` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tanggal` date NULL DEFAULT NULL,
  `kode_penjualan` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `total_nilai` int(11) NULL DEFAULT NULL,
  `total_dibayar` int(11) NULL DEFAULT NULL,
  `total_kurang_piutang` int(11) NULL DEFAULT NULL,
  `laba_retur` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_penjualan`(`id_penjualan`) USING BTREE,
  CONSTRAINT `tb_retur_penjualan_ibfk_1` FOREIGN KEY (`id_penjualan`) REFERENCES `tb_penjualan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_retur_penjualan_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_retur_penjualan_detail`;
CREATE TABLE `tb_retur_penjualan_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_retur_penjualan` int(11) NULL DEFAULT NULL,
  `id_penjualan_detail` int(11) NULL DEFAULT NULL,
  `kode_barang` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nama_barang` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `jumlah` int(11) NULL DEFAULT NULL,
  `satuan` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `harga_beli` int(11) NULL DEFAULT NULL,
  `total` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_retur_pembelian`(`id_retur_penjualan`) USING BTREE,
  INDEX `id_pembelian_detail`(`id_penjualan_detail`) USING BTREE,
  CONSTRAINT `tb_retur_penjualan_detail_ibfk_1` FOREIGN KEY (`id_retur_penjualan`) REFERENCES `tb_retur_penjualan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tb_retur_penjualan_detail_ibfk_2` FOREIGN KEY (`id_penjualan_detail`) REFERENCES `tb_penjualan_detail` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_sales
-- ----------------------------
DROP TABLE IF EXISTS `tb_sales`;
CREATE TABLE `tb_sales`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kode` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nama` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `alamat` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `telepon` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `rekening` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `keterangan` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_sales
-- ----------------------------
INSERT INTO `tb_sales` VALUES (1, 'SALES00001', 'Abi Chandra', 'Jl. Mawar Gg. I no. 41 Malang', '089788497736', 'abichandra@gmail.com', NULL, NULL, NULL);
INSERT INTO `tb_sales` VALUES (2, 'SALES00002', 'Adrian', 'Jl. Sunandar Priyosudarmo no. 28 Malang', '085688379928', 'adrian@gmail.com', NULL, NULL, NULL);
INSERT INTO `tb_sales` VALUES (3, 'SALES00003', 'Ko Liem', 'Jl. Kelapa Sawit no. 77 A Malang', '082177468392', 'liem.ko@gmail.com', NULL, NULL, NULL);
INSERT INTO `tb_sales` VALUES (4, 'SALES00004', 'Wahyu Deddy', 'Villa Bukit Cemara Tidar B-40 Malang', '082288947753', 'd.wahyu@gmail.com', NULL, NULL, NULL);
INSERT INTO `tb_sales` VALUES (5, 'SALES00005', 'Daniel Budiman', 'Jl. Trisakti no. 11 A Malang', '081977568273', 'danibudi@gmail.com', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for tb_stok_barang
-- ----------------------------
DROP TABLE IF EXISTS `tb_stok_barang`;
CREATE TABLE `tb_stok_barang`  (
  `id` int(11) NOT NULL,
  `id_barang` int(11) NULL DEFAULT NULL,
  `stok_masuk` int(11) NULL DEFAULT NULL,
  `stok_keluar` int(11) NULL DEFAULT NULL,
  `keterangan` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_barang`(`id_barang`) USING BTREE,
  CONSTRAINT `tb_stok_barang_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `tb_barang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_stok_barang
-- ----------------------------
INSERT INTO `tb_stok_barang` VALUES (1, 2, 50, 0, 'Penambahan stok dari transaksi pembelian PEMB29102000001');
INSERT INTO `tb_stok_barang` VALUES (2, 3, 50, 0, 'Penambahan stok dari transaksi pembelian PEMB29102000001');
INSERT INTO `tb_stok_barang` VALUES (3, 6, 50, 0, 'Penambahan stok dari transaksi pembelian PEMB29102000001');
INSERT INTO `tb_stok_barang` VALUES (4, 9, 50, 0, 'Penambahan stok dari transaksi pembelian PEMB29102000001');
INSERT INTO `tb_stok_barang` VALUES (5, 2, 100, 0, 'Penambahan stok dari transaksi pembelian PEMB29102000002');
INSERT INTO `tb_stok_barang` VALUES (6, 5, 100, 0, 'Penambahan stok dari transaksi pembelian PEMB29102000002');
INSERT INTO `tb_stok_barang` VALUES (7, 7, 20, 0, 'Penambahan stok dari transaksi pembelian PEMB29102000002');
INSERT INTO `tb_stok_barang` VALUES (8, 8, 20, 0, 'Penambahan stok dari transaksi pembelian PEMB29102000002');
INSERT INTO `tb_stok_barang` VALUES (9, 1, 0, 5, 'Pengurangan stok dari transaksi penjualan 2020-10-29');
INSERT INTO `tb_stok_barang` VALUES (10, 2, 0, 2, 'Pengurangan stok dari transaksi penjualan 2020-10-29');
INSERT INTO `tb_stok_barang` VALUES (11, 3, 0, 4, 'Pengurangan stok dari transaksi penjualan 2020-10-29');
INSERT INTO `tb_stok_barang` VALUES (12, 2, 50, 0, 'Penambahan stok dari transaksi pembelian PEMB31102000001');
INSERT INTO `tb_stok_barang` VALUES (13, 3, 50, 0, 'Penambahan stok dari transaksi pembelian PEMB31102000001');
INSERT INTO `tb_stok_barang` VALUES (14, 5, 50, 0, 'Penambahan stok dari transaksi pembelian PEMB31102000001');
INSERT INTO `tb_stok_barang` VALUES (15, 2, 0, 2, 'Pengurangan stok dari transaksi penjualan 2020-11-01');
INSERT INTO `tb_stok_barang` VALUES (16, 4, 0, 2, 'Pengurangan stok dari transaksi penjualan 2020-11-01');
INSERT INTO `tb_stok_barang` VALUES (17, 5, 0, 2, 'Pengurangan stok dari transaksi penjualan 2020-11-01');

-- ----------------------------
-- Table structure for tb_stok_opname
-- ----------------------------
DROP TABLE IF EXISTS `tb_stok_opname`;
CREATE TABLE `tb_stok_opname`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_barang` int(11) NULL DEFAULT NULL,
  `stok_nyata` int(11) NULL DEFAULT NULL,
  `alasan` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tanggal` date NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_barang`(`id_barang`) USING BTREE,
  CONSTRAINT `tb_stok_opname_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `tb_barang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_supplier
-- ----------------------------
DROP TABLE IF EXISTS `tb_supplier`;
CREATE TABLE `tb_supplier`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kode` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nama` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `alamat` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `telepon` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `rekening` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `keterangan` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_supplier
-- ----------------------------
INSERT INTO `tb_supplier` VALUES (1, 'SUP0000001', 'Supplier 1', 'Jl. Tidar no. 100 Malang', '081234567890', 'supplier1@gmail.com', NULL, NULL, NULL);
INSERT INTO `tb_supplier` VALUES (2, 'SUP0000002', 'Supplier 2', 'Jl. Manyar no. 48 Malang', '081987654321', 'supplier2@gmail.com', NULL, NULL, NULL);
INSERT INTO `tb_supplier` VALUES (3, 'SUP0000003', 'Supplier 3', 'Jl. Kelapa Sawit no. 120 Malang', '088984727392', 'supplier3@gmail.com', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for tb_tipe
-- ----------------------------
DROP TABLE IF EXISTS `tb_tipe`;
CREATE TABLE `tb_tipe`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kode` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `keterangan` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_tipe
-- ----------------------------
INSERT INTO `tb_tipe` VALUES (1, 'TIPE000001', 'Kategori');
INSERT INTO `tb_tipe` VALUES (2, 'TIPE000002', 'Satuan');
INSERT INTO `tb_tipe` VALUES (3, 'TIPE000003', 'Metode Pembayaran');

SET FOREIGN_KEY_CHECKS = 1;
