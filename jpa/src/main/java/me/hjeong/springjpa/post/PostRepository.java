package me.hjeong.springjpa.post;

import me.hjeong.springjpa.MyRepository;

public interface PostRepository extends PostCustomRepository<Post>, MyRepository<Post, Long> {
}
