package com.sparta.controller;

import com.sparta.dto.ScheduleRequestDto;
import com.sparta.dto.ScheduleResponseDto;
import com.sparta.service.ScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedule")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.createSchedule(requestDto);
    }

    @GetMapping("/schedule")
    public Page<ScheduleResponseDto> getSchedules(
            @RequestParam("page") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "modifiedAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean isAsc) {
        return scheduleService.getSchedules(page-1,size, sortBy, isAsc);
    }

    @PutMapping("/schedule/{id}")
    public Long updateSchedule(@PathVariable("id") Long id, @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.updateSchedule(id, requestDto);
    }

    @DeleteMapping("/schedule/{id}")
    public Long deleteSchedule(@PathVariable("id") Long id) {
        return scheduleService.deleteSchedule(id);
    }
}