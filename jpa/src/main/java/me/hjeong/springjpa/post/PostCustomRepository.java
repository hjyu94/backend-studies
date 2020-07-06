package me.hjeong.springjpa.post;

import java.util.List;

public interface PostCustomRepository {

    List<Post> findMyPost();

}
