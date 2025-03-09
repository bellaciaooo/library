package iss.biblioteca.repo;

import iss.biblioteca.domain.Abonat;
import iss.biblioteca.domain.Carte;
import iss.biblioteca.domain.Imprumut;
import iss.biblioteca.domain.Status;
import iss.biblioteca.dto.CarteImprumutDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ImprumutDbRepo implements IImprumutRepo{
    private JdbcUtils dbUtils;
    private CarteDbRepo repoCarte;
    private AbonatDbRepo repoAbonat;

    public ImprumutDbRepo(Properties properties, CarteDbRepo carteDbRepo, AbonatDbRepo abonatDbRepo) {
        System.out.println("Initializing ImprumutDbRepo with properties: {} ");
        dbUtils=new JdbcUtils(properties);
        this.repoCarte = carteDbRepo;
        this.repoAbonat = abonatDbRepo;
    }

    @Override
    public Imprumut findOne(Long aLong) {
        Connection connection = dbUtils.getConnection();
        Imprumut imprumut = null;
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from imprumut where id = (?)")){
            preparedStatement.setLong(1,aLong);
            try(ResultSet result = preparedStatement.executeQuery()){
                if(result.next()){
                    Long id = result.getLong("id");
                    Long id_abonat = result.getLong("id_abonat");
                    Long id_carte = result.getLong("id_carte");
                    Status plata = Status.valueOf(result.getString("plata"));
                    Carte carte = this.repoCarte.findOne(id_carte);
                    Abonat abonat = this.repoAbonat.findOne(id_abonat);
                    imprumut = new Imprumut(abonat,carte,plata);
                    imprumut.setId(id);
                    return imprumut;
                }
            }
        }catch(SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return imprumut;
    }

    @Override
    public Iterable<Imprumut> findAll() {
        return null;
    }

    @Override
    public Imprumut save(Imprumut entity) {
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into imprumut(id_abonat,id_carte,plata) values (?,?,?)")){
            preparedStatement.setLong(1,entity.getAbonat().getId());
            preparedStatement.setLong(2,entity.getCarte().getId());
            preparedStatement.setString(3,entity.getPlata().toString());
            int result = preparedStatement.executeUpdate();
        }catch (SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return entity;
    }

    @Override
    public Imprumut delete(Long aLong) {
        Connection connection = dbUtils.getConnection();
        /*Imprumut imprumut = findOne(aLong);
        if(imprumut == null)
            throw new IllegalArgumentException("Imprumutul nu exista!");*/
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete from imprumut where id = (?)")){
            preparedStatement.setLong(1,aLong);
            int result = preparedStatement.executeUpdate();
        }catch (SQLException exception){
            System.err.println("Error DB "+exception);
        }
        //return imprumut;
        return null;
    }

    @Override
    public Imprumut update(Imprumut entity) {
        return null;
    }

    @Override
    public Iterable<CarteImprumutDTO> findCartiImprumutateForAbonat(Long idAbonat) {
        Connection connection = dbUtils.getConnection();
        List<CarteImprumutDTO> carti = new ArrayList<>();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from imprumut where id_abonat = (?)")){
            preparedStatement.setLong(1,idAbonat);
            try(ResultSet result = preparedStatement.executeQuery()){
                while(result.next()){
                    Long id = result.getLong("id");
                    Long id_carte = result.getLong("id_carte");
                    Status plata = Status.valueOf(result.getString("plata"));
                    Carte carte = this.repoCarte.findOne(id_carte);
                    Abonat abonat = this.repoAbonat.findOne(idAbonat);
                    CarteImprumutDTO imprumutDTO = new CarteImprumutDTO(id,abonat.getUsername(),carte.getTitlu(),carte.getAutor(),carte.getTaxa(),plata);
                    carti.add(imprumutDTO);
                }
            }
        }catch(SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return carti;
    }

    @Override
    public Iterable<CarteImprumutDTO> findCartiImprumutate() {
        Connection connection = dbUtils.getConnection();
        List<CarteImprumutDTO> carti = new ArrayList<>();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from imprumut")){
            try(ResultSet result = preparedStatement.executeQuery()){
                while(result.next()){
                    Long id = result.getLong("id");
                    Long id_abonat = result.getLong("id_abonat");
                    Long id_carte = result.getLong("id_carte");
                    Status plata = Status.valueOf(result.getString("plata"));
                    Carte carte = this.repoCarte.findOne(id_carte);
                    Abonat abonat = this.repoAbonat.findOne(id_abonat);
                    CarteImprumutDTO imprumutDTO = new CarteImprumutDTO(id,abonat.getUsername(),carte.getTitlu(),carte.getAutor(),carte.getTaxa(),plata);
                    carti.add(imprumutDTO);
                }
            }
        }catch(SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return carti;
    }

    @Override
    public Integer nrExemplareImprumutatePerCarte(Long idCarte) {
        Connection connection = dbUtils.getConnection();
        Integer nrExemplareImprumutate = 0;
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from imprumut where id_carte = (?)")){
            preparedStatement.setLong(1,idCarte);
            try(ResultSet result = preparedStatement.executeQuery()){
                while(result.next()){
                    nrExemplareImprumutate++;
                }
            }
        }catch(SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return nrExemplareImprumutate;
    }

    @Override
    public Boolean plataTaxa(Long idImprumut) {
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("update imprumut set plata = (?) where id = (?)")){
            preparedStatement.setString(1,"efectuata");
            preparedStatement.setLong(2, idImprumut);
            int result = preparedStatement.executeUpdate();
            return true;
        }catch (SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return false;
    }
}
