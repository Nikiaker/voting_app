package com.example.studentvotingapp.views.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import com.example.studentvotingapp.GlobalUserData

class BottomNavigationBarView {
    companion object {
        @Composable
        fun BottomNavigationBar(selectedScreen: String, onTabSelected: (String) -> Unit) {
            BottomNavigation {
                BottomNavigationItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Available Votes") },
                    label = { Text("Available") },
                    selected = selectedScreen == "available",
                    onClick = { onTabSelected("available") }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Finished Votes") },
                    label = { Text("Finished") },
                    selected = selectedScreen == "finished",
                    onClick = { onTabSelected("finished") }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Face, contentDescription = "Scan QR") },
                    label = { Text("Scan QR") },
                    selected = selectedScreen == "qr_scanner",
                    onClick = { onTabSelected("qr_scanner") }
                )
                if (GlobalUserData.user.admin) {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Add, contentDescription = "Add Vote") },
                        label = { Text("Add Vote") },
                        selected = selectedScreen == "add_vote",
                        onClick = { onTabSelected("add_vote") }
                    )
                }
                if (GlobalUserData.user.admin) {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Build, contentDescription = "Generate QR") },
                        label = { Text("Generate QR") },
                        selected = selectedScreen == "generate_qr_list",
                        onClick = { onTabSelected("generate_qr_list") }
                    )
                }
            }
        }
    }
}