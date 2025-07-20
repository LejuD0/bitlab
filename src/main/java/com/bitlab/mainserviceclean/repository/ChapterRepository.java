package com.bitlab.mainserviceclean.repository;

import com.bitlab.mainserviceclean.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
}

