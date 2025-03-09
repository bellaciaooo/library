package iss.biblioteca.dto;

import iss.biblioteca.domain.Status;

public class CarteImprumutDTO {
    Long idImprumut;
    String usernameAbonat;
    String titlu;
    String autor;
    Integer taxa;
    Status plata;

    public CarteImprumutDTO(Long idImprumut, String abonat, String titlu, String autor, Integer taxa, Status plata) {
        this.idImprumut = idImprumut;
        this.usernameAbonat = abonat;
        this.titlu = titlu;
        this.autor = autor;
        this.taxa = taxa;
        this.plata = plata;
    }

    public String getUsernameAbonat() {
        return usernameAbonat;
    }

    public void setUsernameAbonat(String usernameAbonat) {
        this.usernameAbonat = usernameAbonat;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getTaxa() {
        return taxa;
    }

    public void setTaxa(Integer taxa) {
        this.taxa = taxa;
    }

    public Status getPlata() {
        return plata;
    }

    public void setPlata(Status plata) {
        this.plata = plata;
    }

    public Long getIdImprumut() {
        return idImprumut;
    }

    public void setIdImprumut(Long idImprumut) {
        this.idImprumut = idImprumut;
    }
}
