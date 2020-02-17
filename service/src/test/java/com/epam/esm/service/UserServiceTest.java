package com.epam.esm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.service.impl.UserServiceImpl;
import java.util.Optional;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  private static User user;
  @Mock
  private UserMapper userMapper;
  @InjectMocks
  private UserServiceImpl userService;

  @BeforeClass
  public static void beforeClass() {
    user = new User(1L, "username", "password", Role.USER);
  }

  @Test(expected = ServerException.class)
  public void createTestUserWithExistedUsernameThrowsException() {
    when(userMapper.selectByUsername(any())).thenReturn(Optional.of(user));
    userService.create(user);
  }

  @Test(expected = ServerException.class)
  public void createTestErrorCreatingUserThrowsException() {
    when(userMapper.selectByUsername(any())).thenReturn(Optional.empty());
    when(userMapper.selectById(any())).thenReturn(Optional.empty());
    userService.create(user);
  }

  @Test()
  public void createTestCreatesUserReturnsUser() {
    when(userMapper.selectByUsername(any())).thenReturn(Optional.empty());
    when(userMapper.selectById(any())).thenReturn(Optional.of(user));
    User actual = userService.create(user);
    Assert.assertEquals(user, actual);
  }

  @Test
  public void findByUsernameTestExistedUsernameReturnsUser() {
    when(userMapper.selectByUsername(any())).thenReturn(Optional.of(user));
    Assert.assertEquals(user, userService.findByUsername("username"));
  }

  @Test(expected = ServerException.class)
  public void findByUsernameTestNotExistedUsernameThrowsException() {
    when(userMapper.selectByUsername(any())).thenReturn(Optional.empty());
    Assert.assertEquals(user, userService.findByUsername("username"));
  }
}