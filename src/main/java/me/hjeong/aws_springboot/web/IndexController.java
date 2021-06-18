package me.hjeong.aws_springboot.web;

import lombok.RequiredArgsConstructor;
import me.hjeong.aws_springboot.config.auth.dto.SessionUser;
import me.hjeong.aws_springboot.service.posts.PostsService;
import me.hjeong.aws_springboot.web.dto.PostsResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
        // Model: 서버 템플릿 엔진에서 사용할 수 있는 객체 저장
        model.addAttribute("posts", postsService.findAllDesc());
        // CustomOAuth2UserService 에서 로그인 성공 시 세션에 저장했던 SessionUser 를 꺼내온다
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }

}
