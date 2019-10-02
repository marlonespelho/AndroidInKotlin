package br.com.espelho.AnexosApp.Entities

import br.com.espelho.AnexosApp.enuns.TipoAnexoEnum
import java.sql.Blob

data class AnexoEntity(val id: Int, val usuarioId: Int, val tipoAnexo: TipoAnexoEnum, var anexo: ByteArray? ) {

}