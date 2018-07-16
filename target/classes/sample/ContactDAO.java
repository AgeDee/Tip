package sample;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactDAO {

    static final String DB_URL = "jdbc:mysql://tipdb.czufm3rgmejc.eu-central-1.rds.amazonaws.com:3306/tip_db";
    //  Database credentials
    static final String USER = "db_user"; //TUTAJ PODAC LOGIN DO BAZY DANYCH
    static final String PASS = "T!Pprojekt135"; //TUTAJ PODAC HASLO DO BAZY DANYCH
    private NamedParameterJdbcTemplate jdbc;

    public ContactDAO(){
        try {

            DataSource dataSource = DBUtil.getDataSource(DB_URL, USER, PASS);
            this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public List<Contact> getAll() {
        String sql = "SELECT * FROM contacts";
        return jdbc.query(sql, new ContactRowMapper());
    }

    public Contact findById(int id){
        String sql = "SELECT * FROM contacts WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        Contact contact = jdbc.queryForObject(sql, namedParameters, new ContactRowMapper());
        return contact;
    }

    public List<Contact> findByUserId(int user_id){
        String sql = "SELECT * FROM contacts WHERE user_id = "+user_id;
        return jdbc.query(sql, new ContactRowMapper());
    }

    public List<Contact> findByContactId(int contact_id){
        String sql = "SELECT * FROM contacts WHERE contact_id = "+contact_id;
        return jdbc.query(sql, new ContactRowMapper());
    }

    public void deleteContactById(int id){
        String sql = "DELETE FROM contacts WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        jdbc.update(sql, namedParameters);
    }

    public void updateContactById(int id, int user_id, int contact_id){
        String sql = "UPDATE contacts SET user_id = :user_id, contact_id = :contact_id WHERE id = :id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("user_id", user_id);
        namedParameters.addValue("contact_id", contact_id);
        namedParameters.addValue("id", id);
        jdbc.update(sql, namedParameters);

    }

    public void create(Contact contact){
        String sql = "INSERT INTO contacts(user_id,contact_id)" +
                "VALUES (:user_id, :contact_id)";
        Map namedParameters = new HashMap();
        namedParameters.put("user_id", contact.getUser_id());
        namedParameters.put("contact_id", contact.getContact_id());
        jdbc.update(sql, namedParameters);
        System.out.println("Created record with: " + contact.toString());
    }

}

class ContactRowMapper implements RowMapper<Contact> {
    @Override
    public Contact mapRow(ResultSet resultSet, int i) throws SQLException {

        Contact contact = new Contact();
        contact.setId(resultSet.getInt("id"));
        contact.setUser_id(resultSet.getInt("user_id"));
        contact.setContact_id(resultSet.getInt("contact_id"));

        return contact;
    }
}
