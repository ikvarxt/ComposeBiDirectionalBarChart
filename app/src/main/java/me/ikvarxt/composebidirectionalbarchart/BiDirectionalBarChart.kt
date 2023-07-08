package me.ikvarxt.composebidirectionalbarchart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BiDirectionalBarData(
    val high: Int,
    val low: Int,
    // val date: Long,
)

data class BiDirectionalBarChartConfig(
    val chartLineCount: Int = 5,
    val chartLineGapCount: Int = chartLineCount - 1,
    val chartLabelNumberMin: Float = 40f,
    val chartLabelNumberMax: Float = 200f,
    val chartLabelNumberRangeSize: Float = chartLabelNumberMax - chartLabelNumberMin,

    val useFloatLabel: Boolean = true,

    val barWidth: Dp = 30.dp,
    val barCornerRadius: Float = 24f,
    val barSpace: Dp = 16.dp,
    val dotLineStartSize: Dp = 20.dp,
    val dotSize: Dp = 1.5.dp,
    val padding: Dp = 24.dp,

    val dotLineColor: Color = Color.Black,
    val backgroundColor: Color = Color.White,
    val rulerLabelTextColor: Color = Color.Black,
    val barColor: Color = Color.Cyan
)

@OptIn(ExperimentalTextApi::class)
@Composable
private fun BarRect(
    data: BiDirectionalBarData,
    config: BiDirectionalBarChartConfig,
    modifier: Modifier = Modifier,
) {
    val dataHeight = data.high - data.low
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .width(config.barWidth)
            .fillMaxHeight(),
        onDraw = {
            val factor = size.height / (config.chartLabelNumberMax - config.chartLabelNumberMin)

            val offset = (config.chartLabelNumberMax - data.high) * factor

            fun horizontalOffset(selfWidth: Int) = (size.width - selfWidth) / 2

            val labelTextStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Black)
            val measuredText = textMeasurer.measure(
                text = data.high.toString(),
                style = labelTextStyle
            )
            drawText(
                measuredText,
                topLeft = Offset(
                    x = horizontalOffset(measuredText.size.width),
                    y = offset - measuredText.size.height
                )
            )

            val rectHeight = dataHeight * factor
            val rectSize = Size(width = config.barWidth.toPx(), height = rectHeight)
            drawRoundRect(
                cornerRadius = CornerRadius(config.barCornerRadius),
                size = rectSize,
                color = config.barColor,
                topLeft = Offset(x = horizontalOffset(rectSize.width.toInt()), y = offset)
            )

            val lowerMeasuredText = textMeasurer.measure(
                text = data.low.toString(),
                style = labelTextStyle
            )
            drawText(
                lowerMeasuredText,
                topLeft = Offset(
                    x = horizontalOffset(lowerMeasuredText.size.width),
                    y = offset + rectHeight
                )
            )
        }
    )
}

@Preview(widthDp = 60, heightDp = 160, showBackground = true)
@Composable
fun BarChartPreview() {
    val data = BiDirectionalBarData(120, 80)
    BarRect(data = data, BiDirectionalBarChartConfig())
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun BiDirectionalBarChart(
    dataList: List<BiDirectionalBarData>,
    modifier: Modifier = Modifier,
    config: BiDirectionalBarChartConfig = BiDirectionalBarChartConfig(),
) {
    val textMeasurer = rememberTextMeasurer()

    Surface(color = config.backgroundColor) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(22.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                val rulerGapCount = config.chartLineCount - 1
                val incHeightSize = (size.height / rulerGapCount).toInt()

                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(config.dotSize.toPx(), 20f))

                for (i in 0 until config.chartLineCount) {
                    val labelTextNumber = (config.chartLabelNumberMax -
                            i * (config.chartLabelNumberRangeSize / config.chartLineGapCount))
                    val labelText = if (config.useFloatLabel) {
                        "%.1f".format(labelTextNumber)
                    } else {
                        labelTextNumber.toInt().toString()
                    }

                    val measuredText = textMeasurer.measure(
                        AnnotatedString(labelText),
                        style = TextStyle(fontSize = 14.sp),
                    )
                    val heightOffset = (i * incHeightSize).toFloat()
                    drawText(
                        measuredText,
                        topLeft = Offset(
                            // align to text right edge
                            config.dotLineStartSize.toPx() - measuredText.size.width,
                            heightOffset - (measuredText.size.height / 2)
                        )
                    )

                    drawLine(
                        start = Offset(config.dotLineStartSize.toPx() + 8.dp.toPx(), heightOffset),
                        end = Offset(size.width, heightOffset),
                        color = config.dotLineColor,
                        strokeWidth = config.dotSize.toPx(),
                        pathEffect = pathEffect,
                    )
                }
            })

            LazyRow(
                modifier = modifier.padding(start = config.dotLineStartSize + 4.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(config.barSpace)
            ) {
                items(dataList) { data ->
                    BarRect(data = data, config = config)
                }
            }
        }
    }
}

@Preview(widthDp = 550, heightDp = 300, showBackground = true)
@Composable
private fun ComposeBiDirectionalBarChartPreview() {
    Box(Modifier.height(300.dp)) {
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
        BiDirectionalBarChart(
            dataList = list,
            modifier = Modifier.fillMaxSize()
        )
    }
}