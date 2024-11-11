package io.ssafy.openticon.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.iamport.sdk.data.sdk.IamPortRequest
import com.iamport.sdk.data.sdk.PayMethod
import com.iamport.sdk.domain.core.Iamport
import io.ssafy.openticon.BuildConfig
import io.ssafy.openticon.R
import io.ssafy.openticon.ui.viewmodel.MemberViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavController,
) {
    val viewModel: MemberViewModel = hiltViewModel()
    val memberEntity by viewModel.memberEntity.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPaymentResultDialog by remember { mutableStateOf(false) }
    val userCode = BuildConfig.IMP_USERCODE
    val purchaseSuccess by viewModel.purchaseSuccess.collectAsState()
    var showPriceSelectionDialog by remember { mutableStateOf(false) }
    var selectedAmount by remember { mutableStateOf(TextFieldValue("")) }
    LaunchedEffect(Unit) {
        viewModel.fetchMemberInfo()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.navigate("settings") },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "앱 설정",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }

        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                        colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp,top = 16.dp, bottom = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(
                                    width = 4.dp,
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = CircleShape
                                )
                        ) {
                            if (memberEntity?.profile_image.isNullOrEmpty()) {
                                Image(
                                    painter = painterResource(R.drawable.ic_launcher_foreground),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            } else {
                                AsyncImage(
                                    model = memberEntity?.profile_image,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(R.drawable.loading_img),
                                    error = painterResource(R.drawable.ic_broken_image),
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = memberEntity?.nickname ?: "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = memberEntity?.bio ?: "",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface // 테마 색상 사용
                        )
                        Spacer(Modifier.height((20.dp)))
                        // 결제 버튼
                        FilledTonalButton(
                            onClick = {
                                showPriceSelectionDialog = true
                            }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Payments,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(4.dp))
                                Text("${memberEntity?.point ?: 0} 포인트")
                            }
                        }

                        // 가격 선택 다이얼로그
                        if (showPriceSelectionDialog) {
                            AlertDialog(
                                onDismissRequest = { showPriceSelectionDialog = false },
                                title = { Text("충전할 금액을 입력하세요", style = MaterialTheme.typography.titleMedium) },
                                text = {
                                    Column(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        // Custom amount input field
                                        TextField(
                                            value = selectedAmount,
                                            onValueChange = { newValue ->
                                                if (newValue.text.all { it.isDigit() }) {
                                                    selectedAmount = newValue
                                                }
                                            },
                                            label = { Text("금액 입력") },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 8.dp),
                                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                            singleLine = true
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Preset amount radio buttons with clickable row
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalArrangement = Arrangement.spacedBy(0.dp) // 간격을 줄임
                                        ) {
                                            listOf(1000, 5000, 10000).forEach { amount ->
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            selectedAmount =
                                                                TextFieldValue(amount.toString())
                                                        } // Row 전체를 클릭 가능하게 설정
                                                        .padding(vertical = 0.dp) // Row의 수직 간격 최소화
                                                ) {
                                                    RadioButton(
                                                        selected = selectedAmount.text == amount.toString(),
                                                        onClick = { selectedAmount = TextFieldValue(amount.toString()) }
                                                    )
                                                    Text(
                                                        text = "${amount}원",
                                                        modifier = Modifier.padding(start = 0.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            val amount = selectedAmount.text.toIntOrNull()
                                            if (amount != null) {
                                                showPriceSelectionDialog = false
                                                Iamport.payment(
                                                    userCode = userCode,
                                                    iamPortRequest = IamPortRequest(
                                                        pg = "kakaopay",
                                                        pay_method = PayMethod.trans.name,
                                                        name = "포인트 추가 결제",
                                                        merchant_uid = "mid_${System.currentTimeMillis()}",
                                                        amount = amount.toString(),
                                                        buyer_name = memberEntity?.nickname,
                                                        buyer_email = memberEntity?.email
                                                    ),
                                                    approveCallback = {
                                                        Log.d("Iamport", "Approve callback triggered.")
                                                    },
                                                    paymentResultCallback = { result ->
                                                        result?.let {
                                                            if (it.imp_success == true) {
                                                                Log.d("Iamport", "결제 성공: ${it.imp_success}")
                                                                viewModel.purchasePoint(amount, it.imp_uid ?: "")
                                                            } else {
                                                                Log.d("Iamport", "결제 실패")
                                                            }
                                                            showPaymentResultDialog = true
                                                        }
                                                    }
                                                )
                                            } else {
                                                Log.w("ProfileScreen", "올바른 금액을 입력하세요")
                                            }
                                        },
                                    ) {
                                        Text("결제")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showPriceSelectionDialog = false }) {
                                        Text("취소")
                                    }
                                }
                            )
                        }

                        // 결제 결과 모달 창
                        if (showPaymentResultDialog) {
                            AlertDialog(
                                onDismissRequest = { showPaymentResultDialog = false },
                                title = {
                                    Text(
                                        if (purchaseSuccess) "결제 완료" else "결제 실패"
                                    )
                                },
                                text = {
                                    Text(
                                        if (purchaseSuccess) "결제가 성공적으로 완료되었습니다!"
                                        else "결제에 실패했습니다. 다시 시도해 주세요."
                                    )
                                },
                                confirmButton = {
                                    Button(
                                        onClick = { showPaymentResultDialog = false }
                                    ) {
                                        Text("확인")
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Column {
                    ListItem(
                        headlineContent = { Text("내 정보 수정") },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .clickable { navController.navigate("edit_profile") }
                            .padding(vertical = 8.dp)
                    )
                    HorizontalDivider()
                    ListItem(
                        headlineContent = { Text("로그아웃") },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    viewModel.logout()

                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true } // 스택의 모든 화면을 제거하고 이동
                                        launchSingleTop = true // 중복된 화면이 스택에 쌓이지 않게 설정
                                    }

                                }
                            }
                            .padding(vertical = 8.dp)
                    )
                    HorizontalDivider()
                    ListItem(
                        headlineContent = { Text("회원탈퇴") },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Filled.PersonRemove,
                                contentDescription = null
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .clickable {
                                showDeleteDialog = true
//                                coroutineScope.launch {
//                                    val result = viewModel.deleteMember()
//                                    if (result.isSuccess) {
//                                        showDeleteDialog = true
//                                    } else {
//                                        println("회원 삭제 실패: ${result.exceptionOrNull()?.message}")
//                                    }
//                                }
                            }
                            .padding(vertical = 8.dp)
                    )
                    HorizontalDivider()
                    Spacer(Modifier.height(128.dp))
                }
            }
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false }, // 모달 외부를 터치하거나 뒤로가기 시 모달 닫기
            title = { Text("회원 탈퇴") }, // 모달 제목
            text = { Text("정말로 회원 탈퇴를 진행하시겠습니까?") }, // 탈퇴 확인 메시지
            confirmButton = {
                TextButton(
                    onClick = {
                        // 탈퇴 확인 시 필요한 로직 (예: viewModel에서 회원 삭제 로직 호출)
                        coroutineScope.launch {
                            val result = viewModel.deleteMember()
                            if (result.isSuccess) {
                                showDeleteDialog = false // 모달 닫기
                                navController.navigate("main") // 탈퇴 후 메인 페이지로 이동
                            } else {
                                Log.e("DeleteMember", "회원 탈퇴 실패: ${result.exceptionOrNull()?.message}")
                            }
                        }
                    }
                ) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false } // 취소 시 모달 닫기
                ) {
                    Text("취소")
                }
            }
        )
    }
}
