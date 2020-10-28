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
public class ViewReturPenjualan {
    private String no;
    private String faktur;
    private String tanggal;
    private String kode_pelanggan;
    private String nama_pelanggan;
    private String total_nilai_retur;
    private String total_dibayar;
    private String total_mengurangi_piutang;
    private String operator;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getFaktur() {
        return faktur;
    }

    public void setFaktur(String faktur) {
        this.faktur = faktur;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKode_pelanggan() {
        return kode_pelanggan;
    }

    public void setKode_pelanggan(String kode_pelanggan) {
        this.kode_pelanggan = kode_pelanggan;
    }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public String getTotal_nilai_retur() {
        return total_nilai_retur;
    }

    public void setTotal_nilai_retur(String total_nilai_retur) {
        this.total_nilai_retur = total_nilai_retur;
    }

    public String getTotal_dibayar() {
        return total_dibayar;
    }

    public void setTotal_dibayar(String total_dibayar) {
        this.total_dibayar = total_dibayar;
    }

    public String getTotal_mengurangi_piutang() {
        return total_mengurangi_piutang;
    }

    public void setTotal_mengurangi_piutang(String total_mengurangi_piutang) {
        this.total_mengurangi_piutang = total_mengurangi_piutang;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
