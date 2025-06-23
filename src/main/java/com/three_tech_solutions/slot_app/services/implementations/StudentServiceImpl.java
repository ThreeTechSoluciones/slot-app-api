package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.repositories.StudentRepository;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        // Lógica de negocio: validaciones, cálculos, guardarVALIDACIONES, Existe el alumno?, Tiene todos los datos?
        //El tipo de pago no puede ser nulo y tiene dos valores posibles:
        //           -Del 1 al 10
        //           -Día específico
        //Las clases extra tiene que ser un valor opcional (obligatorio en caso de que el tipo de pago sea del 1-10 y la fecha actual sea mayor al 10 del mes), pero si se ingresa debe ser mayor a 10 y menor a 20.
        //El día de pago debe ser un dato opcional (obligatorio en caso de que el tipo de pago sea dia específico) y debe ser mayor o igual a 1 y menor o igual a 31
        //La fecha de admisión en caso de no recibirse, debe ser por defecto la fecha actual
        //El tipo de HTTP Status a responder debe ser el 201
        //En caso de que haya un error en los datos de la request se debe responder un status 400 indicando cuál es el error
        // Al registrar el alumno, debe vincularse con el usuario (profesional logueado)



        //Guardar al alumno.
        //Retornar el alumno creado.

    }
}
