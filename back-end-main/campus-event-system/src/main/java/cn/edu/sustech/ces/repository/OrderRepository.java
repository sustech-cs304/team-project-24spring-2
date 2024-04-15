package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.Order;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends ListCrudRepository<Order, UUID> {

}