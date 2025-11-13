package pt.iade.ei.bestumbrella1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import pt.iade.ei.bestumbrella1.MainNavigation.MainNavigation
import pt.iade.ei.bestumbrella1.ui.theme.BestUmbrella1Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BestUmbrella1Theme {
                AppContent()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent() {
    val navController = rememberNavController()

    Scaffold {
        MainNavigation(navController = navController)
    }
}

 

// Créditos removidos a pedido: rodapé não é mais exibido globalmente
