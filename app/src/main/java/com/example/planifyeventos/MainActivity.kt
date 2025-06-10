package com.example.planifyeventos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planifyeventos.screens.Cadastro
import com.example.planifyeventos.screens.Home
import com.example.planifyeventos.screens.Login
import com.example.planifyeventos.screens.Perfil
import com.example.planifyeventos.screens.RecuperarSenha
import com.example.planifyeventos.screens.RedefinirSenhaScreen
import com.example.planifyeventos.utils.SharedPrefHelper
import com.example.planifyeventos.screens.VerificarCodigo


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navegacao = rememberNavController()
            val context = applicationContext

            val rotaInicial = if (SharedPrefHelper.obterEmail(context) != null) {
                "perfil"
            } else {
                "login"
            }

            NavHost(
                navController = navegacao,
                startDestination = rotaInicial
            ) {
                composable(route = "cadastro") { Cadastro(navegacao) }
                composable(route = "login") { Login(navegacao) }
                composable(route = "home") { Home(navegacao) }
                composable(route = "perfil") { Perfil(navegacao) }
                composable(route = "recuperar_senha") { RecuperarSenha(navegacao) }


                // ✅ rota para tela de verificação de código
                composable(route = "verificar_codigo/{email}") { backStackEntry ->
                    val email = backStackEntry.arguments?.getString("email")
                    if (email != null) {
                        VerificarCodigo(navegacao, email)
                    }
                }

                // ✅ rota para tela de redefinir senha
                composable(route = "redefinir_senha/{idUsuario}") { backStackEntry ->
                    val idUsuario = backStackEntry.arguments?.getString("idUsuario")?.toIntOrNull()
                    if (idUsuario != null) {
                        RedefinirSenhaScreen(navegacao, idUsuario)
                    }
                }
            }

        }
    }
}
