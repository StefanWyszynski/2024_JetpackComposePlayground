package com.jetpackcompose.playground.crypto.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jetpackcompose.playground.crypto.presentation.viewmodel.CryptoUtilTestViewModel

@Composable
fun CryptoUtilTestScreen(cryptoUtilTestViewModel: CryptoUtilTestViewModel) {

    var data by rememberSaveable {
        mutableStateOf("")
    }
    var encryptedData: ByteArray? by rememberSaveable {
        mutableStateOf(null)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
            value = data, onValueChange = {
                data = it
            }, placeholder = {
                Text(text = "Data to encrypt")
            })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(modifier = Modifier
                .weight(1f)
                .padding(10.dp),
                onClick = {
                    encryptedData = cryptoUtilTestViewModel.encryptFromString(data)
                }) {
                Text(text = "Encrypt")
            }
            Button(modifier = Modifier
                .weight(1f)
                .padding(10.dp),
                onClick = {
                    encryptedData?.let {
                        data = cryptoUtilTestViewModel.decryptAsString(it) ?: ""
                    }
                }) {
                Text(text = "Decrypt")
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp), text = "Encrypted text:"
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp), text = encryptedData?.decodeToString() ?: ""
        )
    }
}
