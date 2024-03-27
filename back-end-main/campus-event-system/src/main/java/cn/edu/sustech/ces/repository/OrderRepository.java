package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.Order;
import org.springframework.data.repository.ListCrudRepository;

public interface OrderRepository extends ListCrudRepository<Order, Integer> {
}