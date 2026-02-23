package se.hkr.andriod.ui.devices.lock

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import se.hkr.andriod.R
import se.hkr.andriod.ui.theme.AndriodTheme
import se.hkr.andriod.ui.theme.cardBackground
import kotlin.math.roundToInt

@Composable
fun LockStateComponent(
    isLocked: Boolean,
    onToggle: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var trackWidthPx by remember { mutableFloatStateOf(0f) }
    val thumbSize = 64.dp
    val thumbSizePx = with(LocalDensity.current) { thumbSize.toPx() }

    val offsetX = remember { Animatable(0f) }

    val maxOffset = (trackWidthPx - thumbSizePx - with(LocalDensity.current) { 16.dp.toPx() })
        .coerceAtLeast(0f)

    LaunchedEffect(isLocked, maxOffset) {
        val target = if (isLocked) 0f else maxOffset
        offsetX.animateTo(target, tween(300))
    }

    // Background color depending on state
    val trackColor = if (isLocked)
        Color(0xFFD32F2F).copy(alpha = 0.15f)
    else
        Color(0xFF2E7D32).copy(alpha = 0.15f)

    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.cardBackground
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = if (isLocked)
                        Icons.Rounded.Lock
                    else
                        Icons.Rounded.LockOpen,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(
                        if (isLocked)
                             R.string.lock_state_locked
                        else
                            R.string.lock_state_unlocked
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = stringResource(
                        if (isLocked)
                            R.string.lock_swipe_to_unlock
                        else
                            R.string.lock_swipe_to_lock
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Swipe Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .onSizeChanged { trackWidthPx = it.width.toFloat() }
                    .background(trackColor, RoundedCornerShape(40.dp))
                    .pointerInput(isLocked, maxOffset) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                scope.launch {
                                    val newOffset =
                                        (offsetX.value + dragAmount.x)
                                            .coerceIn(0f, maxOffset)
                                    offsetX.snapTo(newOffset)
                                }
                            },
                            onDragEnd = {
                                scope.launch {
                                    val shouldToggle =
                                        if (isLocked)
                                            offsetX.value > maxOffset * 0.5f
                                        else
                                            offsetX.value < maxOffset * 0.5f

                                    if (shouldToggle) {
                                        onToggle()
                                    } else {
                                        val target =
                                            if (isLocked) 0f else maxOffset

                                        offsetX.animateTo(target, tween(300))
                                    }
                                }
                            }
                        )
                    }
            ) {
                // Thumb
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    offsetX.value.roundToInt(),
                                    0
                                )
                            }
                            .size(thumbSize)
                            .background(
                                if (isLocked)
                                    Color(0xFFD32F2F)
                                else
                                    Color(0xFF2E7D32),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isLocked)
                                Icons.Rounded.Lock
                            else
                                Icons.Rounded.LockOpen,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))

}

@Preview(name = "LockStateComponent Locked", showBackground = true)
@Composable
private fun LockStateComponentPreview_Locked() {
    AndriodTheme {
        LockStateComponent(
            isLocked = true,
            onToggle = {}
        )
    }
}

@Preview(name = "LockStateComponent Unlocked", showBackground = true)
@Composable
private fun LockStateComponentPreview_Unlocked() {
    AndriodTheme {
        LockStateComponent(
            isLocked = false,
            onToggle = {}
        )
    }
}