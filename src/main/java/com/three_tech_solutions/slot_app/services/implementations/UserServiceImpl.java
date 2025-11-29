package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.responses.ListSlotsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.mappers.StudentMapper;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.UserRepository;
import com.three_tech_solutions.slot_app.services.interfaces.SlotService;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
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

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Credenciales ingresadas incorrectas"));
    }

    @Override
    public List<StudentResponse> getUserStudents(UUID userId, String filter) {
        return studentService.getStudentsByUserAndNameAndLastNameAndDni(
                    getUserByIdOrThrowException(userId),
                    filter
            )
                .stream()
                .map(studentMapper::toStudentResponse)
                .toList();
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
    public List<PlanResponse> getUserPlans(UUID userId) {
        return userRepository.findById(userId)
                .map(user ->
                        user.getPlans()
                                .stream()
                                .map(plan -> new PlanResponse(
                                        plan.getId(),
                                        plan.getName(),
                                        plan.getCurrentPrice(),
                                        plan.getNumberOfDays()
                                ))
                                .toList()
                )
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Hubo un error al encontrar el usuario"));
    }

    @Override
    public ListSlotsResponse getSlotsByDayOfWeek(UUID userId, DayOfWeek dayOfWeek) {
        return slotService.getSlotsByDayOfWeek(getUserByIdOrThrowException(userId).getId(), dayOfWeek);
    }
}
