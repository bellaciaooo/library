package iss.biblioteca.repo;

import iss.biblioteca.domain.Abonat;
import iss.biblioteca.domain.Carte;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AbonatDbRepo implements IAbonatRepo{
    private JdbcUtils dbUtils;

    public AbonatDbRepo(Properties properties) {
        System.out.println("Initializing AbonatDbRepo with properties: {} ");
        dbUtils=new JdbcUtils(properties);
    }

    @Override
    public Abonat findOne(Long aLong) {
        Connection connection = dbUtils.getConnection();
        Abonat abonat = null;
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from abonat where id = (?)")){
            preparedStatement.setLong(1,aLong);
            try(ResultSet result = preparedStatement.executeQuery()){
                if(result.next()){
                    Long id = result.getLong("id");
                    String username = result.getString("username");
                    String parola = result.getString("parola");
                    String nume = result.getString("nume");
                    String cnp = result.getString("cnp");
                    String telefon = result.getString("telefon");
                    String adresa = result.getString("adresa");
                    abonat = new Abonat(username,parola,nume,cnp,adresa,telefon);
                    abonat.setId(id);
                    return abonat;
                }
            }
        }catch(SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return abonat;
    }


    @Override
    public Iterable<Abonat> findAll() {
        Connection connection = dbUtils.getConnection();
        List<Abonat> abonati = new ArrayList<>();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from abonat")){
            try(ResultSet result = preparedStatement.executeQuery()){
                while(result.next()){
                    Long id = result.getLong("id");
                    String username = result.getString("username");
                    String parola = result.getString("parola");
                    String nume = result.getString("nume");
                    String cnp = result.getString("cnp");
                    String adresa = result.getString("adresa");
                    String telefon = result.getString("telefon");
                    Abonat abonat = new Abonat(username,parola,nume,cnp,adresa,telefon);
                    abonat.setId(id);
                    abonati.add(abonat);
                }
            }
        }catch(SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return abonati;
    }

    @Override
    public Abonat save(Abonat entity) {
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into abonat(username,parola,nume,cnp,adresa,telefon) values (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,entity.getUsername());
            preparedStatement.setString(2,entity.getParola());
            preparedStatement.setString(3,entity.getNume());
            preparedStatement.setString(4,entity.getCnp());
            preparedStatement.setString(5,entity.getAdresa());
            preparedStatement.setString(6,entity.getTelefon());
            int result = preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    entity.setId(id);
                    return entity;
                } else {
                    throw new Exception("Eroare la creare abonatului");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }catch (SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return null;
    }

    @Override
    public Abonat delete(Long aLong) {
        Connection connection = dbUtils.getConnection();
        Abonat abonat = findOne(aLong);
        if(abonat == null)
            throw new IllegalArgumentException("Abonatul nu exista!");
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete from abonat where id = (?)")){
            preparedStatement.setLong(1,aLong);
            int result = preparedStatement.executeUpdate();
        }catch (SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return abonat;
    }

    @Override
    public Abonat update(Abonat entity) {
        Connection connection = dbUtils.getConnection();
        if(findWithUsername(entity.getUsername()) == null)
            throw new IllegalArgumentException("Abonatul cu username-ul introdus nu exista!");
        try(PreparedStatement preparedStatement = connection.prepareStatement("update abonat set nume = (?),cnp = (?),adresa = (?), telefon = (?), parola = (?) where username = (?)")){
            preparedStatement.setString(1,entity.getNume());
            preparedStatement.setString(2,entity.getCnp());
            preparedStatement.setString(3,entity.getAdresa());
            preparedStatement.setString(4,entity.getTelefon());
            preparedStatement.setString(5,entity.getParola());
            preparedStatement.setString(6, entity.getUsername());
            int result = preparedStatement.executeUpdate();
        }catch (SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return entity;
    }

    @Override
    public Abonat logIn(String username, String parola) {
        Abonat abonat = null;
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from abonat where username = (?) and parola = (?)")){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2,parola);
            try(ResultSet result = preparedStatement.executeQuery()){
                while(result.next()){
                    Long id = result.getLong("id");
                    String usrname = result.getString("username");
                    String prl = result.getString("parola");
                    String nume = result.getString("nume");
                    String cnp = result.getString("cnp");
                    String adresa = result.getString("adresa");
                    String telefon = result.getString("telefon");
                    Abonat abonat1 = new Abonat(usrname,prl,nume,cnp,adresa,telefon);
                    abonat1.setId(id);
                    abonat = abonat1;
                }
            }
        }catch(SQLException exception){
            System.err.println("Error DB "+exception);
        }

        return abonat;
    }

    @Override
    public Abonat findWithUsername(String username) {
        Connection connection = dbUtils.getConnection();
        Abonat abonat = null;
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from abonat where username = (?)")){
            preparedStatement.setString(1,username);
            try(ResultSet result = preparedStatement.executeQuery()){
                if(result.next()){
                    Long id = result.getLong("id");
                    String parola = result.getString("parola");
                    String nume = result.getString("nume");
                    String cnp = result.getString("cnp");
                    String telefon = result.getString("telefon");
                    String adresa = result.getString("adresa");
                    abonat = new Abonat(username,parola,nume,cnp,adresa,telefon);
                    abonat.setId(id);
                    return abonat;
                }
            }
        }catch(SQLException exception){
            System.err.println("Error DB "+exception);
        }
        return abonat;
    }
}
