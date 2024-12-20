package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    private ItemDto itemDto;
    private CommentDto commentDto;
    public static final String ENDPOINT = "/items";
    public static final String SHARER_ID = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test");
        itemDto.setDescription("Test");
        itemDto.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setItemId(1L);
        commentDto.setText("Test");
        commentDto.setAuthorName("Test Author");
        commentDto.setCreated(LocalDateTime.now());
    }

    @Test
    void create() throws Exception {
        when(itemService.create(any(), anyLong())).thenReturn(itemDto);

        mockMvc.perform(post(ENDPOINT)
                        .content(mapper.writeValueAsString(itemDto))
                        .header(SHARER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(true)));
        verify(itemService, times(1)).create(any(), anyLong());
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(any(), anyLong(), anyLong())).thenReturn(commentDto);

        mockMvc.perform(post(ENDPOINT + "/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(SHARER_ID, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.itemId", is(commentDto.getItemId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
        verify(itemService, times(1)).addComment(any(), anyLong(), anyLong());
    }

    @Test
    void getById() throws Exception {
        when(itemService.getById(anyLong(), anyLong())).thenReturn(itemDto);

        mockMvc.perform(get(ENDPOINT + "/1")
                        .header(SHARER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(true)));
        verify(itemService, times(1)).getById(anyLong(), anyLong());
    }

    @Test
    void getAll() throws Exception {
        when(itemService.getAll(anyLong())).thenReturn(List.of(itemDto));

        mockMvc.perform(get(ENDPOINT)
                        .header(SHARER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
        verify(itemService, times(1)).getAll(anyLong());
    }

    @Test
    void update() throws Exception {
        when(itemService.update(any(), anyLong(), anyLong())).thenReturn(itemDto);

        mockMvc.perform(patch(ENDPOINT + "/1")
                        .header(SHARER_ID, 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(true)));
        verify(itemService, times(1)).update(any(), anyLong(), anyLong());
    }

    @Test
    void search() throws Exception {
        when(itemService.find(anyString())).thenReturn(List.of(itemDto));

        mockMvc.perform(get(ENDPOINT + "/search")
                        .header(SHARER_ID, 1)
                        .param("text", anyString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
        verify(itemService, times(1)).find(anyString());
    }
}