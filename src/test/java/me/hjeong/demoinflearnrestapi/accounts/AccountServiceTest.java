package me.hjeong.demoinflearnrestapi.accounts;

import me.hjeong.demoinflearnrestapi.common.AppProperties;
import me.hjeong.demoinflearnrestapi.common.TestDescription;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {
    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("Repository에 존재하는 account의 인증정보를 가지고 오는 경우")
    public void findByUserName() {
        // Given
        String username = appProperties.getUserUsername();
        String password = appProperties.getUserPassword();

        // When
        UserDetailsService userDetailsService = (UserDetailsService) this.accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }

    @Test(expected = UsernameNotFoundException.class)
    @TestDescription("Repository에 없는 유저를 읽어오는 경우 테스트")
    public void findByUserName_Fail_1() {
        String username = "random@email.com";
        accountService.loadUserByUsername(username);
    }

    @Test
    @TestDescription("Repository에 없는 유저를 읽어오는 경우 테스트")
    public void findByUserName_Fail_2() {
        String username = "random@email.com";
        try {
            accountService.loadUserByUsername(username);
            fail("supposed to be failed");
        } catch (UsernameNotFoundException e) {
            assertThat(e.getMessage()).containsSequence(username);
        }
    }

    @Test
    @TestDescription("Repository에 없는 유저를 읽어오는 경우 테스트")
    public void findByUserName_Fail_3() {
        // Expected
        String username = "random@email.com";
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        // When
        accountService.loadUserByUsername(username);
    }
}