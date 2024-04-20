package com.jetpackcompose.playground.task_room.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.jetpackcompose.playground.R
import com.jetpackcompose.playground.common.presentation.components.CustomTopAppBar
import com.jetpackcompose.playground.common.presentation.data.CustomTopAppBarData
import com.jetpackcompose.playground.common.presentation.utils.TestConstants
import com.jetpackcompose.playground.task_realm.presentation.viewmodel.RealmTaskViewModel
import com.jetpackcompose.playground.task_room.domain.data.Priority
import com.jetpackcompose.playground.task_room.domain.data.RealmTask

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealmNewTaskScreen(
    navController: NavHostController,
    taskViewModel: RealmTaskViewModel,
    customTopAppBarData: CustomTopAppBarData
) {
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())

    val taskDate = remember {
        mutableStateOf((datePickerState.selectedDateMillis?.let {
            convertMillisToDate(it)
        }.toString()))
    }

    val isDatePickerDialogVisible = remember { mutableStateOf(false) }

    val taskTitle = remember { mutableStateOf("") }

    if (isDatePickerDialogVisible.value) {
        DatePickerDialog(
            confirmButton = {
                Button(onClick = {
                    isDatePickerDialogVisible.value = false
                    taskDate.value =
                        datePickerState.selectedDateMillis?.let { convertMillisToDate(it) }
                            .toString()
                }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            content = { DatePicker(state = datePickerState) },
            onDismissRequest = { isDatePickerDialogVisible.value = false })
    }

    AddRealmTaskContent(
        taskTitle, isDatePickerDialogVisible,
        navController, taskDate, taskViewModel, customTopAppBarData
    )
}

@Composable
private fun AddRealmTaskContent(
    taskTitle: MutableState<String>,
    isDatePickerDialogVisible: MutableState<Boolean>,
    navController: NavHostController,
    taskDate: MutableState<String>,
    taskViewModel: RealmTaskViewModel,
    customTopAppBarData: CustomTopAppBarData
) {
    val options = Priority.values().map { it.toString() }.toList()
    val taskPriority = remember { mutableStateOf(Priority.LOW.name) }
    val dropDownExposed = remember { mutableStateOf(false) }

    Scaffold(topBar = { CustomTopAppBar(customTopAppBarData) })
    { scaffoldPading ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPading)
        )
        {
            Column(
                modifier = Modifier
                    .border(
                        border = BorderStroke(color = Color.Red, width = 2.dp),
                        shape = RectangleShape
                    )
                    .padding(8.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Column {
                    TaskNameSection(taskTitle)
                    DateSection(taskDate, isDatePickerDialogVisible)
                    PrioritySection(dropDownExposed, taskPriority, options)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = Modifier
                            .wrapContentWidth()
                            .testTag(TestConstants.REALM_ADD_TASK_ADD_BUTTON),
                        onClick = {
                            val task =
                                RealmTask().apply {
                                    date = taskDate.value
                                    name = taskTitle.value
                                    priority = Priority.valueOf(taskPriority.value).ordinal
                                }
                            taskViewModel.insertTask(task)
                            navController.popBackStack()
                        }) {
                        Text(text = stringResource(R.string.add_task))
                    }
                    Button(
                        modifier = Modifier
                            .wrapContentWidth()
                            .testTag(TestConstants.REALM_ADD_TASK_CANCEL_BUTTON),
                        onClick = { navController.popBackStack() }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PrioritySection(
    dropDownExposed: MutableState<Boolean>,
    chosenDropdown: MutableState<String>,
    options: List<String>
) {
    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = dropDownExposed.value,
        onExpandedChange = {
            dropDownExposed.value = !dropDownExposed.value
        }
    ) {
        OutlinedTextField(
            value = chosenDropdown.value,
            readOnly = true,
            onValueChange = { },
            label = { Text(stringResource(R.string.priority)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = dropDownExposed.value
                )
            },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = dropDownExposed.value,
            onDismissRequest = { dropDownExposed.value = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption) },
                    onClick = {
                        chosenDropdown.value = selectionOption
                        dropDownExposed.value = false
                    }
                )
            }
        }
    }
}

@Composable
private fun TaskNameSection(taskTitle: MutableState<String>) {
    Text(
        text = "Task name",
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {

            }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            disabledBorderColor = Color.Gray,
        ),
        value = taskTitle.value,
        onValueChange = { taskTitle.value = it },
        textStyle = TextStyle(fontSize = 20.sp),
        modifier = Modifier
            .widthIn(1.dp, Dp.Infinity)
            .fillMaxWidth()
            .testTag(TestConstants.REALM_ADD_TASK_TITLE)
    )
}

@Composable
private fun DateSection(
    taskDate: MutableState<String>,
    isDatePickerDialogVisible: MutableState<Boolean>
) {
    Text(
        text = "Deadline",
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {

            }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.Black, // You can adjust the background color here
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            disabledBorderColor = Color.Gray,
        ),
        leadingIcon = {
            Icon(Icons.Filled.DateRange, "Date Picker")
        },
        value = taskDate.value,
        onValueChange = { isDatePickerDialogVisible.value = true },
        readOnly = true,
        enabled = false,
        textStyle = TextStyle(fontSize = 20.sp),
        modifier = Modifier
            .widthIn(1.dp, Dp.Infinity)
            .clickable { isDatePickerDialogVisible.value = true }
            .fillMaxWidth()
    )
}
