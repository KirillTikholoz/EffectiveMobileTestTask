package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.description.CommentApi;
import org.example.dtos.CommentDto;
import org.example.dtos.SuccessfulResponseDto;
import org.example.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController implements CommentApi {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<SuccessfulResponseDto> createCommentHandler(@Valid @RequestBody CommentDto commentDto){
        commentService.createComment(commentDto);
        return new ResponseEntity<>(
                new SuccessfulResponseDto("The comment has been successfully created"),
                HttpStatus.CREATED
        );
    }
}
