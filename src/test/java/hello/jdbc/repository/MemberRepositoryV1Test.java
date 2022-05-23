package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.PASSWORD;
import static hello.jdbc.connection.ConnectionConst.URL;
import static hello.jdbc.connection.ConnectionConst.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;

    @BeforeEach
    void beforeEach() {
        /**
         * 기본 DriverManager - 항상 새로운 커넥션을 획득
         *     get connection=conn0: url=jdbc:h2:.. user=SA class=class
         *     org.h2.jdbc.JdbcConnection
         *     get connection=conn1: url=jdbc:h2:.. user=SA class=class
         *     org.h2.jdbc.JdbcConnection
         *     get connection=conn2: url=jdbc:h2:.. user=SA class=class
         *     org.h2.jdbc.JdbcConnection
         *     get connection=conn3: url=jdbc:h2:.. user=SA class=class
         *     org.h2.jdbc.JdbcConnection
         *     get connection=conn4: url=jdbc:h2:.. user=SA class=class
         *     org.h2.jdbc.JdbcConnection
         *     get connection=conn5: url=jdbc:h2:.. user=SA class=class
         *     org.h2.jdbc.JdbcConnection
         */
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        /**
         * 한 커넥션을 계속 사용함
         *   get connection=HikariProxyConnection@xxxxxxxx1 wrapping conn0: url=jdbc:h2:...
         *   user=SA
         *   get connection=HikariProxyConnection@xxxxxxxx2 wrapping conn0: url=jdbc:h2:...
         *   user=SA
         *   get connection=HikariProxyConnection@xxxxxxxx3 wrapping conn0: url=jdbc:h2:...
         *   user=SA
         *   get connection=HikariProxyConnection@xxxxxxxx4 wrapping conn0: url=jdbc:h2:...
         *   user=SA
         *   get connection=HikariProxyConnection@xxxxxxxx5 wrapping conn0: url=jdbc:h2:...
         *   user=SA
         *   get connection=HikariProxyConnection@xxxxxxxx6 wrapping conn0: url=jdbc:h2:...
         *   user=SA
         */
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        repository = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("memberV1", 10000);
        repository.save(member);

        // find by id
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember = {}", findMember);
        log.info("member == findMember : ", member == findMember);
        log.info("member equals findMember : ", member.equals(findMember));
        assertThat(findMember).isEqualTo(member);

        // update
        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        // delete
        repository.delete(member.getMemberId());
        Assertions.assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}