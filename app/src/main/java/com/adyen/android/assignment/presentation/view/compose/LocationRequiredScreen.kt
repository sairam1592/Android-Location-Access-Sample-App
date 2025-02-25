package com.adyen.android.assignment.presentation.view.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adyen.android.assignment.R

@Composable
fun LocationRequiredScreen(
    modifier: Modifier = Modifier,
    onEnableLocationClick: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.location_setting_desc),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                onEnableLocationClick()
            }) {
                Text(stringResource(R.string.location_setting_btn_title))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationRequiredScreenPreview() {
    LocationRequiredScreen(onEnableLocationClick = {})
}
