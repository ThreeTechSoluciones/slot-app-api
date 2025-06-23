package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.dto.StudentRequestDTO;
import com.three_tech_solutions.slot_app.dto.StudentResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//definimos el path y el metodo HTTP en la interfaz.
@RequestMapping("/students")
public interface StudentController {
    @PostMapping
    public StudentResponseDTO createStudent(@Valid @RequestBody StudentRequestDTO studentDTO);
}
