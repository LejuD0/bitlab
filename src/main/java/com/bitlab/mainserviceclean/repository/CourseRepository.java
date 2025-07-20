package com.bitlab.mainserviceclean.repository;

import com.bitlab.mainserviceclean.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}

