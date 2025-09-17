package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.controllers.responses.PriceResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.mappers.StudentMapper;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.UserRepository;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                                        plan.getPrices()
                                                .stream()
                                                // TODO: Cambiar lógica para obtener el precio vigente, evitar logica duplicada con PlanServiceImpl
                                                .findFirst()
                                                .map(price -> new PriceResponse(price.getId(), price.getAmount()))
                                                .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "Hubo un error al obtener los precios de los planes"))
                                ))
                                .toList()
                )
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Hubo un error al encontrar el usuario"));
    }
}
