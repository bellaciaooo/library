package iss.biblioteca.repo;

import iss.biblioteca.domain.Bibliotecar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class BibliotecarDbRepo implements IBibliotecarRepo{
    private JdbcUtils dbUtils;

    public BibliotecarDbRepo(Properties properties) {
        System.out.println("Initializing BibliotecarDbRepo with properties: {} ");
        dbUtils=new JdbcUtils(properties);
    }

    @Override
    public Bibliotecar findOne(Long aLong) {
        return null;
    }

    @Override
    public Iterable<Bibliotecar> findAll() {
        return null;
    }

    @Override
    public Bibliotecar save(Bibliotecar entity) {
        return null;
    }

    @Override
    public Bibliotecar delete(Long aLong) {
        return null;
    }

    @Override
    public Bibliotecar update(Bibliotecar entity) {
        return null;
    }

    @Override
    public Bibliotecar logIn(String username, String parola) {
        Bibliotecar bibliotecar = null;
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from bibliotecar where username = (?) and parola = (?)")){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2,parola);
            try(ResultSet result = preparedStatement.executeQuery()){
                while(result.next()){
                    Long id = result.getLong("id");
                    String usrname = result.getString("username");
                    String prl = result.getString("parola");
                    Bibliotecar bibliotecar1 = new Bibliotecar(usrname, prl);
                    bibliotecar1.setId(id);
                    bibliotecar = bibliotecar1;
                }
            }
        }catch(SQLException exception){
            System.err.println("Error DB "+exception);
        }

        return bibliotecar;
    }
}
