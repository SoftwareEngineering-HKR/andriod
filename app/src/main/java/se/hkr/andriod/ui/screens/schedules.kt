package se.hkr.andriod.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.ui.components.AppButton
import se.hkr.andriod.ui.components.AppTextField
import se.hkr.andriod.ui.theme.cardBackground
import se.hkr.andriod.ui.theme.listItemBackground

data class Schedule(
    val id: Int,
    val device: String,
    val room: String,
    val time: String
)

@Composable
fun SchedulesScreen() {

    var device by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    var schedules by remember {
        mutableStateOf(
            listOf(
                Schedule(1, "Bedroom Light", "Bedroom", "07:00"),
                Schedule(2, "Living Room Fan", "Living Room", "22:30")
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.schedules_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.cardBackground
            ),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⏰")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.add_schedule),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = stringResource(R.string.automate_devices),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp, bottom = 20.dp)
                )

                AppTextField(
                    value = device,
                    onValueChange = { device = it },
                    label = stringResource(R.string.schedule_name),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                AppTextField(
                    value = room,
                    onValueChange = { room = it },
                    label = stringResource(R.string.room_name),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                AppTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = stringResource(R.string.save_schedule),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                AppButton(
                    text = stringResource(R.string.add_schedule),
                    onClick = {

                        if (
                            device.isNotEmpty() &&
                            room.isNotEmpty() &&
                            time.isNotEmpty()
                        ) {

                            schedules = schedules + Schedule(
                                id = schedules.size + 1,
                                device = device,
                                room = room,
                                time = time
                            )

                            device = ""
                            room = ""
                            time = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.active_schedules),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(schedules) { schedule ->

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.cardBackground
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MaterialTheme.colorScheme.listItemBackground),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("⏰")
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {

                                Text(
                                    text = schedule.device,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Text(
                                    text = "${schedule.room} • ${schedule.time}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                schedules = schedules.filter {
                                    it.id != schedule.id
                                }
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.save),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}