package com.cloudhumans.smartchat.repository;

import com.cloudhumans.smartchat.entity.SectionRetrieved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRetrievedRepository extends JpaRepository<SectionRetrieved, Long> {
}
