package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.TestValuesGenerator;
import ru.practicum.shareit.exception.EmailConflictException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class UserServiceImplTest extends TestValuesGenerator {
    private final UserService service;

    public UserServiceImplTest(@Autowired EntityManager em, @Autowired UserService service) {
        super(em);
        this.service = service;
    }

    @Test
    void getById_returnUser() {
        setTestUser(1);
        User testUser = getTestUser(1);

        UserDto user = service.getById(testUser.getId());

        assertEqualsUsers(testUser, user);
    }

    @Test
    void create_returnNewUser() {
        UserDto userDto = new UserDto();
        userDto.setName("test");
        userDto.setEmail("test@test.com");

        UserDto user = service.create(userDto);
        User testUser = getTestUser(1);

        assertEqualsUsers(testUser, user);
    }

    @Test
    void create_EmailConflict() {
        setTestUser(1);
        UserDto userDto = new UserDto();
        userDto.setName("test");
        userDto.setEmail("1test@test.com");

        assertThrows(EmailConflictException.class, () -> service.create(userDto));
    }

    @Test
    void update() {
        setTestUser(1);
        UserDto userDto = new UserDto();
        userDto.setName("test updated");
        userDto.setEmail("test_updated@test.com");

        UserDto user = service.update(userDto, 1L);
        User testUser = getTestUser(1);

        assertEqualsUsers(testUser, user);
    }

    @Test
    void delete() {
        setTestUser(1);

        service.delete(1L);

        assertThrows(NoResultException.class, () -> getTestUser(1));
    }

    private void assertEqualsUsers(User user, UserDto userDto) {
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }
}