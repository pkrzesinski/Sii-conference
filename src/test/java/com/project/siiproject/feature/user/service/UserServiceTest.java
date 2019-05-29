package com.project.siiproject.feature.user.service;

import com.project.siiproject.feature.user.dao.UserRepository;
import com.project.siiproject.feature.user.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService classUnderTest;

    @Test
    public void shouldReturnUserList() {
        //given
        List<User> users = createdMockedList();
        when(userRepository.findAll()).thenReturn(users);
        //when
        List<User> result = classUnderTest.getAllUsers();
        //then
        assertEquals(4, result.size());
    }

    @Test
    public void shouldSaveUser() {
        //given;
        User user = new User("UserTest", "user@test.com");
        //when
        classUnderTest.save(user);
        //then
        verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    public void shouldSaveUserOnlyOneTime() {
        //given;
        User user = new User("UserTest", "user@test.com");
        //when
        classUnderTest.save(user);
        //then
        verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void shouldGetUserByLogin() {
        //given
        User user = createdMockedList().get(0);
        when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
        //when
        User result = classUnderTest.getUserByLogin("Test1");
        //then
        assertThat(result).isEqualTo(user);
    }

    @Test
    public void shouldGetUserByEmail() {
        //given
        User user = createdMockedList().get(0);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        //when
        User result = classUnderTest.getUserByEmail("test1@test1.com");
        //then
        assertThat(result).isEqualTo(user);
    }

    @Test
    public void shouldGetUserByLoginAndEmail() {
        //given
        User user = createdMockedList().get(1);
        when(userRepository.findByLoginAndEmail(user.getLogin(), user.getEmail())).thenReturn(user);
        //when
        User result = classUnderTest.getUserByLoginAndEmail("Test2", "test2@test2.com");
        //then
        assertThat(result).isEqualTo(user);
    }

    @Test
    public void shouldUpdateUser() {
        //given
        User user = createdMockedList().get(2);
        user.setEmail("updated@user.com");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(userRepository.getOne(Mockito.any())).thenReturn(user);
        //when
        User result = classUnderTest.update(user);
        //then
        assertThat(result.getEmail()).isEqualTo("updated@user.com");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIfEmailAlreadyInDataBase() {
        //given
        User user = createdMockedList().get(0);
        when(userRepository.getOne(Mockito.any())).thenReturn(user);
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        //when
        classUnderTest.emailUpdate(user);
        //then
    }

    private List<User> createdMockedList() {
        List<User> mockedList = new ArrayList<>();
        mockedList.add(new User("Test1", "test1@test1.com"));
        mockedList.add(new User("Test2", "test2@test2.com"));
        mockedList.add(new User("Test3", "test3@test3.com"));
        mockedList.add(new User("Test4", "test4@test4.com"));
        return mockedList;
    }
}