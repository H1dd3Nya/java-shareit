package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.TestValuesGenerator;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class ItemRequestServiceImplTest extends TestValuesGenerator {
    private final ItemRequestService service;

    public ItemRequestServiceImplTest(@Autowired EntityManager em, @Autowired ItemRequestService service) {
        super(em);
        this.service = service;
    }

    @Test
    void createItemRequest_returnNewRequest() {
        setTestUser(1);
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("description");

        ItemRequestDto request = service.createItemRequest(requestDto, 1L);
        ItemRequest testRequest = getTestRequest(1);

        assertEqualsRequests(testRequest, request);
    }

    @Test
    void getItemRequestById_returnRequest() {
        setTestUser(1);
        setTestRequest(1, 1);

        ItemRequestDto request = service.getItemRequestById(1L);
        ItemRequest testRequest = getTestRequest(1);

        assertEqualsRequests(testRequest, request);
    }

    @Test
    void getItemRequestById_returnRequestWithItems() {
        setTestUser(1);
        setTestUser(2);
        setTestRequest(1, 2);
        ItemRequest testRequest = getTestRequest(1);

        setTestItem(1, 1, true, 1L);
        ItemRequestDto request = service.getItemRequestById(1L);

        assertEqualsRequests(testRequest, request);
        assertEquals(1, request.getItems().size());
        assertEquals(1, request.getItems().get(0).getId());
    }

    @Test
    void getByUserId_returnRequestsForUser() {
        setTestUser(1);
        setTestRequest(1, 1);
        setTestRequest(2, 1);

        List<ItemRequestDto> request = service.getByUserId(1L);
        ItemRequest testRequest = getTestRequest(1);

        assertEquals(2, request.size());
        assertEqualsRequests(testRequest, request.get(0));
    }

    @Test
    void getByUserId_returnRequestsForUserWithItems() {
        setTestUser(1);
        setTestUser(2);
        setTestRequest(1, 2);
        setTestRequest(2, 2);

        setTestItem(1, 1, true, 1L);
        setTestItem(2, 1, true, 2L);
        List<ItemRequestDto> request = service.getByUserId(2L);
        ItemRequest testRequest = getTestRequest(1);

        assertEquals(2, request.size());
        assertEqualsRequests(testRequest, request.get(0));
        assertEquals(1, request.get(0).getItems().get(0).getId());
        assertEquals(2, request.get(1).getItems().get(0).getId());
    }

    @Test
    void getAll_ReturnRequestsList() {
        setTestUser(1);
        setTestRequest(1, 1);
        setTestRequest(2, 1);

        List<ItemRequestDto> request = service.getAll();
        ItemRequest testRequest = getTestRequest(1);

        assertEquals(2, request.size());
        assertEqualsRequests(testRequest, request.get(0));
    }

    private void assertEqualsRequests(ItemRequest request, ItemRequestDto requestDto) {
        assertEquals(request.getId(), requestDto.getId());
        assertEquals(request.getDescription(), requestDto.getDescription());
        assertEquals(request.getAuthor(), requestDto.getAuthor());
        assertEquals(request.getCreatedAt(), requestDto.getCreated());
    }
}