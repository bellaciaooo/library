package iss.biblioteca.repo;

import iss.biblioteca.domain.Carte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarteDbRepo implements ICarteRepo{
    private JdbcUtils dbUtils;

    public CarteDbRepo(Properties properties) {
        System.out.println("Initializing CarteDbRepo with properties: {} ");
        dbUtils=new JdbcUtils(properties);
    }

    @Override
    public Carte findOne(Long aLong) {
        Connection connection = dbUtils.getConnection();
        Carte the_cartea = null;
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from carte where id = (?)")){
            preparedStatement.setLong(1,aLong);
            try(ResultSet result = preparedStatement.executeQuery()){
                while(result.next()){
                    Long id = result.getLong("id");
                    String titlu = result.getString("titlu");
                    String autor = result.getString("autor");
                    Integer nrEx = result.getInt("exemplare");
                    Integer taxa = result.getInt("taxa");
                    Carte carte = new Carte(titlu,autor,nrEx,taxa);
                    carte.setId(id);
                    the_cartea = carte;
                }
            }
        }catch(SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return the_cartea;
    }

    @Override
    public Iterable<Carte> findAll() {
        Connection connection = dbUtils.getConnection();
        List<Carte> carti = new ArrayList<>();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from carte")){
            try(ResultSet result = preparedStatement.executeQuery()){
                while(result.next()){
                    Long id = result.getLong("id");
                    String titlu = result.getString("titlu");
                    String autor = result.getString("autor");
                    Integer nrEx = result.getInt("exemplare");
                    Integer taxa = result.getInt("taxa");
                    Carte carte = new Carte(titlu,autor,nrEx,taxa);
                    carte.setId(id);
                    carti.add(carte);
                }
            }
        }catch(SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return carti;
    }

    @Override
    public Carte save(Carte entity) {
        return null;
    }

    @Override
    public Carte delete(Long aLong) {
        return null;
    }

    @Override
    public Carte update(Carte entity) {
        return null;
    }
}
