package com.thwackstudio.permissions_util.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.thwackstudio.permissions_util.domain.PermissionsDialogHelper

@Composable
fun DefaultPermissionDialog(permissionsDialogHelper: PermissionsDialogHelper) {
    AlertDialog(
        onDismissRequest = {
            permissionsDialogHelper.onDismiss()
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = {
                        permissionsDialogHelper.onConfirm()
                    }) {
                        Text("Confirm")
                    }
                    TextButton(onClick = {
                        permissionsDialogHelper.onDeny()
                    }) {
                        Text("Deny")
                    }
                }
            }
        },
        title = {
            Text(
                "Permission Required",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                permissionsDialogHelper.getDescription(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        })
}