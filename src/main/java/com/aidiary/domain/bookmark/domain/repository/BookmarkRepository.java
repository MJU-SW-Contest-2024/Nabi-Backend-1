package com.aidiary.domain.bookmark.domain.repository;

import com.aidiary.domain.bookmark.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkQueryDslRepository {
    Optional<Bookmark> findByDiaryId(Long id);
}
