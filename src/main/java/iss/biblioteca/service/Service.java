package iss.biblioteca.service;

import iss.biblioteca.domain.*;
import iss.biblioteca.dto.CarteImprumutDTO;
import iss.biblioteca.repo.IAbonatRepo;
import iss.biblioteca.repo.IBibliotecarRepo;
import iss.biblioteca.repo.ICarteRepo;
import iss.biblioteca.repo.IImprumutRepo;

public class Service {
    private IAbonatRepo abonatRepo;
    private IBibliotecarRepo bibliotecarRepo;
    private ICarteRepo carteRepo;
    private IImprumutRepo imprumutRepo;

    public Service(IAbonatRepo abonatRepo, IBibliotecarRepo bibliotecarRepo, ICarteRepo carteRepo, IImprumutRepo imprumutRepo) {
        this.abonatRepo = abonatRepo;
        this.bibliotecarRepo = bibliotecarRepo;
        this.carteRepo = carteRepo;
        this.imprumutRepo = imprumutRepo;
    }

    public Iterable<Abonat> getAbonati(){
        return this.abonatRepo.findAll();
    }

    public Iterable<Carte> getCarti(){
        return this.carteRepo.findAll();
    }

    public Abonat loginAbonat(String username, String parola){
        Abonat abonat = this.abonatRepo.logIn(username,parola);
        if(abonat == null)
            throw new IllegalArgumentException("Nu exista abonatul cu credentialele introduse!");
        return abonat;
    }

    public Abonat saveAbonat(Abonat abonat){
        if(this.abonatRepo.findWithUsername(abonat.getUsername()) != null)
            throw new IllegalArgumentException("Exista deja abonat cu username-ul respectiv!");
        return this.abonatRepo.save(abonat);
    }
    public Abonat deleteAbonat(Abonat abonat){
        for(CarteImprumutDTO imprumut: this.getCartiImprumutateForAbonat(abonat.getId())){
            removeImprumut(imprumut.getIdImprumut());
        }
        return this.abonatRepo.delete(abonat.getId());
    }

    public Bibliotecar loginBibliotecar(String username, String parola){
        Bibliotecar bibliotecar = this.bibliotecarRepo.logIn(username,parola);
        if(bibliotecar == null)
            throw new IllegalArgumentException("Nu exista bibliotecarul cu credentialele introduse!");
        return bibliotecar;
    }

    public Iterable<CarteImprumutDTO> getCartiImprumutateForAbonat(Long idAbonat){
        return this.imprumutRepo.findCartiImprumutateForAbonat(idAbonat);
    }

    public Iterable<CarteImprumutDTO> getCartiImprumutate(){
        return this.imprumutRepo.findCartiImprumutate();
    }

    public Integer nrExemplareImprumutatePerCarte(Long idCarte){
        return this.imprumutRepo.nrExemplareImprumutatePerCarte(idCarte);
    }

    public Boolean plataTaxaImprumut(Long idImprumut){
        Imprumut imprumut = this.imprumutRepo.findOne(idImprumut);
        if(imprumut == null)
            throw new IllegalArgumentException("Nu exista imprumutul respectiv!");
        else if(imprumut.getPlata() == Status.efectuata)
            throw new IllegalArgumentException("Plata a fost deja efectuata!");
        return this.imprumutRepo.plataTaxa(idImprumut);
    }

    public Imprumut addImprumut(Imprumut imprumut){
        Carte carte = imprumut.getCarte();
        if(carte.getNrExemplare() <= nrExemplareImprumutatePerCarte(carte.getId())){
            throw new IllegalArgumentException("Nu mai este disponibila momentan cartea!");
        }
        this.imprumutRepo.save(imprumut);
        return imprumut;
    }

    public Imprumut removeImprumut(Long idImprumut){
        Imprumut imprumut = this.imprumutRepo.findOne(idImprumut);
        if(imprumut == null)
            throw new IllegalArgumentException("Nu exista imprumutul respectiv!");
        else if(imprumut.getPlata() == Status.neefectuata)
            throw new IllegalArgumentException("Plata imprumutului nu a fost efectuata!");
        return this.imprumutRepo.delete(idImprumut);
    }

    public Abonat updateAbonat(Abonat abonat) {
        return this.abonatRepo.update(abonat);
    }
}
