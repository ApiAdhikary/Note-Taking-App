package com.example.noteapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.noteapp.data.repository.NoteRepository
import com.example.noteapp.ui.screens.home.ViewModelFactory
import com.example.noteapp.ui.screens.addedit.AddEditScreen
import com.example.noteapp.ui.screens.addedit.AddEditViewModel
import com.example.noteapp.ui.screens.home.HomeScreen
import com.example.noteapp.ui.screens.home.HomeViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddNote : Screen("add_note")
    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    repository: NoteRepository
) {
    val viewModelFactory = ViewModelFactory(repository)

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home Screen
        composable(route = Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(factory = viewModelFactory)

            HomeScreen(
                viewModel = viewModel,
                onAddNoteClick = {
                    navController.navigate(Screen.AddNote.route)
                },
                onNoteClick = { noteId ->
                    navController.navigate(Screen.EditNote.createRoute(noteId))
                }
            )
        }

        // Add Note Screen
        composable(route = Screen.AddNote.route) {
            val viewModel: AddEditViewModel = viewModel(factory = viewModelFactory)

            AddEditScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                isEditMode = false
            )
        }

        // Edit Note Screen
        composable(
            route = Screen.EditNote.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                }
            )
        ) {
            val viewModel: AddEditViewModel = viewModel(factory = viewModelFactory)

            AddEditScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                isEditMode = true
            )
        }
    }
}