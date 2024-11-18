package io.ssafy.openticon.ui.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun UnAuthModal(navController: NavController, onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "오류 발생") },
        text = { Text(text = "로그인에 실패했습니다. 로그인 페이지로 이동합니다.") },
        confirmButton = {
            Button(onClick = {
                onDismiss()
                navController.navigate("login") // 로그인 페이지로 이동
            }) {
                Text("확인")
            }
        }
    )
}

