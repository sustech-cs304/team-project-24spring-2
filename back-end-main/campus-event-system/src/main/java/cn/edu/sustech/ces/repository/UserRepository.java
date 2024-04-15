package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.UUID;


public interface UserRepository extends ListCrudRepository<User, UUID> {
    List<User> findByRealname(String realName);
    List<User> findByEmail(String email);
    List<User> findByNickname(String nickname);
    List<User> findByPhone(String phone);
}
