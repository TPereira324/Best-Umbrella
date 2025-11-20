package pt.iade.ei.bestumbrella1.network

data class AluguerDto(
    val aluguerId: Long?,
    val utilizadorId: Int?,
    val guardaChuvaId: Int?,
    val pontoInicioId: Int?,
    val pontoFimId: Int?,
    val dataInicio: String?,
    val dataFim: String?,
    val custo: Double?,
    val estado: String?
)