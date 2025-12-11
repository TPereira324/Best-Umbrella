package pt.iade.ei.bestumbrella1.model

data class Umbrella(
    val guardaChuvaId: Int,
    val codigoQr: String,
    val estado: String,
    val cor: String,
    val tipo: String,
    val pontoId: Int,
    val dataRegisto: String
)

object UmbrellaData {
    private val stationNames = mapOf(
        1 to "IADE",
        2 to "Parque das Nações",
        3 to "Metro Moscavide",
        4 to "Metro Oriente",
        5 to "Terreiro do Paço",
        6 to "Rossio",
        7 to "Baixa-Chiado",
        8 to "Marquês de Pombal"
    )

    val umbrellas = listOf(
        Umbrella(1, "QR001", "Disponível", "Azul", "Automático", 1, "2025-11-05 13:58:09"),
        Umbrella(2, "QR002", "Disponível", "Vermelho", "Compacto", 2, "2025-11-05 13:58:09"),
        Umbrella(3, "QR003", "Em uso", "Preto", "Automático", 3, "2025-11-05 13:58:09"),
        Umbrella(4, "QR004", "Manutenção", "Amarelo", "Manual", 4, "2025-11-05 13:58:09"),
        Umbrella(5, "QR005", "Disponível", "Roxo", "Compacto", 5, "2025-11-05 13:58:09"),
        Umbrella(6, "QR006", "Disponível", "Cinza", "Automático", 6, "2025-11-05 13:58:09"),
        Umbrella(7, "QR007", "Em uso", "Verde", "Manual", 7, "2025-11-05 13:58:09"),
        Umbrella(8, "QR008", "Disponível", "Preto", "Compacto", 8, "2025-11-05 13:58:09")
    )

    fun findByQrCode(code: String): Umbrella? =
        umbrellas.find { it.codigoQr.equals(code, ignoreCase = true) }

    fun stationNameFor(pontoId: Int): String = stationNames[pontoId] ?: "Ponto #$pontoId"
}

data class RentalEntry(
    val date: String,
    val from: String,
    val to: String,
    val cost: Double,
    val duration: String
)

fun extractHours(duration: String): Int {
    val regex = Regex("(\\d+)h")
    val match = regex.find(duration)
    return match?.groupValues?.get(1)?.toIntOrNull() ?: 0
}

fun historySortKey(dateStr: String): Long {
    val cal = java.util.Calendar.getInstance()
    cal.set(java.util.Calendar.SECOND, 0)
    cal.set(java.util.Calendar.MILLISECOND, 0)
    var hour = 0
    var minute = 0
    val timeMatch = Regex("(\\d{2}):(\\d{2})").find(dateStr)
    if (timeMatch != null) {
        hour = timeMatch.groupValues[1].toIntOrNull() ?: 0
        minute = timeMatch.groupValues[2].toIntOrNull() ?: 0
    }
    when {
        dateStr.startsWith("Hoje", ignoreCase = true) -> {}
        dateStr.startsWith("Ontem", ignoreCase = true) -> cal.add(
            java.util.Calendar.DAY_OF_MONTH,
            -1
        )

        dateStr.startsWith("Há", ignoreCase = true) -> {
            val d =
                Regex("Há\\s+(\\d+)\\s+dias").find(dateStr)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            cal.add(java.util.Calendar.DAY_OF_MONTH, -d)
        }
    }
    cal.set(java.util.Calendar.HOUR_OF_DAY, hour)
    cal.set(java.util.Calendar.MINUTE, minute)
    return cal.timeInMillis
}

