package com.three_tech_solutions.slot_app.constants;

public class SlotConstants {
    private SlotConstants() {}

    /**
     * Número de meses por adelantado para los cuales se crearán los slots específicos.
     * Esto se utiliza en el proceso de creación de slots específicos próximos,
     * donde se generan slots para un período futuro determinado.
     * Al establecer este valor, se asegura que siempre haya una cantidad suficiente
     * de slots disponibles para los usuarios en los próximos meses.
     */
    public static final int MONTHS_AHEAD = 6;
}
