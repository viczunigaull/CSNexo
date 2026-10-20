package com.example.csnexo.domain.model

/**
 * Estados de gestión definidos en el caso.
 *
 * En la primera entrega el estado se selecciona en el formulario; las
 * transiciones obligatorias y el historial quedan para el alcance transversal.
 */
enum class EstadoOportunidad {
    EVENTO_IDENTIFICADO,
    CONTACTO_PENDIENTE,
    CONTACTO_REALIZADO,
    PROPUESTA_ENVIADA,
    EN_ESPERA_RESPUESTA,
    PARTICIPACION_CONFIRMADA,
    DESCARTADA
}
