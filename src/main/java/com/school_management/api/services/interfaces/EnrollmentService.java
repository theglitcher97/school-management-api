package com.school_management.api.services.interfaces;


import com.school_management.api.dto.PostEnrollDTO;

public interface EnrollmentService {
    void enroll(PostEnrollDTO postEnrollDTO);

    void remove(Long enrollId);
}
