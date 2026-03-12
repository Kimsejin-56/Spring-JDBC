package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import java.sql.SQLException;

/**
 * 특정 기술에 종속되는 인터페이스
 * 인터페이스를 만드는 목적은 구현체를 쉽게 변경하기 위함인데, 이미 인터페이스가 특정 구현 기술에 의존
 * 향후 다른 기술로 변경 시에 인터페이스 수정 (OCP 위반)
*/
public interface MemberRepositoryEx {
    Member save(Member member) throws SQLException;
    Member findById(String memberId) throws SQLException;
    void update(String memberId, int money) throws SQLException;
    void delete(String memberId) throws SQLException;
}