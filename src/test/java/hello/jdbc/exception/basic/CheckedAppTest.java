package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

@Slf4j
public class CheckedAppTest {

    /**
     * 2가지 문제
     * 1. 복구 불가능한 예외(대부분 예외 -> 시스템 예외)
     * 2. 의존 관계 문제 (OCP. DI 위반 모델 교체 시 코드 대거 수정)
     */
    @Test
    void checked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }

    //예외 처리 못해서 밖으로 던짐
    static class Controller{
        Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }

    //예외 처리 못해서 밖으로 던짐
    static class Service{
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() throws ConnectException, SQLException {
            repository.call();
            networkClient.call();
        }
    }

    static class Repository{
        public void call() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class NetworkClient{
        public void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }
}
