package com.diajarkoding.timefit.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.diajarkoding.timefit.data.local.Schedule
import com.diajarkoding.timefit.presentation.components.CreateScheduleDialog
import com.diajarkoding.timefit.presentation.components.WorkoutScheduleItem
import com.diajarkoding.timefit.presentation.navigation.Screen
import com.diajarkoding.timefit.presentation.ui.theme.TimeFitTheme

val dummyWorkoutSchedule = listOf(
    "Push Day",
    "Pull Day",
    "Leg Day",
    "Cardio Focus"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.isCreateDialogOpen) {
        CreateScheduleDialog(
            newScheduleName = state.newScheduleName,
            onNameChange = { viewModel.onEvent(HomeScreenEvent.OnScheduleNameChange(it)) },
            onCreate = { viewModel.onEvent(HomeScreenEvent.OnCreateSchedule) },
            onDismiss = { viewModel.onEvent(HomeScreenEvent.OnDismissCreateDialog) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Schedule") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {viewModel.onEvent(HomeScreenEvent.OnShowCreateDialog)}
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Create Schedule"
                )
            }
        }
    ) {
        paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.schedules, key = {it.id}) { schedule ->
                WorkoutScheduleItem(
                    schedule = schedule,
                    onClick = { navController.navigate(Screen.Detail.createRoute(schedule.id))},
                    onDelete = { viewModel.onEvent(HomeScreenEvent.OnDeleteSchedule(schedule)) }
                )

            }
        }
    }
}



@Preview
@Composable
fun HomeScreenPreview(showBackground: Boolean = true) {
    val navController = rememberNavController()
    TimeFitTheme(darkTheme = true) {
        HomeScreen(navController)
    }
}