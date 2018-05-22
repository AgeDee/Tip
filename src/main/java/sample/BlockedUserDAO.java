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


public class BlockedUserDAO {

    static final String DB_URL = "jdbc:mysql://tipdb.czufm3rgmejc.eu-central-1.rds.amazonaws.com:3306/tip_db";
    //  Database credentials
    static final String USER = ""; //TUTAJ PODAC LOGIN DO BAZY DANYCH
    static final String PASS = ""; //TUTAJ PODAC HASLO DO BAZY DANYCH
    private NamedParameterJdbcTemplate jdbc;

    public BlockedUserDAO(){
        try {
            DataSource dataSource = DBUtil.getDataSource(DB_URL, USER, PASS);
            this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public List<BlockedUser> getAll() {
        String sql = "SELECT * FROM blocked_users";
        return jdbc.query(sql, new BlockedUserRowMapper());
    }

    public BlockedUser findById(int id){
        String sql = "SELECT * FROM blocked_users WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        BlockedUser user = jdbc.queryForObject(sql, namedParameters, new BlockedUserRowMapper());
        return user;
    }

    public List<BlockedUser> getByUserId(int userId){
        String sql = "SELECT * FROM blocked_users WHERE user_id = "+userId;
        return jdbc.query(sql, new BlockedUserRowMapper());
    }

    public List<BlockedUser> getByBlockedId(int blockedId){
        String sql = "SELECT * FROM blocked_users WHERE user_id = "+blockedId;
        return jdbc.query(sql, new BlockedUserRowMapper());
    }

    public void deleteById(int id){
        String sql = "DELETE FROM blocked_users WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        jdbc.update(sql, namedParameters);
    }

    public void updateById(int id, int userId, int blockedId, String date){
        String sql = "UPDATE users SET userId = :userId, blockedId = :blockedId, date = :date WHERE id = :id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        namedParameters.addValue("blockedId", blockedId);
        namedParameters.addValue("date", date);
        namedParameters.addValue("id", id);
        jdbc.update(sql, namedParameters);
    }

    public void create(BlockedUser user){
        String sql = "INSERT INTO blocked_users(user_id,blocked_id,date)" +
                "VALUES (:user_id, :blocked_id, CURRENT_TIMESTAMP)";
        Map namedParameters = new HashMap();
        namedParameters.put("user_id", user.getUserId());
        namedParameters.put("blocked_id", user.getBlockedId());
        jdbc.update(sql, namedParameters);
        System.out.println("Created record with: " + user.toString());
    }
}

class BlockedUserRowMapper implements RowMapper<BlockedUser> {
    @Override
    public BlockedUser mapRow(ResultSet resultSet, int i) throws SQLException {

        BlockedUser blockedUser = new BlockedUser();
        blockedUser.setId(resultSet.getInt("id"));
        blockedUser.setUserId(resultSet.getInt("user_id"));
        blockedUser.setBlockedId(resultSet.getInt("blocked_id"));
        blockedUser.setDate(resultSet.getString("date"));

        return blockedUser;
    }
}
