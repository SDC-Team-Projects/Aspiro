package com.capcom.aspiro.controller;

import com.capcom.aspiro.api.controller.UserController;
import com.capcom.aspiro.api.dto.response.UserResponse;
import com.capcom.aspiro.api.service.interfaces.UserService;
import com.capcom.aspiro.api.exception.custom.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Test
    void getCurrentUser_success() {
        UserController controller = new UserController(userService);

        UserResponse respDto = UserResponse.builder().id(1L).name("U").email("u@example.com").build();
        when(userService.getCurrentUser()).thenReturn(respDto);

        ResponseEntity<UserResponse> resp = controller.getCurrentUser();
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getEmail()).isEqualTo("u@example.com");
    }

    @Test
    void getCurrentUser_notFound_propagates() {
        UserController controller = new UserController(userService);

        when(userService.getCurrentUser()).thenThrow(new ResourceNotFoundException("User not found"));

        assertThrows(ResourceNotFoundException.class, controller::getCurrentUser);
    }
}
