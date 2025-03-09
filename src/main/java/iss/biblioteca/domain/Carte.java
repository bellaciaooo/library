package iss.biblioteca.domain;

public class Carte extends Entity<Long>{
    String titlu;
    String autor;
    Integer nrExemplare;
    Integer taxa;

    public Carte(String titlu, String autor, Integer nrExemplare, Integer taxa) {
        this.titlu = titlu;
        this.autor = autor;
        this.nrExemplare = nrExemplare;
        this.taxa = taxa;
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

    public Integer getNrExemplare() {
        return nrExemplare;
    }

    public void setNrExemplare(Integer nrExemplare) {
        this.nrExemplare = nrExemplare;
    }

    public Integer getTaxa() {
        return taxa;
    }

    public void setTaxa(Integer taxa) {
        this.taxa = taxa;
    }

    @Override
    public String toString() {
        return "Carte{" +
                "titlu='" + titlu + '\'' +
                ", autor='" + autor + '\'' +
                ", nrExemplare=" + nrExemplare +
                ", taxa=" + taxa +
                '}';
    }
}
