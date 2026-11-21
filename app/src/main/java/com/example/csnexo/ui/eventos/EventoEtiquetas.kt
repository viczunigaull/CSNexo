package com.example.csnexo.ui.eventos

import com.example.csnexo.domain.model.TipoEvento

/**
 * Etiquetas visibles de los tipos de evento.
 */
fun TipoEvento.etiqueta(): String = when (this) {
    TipoEvento.EVENTO_CORPORATIVO -> "Evento corporativo"
    TipoEvento.FERIA_EMPRESARIAL -> "Feria empresarial"
    TipoEvento.ACTIVIDAD_MUNICIPAL -> "Actividad municipal"
    TipoEvento.JORNADA_BENEFICIOS -> "Jornada de beneficios"
    TipoEvento.FERIA_INSTITUCIONAL -> "Feria institucional"
    TipoEvento.ACTIVIDAD_TRABAJADORES -> "Actividad de trabajadores"
    TipoEvento.ACTIVACION_COMERCIAL -> "Activación comercial"
    TipoEvento.OTRA -> "Otra"
}
