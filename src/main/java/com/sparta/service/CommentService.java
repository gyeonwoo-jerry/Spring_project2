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
    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
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
    public CommentResponseDto updateComment(Long id, CommentUpdateRequestDto requestDto, User user) {
        // 유효성 검사 -> 해당 일정 존재여부 -> 댓글 존재 여부 -> 유저 존재 여부->
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        Long author = comment.getUser().getId();
        Long scheduleId = comment.getSchedule().getId();
        Long myId = user.getId();

        scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException("Schedule not found"));

        if (!author.equals(myId)) {
            throw new EntityNotFoundException("User not found");
        }

        comment.setContents(requestDto.getContents());

        commentRepository.save(comment);

        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

        return commentResponseDto;
    }

    public void deleteComment(Long id, User user) {

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        Long author = comment.getUser().getId();
        Long scheduleId = comment.getSchedule().getId();
        Long myId = user.getId();

        scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException("Schedule not found"));

        if (!author.equals(myId)) {
            throw new EntityNotFoundException("User not found");
        }

        commentRepository.delete(comment);
    }
}
