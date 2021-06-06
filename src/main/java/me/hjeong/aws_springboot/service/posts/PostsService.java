package me.hjeong.aws_springboot.service.posts;

import lombok.RequiredArgsConstructor;
import me.hjeong.aws_springboot.domain.posts.Posts;
import me.hjeong.aws_springboot.domain.posts.PostsRepository;
import me.hjeong.aws_springboot.web.dto.PostsResponseDto;
import me.hjeong.aws_springboot.web.dto.PostsSaveRequestDto;
import me.hjeong.aws_springboot.web.dto.PostsUpdateRequestDto;
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

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) { // 1
        Posts posts = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id)
        );
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id)
        );
        return new PostsResponseDto(entity);
    }

}

/*
    1) update 기능에서 데이터베이스에 쿼리를 날리는 부분이 없다.
    - JPA 영속성 컨텍스트 덕분 (엔티티를 영구 저장하는 환경)
    - JPA의 엔티티 매니저가 활성화 된 상태로 트랜잭션 안에서 데이터베이스에서 데이터를 가지고 오면 이 데이터는 영속성 컨텍스트가 유지됨
    - 이후 해당 데이터의 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영함
    - 즉 Entity 의 객체 값만 변경하면 별도로 쿼리를 날릴 필요가 없다 [더티 체킹]
 */