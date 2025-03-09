package iss.biblioteca.domain;

public class Bibliotecar extends Utilizator{
    public Bibliotecar(String username, String parola) {
        super(username, parola);
    }

    @Override
    public String toString() {
        return "Bibliotecar{}";
    }
}
