package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createBookingTest() throws Exception {
        //Accept
        Long userId = 1L;
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().toString());
        bookingDto.setEnd(LocalDateTime.now().plusDays(5L).toString());

        when(bookingService.create(userId, bookingDto)).thenReturn(new BookingDto());
        //Act
        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                //Assert
                .andExpect(status().isOk());
    }


    @Test
    public void approvedTest() throws Exception {
        //Accept
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().toString());
        bookingDto.setEnd(LocalDateTime.now().plusDays(5L).toString());

        when(bookingService.approved(userId, bookingId, approved)).thenReturn(new BookingDto());
        //Act
        mockMvc.perform(patch("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .queryParam("approved", "true"))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    public void findBookingByIdTest() throws Exception {
        //Accept
        Long userId = 1L;
        Long bookingId = 1L;

        when(bookingService.findBookingDtoById(bookingId, userId)).thenReturn(new BookingDto());
        //Act
        mockMvc.perform(get("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", userId))

                //Assert
                .andExpect(status().isOk());

    }

    @Test
    public void findAllBookingDtoByBookerTest() throws Exception {
        //Accept
        Long userId = 1L;
        Integer from = 0;
        Integer size = 1;
        String state = "ALL";

        when(bookingService.findAllBookingDtoByBookerId(userId, state, from, size))
                .thenReturn(List.of(new BookingDto()));

        //Act
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .queryParam("from", from.toString())
                        .queryParam("size", size.toString())
                        .queryParam("state", "ALL"))
                //Assert
                .andExpect(status().isOk());
    }

    @Test
    public void findAllBookingDtoByOwnerTest() throws Exception {
        //Accept
        Long userId = 1L;
        Integer from = 0;
        Integer size = 1;
        String state = "ALL";

        when(bookingService.findAllBookingDtoByOwnerId(userId, state, from, size))
                .thenReturn(List.of(new BookingDto()));

        //Act
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .queryParam("from", from.toString())
                        .queryParam("size", size.toString())
                        .queryParam("state", state))
                //Assert
                .andExpect(status().isOk());
    }
}


