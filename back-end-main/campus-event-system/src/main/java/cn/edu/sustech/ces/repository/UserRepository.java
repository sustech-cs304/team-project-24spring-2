package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends ListCrudRepository<User, UUID> {

    // TODO: List may waste efficiency, maybe we can use limit 1

    List<User> findByRealName(String realName);
    List<User> findByEmail(String email);
    List<User> findByNickname(String nickname);
    List<User> findByPhone(String phone);

    // manually create new suer
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO users (id, nickname, password) VALUES (:id, :nickname, :password)")
    void createNewUser(@Param("id") UUID id, @Param("nickname") String nickname, @Param("password") String password);
}
