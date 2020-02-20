package com.epam.esm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.security.SecurityUserDetails;
import com.epam.esm.service.impl.UserServiceImpl;
import java.util.Optional;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

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
    doThrow(DataIntegrityViolationException.class).when(userMapper).insert(any());
    userService.create(user);
  }

  @Test
  public void createTestCreatesUserReturnsUser() {
    User actual = userService.create(user);
    Assert.assertEquals(user, actual);
  }

  @Test
  public void loadUserByUsernameTestCorrectFoundUser() {
    User user = new User(1L, "username", "password", Role.USER);
    SecurityUserDetails expected = new SecurityUserDetails(user);
    when(userMapper.selectByUsername(any())).thenReturn(Optional.of(user));
    SecurityUserDetails actual = (SecurityUserDetails) userService.loadUserByUsername("username");
    Assert.assertTrue(new ReflectionEquals(actual).matches(expected));
  }

  @Test(expected = ServerException.class)
  public void loadUserByUsernameTestEmptyUserThrowsException() {
    when(userMapper.selectByUsername(any())).thenReturn(Optional.empty());
    userService.loadUserByUsername("username");
  }
}