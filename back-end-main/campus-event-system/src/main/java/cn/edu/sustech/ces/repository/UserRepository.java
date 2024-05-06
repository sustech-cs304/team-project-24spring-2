package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
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

}
