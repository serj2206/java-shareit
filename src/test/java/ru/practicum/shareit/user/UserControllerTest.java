package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mockMvc;

    //created

    @Test
    public void createdTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto();
        userDto.setName("user1");
        userDto.setEmail("user1@email.ru");

        when(userService.create(userDto))
                .thenReturn(new UserDto(1L, "user1", "user1@email.ru"));

        //Act
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    public void createdNameIsNullTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto();

        userDto.setEmail("user1@email.ru");

        when(userService.create(userDto))
                .thenReturn(new UserDto(1L, "user1", "user1@email.ru"));

        //Act
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isBadRequest());
    }


    @Test
    public void createdNameIsEmptyTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto();
        userDto.setName("");
        userDto.setEmail("user1@email.ru");

        when(userService.create(userDto))
                .thenReturn(new UserDto(1L, "user1", "user1@email.ru"));

        //Act
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createdNameIsBlankTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto();
        userDto.setName(" ");
        userDto.setEmail("user1@email.ru");

        when(userService.create(userDto))
                .thenReturn(new UserDto(1L, "user1", "user1@email.ru"));

        //Act
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createdNameIsFalseTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto();
        userDto.setName("User1 User1");
        userDto.setEmail("user1@email.ru");

        when(userService.create(userDto))
                .thenReturn(new UserDto(1L, "user1", "user1@email.ru"));

        //Act
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createdEmailIsNullTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto();
        userDto.setName("user1");

        when(userService.create(userDto))
                .thenReturn(new UserDto(1L, "user1", "user1@email.ru"));

        //Act
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createdEmailIsEmptyTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto();
        userDto.setName("user1");
        userDto.setEmail("");

        when(userService.create(userDto))
                .thenReturn(new UserDto(1L, "user1", "user1@email.ru"));

        //Act
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createdEmailIsBlankTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto();
        userDto.setName("user1");
        userDto.setEmail(" ");

        when(userService.create(userDto))
                .thenReturn(new UserDto(1L, "user1", "user1@email.ru"));

        //Act
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createdEmailBadTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto();
        userDto.setName("user1");
        userDto.setEmail("user_email");

        when(userService.create(userDto))
                .thenReturn(new UserDto(1L, "user1", "user1@email.ru"));

        //Act
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isBadRequest());
    }

    //findAll

    @Test
    public void findAllTest() throws Exception {
        //Accept
        List<UserDto> userDtoList = List.of(
                new UserDto(1L, "user1", "user1@emal.ru"),
                new UserDto(2L, "user2", "user2@emal.ru"),
                new UserDto(3L, "user2", "user3@emal.ru"));

        when(userService.findAll())
                .thenReturn(userDtoList);

        //Act
        mockMvc.perform(get("/users"))
                //Assert
                .andExpect(status().isOk());
    }

    //findUserById

    @Test
    public void findUserByIdTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto(1L, "user1", "user1@emal.ru");
        when(userService.findUserById(1L))
                .thenReturn(userDto);

        //Act
        mockMvc.perform(get("/users/1"))
                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    public void findUserByIdNegativTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto(1L, "user1", "user1@emal.ru");
        when(userService.findUserById(1L))
                .thenReturn(userDto);

        //Act
        mockMvc.perform(get("/users/-1"))
                //Assert
                .andExpect(status().isConflict());
    }

    @Test
    public void findUserByIdZeroTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto(1L, "user1", "user1@emal.ru");
        when(userService.findUserById(1L))
                .thenReturn(userDto);

        //Act
        mockMvc.perform(get("/users/0"))
                //Assert
                .andExpect(status().isConflict());
    }

    //update
    @Test
    public void updateTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto(1L, "user1Update", "user1Update@emal.ru");
        when(userService.update(userDto, 1L))
                .thenReturn(userDto);

        //Act
        mockMvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    public void updateIdNegativTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto(1L, "user1Update", "user1Update@emal.ru");
        when(userService.update(userDto, 1L))
                .thenReturn(userDto);

        //Act
        mockMvc.perform(patch("/users/-1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isConflict());
    }

    @Test
    public void updateIdZeroTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto(1L, "user1Update", "user1Update@emal.ru");
        when(userService.update(userDto, 1L))
                .thenReturn(userDto);

        //Act
        mockMvc.perform(patch("/users/0")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isConflict());
    }

    @Test
    public void updateNameBadTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto(1L, "user1 Update", "user1Update@emal.ru");
        when(userService.update(userDto, 1L))
                .thenReturn(userDto);

        //Act
        mockMvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateEmailBadTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto(1L, "user1Update", "user1Updateemalru");
        when(userService.update(userDto, 1L))
                .thenReturn(userDto);

        //Act
        mockMvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Assert
                .andExpect(status().isBadRequest());
    }

    //delete

    @Test
    public void deleteTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto(1L, "user1Update", "user1Update@emal.ru");
        when(userService.delete(1L))
                .thenReturn(userDto);

        //Act
        mockMvc.perform(delete("/users/1"))
                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }


    @Test
    public void deleteIdZeroTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto(1L, "user1Update", "user1Update@emal.ru");
        when(userService.delete(1L))
                .thenReturn(userDto);

        //Act
        mockMvc.perform(delete("/users/0"))
                //Assert
                .andExpect(status().isConflict());
    }

    @Test
    public void deleteIdNegativTest() throws Exception {
        //Assign
        UserDto userDto = new UserDto(1L, "user1Update", "user1Update@emal.ru");
        when(userService.delete(1L))
                .thenReturn(userDto);

        //Act
        mockMvc.perform(delete("/users/-1"))
                //Assert
                .andExpect(status().isConflict());
    }
}
