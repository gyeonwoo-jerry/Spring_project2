package com.sparta.controller;

import com.sparta.dto.CommentRequestDto;
import com.sparta.dto.CommentResponseDto;
import com.sparta.dto.CommentUpdateRequestDto;
import com.sparta.entity.User;
import com.sparta.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody @Valid CommentRequestDto requestDto, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createComment(requestDto, user));
    }

    @GetMapping("/{id}")
    public List<CommentResponseDto> getComment(@RequestParam("scheduleId") Long scheduleId) {
        return commentService.getComments(scheduleId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable("id") Long id, @RequestBody @Valid CommentUpdateRequestDto requestDto, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        commentService.updateComment(id, requestDto, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        commentService.deleteComment(id, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
