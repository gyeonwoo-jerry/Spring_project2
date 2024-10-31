package com.sparta.repository;

import com.sparta.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByOrderByModifiedAtDesc();

    Page<Schedule> findAll(Pageable pageable);

//    @Query("select s from Schedule s Join fetch s.user where s.id=:id")
//    Optional<Schedule> findById(@Param("id") Long id);
}
