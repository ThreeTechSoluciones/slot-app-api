package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.CreatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdatePriceRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.controllers.responses.PriceResponse;
import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.Price;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.PlanRepository;
import com.three_tech_solutions.slot_app.services.interfaces.PlanService;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@Service
@AllArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final UserService userService;
    private final PlanRepository planRepository;

    @Override
    public PlanResponse createPlan(CreatePlanRequest createPlanRequest) {
        try {
            Plan plan = createAndSavePlan(createPlanRequest);
            return buildPlanResponse(plan);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El plan con el nombre ingresado ya existe");
        }
    }

    private PlanResponse buildPlanResponse(Plan plan) {
        return new PlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getPrices()
                        .stream()
                        // TODO: Cambiar lógica para obtener el precio vigente, evitar logica duplicada con UserServiceImpl
                        .findFirst()
                        .map(price -> new PriceResponse(price.getId(), price.getAmount()))
                        .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "Hubo un error al obtener los precios de los planes"))
        );
    }

    @Override
    public Plan getPlanByIdOrThrowException(UUID planId) {
        return this.planRepository.findById(planId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El plan ingresado no existe"));
    }

    @Override
    public PlanResponse updatePrice(UUID planId, UpdatePriceRequest updatePriceRequest) {
        Plan plan = this.getPlanByIdOrThrowException(planId);
        plan.getPrices().add(new Price(updatePriceRequest.amount(), updatePriceRequest.startDate()));
        return buildPlanResponse(
                this.planRepository.save(plan)
        );
    }

    private Plan createAndSavePlan(CreatePlanRequest createPlanRequest) {
        return planRepository.save(
                buildPlan(createPlanRequest)
        );
    }

    private Plan buildPlan(CreatePlanRequest createPlanRequest) {
        return new Plan(
                createPlanRequest.name(),
                createPlanRequest.numberOfDays(),
                createListOfPrices(createPlanRequest),
                getUser(createPlanRequest)
        );
    }

    private static List<Price> createListOfPrices(CreatePlanRequest createPlanRequest) {
        return List.of(
                new Price(
                        createPlanRequest.amount(),
                        createPlanRequest.startDate()
                )
        );
    }

    private User getUser(CreatePlanRequest createPlanRequest) {
        return userService.getUserByIdOrThrowException(createPlanRequest.userId());
    }
}
