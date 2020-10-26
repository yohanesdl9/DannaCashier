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
public class Barang {
    private String id;
    private String kode;
    private String nama;
    private String id_kategori;
    private String id_satuan;
    private String stok;
    private String harga_beli;
    private String harga_jual;
    private String id_supplier;
    private String stok_minimal;
    private String tgl_expired;
    private String lokasi;
    private String diskon_nominal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(String id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getId_satuan() {
        return id_satuan;
    }

    public void setId_satuan(String id_satuan) {
        this.id_satuan = id_satuan;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getHarga_beli() {
        return harga_beli;
    }

    public void setHarga_beli(String harga_beli) {
        this.harga_beli = harga_beli;
    }

    public String getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(String harga_jual) {
        this.harga_jual = harga_jual;
    }

    public String getId_supplier() {
        return id_supplier;
    }

    public void setId_supplier(String id_supplier) {
        this.id_supplier = id_supplier;
    }

    public String getStok_minimal() {
        return stok_minimal;
    }

    public void setStok_minimal(String stok_minimal) {
        this.stok_minimal = stok_minimal;
    }

    public String getTgl_expired() {
        return tgl_expired;
    }

    public void setTgl_expired(String tgl_expired) {
        this.tgl_expired = tgl_expired;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getDiskon_nominal() {
        return diskon_nominal;
    }

    public void setDiskon_nominal(String diskon_nominal) {
        this.diskon_nominal = diskon_nominal;
    }
    
    
}
