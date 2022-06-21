package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/*
* JDBC - DriverManager 사용해서 저장하기
* */
@Slf4j
public class MemberRepositoryV0 {

    //저장
    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?, ?)";

        Connection con = null; //얘가 있어야 연결
        PreparedStatement pstmt = null; //얘를 가지고 db에 쿼리 날리기
        
        try {
            //연결하고
            con = getConnection(); //다른 곳에서 또 쓸 것 같아서 메소드로 따로 빼놓았음
            //sql 던지기
            pstmt = con.prepareStatement(sql);
            //sql에 대한 파라미터 바인딩
            pstmt.setString(1, member.getMemberId()); //첫 번째 ?에 memberId
            pstmt.setInt(2, member.getMoney()); //두 번째 ?에 money
            pstmt.executeUpdate(); //준비된 쿼리를 실제 db에 실행하기
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e; //예외 밖으로 던지기
        } finally {
            close(con, pstmt, null);
        }
    }

    //조회
    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        //밖에 선언하는 이유: try catch 때문에 어쩔 수 없음. finally 에서 호출해야 하기 때문.
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null; //select 쿼리의 결과를 담고 있는 통이라고 생각하기

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            //executeUpdate: 변경
            //executeQuery: 조회
            rs = pstmt.executeQuery();

            if (rs.next()) { //next로 한 번 호출해줘야 실제 데이터가 있는 지점부터 시작
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else { //데이터가 없을 때
                throw new NoSuchElementException("member not found. memberId = " + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    //수정
    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = " + resultSize);
        } catch (SQLException e) {
            log.info("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    //삭제
    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.info("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    //Statement: sql을 그냥 그대로 넣는 거
    //PreparedStatement: 파라미터를 바인딩 할 수 있는 거
    private void close(Connection con, Statement stmt, ResultSet rs) {

        //리소스는 항상 역순으로 정리
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if(con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
