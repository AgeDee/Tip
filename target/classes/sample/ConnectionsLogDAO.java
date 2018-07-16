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


public class ConnectionsLogDAO {

    static final String DB_URL = "jdbc:mysql://tipdb.czufm3rgmejc.eu-central-1.rds.amazonaws.com:3306/tip_db";
    //  Database credentials
    static final String USER = "db_user"; //TUTAJ PODAC LOGIN DO BAZY DANYCH
    static final String PASS = "T!Pprojekt135"; //TUTAJ PODAC HASLO DO BAZY DANYCH
    private NamedParameterJdbcTemplate jdbc;

    public ConnectionsLogDAO(){
        try {

            DataSource dataSource = DBUtil.getDataSource(DB_URL, USER, PASS);
            this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public List<ConnectionLog> getAll() {
        String sql = "SELECT * FROM connections_log";
        return jdbc.query(sql, new ConnectionsLogRowMapper());
    }

    public ConnectionLog findById(int id){
        String sql = "SELECT * FROM connections_log WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        ConnectionLog connectionLog = jdbc.queryForObject(sql, namedParameters, new ConnectionsLogRowMapper());
        return connectionLog;
    }

    public List<ConnectionLog> findByUser1Id(int user1_id){
        String sql = "SELECT * FROM connections_log WHERE user1_id = "+user1_id;
        return jdbc.query(sql, new ConnectionsLogRowMapper());
    }

    public List<ConnectionLog> findByUser2Id(int user2_id){
        String sql = "SELECT * FROM connections_log WHERE user2_id = "+user2_id;
        return jdbc.query(sql, new ConnectionsLogRowMapper());
    }

    public void deleteConnecionLogById(int id){
        String sql = "DELETE FROM connections_log WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        jdbc.update(sql, namedParameters);
    }

    public void updateConnectionLogById(int id, int user1_id, int user2_id, String date, String description){
        String sql = "UPDATE contacts SET user1_id = :user1_id, user1_id = :user1_id, date = :date, description = :description WHERE id = :id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("user1_id", user1_id);
        namedParameters.addValue("user2_id", user2_id);
        namedParameters.addValue("date", date);
        namedParameters.addValue("description", description);
        namedParameters.addValue("id", id);
        jdbc.update(sql, namedParameters);

    }

    public void create(ConnectionLog connectionLog){
        String sql = "INSERT INTO connections_log(user1_id, user2_id, date, description)" +
                "VALUES (:user1_id, :user2_id, CURRENT_TIMESTAMP , :description)";
        Map namedParameters = new HashMap();
        namedParameters.put("user1_id", connectionLog.getUser1Id());
        namedParameters.put("user2_id", connectionLog.getUser2Id());
        namedParameters.put("description", connectionLog.getDescription());
        jdbc.update(sql, namedParameters);
        System.out.println("Created record with: " + connectionLog.toString());
    }

}

class ConnectionsLogRowMapper implements RowMapper<ConnectionLog> {
    @Override
    public ConnectionLog mapRow(ResultSet resultSet, int i) throws SQLException {

        ConnectionLog connectionLog = new ConnectionLog();
        connectionLog.setId(resultSet.getInt("id"));
        connectionLog.setUser1Id(resultSet.getInt("user1_id"));
        connectionLog.setUser2Id(resultSet.getInt("user2_id"));
        connectionLog.setDate(resultSet.getString("date"));
        connectionLog.setDescription(resultSet.getString("description"));

        return connectionLog;
    }
}
