package com.example.shoppinglist


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import org.w3c.dom.Text

data class ShoppingItem(val id:Int
                        ,var name:String,
                        var quantity:Int ,
                        var isEditable:Boolean=false)

@Composable
fun ShoppingList(){
    var sItems by remember {
        mutableStateOf(listOf<ShoppingItem>())
    }
    var showAlert by remember {
        mutableStateOf(false)
    }
    var itemName by remember {
        mutableStateOf("")
    }
    var itemQuantity by remember {
        mutableStateOf("1")
    }
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Button(onClick = { showAlert=true },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            shape = CircleShape
        ) {
            Text(text = "Add item")
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ){
            items(sItems){
                   item->
                if(item.isEditable){
                    ShoppingItemEdit(item = item, onEditComplete ={
                        editedName,editedQuantity->
                        sItems=sItems.map{it.copy(isEditable = false)}
                        val editedItem=sItems.find{it.id==item.id}
                        editedItem?.let {
                            it.name=editedName
                            it.quantity=editedQuantity
                        }
                    } )
                }
                else{
                    ListItemLook(item = item, onEditClick = {
                        sItems=sItems.map{it.copy(isEditable = it.id==item.id)}
                    }, onDeleteClick = {
                        sItems=sItems-item
                    })


                }
            }
        }
    }
    if(showAlert){
       AlertDialog(onDismissRequest = { showAlert=false },
           confirmButton = {
               Row(modifier = Modifier
                   .fillMaxSize()
                   .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                   Button(onClick = {
                                    if(itemName.isNotBlank()){
                                        val newItem=ShoppingItem(id = sItems.size+1,
                                            name = itemName,
                                            quantity = itemQuantity.toInt())
                                        sItems=sItems+newItem
                                        showAlert=false
                                        itemName=""
                                        //itemQuantity=""
                                    }
                   }, shape = CircleShape) {
                       Text(text = "Add")
                   }
                   Spacer(modifier = Modifier.size(16.dp))
                   Button(onClick = { showAlert=false}, shape = CircleShape) {
                       Text(text = "Cancel")

                   }
               }
           },
           title = { Text(text = "Add Shopping item")},
           text = {
                 Column() {
                     Spacer(modifier = Modifier.size(16.dp))
                     OutlinedTextField(value = itemName, onValueChange = {itemName=it}, singleLine = true,
                     modifier = Modifier
                         .padding(8.dp)
                     )

                     OutlinedTextField(value = itemQuantity, onValueChange = {itemQuantity=it}, singleLine = true,
                     modifier = Modifier
                         .padding(8.dp)
                         )

                 }
           }
       )
    }
}

@Composable
fun ShoppingItemEdit(item: ShoppingItem,onEditComplete:(String,Int)->Unit){
    var editName by remember {
        mutableStateOf(item.name)
    }
    var editQuantity by remember {
        mutableStateOf(item.quantity.toString())
    }
    var isEditing by remember {
        mutableStateOf(item.isEditable)
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
    horizontalArrangement = Arrangement.SpaceEvenly) {
            Column() {
                BasicTextField(value = editName, onValueChange = {editName=it}, singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
                BasicTextField(value = editQuantity, onValueChange = {editQuantity=it}, singleLine = true,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp))
            }
        Button(onClick = {
            isEditing=false
            onEditComplete(editName,editQuantity.toIntOrNull()?:1)
        }) {
            Text(text = "Save")
        }
    }
}


@Composable
fun ListItemLook(
    item:ShoppingItem,
    onEditClick:()->Unit,
    onDeleteClick:()->Unit
){
Row(modifier = Modifier
    .padding(8.dp)
    .fillMaxWidth()
    .border(
        border = BorderStroke(2.dp, Color(0XFF018786)),
        shape = RoundedCornerShape(20)
    ),
    horizontalArrangement = Arrangement.SpaceBetween){
Text(text = item.name, modifier = Modifier.padding(8.dp))
    Text(text = item.quantity.toString(), modifier = Modifier.padding(8.dp))
    Row(modifier = Modifier.padding(8.dp)){
        IconButton(onClick = onEditClick) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
        }
        IconButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}
}






