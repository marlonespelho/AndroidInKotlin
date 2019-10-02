package br.com.espelho.AnexosApp.Entities

data class UsuarioEntity(val id: Int , var nome:String, var email:String, var senha: String = "", var telefone :String?){

}