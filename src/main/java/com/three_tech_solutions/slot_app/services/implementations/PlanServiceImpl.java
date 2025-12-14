package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.CreatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdatePriceRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.Price;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.PlanRepository;
import com.three_tech_solutions.slot_app.services.interfaces.PlanService;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
public class PlanServiceImpl implements PlanService {

    private final UserService userService;
    private final PlanRepository planRepository;

    public PlanServiceImpl(@Lazy UserService userService, PlanRepository planRepository) {
        this.userService = userService;
        this.planRepository = planRepository;
    }

    @Override
    public PlanResponse createPlan(CreatePlanRequest createPlanRequest) {
        try {
            Plan plan = createAndSavePlan(createPlanRequest);
            return buildPlanResponse(plan);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El plan con el nombre ingresado ya existe");
        }
    }

    @Override
    public Plan getPlanByIdOrThrowException(UUID planId) {
        return this.planRepository.findById(planId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El plan ingresado no existe"));
    }

    @Override
    public PlanResponse updatePrice(UUID planId, UpdatePriceRequest updatePriceRequest) {
        Plan plan = this.getPlanByIdOrThrowException(planId);
        setEndDateToCurrentPriceIfNecessary(updatePriceRequest, plan);
        plan.getPrices().addFirst(new Price(updatePriceRequest.amount(), updatePriceRequest.startDate()));
        return buildPlanResponse(
                this.planRepository.save(plan)
        );
    }

    @Override
    public List<PlanResponse> getPlansByUserAndName(User user, String planName) {
        return this.planRepository
                .findAllByUserAndPlanName(user, planName)
                .stream()
                .map(PlanServiceImpl::buildPlanResponse)
                .toList();
    }

    private static void setEndDateToCurrentPriceIfNecessary(UpdatePriceRequest updatePriceRequest, Plan plan) {
        LocalDate today = LocalDate.now();
        if (updatePriceRequest.startDate().isBefore(today) || updatePriceRequest.startDate().isEqual(today)) {
            plan.getCurrentPrice().setEndDate(today);
        }
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

    private static PlanResponse buildPlanResponse(Plan plan) {
        return new PlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getCurrentPrice().getAmount(),
                plan.getNumberOfDays()
        );
    }

    private static List<Price> createListOfPrices(CreatePlanRequest createPlanRequest) {
        return List.of(
                new Price(
                        createPlanRequest.amount(),
                        LocalDate.now()
                )
        );
    }

    private User getUser(CreatePlanRequest createPlanRequest) {
        return userService.getUserByIdOrThrowException(createPlanRequest.userId());
    }
}
