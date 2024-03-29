package ru.practicum.shareit.user.dao;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCaseAndIdIsNot(String email, Long id);

    List<ResponseUserDto> findUsersBy(PageRequest pageRequest);

    Optional<ResponseUserDto> findUserById(Long id);
}
