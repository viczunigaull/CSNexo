package com.example.csnexo.navigation

/**
 * Rutas de navegación de la aplicación.
 *
 * Se usan rutas con texto simple para mantener el MVP fácil de explicar. La
 * navegación con rutas tipadas se puede incorporar más adelante sin cambiar los
 * ViewModels.
 */
object Routes {

    const val MENU = "menu"
    const val FERIADOS = "feriados"
    const val OPORTUNIDADES = "oportunidades"
    const val OPORTUNIDAD_NUEVA = "oportunidades/nueva"
    const val OPORTUNIDAD_EDITAR = "oportunidades/editar/{id}"
    const val RESUMEN = "oportunidades/resumen/{id}"

    const val ORGANIZACIONES = "organizaciones"
    const val ORGANIZACION_NUEVA = "organizaciones/nueva"
    const val ORGANIZACION_EDITAR = "organizaciones/editar/{id}"

    const val ARG_OPORTUNIDAD_ID = "id"
    const val ARG_ORGANIZACION_ID = "id"

    fun editar(oportunidadId: Long): String =
        "oportunidades/editar/$oportunidadId"

    fun resumen(oportunidadId: Long): String =
        "oportunidades/resumen/$oportunidadId"

    fun organizacionEditar(id: Long): String =
        "organizaciones/editar/$id"
}
