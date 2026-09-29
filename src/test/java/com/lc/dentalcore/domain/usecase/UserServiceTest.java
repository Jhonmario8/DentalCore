package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.api.IPasswordServicePort;
import com.lc.dentalcore.domain.api.ITokenServicePort;
import com.lc.dentalcore.domain.exception.InvalidCredentialsException;
import com.lc.dentalcore.domain.exception.InvalidPassword;
import com.lc.dentalcore.domain.exception.UsernameAlreadyExistsException;
import com.lc.dentalcore.domain.model.User;
import com.lc.dentalcore.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // Credenciales ficticias, solo para pruebas.
    private static final String USERNAME = "usuario_ejemplo";
    private static final String RAW_PASSWORD = "ClaveEjemplo1";
    private static final String HASHED_PASSWORD = "hash-de-ejemplo";
    private static final String TOKEN = "token-de-ejemplo";

    @Mock
    private IUserPersistencePort userPersistencePort;
    @Mock
    private IPasswordServicePort passwordServicePort;
    @Mock
    private ITokenServicePort tokenServicePort;

    @InjectMocks
    private UserService userService;

    private static User user(String password) {
        return new User(null, USERNAME, password, true);
    }

    private static User storedUser() {
        return new User(1L, USERNAME, HASHED_PASSWORD, true);
    }

    @Nested
    @DisplayName("createUser")
    class CreateUser {

        @Test
        @DisplayName("un usuario nuevo se guarda con la contraseña codificada")
        void newUserIsSavedWithEncodedPassword() {
            // given
            when(userPersistencePort.findByUsername(USERNAME)).thenReturn(Optional.empty());
            when(passwordServicePort.encodePassword(RAW_PASSWORD)).thenReturn(HASHED_PASSWORD);

            // when
            userService.createUser(user(RAW_PASSWORD));

            // then
            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(userPersistencePort).saveUser(captor.capture());
            assertEquals(USERNAME, captor.getValue().getUsername());
            assertEquals(HASHED_PASSWORD, captor.getValue().getPassword());
        }

        @Test
        @DisplayName("un username ya registrado lanza UsernameAlreadyExistsException")
        void duplicateUsernameThrows() {
            when(userPersistencePort.findByUsername(USERNAME)).thenReturn(Optional.of(storedUser()));

            assertThrows(UsernameAlreadyExistsException.class, () -> userService.createUser(user(RAW_PASSWORD)));

            verify(userPersistencePort, never()).saveUser(any());
            verify(passwordServicePort, never()).encodePassword(anyString());
        }

        @ParameterizedTest
        @ValueSource(strings = {"corta1A", "sinmayuscula1", "SinNumeroAqui", ""})
        @DisplayName("una contraseña débil (menos de 8, sin mayúscula o sin número) lanza InvalidPassword")
        void weakPasswordThrows(String password) {
            assertThrows(InvalidPassword.class, () -> userService.createUser(user(password)));

            verifyNoInteractions(userPersistencePort, passwordServicePort);
        }
    }

    @Nested
    @DisplayName("login")
    class Login {

        @Test
        @DisplayName("credenciales correctas devuelven el token")
        void validCredentialsReturnToken() {
            // given
            User stored = storedUser();
            when(userPersistencePort.findByUsername(USERNAME)).thenReturn(Optional.of(stored));
            when(passwordServicePort.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenReturn(true);
            when(tokenServicePort.generateToken(stored)).thenReturn(TOKEN);

            // when
            String token = userService.login(user(RAW_PASSWORD));

            // then
            assertEquals(TOKEN, token);
        }

        @Test
        @DisplayName("contraseña incorrecta lanza InvalidCredentialsException y no genera token")
        void wrongPasswordThrows() {
            when(userPersistencePort.findByUsername(USERNAME)).thenReturn(Optional.of(storedUser()));
            when(passwordServicePort.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenReturn(false);

            assertThrows(InvalidCredentialsException.class, () -> userService.login(user(RAW_PASSWORD)));

            verify(tokenServicePort, never()).generateToken(any());
        }

        @Test
        @DisplayName("usuario inexistente lanza InvalidCredentialsException sin comparar contraseñas")
        void unknownUserThrows() {
            when(userPersistencePort.findByUsername(USERNAME)).thenReturn(Optional.empty());

            assertThrows(InvalidCredentialsException.class, () -> userService.login(user(RAW_PASSWORD)));

            verify(passwordServicePort, never()).matches(anyString(), anyString());
            verify(tokenServicePort, never()).generateToken(any());
        }

        @Test
        @DisplayName("una contraseña con formato inválido se rechaza antes de consultar la BD")
        void malformedPasswordIsRejectedEarly() {
            // Comportamiento actual: login también aplica la política de contraseñas y responde 400, no 401.
            assertThrows(InvalidPassword.class, () -> userService.login(user("debil")));

            verifyNoInteractions(userPersistencePort, passwordServicePort, tokenServicePort);
        }
    }
}
