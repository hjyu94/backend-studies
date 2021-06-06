package me.hjeong.aws_springboot.service.posts;

import lombok.RequiredArgsConstructor;
import me.hjeong.aws_springboot.domain.posts.PostsRepository;
import me.hjeong.aws_springboot.web.dto.PostsSaveRequestDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

}
