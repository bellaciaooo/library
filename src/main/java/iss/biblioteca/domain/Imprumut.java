package iss.biblioteca.domain;

public class Imprumut extends Entity<Long>{
    Abonat abonat;
    Carte carte;
    Status plata;

    public Imprumut(Abonat abonat, Carte carte, Status plata) {
        this.abonat = abonat;
        this.carte = carte;
        this.plata = plata;
    }

    public Abonat getAbonat() {
        return abonat;
    }

    public void setAbonat(Abonat abonat) {
        this.abonat = abonat;
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }

    public Status getPlata() {
        return plata;
    }

    public void setPlata(Status plata) {
        this.plata = plata;
    }

    @Override
    public String toString() {
        return "Imprumut{" +
                "abonat=" + abonat +
                ", carte=" + carte +
                ", plata=" + plata +
                '}';
    }
}
