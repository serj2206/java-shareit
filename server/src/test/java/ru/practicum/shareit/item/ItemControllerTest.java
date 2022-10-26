package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    //create

    @Test
    public void createTest() throws Exception {
        //Accept
        ItemDto itemDto = new ItemDto();
        itemDto.setName("item1");
        itemDto.setDescription("descriptionItem1");
        itemDto.setAvailable(true);

        when(itemService.create(1L, itemDto))
                .thenReturn(new ItemDto(1L, "item1", "descriptionItem1", true, null));
        //Act
        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))

                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    //update

    @Test
    public void updateTest() throws Exception {
        //Accept
        ItemDto itemDto = new ItemDto();
        itemDto.setName("item1Update");
        itemDto.setDescription("descriptionItem1Update");
        itemDto.setAvailable(false);

        when(itemService.update(1L, 1L, itemDto))
                .thenReturn(new ItemDto(1L, "item1Update", "descriptionItem1Update", false, null));
        //Act
        mockMvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))

                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    public void addCommentTest() throws Exception {
        //Accept
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Comment1");
        LocalDateTime localDateTime = LocalDateTime.now();

        when(itemService.addComment(1L, 1L, commentDto))
                .thenReturn(new CommentDto(1L, "Comment1", "user1", localDateTime));
        //Act
        mockMvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    public void findItemDtoByIdTest() throws Exception {
        //Accept
        when(itemService.findItemDtoById(anyLong(), anyLong()))
                .thenReturn(new ItemDto(1L, "item1", "descriptionItem1", true, null));

        //Act
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))

                //Assert
                .andExpect(status().isOk());
    }

    //findItemDtoAll

    @Test
    public void findItemDtoAllTest() throws Exception {
        //Accept
        when(itemService.findItemDtoAll(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(new ItemDto(1L, "item1", "descriptionItem1", true, null)));
        //Act
        mockMvc.perform(get("/items")
                        .queryParam("from", "0")
                        .queryParam("size", "1")
                        .header("X-Sharer-User-Id", 1L))
                //Accept
                .andExpect(status().isOk());
    }

    //searchItems

    @Test
    public void searchItemsTest() throws Exception {
        //Accept
        long userId = 1L;
        Integer from = 0;
        Integer size = 1;
        when(itemService.searchItem(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(new ItemDto(1L, "item1", "descriptionItem1", true, null)));
        //Act
        mockMvc.perform(get("/items/search")
                        .queryParam("text", "ручка")
                        .queryParam("from", from.toString())
                        .queryParam("size", size.toString())
                        .header("X-Sharer-User-Id", userId))
                //Accept
                .andExpect(status().isOk());
    }


    //delete

    @Test
    public void deleteTest() throws Exception {
        //Accept
        when(itemService.delete(anyLong(), anyLong()))
                .thenReturn(new ItemDto(1L, "item1", "descriptionItem1", true, null));
        //Act
        mockMvc.perform(delete("/items/1")
                .header("X-Sharer-User-Id", 1L))
                //Accept
                .andExpect(status().isOk());
    }

}
