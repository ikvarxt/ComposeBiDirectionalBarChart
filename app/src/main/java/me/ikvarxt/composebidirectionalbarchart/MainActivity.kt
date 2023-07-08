package me.ikvarxt.composebidirectionalbarchart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ikvarxt.composebidirectionalbarchart.ui.theme.ComposeBiDirectionalBarChartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = listOf(
            BiDirectionalBarData(94, 80),
            BiDirectionalBarData(106, 80),
            BiDirectionalBarData(94, 76),
            BiDirectionalBarData(150, 90),
            BiDirectionalBarData(120, 70),
            BiDirectionalBarData(94, 80),
            BiDirectionalBarData(106, 80),
            BiDirectionalBarData(94, 76),
            BiDirectionalBarData(150, 90),
            BiDirectionalBarData(120, 70),
            BiDirectionalBarData(94, 80),
            BiDirectionalBarData(106, 80),
            BiDirectionalBarData(94, 76),
            BiDirectionalBarData(150, 90),
            BiDirectionalBarData(120, 70),
        )
        setContent {
            ComposeBiDirectionalBarChartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 200.dp)
                        .height(300.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val config = BiDirectionalBarChartConfig().copy(

                    )
                    BiDirectionalBarChart(dataList = list, config = config)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeBiDirectionalBarChartTheme {
        Greeting("Android")
    }
}