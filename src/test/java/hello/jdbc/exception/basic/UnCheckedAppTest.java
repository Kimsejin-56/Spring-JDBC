package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

/**
 * 2가지 문제
 * 1. 복구 불가능한 예외 해결(처리 못하는 예외 무시)
 * 2. 의존 관계 문제 해결(처리 못하면 무시하기에 기술이 변경되어도 controller, service 코드 수정 X)
 */
@Slf4j
public class UnCheckedAppTest {

    @Test
    void unChecked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }

    //예외를 전환할 때는 꼭 기존 예외를 포함하자
    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            // e.printStackTreace(); 추천X, 로그 방식 사용하자
            log.info("ex", e);
        }
    }

    //예외 처리 못해서 밖으로 던짐
    static class Controller{
        Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    //예외 처리 못해서 밖으로 던짐
    static class Service{
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class Repository{
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) { //checkException -> unCheckException 전환
                throw new RuntimeSQLException(e); //기존 예외 포함
            }
        }

        private void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class NetworkClient{
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
