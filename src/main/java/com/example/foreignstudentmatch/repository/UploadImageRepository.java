package com.example.foreignstudentmatch.repository;

import com.example.foreignstudentmatch.domain.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadImageRepository extends JpaRepository<UploadImage, Long> {
}
