package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.User;
import org.springframework.data.repository.ListCrudRepository;


public interface UserRepository extends ListCrudRepository<User, Integer> {


    public User findByUsername(String username);

}
