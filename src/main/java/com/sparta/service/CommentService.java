package com.sparta.service;

import com.sparta.dto.CommentRequestDto;
import com.sparta.dto.CommentResponseDto;
import com.sparta.dto.CommentUpdateRequestDto;
import com.sparta.entity.Comment;
import com.sparta.entity.Schedule;
import com.sparta.entity.User;
import com.sparta.repository.CommentRepository;
import com.sparta.repository.ScheduleRepository;
import com.sparta.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, ScheduleRepository scheduleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;

    }

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId()).orElseThrow(() -> new EntityNotFoundException("Schedule not found"));

        Comment comment = new Comment(requestDto.getContents(), user, schedule);

        Comment savedComment = commentRepository.save(comment);

        CommentResponseDto responseDto = new CommentResponseDto(savedComment);

        return responseDto;
    }

    public List<CommentResponseDto> getComments(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        List<Comment> comments = schedule.getCommentList();
        return comments.stream().map(CommentResponseDto::new).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentUpdateRequestDto requestDto) {
//        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        comment.setContents(requestDto.getContents());

        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

        return commentResponseDto;
    }

    public Long deleteComment(Long id) {

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        commentRepository.delete(comment);

        return id;
    }
}
