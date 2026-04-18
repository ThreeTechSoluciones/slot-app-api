package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.CreatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.controllers.responses.PriceResponse;
import com.three_tech_solutions.slot_app.data.mappers.PriceMapper;
import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.Price;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.PlanRepository;
import com.three_tech_solutions.slot_app.services.interfaces.PlanService;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class PlanServiceImpl implements PlanService {

    private final UserService userService;
    private final PlanRepository planRepository;
    private final PriceMapper priceMapper;


    public PlanServiceImpl(@Lazy UserService userService, PlanRepository planRepository, PriceMapper priceMapper) {
        this.userService = userService;
        this.planRepository = planRepository;
        this.priceMapper = priceMapper;
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
    public Page<PlanResponse> getPlansByUserAndName(User user, String planName, Pageable pageable) {
        /*
        * We need to remove the price sort from pageable sorts because price is not a property of the Plan entity,
        * and we sort by price in memory after we get the plans from the repository.
         */
        List<Sort.Order> sorts = removePriceSortFromPageableSorts(pageable);
        Page<PlanResponse> plans = getPlansFromRepository(user, planName, pageable, sorts);

        return new PageImpl<>(
                sortPlansByPriceIfNecessary(pageable, plans.getContent().stream()),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                plans.getTotalElements()
        );
    }

    @Override
    public void deletePlan(UUID planId) {
        Plan plan = this.getPlanByIdOrThrowException(planId);
        try {
            planRepository.delete(plan);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo eliminar el plan porque posee alumnos");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo eliminar el plan");
        }
    }

    @Override
    public PlanResponse updatePlan(UUID planId, UpdatePlanRequest updatePlanRequest) {
        return planRepository.findById(planId)
                .map(plan -> {
                    try {
                        if (mustUpdatePrice(updatePlanRequest)) {
                            validateStartDateOfNewPrice(updatePlanRequest);
                            setEndDateToCurrentPrice(updatePlanRequest, plan);
                            plan.getPrices().addFirst(new Price(updatePlanRequest.amount(), updatePlanRequest.startDate()));
                        }

                        plan.setName(updatePlanRequest.name());
                        return buildPlanResponse(planRepository.save(plan));
                    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El plan con el nombre ingresado ya existe");
                    }
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El plan ingresado no existe"));
    }

    private static boolean mustUpdatePrice(UpdatePlanRequest updatePlanRequest) {
        return updatePlanRequest.amount() != null;
    }

    private static void validateStartDateOfNewPrice(UpdatePlanRequest updatePlanRequest) {
        if (updatePlanRequest.startDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe ingresar la fecha de inicio del nuevo precio");
        }

        if (updatePlanRequest.startDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de inicio del nuevo precio no puede ser anterior a la fecha actual");
        }
    }

    private void setEndDateToCurrentPrice(UpdatePlanRequest updatePlanRequest, Plan plan) {
        plan
            .getPrices()
            .stream()
            .filter(price -> price.getEndDate() == null)
            .forEach(price -> price.setEndDate(updatePlanRequest.startDate()));
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

    private PlanResponse buildPlanResponse(Plan plan) {
        return new PlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getCurrentPrice().getAmount(),
                plan.getNextPrice()
                        .map(priceMapper::toPriceResponse)
                        .orElse(null),
                priceMapper.toPriceResponseList(plan.getFuturePrices()),
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


    private Page<PlanResponse> getPlansFromRepository(User user, String planName, Pageable pageable, List<Sort.Order> sorts) {
        return this.planRepository
                .findAllByUserAndPlanName(user, planName, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sorts)))
                .map(this::buildPlanResponse);
    }

    private List<Sort.Order> removePriceSortFromPageableSorts(Pageable pageable) {
        return pageable
                .getSort()
                .stream()
                .filter(order -> !order.getProperty().equalsIgnoreCase("price"))
                .toList();
    }

    private List<PlanResponse> sortPlansByPriceIfNecessary(Pageable pageable, Stream<PlanResponse> plansSortedByPrice) {
        Sort.Order priceSortOrder = pageable.getSort().getOrderFor("price");
        if (mustSortByPrice(priceSortOrder)) {
            plansSortedByPrice = sortPriceIsDesc(priceSortOrder) ? getPlansSortedByPriceDesc(plansSortedByPrice) : getPlansSortedByPriceAsc(plansSortedByPrice);
        }
        return plansSortedByPrice.toList();
    }

    private boolean mustSortByPrice(Sort.Order priceSortOrder) {
        return priceSortOrder != null;
    }

    private boolean sortPriceIsDesc(Sort.Order priceSortOrder) {
        return priceSortOrder.isDescending();
    }

    private Stream<PlanResponse> getPlansSortedByPriceDesc(Stream<PlanResponse> plansSortedByPrice) {
        return plansSortedByPrice.sorted(Comparator.comparingDouble(PlanResponse::currentPrice).reversed());
    }

    private Stream<PlanResponse> getPlansSortedByPriceAsc(Stream<PlanResponse> plansSortedByPrice) {
        return plansSortedByPrice.sorted(Comparator.comparingDouble(PlanResponse::currentPrice));
    }

}
