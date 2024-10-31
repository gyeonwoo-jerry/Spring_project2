package com.sparta.service;

import com.sparta.dto.ScheduleRequestDto;
import com.sparta.dto.ScheduleResponseDto;
import com.sparta.entity.Schedule;
import com.sparta.entity.User;
import com.sparta.repository.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, User user) {
        // RequestDto -> Entity
        Schedule schedule = new Schedule(requestDto.getSubject(), requestDto.getContents(), user);

        // DB 저장
        Schedule saveSchedule = scheduleRepository.save(schedule);

        // Entity -> ResponseDto
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(saveSchedule);

        return scheduleResponseDto;
    }

    public Page<ScheduleResponseDto> getSchedules(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Schedule> ScheduleList = scheduleRepository.findAll(pageable);

        return ScheduleList.map(ScheduleResponseDto::new);
    }

    //선택조회
    public ScheduleResponseDto getSchedule(Long scheduleId) {
        Schedule schedule = findSchedule(scheduleId);
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(schedule);
        return scheduleResponseDto;
    }

    @Transactional
    public void updateSchedule(Long scheduleId, ScheduleRequestDto requestDto, User user) {
        Long userId = user.getId();

        // 해당 schedule이 DB에 존재하는지 확인
        Schedule schedule = findSchedule(scheduleId);

        if (schedule.getUser().getId() != userId) {
            throw new IllegalArgumentException("해당 유저는 수정을 할 수 없습니다.");
        }

        // schedule 내용 수정
        schedule.update(requestDto.getSubject(), requestDto.getContents());
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, User user) {
        Long userId = user.getId();

        // 해당 schedule이 DB에 존재하는지 확인
        Schedule schedule = findSchedule(scheduleId);

        if (schedule.getUser().getId() != userId) {
            throw new IllegalArgumentException("해당 유저는 삭제를 할 수 없습니다.");
        }

        // schedule 삭제
        scheduleRepository.delete(schedule);
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 스케줄은 존재하지 않습니다.")
        );
    }
}