package com.stage.spring.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.stage.spring.entity.Event;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByClubs_IdClub(Long clubId);

  /*
  @Query("Select c from Event c where c.isDeleted= false ")
    List<Event> getUndeletedEvents();
    List<Event> findByDeletedFalse();

*/

}
