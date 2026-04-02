package se.hkr.andriod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import se.hkr.andriod.navigation.AppNavGraph
import se.hkr.andriod.ui.theme.AndriodTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndriodTheme {
                AppNavGraph()
            }
        }
    }
}
