package me.hjeong.springjpa.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired PostRepository postRepository;

    @Test
    public void getAPost() throws Exception {
        Post post = new Post();
        post.setTitle("jpa");
        Post save = postRepository.save(post);

        mockMvc.perform(get("/posts/" + save.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("jpa"));
    }

    @Test
    public void getPosts() throws Exception {
        for(int i=0; i<30; i++) {
            Post post = new Post();
            post.setTitle("jpa-" + i);
            post.setCreated(LocalDateTime.now());
            postRepository.save(post);
        }

        mockMvc.perform(get("/posts")
                    .param("page", "0")
                    .param("size", "10")
                    .param("sort", "created,desc") // 우선 날짜순 그 다음엔 제목 순
                    .param("sort", "title")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}