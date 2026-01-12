package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.UpdateUserCapacityRequest;
import com.three_tech_solutions.slot_app.controllers.responses.*;
import com.three_tech_solutions.slot_app.data.enums.CalendarViewType;
import com.three_tech_solutions.slot_app.data.mappers.StudentMapper;
import com.three_tech_solutions.slot_app.data.mappers.UserPreferencesMapper;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.UserRepository;
import com.three_tech_solutions.slot_app.services.interfaces.*;
import lombok.AllArgsConstructor;
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


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Credenciales ingresadas incorrectas"));
    }

    @Override
    public Page<StudentResponse> getUserStudents(UUID userId, String filter, boolean filterByAbsences, Pageable pageable) {
        return studentService.getStudentsByUserAndNameAndLastNameAndDni(
                        getUserByIdOrThrowException(userId),
                        filter,
                        filterByAbsences,
                        pageable
                )
                .map(studentMapper::toStudentResponse);
    }

    @Override
    public User getUserByIdOrThrowException(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Hubo un error al encontrar el usuario"));
    }

    @Override
    public void createUser(String username, String password) {
        try {
            userRepository.save(new User(
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
    public List<PlanResponse> getUserPlans(UUID userId, String planName) {
        return userRepository.findById(userId)
                .map(user ->
                        planService.getPlansByUserAndName(user, planName)
                )
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Hubo un error al encontrar el usuario"));
    }

    @Override
    public void updateUserCapacityPreference(UUID userId, UpdateUserCapacityRequest updateUserCapacityRequest) {
        this.userRepository.findById(userId)
                .ifPresentOrElse(
                        (user) -> updateUserCapacityAndSaveIt(updateUserCapacityRequest, user),
                        () -> {
                            throw new ResponseStatusException(BAD_REQUEST, "Hubo un error al encontrar el usuario");
                        }
                );
    }

    @Override
    public List<UserSlotsByDayResponse> getSlotsByDayOfWeek(UUID userId, DayOfWeek dayOfWeek) {
        return slotService.getSlotsByDayOfWeek(getUserByIdOrThrowException(userId), dayOfWeek);
    }

    @Override
    public List<CalendarResponse> getCalendar(UUID userId, CalendarViewType viewType, LocalDate date) {
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

    private void updateUserCapacityAndSaveIt(UpdateUserCapacityRequest updateUserCapacityRequest, User user) {
        user.getUserPreferences().setSlotCapacity(updateUserCapacityRequest.capacity());
        userRepository.save(user);
    }
}
