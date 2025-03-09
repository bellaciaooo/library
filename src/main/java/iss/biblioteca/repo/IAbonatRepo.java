package iss.biblioteca.repo;

import iss.biblioteca.domain.Abonat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface IAbonatRepo extends Repository<Long, Abonat> {
    Abonat logIn(String username,String parola);
    Abonat findWithUsername(String username);
    Optional a = Optional.ofNullable(null);
    List<String> t = new ArrayList<>(Arrays.asList("1","1"));

}
