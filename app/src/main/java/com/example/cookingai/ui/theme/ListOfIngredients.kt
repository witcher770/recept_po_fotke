package com.example.cookingai.ui.theme

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cookingai.MainViewModel
import com.example.cookingai.models.ServerViewModel
import androidx.lifecycle.ViewModel
import com.example.cookingai.models.RecipeViewModel

@Composable
fun ListOfIngredients(navController: NavController, viewModel: MainViewModel, recipeViewModel: RecipeViewModel = viewModel()){

    // Используем состояние для списка
    val itemList = remember { mutableStateListOf<String>() }
    //itemList.addAll(recipeViewModel.strings) // передаем данные из списка с прошлой страницы

    // Добавляем элементы в список только один раз при создании Composable
    LaunchedEffect(Unit) {
        if (itemList.isEmpty()) { // Проверяем, пустой ли список
            itemList.addAll(recipeViewModel.strings)
        }
    }

    //var itemList = listOf("яблоко", "банан")
    //val itemList = remember { mutableStateOf(recipeViewModel.strings) }
//    var itemList = remember { mutableStateListOf<String>() }
//    itemList = remember { recipeViewModel.strings }
    //itemList = recipeViewModel.strings

    var newItem by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(),
        //.background(Color.Red), //,
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Top )
    {

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            //.background(Color.Green),
            contentAlignment = Alignment.TopCenter,
            ) {
            // Отображаем последнее фото под кнопкой "История", если оно есть
            viewModel.lastPhotoUri.value?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = "Последнее фото",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(200.dp)
                )
            }
        }
        // Поле ввода для добавления нового элемента
        TextField(
            value = newItem,
            onValueChange = { newItem = it },
            placeholder = { Text("Введите новый ингредиент") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка для добавления нового элемента
        Button(
            onClick = {
                if (newItem.isNotBlank()) {
                    itemList.add(newItem.trim())
                    newItem = "" // Очистка поля ввода
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text("Добавить ингредиент")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.95f)
                .border(
                    width = 2.dp, // Толщина рамки
                    color = Color.Black, // Цвет рамки
                    shape = RoundedCornerShape(8.dp) // Скругленные углы рамки
                )
                .padding(10.dp) // Отступ внутри рамки
                //.background(Color.Blue)
        ) {
            itemsIndexed(itemList) { index, value ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Поле ввода для редактирования элемента
                    TextField(
                        value = value,
                        onValueChange = { newValue -> itemList[index] = newValue },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Кнопка для удаления элемента
                    IconButton(onClick = { itemList.removeAt(index) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Удалить элемент"
                        )
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), // Добавляем отступы от краев экрана
            contentAlignment = Alignment.BottomCenter // Размещаем содержимое внизу по центру
        ) {
            Button(
                onClick = {
                    // здесь передаем список на сервер и получаем список с первым элементом названием рецепта, вторым самим рецептом
                    val listOfIngredients = listOf("яблочный пирог 2", "яблокам сезам откройся говоришь и готово")
                    val listOfIngredients1 = listOf("суп", "1. Подготовьте ингредиенты: вымойте и очистите овощи.\n" +
                            "2. Нарежьте морковь кружочками, лук кубиками.\n" +
                            "3. Картофель порежьте на средние кубики.\n" +
                            "4. Разогрейте сковороду с растительным маслом.\n" +
                            "5. Обжарьте лук до прозрачности.\n" +
                            "6. Добавьте морковь, жарьте 3-5 минут.\n" +
                            "7. В кастрюлю налейте 2 литра воды.\n" +
                            "8. Положите обжаренные овощи в воду.\n" +
                            "9. Доведите до кипения.\n" +
                            "10. Добавьте картофель, варите 15 минут.\n" +
                            "11. Подготовьте специи: соль, перец, лавровый лист.\n" +
                            "12. Добавьте специи в кастрюлю.\n" +
                            "13. Очистите и нарежьте чеснок.\n" +
                            "14. Добавьте чеснок за 5 минут до готовности.\n" +
                            "15. Проверьте готовность картофеля.\n" +
                            "16. Добавьте любимую зелень: укроп, петрушку.\n" +
                            "17. Перемешайте суп, дайте настояться 5 минут.\n" +
                            "18. Подготовьте тарелки для подачи.\n" +
                            "19. Разлейте суп по тарелкам.\n" +
                            "20. Нарежьте хлеб для подачи.\n" +
                            "21. Попробуйте суп на вкус, при необходимости добавьте соли.\n" +
                            "22. Украсьте зеленью перед подачей.\n" +
                            "23. Подготовьте напитки, например чай или сок.\n" +
                            "24. Подайте суп на стол.\n" +
                            "25. Убедитесь, что все довольны.\n" +
                            "26. Наслаждайтесь трапезой.\n" +
                            "27. Уберите остатки в холодильник.\n" +
                            "28. Помойте посуду.\n" +
                            "29. Протрите стол.\n" +
                            "30. Напишите рецепт в свою кулинарную книгу.\n")

                    recipeViewModel.initializeList(listOfIngredients)

                    val recipeId = 0
                    navController.navigate("RecipeScreen/$recipeId")
//                    navController.navigate("RecipeScreen")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF98d7ff))
            ) {
                Text(text = "Подтвердить")
            }
        }


    }
}