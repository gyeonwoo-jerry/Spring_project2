package com.sparta.controller;

import com.sparta.dto.CommentRequestDto;
import com.sparta.dto.CommentResponseDto;
import com.sparta.dto.CommentUpdateRequestDto;
import com.sparta.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto) {
        return commentService.createComment(requestDto);
    }

    @GetMapping("/comment")
    public List<CommentResponseDto> getComment(@RequestParam("scheduleId") Long scheduleId) {
        return commentService.getComments(scheduleId);
    }

    @PutMapping("/comment/{id}")
    public CommentResponseDto updateComment(@PathVariable("id") Long id, @RequestBody CommentUpdateRequestDto requestDto) {
        return commentService.updateComment(id, requestDto);
    }

    @DeleteMapping("/comment/{id}")
    public Long deleteComment(@PathVariable("id") Long id) {
        return commentService.deleteComment(id);
    }
}
