package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.net.ContentHandler;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EventRepository extends ListCrudRepository<Event, UUID> {
    List<Event> findAllByTitle(String title);

    List<Event> findAllByPublisher(UUID publisher);

    Page<Event> findAllByStatusIn(Pageable pageable, Set<EventStatus> statusSet);

    long countByStatusIn(Set<EventStatus> statusSet);

    Page<Event> findAll(Pageable pageable);

    Page<Event> findAllByPublisher(Pageable pageable, UUID publisherId);

    long countByPublisher(UUID id);

    Page<Event> findAllByTitleContainingAndStatusIn(Pageable pageable, String title, Set<EventStatus> statuses);

    Page<Event> findAllByTitleContainingAndStatusInAndCategoryId(Pageable pageable, String title, Set<EventStatus> statuses, Integer categoryId);

    Page<Event> findAllByCategoryIdAndStatusIn(Pageable pageable, Integer categoryId, Set<EventStatus> statuses);

    long countByCategoryIdAndStatusIn(Integer categoryId, Set<EventStatus> statuses);

    long countByTitleContainingAndStatusIn(String title, Set<EventStatus> statuses);

    long countByTitleContainingAndStatusInAndCategoryId(String title, Set<EventStatus> statuses, Integer categoryId);

    Page<Event> findAllByCategoryIdAndPublisherAndStatusIn(Pageable pageable, Integer categoryId, UUID publisher, Set<EventStatus> statuses);

    Page<Event> findAllByTitleContainingAndPublisherAndStatusIn(Pageable pageable, String title, UUID publisher, Set<EventStatus> statuses);

    Page<Event> findAllByTitleContainingAndCategoryIdAndStatusIn(Pageable pageable, String title, Integer categoryId, Set<EventStatus> statuses);

    Page<Event> findAllByTitleContainingAndCategoryIdAndPublisherAndStatusIn(Pageable pageable, String title, Integer categoryId, UUID publisher, Set<EventStatus> statuses);

    long countByCategoryIdAndPublisherAndStatusIn(Integer categoryId, UUID publisher, Set<EventStatus> statuses);

    long countByTitleContainingAndPublisherAndStatusIn(String title, UUID publisher, Set<EventStatus> statuses);

    long countByTitleContainingAndCategoryIdAndStatusIn(String title, Integer categoryId, Set<EventStatus> statuses);

    long countByTitleContainingAndCategoryIdAndPublisherAndStatusIn(String title, Integer categoryId, UUID publisher, Set<EventStatus> statuses);
}

