package ru.practicum.shareit.item.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.TestValuesGenerator;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class ItemServiceImplTest extends TestValuesGenerator {
    private final ItemService service;

    public ItemServiceImplTest(@Autowired EntityManager em, @Autowired ItemService service) {
        super(em);
        this.service = service;
    }

    @Test
    void getById_returnItemForOwner() {
        setTestUser(1);
        setTestUser(2);
        setTestItem(1, 1, true, null);
        setTestBooking(1, 1, 2,
                LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
        setTestBooking(2, 1, 2,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        setTestComment(1, 2, 1);
        Item testItem = getTestItem(1);

        ItemDto item = service.getById(1L, 1L);

        assertEqualsItems(testItem, item);
        assertEquals(1, item.getComments().size());
        assertEquals(1, item.getLastBooking().getItem().getId());
        assertEquals(1, item.getLastBooking().getItem().getOwner().getId());
        assertEquals(1, item.getNextBooking().getItem().getId());
        assertEquals(1, item.getNextBooking().getItem().getOwner().getId());
    }

    @Test
    void getById_returnItemForUser() {
        setTestUser(1);
        setTestUser(2);
        setTestItem(1, 1, true, null);
        setTestBooking(1, 1, 2,
                LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
        setTestBooking(2, 1, 2,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        setTestComment(1, 2, 1);
        Item testItem = getTestItem(1);

        ItemDto item = service.getById(1L, 1L);

        assertEqualsItems(testItem, item);
        assertEquals(1, item.getComments().size());
    }

    @Test
    void getAll_returnItems() {
        setTestUser(1);
        setTestUser(2);
        setTestItem(1, 1, true, null);
        setTestItem(2, 1, true, null);
        setTestComment(1, 2, 1);
        List<Item> testItems = getAllTestItems();

        List<ItemDto> items = service.getAll(1L);

        assertEqualsItems(testItems.get(0), items.get(0));
        assertEqualsItems(testItems.get(1), items.get(1));
        assertEquals(1, items.get(0).getComments().size());
    }

    @Test
    void getAll_returnItemsWitBookings() {
        setTestEntities();
        setTestItem(2, 1, true, null);
        List<Item> testItems = getAllTestItems();
        setTestBooking(1, 1, 2,
                LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(4));
        setTestBooking(2, 1, 2,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        setTestBooking(3, 1, 2,
                LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
        setTestBooking(4, 1, 2,
                LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4));
        setTestComment(1, 2, 1);

        List<ItemDto> items = service.getAll(1L);

        assertEqualsItems(testItems.get(0), items.get(0));
        assertEqualsItems(testItems.get(1), items.get(1));
        assertEquals(1, items.get(0).getNextBooking().getItemId());
        assertEquals(1, items.get(0).getLastBooking().getItemId());
        assertEquals(1, items.get(0).getComments().size());
    }


    @Test
    void create_returnNewItem() {
        setTestUser(1);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test item");
        itemDto.setDescription("Test description");
        itemDto.setAvailable(true);

        ItemDto item = service.create(itemDto, 1L);
        Item testItem = getTestItem(1);

        assertEqualsItems(testItem, item);
    }

    @Test
    void create_userNotFound() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test item");
        itemDto.setDescription("Test description");
        itemDto.setAvailable(true);

        assertThrows(NotFoundException.class, () -> service.create(itemDto, 2L));
    }

    @Test
    void update_returnUpdatedItem() {
        setTestUser(1);
        setTestItem(1, 1, true, null);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test updated item");
        itemDto.setDescription("Test updated description");
        itemDto.setAvailable(false);

        ItemDto item = service.update(itemDto, 1L, 1L);
        Item testItem = getTestItem(1);

        assertEqualsItems(testItem, item);
    }

    @Test
    void update_userNotFound() {
        setTestUser(1);
        setTestItem(1, 1, true, null);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test updated item");
        itemDto.setDescription("Test updated description");
        itemDto.setAvailable(false);

        assertThrows(NotFoundException.class, () -> service.update(itemDto, 1L, 2L));
    }

    @Test
    void update_itemNotFound() {
        setTestUser(1);
        setTestItem(1, 1, true, null);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test updated item");
        itemDto.setDescription("Test updated description");
        itemDto.setAvailable(false);

        assertThrows(NotFoundException.class, () -> service.update(itemDto, 2L, 1L));
    }

    @Test
    void find_returnNotEmptyList() {
        setTestUser(1);
        setTestItem(1, 1, true, null);

        List<ItemDto> item = service.find("Test");
        Item testItem = getTestItem(1);

        assertEquals(1, item.size());
        assertEqualsItems(testItem, item.get(0));
    }

    @Test
    void find_returnList() {
        setTestUser(1);
        setTestItem(1, 1, true, null);

        List<ItemDto> item = service.find("");

        assertEquals(0, item.size());
    }

    @Test
    void addComment_returnItemWithNewComment() {
        setTestUser(1);
        setTestUser(2);
        setTestItem(1, 1, true, null);
        setTestBooking(1, 1, 2, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(2));
        Comment comment = new Comment();
        comment.setText("Test comment");

        service.addComment(comment, 1L, 2L);
        ItemDto item = service.getById(1L, 1L);

        assertEquals(1, item.getComments().size());
        assertEquals(comment.getText(), item.getComments().get(0).getText());
    }

    @Test
    void addComment_IllegalArgument() {
        setTestUser(1);
        setTestUser(2);
        setTestItem(1, 1, true, null);
        Comment comment = new Comment();
        comment.setText("Test comment");

        assertThrows(IllegalArgumentException.class, () -> service.addComment(comment, 1L, 2L));
    }

    private void assertEqualsItems(Item item, ItemDto itemDto) {
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getIsAvailable(), itemDto.getAvailable());

        if (item.getRequestId() != null && itemDto.getRequestId() != null) {
            assertEquals(item.getRequestId(), itemDto.getRequestId());
        }
    }
}