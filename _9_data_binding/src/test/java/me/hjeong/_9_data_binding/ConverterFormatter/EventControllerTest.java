package me.hjeong._9_data_binding.ConverterFormatter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
({
        EventController.class
        , EventConverter.StringToEventConverter.class
        , EventFormatter.class
})
// Web과 관련된 빈만을 주입받아 테스트 하는데,
// 이 때 웹과 관련되지 않은 빈이라고 여겨지면 주입받지 않는다.
// 테스트에 필요한 빈이라서 꼭 들어가야 하므로 주입해야 함을 명시한다.

public class EventControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void getEvent() throws Exception {
        mockMvc.perform(get("/event/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

    }
}