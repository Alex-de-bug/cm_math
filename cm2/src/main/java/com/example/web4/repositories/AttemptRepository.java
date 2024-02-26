package com.example.web4.repositories;

import com.example.web4.models.Attempt;
import com.example.web4.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    List<Attempt> findByUser(User user);

    List<Attempt> findByUserId(Long id);

    void deleteByUser(Optional<User> user);

//    void deleteByUser_Id(Long id);

    @Modifying
    @Query("DELETE FROM Attempt e WHERE e.user_id = :user_id")
    void deleteByUserId(@Param("user_id") Long user_id);

}
