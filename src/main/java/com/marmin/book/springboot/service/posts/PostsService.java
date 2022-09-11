package com.marmin.book.springboot.service.posts;

import com.marmin.book.springboot.domain.posts.Posts;
import com.marmin.book.springboot.domain.posts.PostsRepository;
import com.marmin.book.springboot.web.Dto.PostsResponseDto;
import com.marmin.book.springboot.web.Dto.PostsSaveRequestDto;
import com.marmin.book.springboot.web.Dto.PostsUpdateRequestDto;
import javafx.geometry.Pos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id).orElseThrow(()->new IllegalArgumentException(
                "해당 게시글이 없습니다. Id=" + id
        ));
        posts.update(requestDto.getTitle(),requestDto.getContent());
        return id;
    }
    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id).orElseThrow(()->
            new IllegalArgumentException("해당 게시글이 없습니다. Id=" + id));
            return new PostsResponseDto(entity);
    }
}
