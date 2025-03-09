package iss.biblioteca.repo;

import iss.biblioteca.domain.Bibliotecar;

public interface IBibliotecarRepo extends Repository<Long, Bibliotecar> {
    Bibliotecar logIn(String username,String parola);

}
