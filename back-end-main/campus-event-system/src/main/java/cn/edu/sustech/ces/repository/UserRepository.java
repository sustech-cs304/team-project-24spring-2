package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;


public interface UserRepository extends ListCrudRepository<User, Integer> {
}
