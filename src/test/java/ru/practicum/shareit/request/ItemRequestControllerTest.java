package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addItemRequestTest() throws Exception {
        //Accept
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description1");
        LocalDateTime created = LocalDateTime.now();
        Long userId = 1L;

        when(itemRequestService.addItemRequest(itemRequestDto, userId))
                .thenReturn(new ItemRequestDto(1L, "description1", 1L, created));

        //Act
        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                //Assert
                .andExpect(status().isOk());
    }

    @Test
    public void addItemRequestUserIdNegativeTest() throws Exception {
        //Accept
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description1");
        LocalDateTime created = LocalDateTime.now();
        Long userId = -1L;

        when(itemRequestService.addItemRequest(itemRequestDto, userId))
                .thenReturn(new ItemRequestDto(1L, "description1", 1L, created));

        //Act
        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                //Assert
                .andExpect(status().isConflict());
    }

    @Test
    public void findItemRequestTest() throws Exception {
        //Accept
        Long userId = 1L;
        when(itemRequestService.findItemRequest(userId)).thenReturn(List.of(new ItemRequestDto()));
        //Act
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                //Assert
                .andExpect(status().isOk());
    }

    @Test
    public void findItemRequestUserIdIsNegativeTest() throws Exception {
        //Accept
        Long userId = -1L;
        when(itemRequestService.findItemRequest(userId)).thenReturn(List.of(new ItemRequestDto()));
        //Act
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                //Assert
                .andExpect(status().isConflict());
    }


    @Test
    public void findItemRequestByRequestorIdTest() throws Exception {
        //Accept
        Long userId = 1L;
        Integer from = 0;
        Integer size = 1;

        when(itemRequestService.findItemRequestByRequestorId(userId, from, size))
                .thenReturn(List.of(new ItemRequestDto()));
        //Act
        mockMvc.perform(get("/requests/all")
                .header("X-Sharer-User-Id", userId)
                        .queryParam("from", from.toString())
                        .queryParam("size", size.toString()))
                //Assert
                .andExpect(status().isOk());
    }

    @Test
    public void findItemRequestByRequestorIdWhenUserIdIsNegetiveTest() throws Exception {
        //Accept
        Long userId = -1L;
        Integer from = 0;
        Integer size = 1;

        when(itemRequestService.findItemRequestByRequestorId(userId, from, size))
                .thenReturn(List.of(new ItemRequestDto()));
        //Act
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .queryParam("from", from.toString())
                        .queryParam("size", size.toString()))
                //Assert
                .andExpect(status().isConflict());
    }

    @Test
    public void findItemRequestByRequestorIdWhenSizeIsNegetiveTest() throws Exception {
        //Accept
        Long userId = 1L;
        Integer from = 0;
        Integer size = -1;

        when(itemRequestService.findItemRequestByRequestorId(userId, from, size))
                .thenReturn(List.of(new ItemRequestDto()));
        //Act
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .queryParam("from", from.toString())
                        .queryParam("size", size.toString()))
                //Assert
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void findItemRequestByRequestorIdWhenFromIsNegetiveTest() throws Exception {
        //Accept
        Long userId = 1L;
        Integer from = -1;
        Integer size = 1;

        when(itemRequestService.findItemRequestByRequestorId(userId, from, size))
                .thenReturn(List.of(new ItemRequestDto()));
        //Act
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .queryParam("from", from.toString())
                        .queryParam("size", size.toString()))
                //Assert
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void findItemRequestByIdTest() throws Exception {
        //Accept
        Long userId = 1L;
        Long requestId = 1L;


        when(itemRequestService.findItemById(userId, requestId))
                .thenReturn(new ItemRequestDto());
        //Act
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", userId))
                //Assert
                .andExpect(status().isOk());
    }

    @Test
    public void findItemRequestByIdWhenUserIdIsNegativeTest() throws Exception {
        //Accept
        Long userId = -1L;
        Long requestId = 1L;


        when(itemRequestService.findItemById(userId, requestId))
                .thenReturn(new ItemRequestDto());
        //Act
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", userId))
                //Assert
                .andExpect(status().isConflict());
    }

}
