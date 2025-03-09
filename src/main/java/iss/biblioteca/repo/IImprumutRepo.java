package iss.biblioteca.repo;

import iss.biblioteca.domain.Carte;
import iss.biblioteca.domain.Imprumut;
import iss.biblioteca.dto.CarteImprumutDTO;

public interface IImprumutRepo extends Repository<Long, Imprumut>{
    Iterable<CarteImprumutDTO> findCartiImprumutateForAbonat(Long idAbonat);
    Iterable<CarteImprumutDTO> findCartiImprumutate();
    Integer nrExemplareImprumutatePerCarte(Long idCarte);
    Boolean plataTaxa(Long idImprumut);
}
