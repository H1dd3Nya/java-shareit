package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class TestValuesGenerator {
    private final EntityManager em;

    public TestValuesGenerator(EntityManager em) {
        this.em = em;
    }

    protected Booking getTestBooking(Long bookingId) {
        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.id = :bookingId", Booking.class);
        query.setParameter("bookingId", bookingId);
        return query.getSingleResult();
    }

    protected void setTestBooking(Status status, Long id) {
        Query insert = em.createNativeQuery("INSERT INTO bookings VALUES (:id,1,:start,:end,2,:status)");

        insert.setParameter("id", id);
        insert.setParameter("start", LocalDateTime.now());
        insert.setParameter("end", LocalDateTime.now().plusDays(1));
        insert.setParameter("status", status.toString());

        insert.executeUpdate();
    }

    protected void setTestBooking(int bookingId, int itemId, int bookerId, LocalDateTime start, LocalDateTime end) {
        Query insert = em.createNativeQuery("INSERT INTO bookings " +
                "VALUES (:bookingId,:itemId,:start,:end,:bookerId,:status)");

        insert.setParameter("bookingId", bookingId);
        insert.setParameter("itemId", itemId);
        insert.setParameter("start", start);
        insert.setParameter("end", end);
        insert.setParameter("bookerId", bookerId);
        insert.setParameter("status", Status.APPROVED.toString());

        insert.executeUpdate();
    }

    protected List<Booking> getAllTestBookings() {
        TypedQuery<Booking> query = em.createQuery("select b from Booking b", Booking.class);
        return query.getResultList();
    }

    protected void setTestUser(int id) {
        Query userQuery = em.createNativeQuery("INSERT INTO users " +
                        "VALUES (:id,'test user', :email)")
                .setParameter("id", id)
                .setParameter("email", id + "test@test.com");
        userQuery.executeUpdate();
    }

    protected User getTestUser(int id) {
        TypedQuery<User> query = em.createQuery("select i from User i where i.id = :id", User.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    protected void setTestItem(int id, int userId, Boolean available, Long request) {
        Query insert = em.createNativeQuery("INSERT INTO items " +
                "VALUES (:id,:userId,:name,:description,:available,:request)");

        insert.setParameter("id", id);
        insert.setParameter("userId", userId);
        insert.setParameter("name", "test item");
        insert.setParameter("description", "item description");
        insert.setParameter("available", available);
        insert.setParameter("request", request);

        insert.executeUpdate();
    }

    protected Item getTestItem(int id) {
        TypedQuery<Item> query = em.createQuery("select i from Item i where i.id = :id", Item.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }


    protected List<Item> getAllTestItems() {
        TypedQuery<Item> query = em.createQuery("select i from Item i", Item.class);
        return query.getResultList();
    }

    protected ItemRequest getTestRequest(int id) {
        TypedQuery<ItemRequest> query = em.createQuery("select i " +
                "from ItemRequest i " +
                "where i.id = :id", ItemRequest.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    protected void setTestRequest(int id, int authorId) {
        Query userQuery = em.createNativeQuery("INSERT INTO requests " +
                        "VALUES (:id,'test description', :authorId, :created)")
                .setParameter("id", id)
                .setParameter("authorId", authorId)
                .setParameter("created", LocalDateTime.now());
        userQuery.executeUpdate();
    }

    protected void setTestComment(int id, int authorId, int itemId) {
        Query userQuery = em.createNativeQuery("INSERT INTO comments " +
                        "VALUES (:id, :itemId, :authorId, 'test comment', :created)")
                .setParameter("id", id)
                .setParameter("itemId", itemId)
                .setParameter("authorId", authorId)
                .setParameter("created", LocalDateTime.now());
        userQuery.executeUpdate();
    }

    protected Comment getTestComment(int id) {
        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.id = :id", Comment.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    protected void setTestEntities() {
        setTestUser(1);
        setTestUser(2);
        setTestItem(1, 1, true, null);
    }
}
