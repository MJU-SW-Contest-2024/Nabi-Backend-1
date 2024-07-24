package com.aidiary.domain.user.domain.repository;

import com.aidiary.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
