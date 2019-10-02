package br.com.espelho.AnexosApp.constants

class DataBaseConstants {

    object USER{
        const val TABLE_NAME = "usuario"
        object COLUMNS{
            const val ID = "id"
            const val NOME = "nome"
            const val TELEFONE = "telefone"
            const val EMAIL = "email"
            const val SENHA = "senha"
        }
    }

    object ANEXO{
        const val TABLE_NAME = "anexo"
        object COLUMNS{
            const val ID = "id"
            const val USER = "user_id"
            const val TIPOANEXO = "tipo_anexo"
            const val ANEXO = "anexo"
        }
    }

}