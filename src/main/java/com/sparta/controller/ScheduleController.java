package com.sparta.controller;

import com.sparta.dto.ScheduleRequestDto;
import com.sparta.dto.ScheduleResponseDto;
import com.sparta.entity.User;
import com.sparta.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 일정생성
    @PostMapping()
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody @Valid ScheduleRequestDto requestDto, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    // 전체 일정 조회
    @GetMapping()
    public Page<ScheduleResponseDto> getSchedules(
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "modifiedAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean isAsc) {
        return scheduleService.getSchedules(page,size, sortBy, isAsc);
    }

    // 선택일정 조회
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getSchedule(scheduleId));
    }

    // 일정 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSchedule(@PathVariable("id") Long scheduleId, @RequestBody @Valid ScheduleRequestDto requestDto, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        scheduleService.updateSchedule(scheduleId, requestDto, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable("id") Long scheduleId, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        scheduleService.deleteSchedule(scheduleId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}