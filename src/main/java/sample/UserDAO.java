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


public class UserDAO {

    static final String DB_URL = "jdbc:mysql://tipdb.czufm3rgmejc.eu-central-1.rds.amazonaws.com:3306/tip_db";
    //  Database credentials
    static final String USER = ""; //TUTAJ PODAC LOGIN DO BAZY DANYCH
    static final String PASS = ""; //TUTAJ PODAC HASLO DO BAZY DANYCH
    private NamedParameterJdbcTemplate jdbc;

    public UserDAO(){
        try {

            DataSource dataSource = DBUtil.getDataSource(DB_URL, USER, PASS);
            this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        return jdbc.query(sql, new UserRowMapper());
    }

    public User findByUserId(int id){
        String sql = "SELECT * FROM users WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        User user = (User) jdbc.queryForObject(sql, namedParameters, new UserRowMapper());
        return user;
    }

    public User findByUserLogin(String login){
        String sql = "SELECT * FROM users WHERE login = :login";
        SqlParameterSource namedParameters = new MapSqlParameterSource("login", login);
        try {
            User user = (User) jdbc.queryForObject(sql, namedParameters, new UserRowMapper());
            return user;
        }catch (Exception ex){
            return null;
        }
    }

    public User findByUserEmail(String email){
        String sql = "SELECT * FROM users WHERE email = :email";
        SqlParameterSource namedParameters = new MapSqlParameterSource("email", email);
        try {
            User user = (User) jdbc.queryForObject(sql, namedParameters, new UserRowMapper());
            return user;
        }catch (Exception ex){
            return null;
        }
    }


    public List<User> searchByUserLogin(String term){
        String sql = "SELECT * FROM users WHERE login LIKE '%"+term+"%'";
        return jdbc.query(sql, new UserRowMapper());
    }

    public void deleteUserById(int id){
        String sql = "DELETE FROM users WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        jdbc.update(sql, namedParameters);
    }

    public void updateUserById(int id, String login, String password, String email){
        String sql = "UPDATE users SET login = :login, password = :password, email = :email WHERE id = :id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("login", login);
        namedParameters.addValue("password", password);
        namedParameters.addValue("email", email);
        namedParameters.addValue("id", id);
        jdbc.update(sql, namedParameters);

    }

    public void create(User user){
        String sql = "INSERT INTO users(login,password,email)" +
                "VALUES (:login, :password, :email)";
        Map namedParameters = new HashMap();
        namedParameters.put("login", user.getLogin());
        namedParameters.put("password", user.getPassword());
        namedParameters.put("email",user.getEmail());
        jdbc.update(sql, namedParameters);
        System.out.println("Created record with: " + user.toString());
    }



}

class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {

        User user = new User();
        user.setUserId(resultSet.getInt("id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));

        return user;
    }
}
