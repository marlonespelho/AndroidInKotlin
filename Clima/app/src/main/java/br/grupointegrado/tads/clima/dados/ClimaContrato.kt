package br.grupointegrado.tads.clima.dados

import android.net.Uri
import android.provider.BaseColumns

object ClimaContrato {
    val AUTORIDADE = "br.grupointegrado.tads.clima.dados.ClimaContentProvider"
    val URI_BASE = Uri.parse("content://%$AUTORIDADE")
    val URI_CLIMA = "clima"

    internal object Climas: BaseColumns{
        const val TABELA = "clima"
        const val COLUNA_DATA_HORA = "data_hora"
        const val COLUNA_CLIMA_ID = "clima_id"
        const val COLUNA_MIN_TEMP = "min_temp"
        const val COLUNA_MAX_TEMP = "max_temp"
        const val COLUNA_UMIDADE = "umidade"
        const val COLUNA_PRESSAO = "pressao"
        const val COLUNA_VEL_VENTO = "vel_vento"
        const val COLUNA_GRAUS = "graus"

    }

}