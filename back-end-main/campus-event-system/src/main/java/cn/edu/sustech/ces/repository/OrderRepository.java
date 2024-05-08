package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.Order;
import cn.edu.sustech.ces.enums.OrderStatus;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface OrderRepository extends ListCrudRepository<Order, UUID> {

    List<Order> findAllByPayerId(UUID userId);

    Optional<Order> findFirstByPayerIdAndStatusIn(UUID userId, Set<OrderStatus> unpaid);
}