package com.unilibre.taller05.domain.usecase

import com.unilibre.taller05.domain.model.Receta
import com.unilibre.taller05.domain.repository.RecetasRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GenerarRecetasUseCase @Inject constructor(
    private val repo: RecetasRepository
) {
    operator fun invoke(ingredientes: List<String>): Flow<String> =
        repo.streamRecetasIA(ingredientes)
}

class GuardarFavoritaUseCase @Inject constructor(
    private val repo: RecetasRepository
) {
    suspend operator fun invoke(receta: Receta): Long = repo.toggleFavorita(receta)
}

class ObtenerFavoritasUseCase @Inject constructor(
    private val repo: RecetasRepository
) {
    operator fun invoke(): Flow<List<Receta>> = repo.favoritas()
}

class ObtenerRecetaUseCase @Inject constructor(
    private val repo: RecetasRepository
) {
    suspend operator fun invoke(id: Long): Receta? = repo.obtener(id)
}
