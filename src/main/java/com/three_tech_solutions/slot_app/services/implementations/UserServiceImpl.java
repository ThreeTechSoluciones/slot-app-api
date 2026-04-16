package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.RecoverPasswordRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateUserCapacityRequest;
import com.three_tech_solutions.slot_app.controllers.responses.*;
import com.three_tech_solutions.slot_app.data.enums.CalendarViewType;
import com.three_tech_solutions.slot_app.data.enums.StudentSituation;
import com.three_tech_solutions.slot_app.data.mappers.StudentMapper;
import com.three_tech_solutions.slot_app.data.mappers.UserPreferencesMapper;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.UserRepository;
import com.three_tech_solutions.slot_app.security.components.CodeGenerator;
import com.three_tech_solutions.slot_app.services.interfaces.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;
    private final SlotService slotService;
    private final PlanService planService;
    private final CalendarService calendarService;
    private final UserPreferencesMapper userPreferencesMapper;
    private final PasswordRecoveryTokenService passwordRecoverytokenService;


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Credenciales ingresadas incorrectas"));
    }

    @Override
    public Page<StudentResponse> getUserStudents(UUID userId, String filter, boolean filterByAbsences, StudentSituation status, Boolean isActive, Pageable pageable) {
        return studentService.getStudentsByUserAndNameAndLastNameAndDni(
                getUserByIdOrThrowException(userId),
                filter,
                filterByAbsences,
                status,
                isActive,
                pageable
        );
    }

    @Override
    public User getUserByIdOrThrowException(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Hubo un error al encontrar el usuario"));
    }

    @Override
    public void createUser(String username, String password) {
        try {
            saveUser(new User(
                    username,
                    passwordEncoder.encode(password)
            ));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(BAD_REQUEST, "El usuario ya existe.");
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Ocurrió un error al registrar el usuario. Por favor, contacte con el administrador.");
        }
    }
    @Override
    public Page<PlanResponse> getUserPlans(UUID userId, String planName, Pageable pageable) {
        return userRepository.findById(userId)
                .map(user ->
                        planService.getPlansByUserAndName(user, planName, pageable)
                )
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Hubo un error al encontrar el usuario"));
    }

    @Override
    @Transactional
    public void updateUserCapacityPreference(UUID userId, UpdateUserCapacityRequest request) {
        User user = getUserByIdOrThrowException(userId);
        byte newCapacity = (byte) request.capacity();

        slotService.validateFutureSpecificSlotsCapacity(user, newCapacity);
        user.getUserPreferences().setSlotCapacity(newCapacity);
        updateSlotsAndSpecificSlotsCapacity(user, newCapacity);
        saveUser(user);
    }

    @Override
    public List<UserSlotsByDayResponse> getSlotsByDayOfWeek(UUID userId, DayOfWeek dayOfWeek) {
        return slotService.getSlotsByDayOfWeek(getUserByIdOrThrowException(userId), dayOfWeek);
    }

    @Override
    public CalendarResponse getCalendar(UUID userId, CalendarViewType viewType, LocalDate date) {
        return userRepository.findById(userId)
                .map(user -> calendarService.getUserCalendar(user, viewType, date))
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Hubo un error al encontrar el usuario"));
    }

    @Override
    public UserPreferencesResponse getUserPreferences(@PathVariable UUID userId) {
        return this.userRepository.findById(userId)
                .map(userPreferencesMapper::toUserPreferencesResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Hubo un error al encontrar el usuario"));
    }

    @Override
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    @Transactional
    public void recoverPassword(RecoverPasswordRequest request) {

        try {
            User user = loadUserByUsername(request.username());

            updateUserPassword(user, request.password());

            passwordRecoverytokenService.validateTokenAndDisableIt(user, request.token());

        } catch (ResponseStatusException e) {
            log.info("Error al recuperar contraseña para el usuario {}: {}", request.username(), e.getReason());
            throw e;

        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al recuperar la contraseña. Por favor, contacte con el administrador.");
        }
    }

    @Override
    public String generateRestorePasswordCode(String username) {
        String code = CodeGenerator.generate(6);
        passwordRecoverytokenService
                .savePasswordRecoveryTokenForUser(loadUserByUsername(username), code);
        return code;
    }


    private void updateSlotsAndSpecificSlotsCapacity(User user, byte newCapacity) {
        slotService.updateSlotsAndSpecificSlotsCapacity(user, newCapacity);
    }

    private void updateUserPassword(User user, String rawPassword) {
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }

}