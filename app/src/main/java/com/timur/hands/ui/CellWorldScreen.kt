package com.timur.hands.ui

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.timur.hands.data.CellState
import com.timur.hands.data.checkLifeCreationOrDeath
import com.timur.hands.data.generateRandomCell
import com.timur.hands.ui.theme.AliveCellText
import com.timur.hands.ui.theme.AliveCellText_2
import com.timur.hands.ui.theme.AppBarTitle
import com.timur.hands.ui.theme.Black
import com.timur.hands.ui.theme.CreateButtonText
import com.timur.hands.ui.theme.DarkPurple
import com.timur.hands.ui.theme.DeadCellText
import com.timur.hands.ui.theme.DeadCellText_2
import com.timur.hands.ui.theme.LifeCellText
import com.timur.hands.ui.theme.LifeCellText_2
import com.timur.hands.ui.theme.Purple700
import com.timur.hands.ui.theme.Yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CellWorldScreen() {
    var cells by remember { mutableStateOf(listOf<CellState>()) }
    var message by remember { mutableStateOf<String?>(null) }

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        AppBarTitle,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple700,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            CreateButton {
                val newCell = generateRandomCell()
                cells = cells + newCell
                message = checkLifeCreationOrDeath(cells)
                cells = handleConsecutiveCells(cells)
            }
        }
    ) { paddingValues ->
        LaunchedEffect(cells) {
            if (cells.isNotEmpty()) {
                listState.animateScrollToItem(cells.size - 1)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                CellList(cells = cells, listState = listState)

                message?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = Color.Red,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CellList(cells: List<CellState>, listState: LazyListState) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        items(cells) { cell ->
            CellItem(cellState = cell)
        }
    }
}


fun handleConsecutiveCells(cells: List<CellState>): List<CellState> {
    if (cells.size >= 3) {
        val lastThreeCells = cells.takeLast(3)
        if (lastThreeCells.all { it == CellState.ALIVE }) {
            return cells + CellState.LIFE
        } else if (lastThreeCells.all { it == CellState.DEAD }) {
            var cells_new = mutableListOf<CellState>()
            var flag = 0
            for (i in cells.size-1 downTo 0) {
                if(cells[i] == CellState.LIFE && flag == 0){
                    flag = 1
                }
                else{
                    cells_new.add(cells[i])
                }
            }
            return cells_new.toList()
        }
    }
    return cells
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CellItem(cellState: CellState) {
    val color = Color.White

    val text = when (cellState) {
        CellState.DEAD -> DeadCellText
        CellState.ALIVE -> AliveCellText
        CellState.LIFE -> LifeCellText
    }

    val text_2 = when (cellState) {
        CellState.DEAD -> DeadCellText_2
        CellState.ALIVE -> AliveCellText_2
        CellState.LIFE -> LifeCellText_2
    }

    val imageUrl = when (cellState) {
        CellState.DEAD -> "https://sportishka.com/uploads/posts/2021-12/1638964271_5-sportishka-com-p-skelet-na-belom-fone-sport-krasivo-foto-6.png"
        CellState.ALIVE -> "https://avatars.mds.yandex.net/i?id=09b3d1461f69b05d4fe4d16e0f902505_l-5322158-images-thumbs&n=13"
        CellState.LIFE -> "https://gas-kvas.com/grafic/uploads/posts/2024-01/gas-kvas-com-p-chelovechek-i-znaki-na-prozrachnom-fone-34.png"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color, shape = RoundedCornerShape(8.dp))
            .height(50.dp)
    ) {
        GlideImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 8.dp)
                .clip(CircleShape)
                .size(40.dp)
                .align(Alignment.CenterVertically)
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = text,
                color = Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                text = text_2,
                color = Black,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun CreateButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 3.dp, bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DarkPurple)
    ) {
        Text(text = CreateButtonText, color = Color.White)
    }
}

