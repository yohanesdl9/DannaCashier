/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Yohanes Dwi Listio
 */
public class ReturPembelianDetail {
    private String id;
    private String id_retur_pembelian;
    private String id_pembelian_detail;
    private String kode_barang;
    private String nama_barang;
    private String jumlah;
    private String satuan;
    private String isi;
    private String total_isi;
    private String harga_beli;
    private String total;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_retur_pembelian() {
        return id_retur_pembelian;
    }

    public void setId_retur_pembelian(String id_retur_pembelian) {
        this.id_retur_pembelian = id_retur_pembelian;
    }

    public String getId_pembelian_detail() {
        return id_pembelian_detail;
    }

    public void setId_pembelian_detail(String id_pembelian_detail) {
        this.id_pembelian_detail = id_pembelian_detail;
    }

    public String getKode_barang() {
        return kode_barang;
    }

    public void setKode_barang(String kode_barang) {
        this.kode_barang = kode_barang;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getTotal_isi() {
        return total_isi;
    }

    public void setTotal_isi(String total_isi) {
        this.total_isi = total_isi;
    }

    public String getHarga_beli() {
        return harga_beli;
    }

    public void setHarga_beli(String harga_beli) {
        this.harga_beli = harga_beli;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    
    
}
