package com.sparta.service;

import com.sparta.dto.ScheduleRequestDto;
import com.sparta.dto.ScheduleResponseDto;
import com.sparta.entity.Schedule;
import com.sparta.entity.User;
import com.sparta.entity.UserSchedule;
import com.sparta.repository.ScheduleRepository;
import com.sparta.repository.UserRepository;
import com.sparta.repository.UserScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final UserScheduleRepository userScheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, UserRepository userRepository, UserScheduleRepository userScheduleRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.userScheduleRepository = userScheduleRepository;
    }

    public Page<ScheduleResponseDto> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;

        Page<Schedule> SchedulePages = scheduleRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "ModifiedAt")));

        Page<ScheduleResponseDto> scheduleResponseDtos = SchedulePages.map(schedulePage -> new ScheduleResponseDto(schedulePage));
        return scheduleResponseDtos;
    }

    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        // RequestDto -> Entity
        Schedule schedule = new Schedule(requestDto.getSubject(), requestDto.getContents(), user);

        // DB 저장
        Schedule saveSchedule = scheduleRepository.save(schedule);

        // User스케줄에 대한 데이터 삽입
        UserSchedule userSchedule = new UserSchedule(user, saveSchedule);

        // DB 저장
        userScheduleRepository.save(userSchedule);

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

    @Transactional
    public Long updateSchedule(Long id, ScheduleRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 해당 schedule이 DB에 존재하는지 확인
        Schedule schedule = findSchedule(id);

        // schedule 내용 수정
        schedule.update(user, requestDto.getSubject(), requestDto.getContents());

        return id;
    }

    @Transactional
    public Long deleteSchedule(Long id) {
        // 해당 schedule이 DB에 존재하는지 확인
        Schedule schedule = findSchedule(id);

        // schedule 삭제
        scheduleRepository.delete(schedule);

        return id;
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 스케줄은 존재하지 않습니다.")
        );
    }
}